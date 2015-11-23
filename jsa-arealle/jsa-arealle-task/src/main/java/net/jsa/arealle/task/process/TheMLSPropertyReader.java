package net.jsa.arealle.task.process;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import net.jsa.arealle.dto.Property;
import net.jsa.arealle.dto.PropertyListing;
import net.jsa.arealle.task.pojo.AddressInfo;
import net.jsa.arealle.task.util.AcreCalc;
import net.jsa.arealle.task.util.INameNormalizer;
import net.jsa.crib.ds.utils.type.ToDblUtils;
import net.jsa.crib.ds.utils.type.ToIntUtils;

@Component
public class TheMLSPropertyReader extends AbstractSitePropertyReader {
	private static final String SESSION_ID_PREFIX = "<form action=\"saveresults,";
	private static final String SESSION_ID_SUFFIX = ".html";
	private static final String COUNT_INFO_PREFIX = "Viewing listings &nbsp;&nbsp;";
	private static final String COUNT_INFO_SUFFIX = "</td>";
	private static final String HIT_COUNT_PREFIX = "of &nbsp;";
	private static final String LISTING_PREFIX = "<div id=\"listingtbl";
	private static final String LISTING_SUFFIX = "</table>";
	private static final String MLSID_PREFIX = "_";
	private static final String MLSID_SUFFIX = "\"";
	private static final String ADDR_INFO_PREFIX = "<div class=\"results_address\">";
	private static final String ADDR_INFO_SUFFIX = "</div>";
	private static final String ADDRESS_PREFIX = "/>";
	private static final String PRICE_PREFIX = "<span class=\"results_price\">$";
	private static final String PRICE_SUFFIX = ".";
	private static final String RESULTS2_PREFIX = "<div class=\"results_data2\"";
	private static final String RESULTS2_SUFFIX = "</div>";
	private static final String TYPE_PREFIX = "<span class=\"results_style\"><strong>";
	private static final String TYPE_SUFFIX = "</strong>";
	private static final String BED_COUNT_PREFIX = "<strong>";
	private static final String BED_COUNT_SUFFIX = " beds";
	private static final String RESULTS3_PREFIX = "<div class=\"results_data3\"";
	private static final String RESULTS3_SUFFIX = "</div>";
	private static final String BATH_COUNT_PREFIX = "<strong>";
	private static final String BATH_COUNT_SUFFIX = " baths";
	private static final String GARAGE_COUNT_PREFIX = "</strong><br />";
	private static final String GARAGE_COUNT_SUFFIX = " garage spaces";
	private static final String LISTING_BROKER_PREFIX = "<span title=\"Listing courtesy of ";
	private static final String LISTING_BROKER_SUFFIX = "\">";
	private static final String LIVING_AREA_PREFIX = "beds</strong><br />";
	private static final String LIVING_AREA_SUFFIX = " sq ft";
	private static final String YEAR_BUILT_PREFIX = "Built in ";
	private static final String YEAR_BUILT_SUFFIX = " &nbsp;";
	private static final String ACRES_PREFIX = "sq ft<br />";
	private static final String ACRES_SUFFIX = " acre";
	private static final int ITEMS_PER_PAGE = 10;
	private static final String BASE_IMAGE_URL = "http://photos.themlsonline.com";
	
	// TheMLS property type names. These are converted to Arealle equivalent types and unit counts when applicable
	/*
		Single Family
		Multi-Family
		Condo/Co-op
		Lot/Land
		Commercial
		Business Opportunity
	 */
	private static Map<String, PropertyType> propTypeMap = new HashMap<String, PropertyType>();
	
	static {
		propTypeMap.put("Single&nbsp;Family", PropertyType.SINGLE);
		propTypeMap.put("Condo/Co-op", PropertyType.CONDO);
		propTypeMap.put("Business Opportunity", PropertyType.OTHER);
		propTypeMap.put("Multi-Family", PropertyType.MULTI);
		propTypeMap.put("Commercial", PropertyType.OTHER);
		propTypeMap.put("Lot/Land", PropertyType.LAND);
	}
	
	@Resource(name="unitNormalizer")
	INameNormalizer unitNormalizer;
	
	private String sessionId;

	// TODO - data-drive these values
	private String regionId = "Boston";
	private static final String THE_MLSONLINE_SOURCE_ID = "THEMLSONLINE";
	
	public TheMLSPropertyReader() {
		super("TheMLS Listing Scraper", "http://www.themlsonline.com");
	}

	public boolean start(Map<String, Object> context) {
		super.start(context);
		
		// Init run variables
		sessionId = null;
		
		// Fetch search home (starting point) page
		String content = readUrl(makeSearchHomeUrl());	
		if (StringUtils.isEmpty(content)) {
			return false;
		}
		
		// Read search page content
		processSearchPage(content);
		
		// Post url and get response
		content = postUrl(makeSearchUrl(), createPostAttrs());
		
		if (StringUtils.isEmpty(content)) {
			return false;
		}
		
		initResults(content);
		
		return true;
	}
	
	@Override
	public boolean nextItem() {
		if (!super.nextItem()) {
			return false;
		}
		
		extractListings(readUrl(makeResultsPageUrl()));
		
		return true;
	}

	protected void processSearchPage(String content) {
		sessionId = extract(content, SESSION_ID_PREFIX, SESSION_ID_SUFFIX);
	}

	protected String makeRootUrl() {
		return getBaseUrl() + '/' + getContext().get("STATE_ID") + '/' + regionId;
	}
	
	protected String makeSearchHomeUrl() {
		return makeRootUrl();
	}

	protected String makeSearchUrl() {
		return makeRootUrl() + "/saveresults," + sessionId + ".html";
	}

	protected String makeResultsPageUrl() {
		return makeRootUrl() + "/results," + sessionId + "," + getCurPage() + ",0.html";
	}
	
