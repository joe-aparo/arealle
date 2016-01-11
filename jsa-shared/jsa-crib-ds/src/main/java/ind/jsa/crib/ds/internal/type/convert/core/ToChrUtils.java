package ind.jsa.crib.ds.internal.type.convert.core;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;

public class ToChrUtils {
	private ToChrUtils() {
	}

	public static Character str2Chr(String val) {
		return convertStrToChar(val);
	}
	
	public static Character byte2Chr(Byte val) {
		return convertStrToChar(val.toString());
	}
	
	public static Character shrt2Chr(Short val) {
		return convertStrToChar(val.toString());
	}
	
	public static Character int2Chr(Integer val) {
		return convertStrToChar(val.toString());
	}
	
	public static Character lng2Chr(Long val) {
		return convertStrToChar(val.toString());
	}
	
	public static Character bgi2Chr(BigInteger val) {
		return convertStrToChar(val.toString());
	}
	
	public static Character flt2Chr(Float val) {
		return convertStrToChar(val.toString());
	}
	
	public static Character dbl2Chr(Double val) {
		return convertStrToChar(val.toString());
	}
	
	public static Character bgd2Chr(BigDecimal val) {
		return convertStrToChar(val.toString());
	}
	
	public static Character bool2Chr(Boolean val) {
		return convertStrToChar(val != null && val ? "1" : "0");
	}
	
	/**
	 * Utility for converting a string to a character object. This is
	 * done by simply taking the first character of the string as the value.
	 * 
	 * @param val String to convert
	 * @return A Character object
	 */
	private static Character convertStrToChar(String val) {
		return val != null && !StringUtils.isEmpty(val) ? Character.valueOf(val.charAt(0)) : null;
	}
}
