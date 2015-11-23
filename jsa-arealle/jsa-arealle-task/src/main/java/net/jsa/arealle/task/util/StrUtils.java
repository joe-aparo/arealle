package net.jsa.arealle.task.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import net.jsa.crib.ds.utils.type.ToIntUtils;

/**
 * Custom string utilities
 * @author jsaparo
 *
 */
public class StrUtils {
	private static final String NVP_DELIM = "$";
	private static final String NVP_SEP = " ";
		
	public static String properCase(String part) {
		if (StringUtils.isEmpty(part)) {
			return part;
		}
		
		int len = part.length();
		char[] buf = new char[len];
		boolean uc = true;
		
		for (int i = 0; i < len; i++) {
			Character ch = part.charAt(i);
			if (uc) {
				ch = Character.toUpperCase(ch);
				uc = false;
			} else if (ch == ' ' || ch == '-' || ch == '\\' || ch == '/') {
				uc = true;
			}

			buf[i] = ch;
		}
		
		return new String(buf);
	}
	
	public static String filterNonAlphaCharacters(String str) {
		char[] chars = str.toCharArray();
		StringBuilder buf = new StringBuilder(str.length());
		
		for (char ch : chars) {
			if (Character.isLetterOrDigit(ch) || ch == ' ') {
				buf.append(ch);
			}
		}
		
		return buf.toString();
	}
	
	public static String[] chop(String[] arr, int firstIdx, int lastIdx) {
		String[] newArr = new String[arr.length - (lastIdx - firstIdx + 1)];
		
		int k = 0;
		for (int i = 0; i < arr.length; i++) {
			if (i < firstIdx || i > lastIdx) {
				newArr[k++] = arr[i];
			}
		}
		
		return newArr;
	}	
	
	public static String[] slice(String[] arr, int firstIdx, int lastIdx) {
		String[] newArr = new String[lastIdx - firstIdx];
		
		int k = 0;
		for (int i = 0; i < arr.length; i++) {
			if (i >= firstIdx && i <= lastIdx) {
				newArr[k++] = arr[i];
			}
		}
		
		return newArr;
	}
	
	public static int extractNum(String str) {
		boolean inNum = false;
		int result = 0;
		StringBuilder numbuf = new StringBuilder(10);
		
		for (Character c : str.toCharArray()) {
			if (Character.isDigit(c)) {
				inNum = true;
				numbuf.append(c);
			} else if (inNum) {
				break;
			}
		}
		
		if (numbuf.length() > 0) {
			result = ToIntUtils.str2Int(numbuf.toString());
		}
		
		return result;
	}
	
	public static String firstWord(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		
		int pos = str.indexOf(' ');
		
		return pos != -1 ? str.substring(0, pos) : str;
	}
	
	public static String arrStr(String[] parts) {
		return StringUtils.join(parts, ", ");
	}

	public static String mapToString(Map<String, String> attrs) {
		return mapToString(attrs, NVP_SEP, NVP_DELIM);
	}	
	

	public static String mapToString(Map<String, String> attrs, String nvSep, String nvDelim) {
		StringBuilder buf = new StringBuilder();
		
		for (Entry<String, String> e : attrs.entrySet()) {
			if (buf.length() > 0) {
				buf.append(nvSep);
			}

			buf.append(e.getKey()).append(nvDelim).append(e.getValue());
		}

		return buf.toString();
	}	

	public static Map<String, String> stringToMap(String mapStr) {
		return stringToMap(mapStr, NVP_SEP, NVP_DELIM);
	}

	public static Map<String, String> stringToMap(String mapStr, String nvSep, String nvDelim) {
		if (StringUtils.isEmpty(mapStr)) {
			return null;
		}
		
		String[] nvps = StringUtils.split(mapStr, nvSep);
		Map<String, String> attrs = new HashMap<String, String>(nvps.length);
		for (String nvp : nvps) {
			String[] parts = StringUtils.split(nvp, nvDelim);
			if (parts.length == 2) {
				attrs.put(parts[0], parts[1]);
			}
		}
		
		return attrs;
	}

}