	protected void initResults(String content) {
		setCurPage(1);
		
		Integer hitCount = ToIntUtils.str2Int(
			extract(extract(content, COUNT_INFO_PREFIX, COUNT_INFO_SUFFIX), HIT_COUNT_PREFIX));
		
		int totalPages = hitCount / ITEMS_PER_PAGE;
		if (hitCount % ITEMS_PER_PAGE != 0) {
			totalPages++;
		}

		setTotalPages(totalPages);

		extractListings(content);
	}
	
	private void extractListings(String content) {
		int pos = 0;
		
		getLog().debug("Extracting listings for page: " + getCurPage());
		
		while ((pos = content.indexOf(LISTING_PREFIX, pos)) != -1) {
			// Extract listing from point of next listing prefix
			extractListing(extract(content, LISTING_PREFIX, LISTING_SUFFIX, pos));

			// Advance next starting point past current listing prefix
			pos += LISTING_PREFIX.length();
		}
	}
	
	private void extractListing(String content) {
		String srcPropId = extract(content, MLSID_PREFIX, MLSID_SUFFIX);
		if (StringUtils.isEmpty(srcPropId)) {
			getLog().warn("No source identifier provided for MLS property: " + content);
			return;			
		}
		
		String addrStr = extract(extract(content, ADDR_INFO_PREFIX, ADDR_INFO_SUFFIX), ADDRESS_PREFIX);
		if (StringUtils.isEmpty(addrStr)) {
			getLog().warn("No address provided for property: " + srcPropId);
			return;			
		}
		
		AddressInfo addrInfo = splitRoadAndAddress(addrStr);
		if (addrInfo == null) {
			getLog().warn("Could not parse address string: " + addrInfo);
			return;			
		}
		
		String propTypeKey = extract(content, TYPE_PREFIX, TYPE_SUFFIX);
		PropertyType propType = null;
		if (!StringUtils.isEmpty(propTypeKey)) {
			propType = propTypeMap.get(propTypeKey);
		}
		
		if (propType == null) {
			getLog().warn("Could not derive property type for: " + propTypeKey);
			return;
		}
		
		Property prop = new Property();
		prop.setStateId((String) getContext().get("STATE_ID"));
		prop.setTownId((String) getContext().get("TOWN_ID"));
		prop.setType(propTypeKey);
		prop.setRoadName(addrInfo.getRoadName());
		prop.setAddrNum(addrInfo.getAddress().getNumber());
		prop.setAddrStr(addrInfo.getAddress().getString());
		
		PropertyListing listing = new PropertyListing();
		listing.setListingSourceId(THE_MLSONLINE_SOURCE_ID);
		listing.setSourcePropertyId(srcPropId);
		listing.setDetailUrl(makeRootUrl() + "/" + srcPropId);
		listing.setImgUrl(BASE_IMAGE_URL + "/" + regionId + "/s/" + srcPropId.substring(0, 4) + "/" + srcPropId + "_0.jpg");
		listing.setPrice(ToIntUtils.str2Int(makeIntStr(extract(content, PRICE_PREFIX, PRICE_SUFFIX))));
		listing.setUnit(unitNormalizer.normalize(StringUtils.EMPTY));
		
		String results2 = extract(content, RESULTS2_PREFIX, RESULTS2_SUFFIX);
		
		String acres = extract(results2, ACRES_PREFIX, ACRES_SUFFIX);
		if (!StringUtils.isEmpty(acres)) {
			Double dbl = ToDblUtils.str2Dbl(acres);
			if (dbl != null) {
				listing.setLandAreaSqFt(AcreCalc.calcSqFeet(dbl, AcreCalc.SQUARE_FEET_UNITS));
				listing.setLandAreaAcres(AcreCalc.calcAcres(dbl, AcreCalc.SQUARE_FEET_UNITS));
			}
		}
		
		String yrBlt = extract(results2, YEAR_BUILT_PREFIX,  YEAR_BUILT_SUFFIX);
		if (!StringUtils.isEmpty(yrBlt)) {
			listing.setYearBuilt(ToIntUtils.str2Int(yrBlt));
		}
		
		String bedCt = extract(results2, BED_COUNT_PREFIX, BED_COUNT_SUFFIX);
		if (!StringUtils.isEmpty(bedCt)) {
			listing.setBedRooms(ToIntUtils.str2Int(bedCt));
		}
		
		String lvgArea = extract(results2, LIVING_AREA_PREFIX, LIVING_AREA_SUFFIX);
		if (!StringUtils.isEmpty(lvgArea)) {
			listing.setLivingArea(ToIntUtils.str2Int(StringUtils.remove(lvgArea, ',')));
		}
		
		String results3 = extract(content, RESULTS3_PREFIX, RESULTS3_SUFFIX);
		
		String bathCt = extract(results3, BATH_COUNT_PREFIX, BATH_COUNT_SUFFIX);
		if (!StringUtils.isEmpty(bathCt)) {
			listing.setBathRooms(ToDblUtils.str2Dbl(bathCt));
		}
		
		String bayCt = extract(results3, GARAGE_COUNT_PREFIX, GARAGE_COUNT_SUFFIX);
		if (!StringUtils.isEmpty(bayCt)) {
			listing.setGarageBays(ToIntUtils.str2Int(bayCt));
		}
		
		listing.setBroker(extract(content, LISTING_BROKER_PREFIX, LISTING_BROKER_SUFFIX));
		
		getPropertyCache().putProperty(prop, listing);
	}

	protected Map<String, String> createPostAttrs() {
		// Create post attributes
		Map<String, String> attrs = new HashMap<String, String>(1);
		attrs.put("city[]", fetchTownName());
		
		return attrs;
	}
}
