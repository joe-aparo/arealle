package net.jsa.arealle.task.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import net.jsa.arealle.task.pojo.AddressInfo;
import net.jsa.arealle.task.util.BasicNameNormalizer;
import net.jsa.arealle.task.util.INameNormalizer;
import net.jsa.arealle.task.util.StrUtils;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetItem;

public abstract class AbstractSitePropertyReader extends AbstractProcessStep {
	
	@Resource(name="dsFeatures")
	private IDataSet dsFeatures;
	
	@Resource(name="roadNameNormalizer")
	private INameNormalizer roadNameNormalizer;
	
	private PropertyCache propertyCache;
	private BasicNameNormalizer basicNormalizer = new BasicNameNormalizer();
	
	private String baseUrl;
	private int curPage;
	private int totalPages;
	
	public AbstractSitePropertyReader(String name, String baseUrl) {
		super(name);
		
		this.baseUrl = baseUrl;
	}
	
	protected String getBaseUrl() {
		return baseUrl;
	}

	protected void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	protected int getCurPage() {
		return curPage;
	}

	protected void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	protected int getTotalPages() {
		return totalPages;
	}

	protected void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public float getPctComplete() {
		return (float) curPage / totalPages;
	}
	
	public boolean start(Map<String, Object> context) {
		super.start(context);
		
		// Init run variables
		setCurPage(0);
		
		propertyCache = PropertyCache.fetchFromContext(getContext());

		return true;
	}

	@Override
	public boolean nextItem() {
		if (getCurPage() >= getTotalPages()) {
			return false;
		}
		
		setCurPage(getCurPage() + 1);
		
		return true;
	}

	protected PropertyCache getPropertyCache() {
		return propertyCache;
	}

	protected String readUrl(String urlString) {
		StringBuilder buf = new StringBuilder(10000);
		
		try {
			URL url = new URL(urlString);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			
			String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	        	buf.append(inputLine).append('\n');
	        }

	        in.close();
	    } catch (Exception e) {
			getLog().error("Error reading URL: " + urlString, e);
		}
		
		return buf.toString();
	}
	
	protected String postUrl(String urlString, Map<String, String> attrs) {
		StringBuilder buf = new StringBuilder(10000);
		
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
           getLog().error("Invalid url: " + urlString, ex);
        }

        // Construct the POST data.
        StringBuilder postData = new StringBuilder(1000);
        for (Entry<String, String> e : attrs.entrySet()) {
        	if (postData.length() > 0) {
        		postData.append('&');
        	}
        	
        	try {
				postData.append(e.getKey()).append('=').append(URLEncoder.encode(e.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException ex) {
	            getLog().error("Unsupported encoding", ex);
			}
        }

        HttpURLConnection urlConn = null;
        try {
            // URL connection channel.
            urlConn = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            getLog().error("Error opening to url: " + urlString, ex);
        }

        // Let the run-time system (RTS) know that we want input.
        urlConn.setDoInput (true);

        // Let the RTS know that we want to do output.
        urlConn.setDoOutput (true);

        // No caching, we want the real thing.
        urlConn.setUseCaches (false);

        try {
            urlConn.setRequestMethod("POST");
        } catch (ProtocolException ex) {
            getLog().error("Unsupported POST: " + urlString, ex);
        }

        // Specify the content type
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try {
            urlConn.connect();
        } catch (IOException ex) {
            getLog().error("Error connecting to url: " + urlString, ex);
        }

        Writer writer = null;
        
        try {
        	writer = new BufferedWriter(new OutputStreamWriter(urlConn.getOutputStream()));
        } catch (IOException ex) {
            getLog().error("Error creating output stream", ex);
        }

        // Send the request data.
        try {
            writer.write(postData.toString());
            writer.close();
        } catch (IOException ex) {
            getLog().error("Error posting data to url: " + urlString, ex);
        }

        // Get response data.
        String str = null;
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            while (null != ((str = reader.readLine()))) {
                buf.append(str).append('\n');
            }
            reader.close ();
        } catch (IOException ex) {
            getLog().error("Error reading response from url: " + urlString, ex);
        }
		
		return buf.toString();
	}
	
	protected String extract(String content, String prefix) {
		return extract(content, prefix, null, 0);
	}
	
	protected String extract(String content, String prefix, String suffix) {
		return extract(content, prefix, suffix, 0);
	}
	
	protected String extract(String content, String prefix, String suffix, int startPos) {
		String str = null;
		int pos1 = content.indexOf(prefix, startPos);
		
		if (pos1 != -1) {
			pos1 += prefix.length();
			if (!StringUtils.isEmpty(suffix)) {
				int pos2 = content.indexOf(suffix, pos1);
				if (pos2 != -1) {
					str = content.substring(pos1, pos2);
				}
			} else {
				str = content.substring(pos1);
			}
		}
		
		return str;
	}
	
	protected String makeIntStr(String numStr ) {
		StringBuilder buf = new StringBuilder(numStr.length());
		char[] chrs = numStr.toCharArray();
		for (char ch : chrs) {
			if (Character.isDigit(ch)) {
				buf.append(ch);
			}
		}
		
		return buf.toString();
	}
	
	protected String fetchTownName() {
		Map<String, Object> k = new HashMap<String, Object>();
		k.put("FEATURE_ID", getContext().get("TOWN_ID"));
		
		IDataSetItem feature = dsFeatures.retrieve(k);
		
		return feature != null ? feature.getString("FEATURE_NAME") : null;
	}
	
	protected AddressInfo splitRoadAndAddress(String address) {
		// Basic string cleaning
		String[] parts = StringUtils.split(address, ' ');
		
		int idx = 0;
		for (String part : parts) {
			String normalizedPart = basicNormalizer.normalize(part);
			
			// Find first non-numeric part
			if (!Character.isDigit(normalizedPart.charAt(0))) {
				break;
			}
			
			idx++;
		}
		
		// Non-numeric name component found?
		if (idx >= parts.length) {
			getLog().debug("Invalid address: " + address);
			return null;
		}
		
		AddressInfo addrInfo = new AddressInfo();
		String addrStr = StringUtils.join(parts, " ", 0, idx).toUpperCase();
		
		addrInfo.getAddress().setString(addrStr);
		addrInfo.getAddress().setNumber(StrUtils.extractNum(addrStr));
		addrInfo.setRoadName(roadNameNormalizer.normalize(StringUtils.join(parts, " ", idx, parts.length)));

		getLog().debug("Converted: " + address + " to " + addrInfo.toString());
		
		return addrInfo;
	}
	
	protected String[] splitAddressNumbers(String addrNumStr) {
		// Convert common separators and junky characters to spaces, then split on and squeeze out
		// spaces leaving whatever's left as a collection of address number strings
		String[] parts = StringUtils.split(addrNumStr.replace(',', ' ').replace('-', ' ').replace('&',  ' ').replace('#',  ' '), ' ');
		
		getLog().debug("addrNumStr '" + addrNumStr + "' split into: " + StrUtils.arrStr(parts));
		
		return parts;
	}
}
