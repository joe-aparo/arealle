package net.jsa.crib.ds.utils.type;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.jsa.common.logging.LogUtils;

public class ToByteUtils {
	private ToByteUtils() {
	}

	public static Byte str2Byte (String val) {
		Byte ret = null;
		try {
			ret = Byte.parseByte(val);
		} catch (NumberFormatException ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn("Unable to convert string " + val + " to a byte value");
		}
		return ret;
	}
	
	public static Byte chr2Byte (Character val) {
		return (val != null) ? convertIntToByte(Character.getNumericValue(val)) : null;
	}

	public static Byte shrt2Byte(Short val) {
		return (val != null) ? convertIntToByte(val) : null;
	}
	
	public static Byte int2Byte(Integer val) {
		return (val != null) ? convertIntToByte(val) : null;
	}
	
	public static Byte lng2Byte(Long val) {
		return (val != null) ? convertIntToByte(val.intValue()) : null;
	}
	
	public static Byte bgi2Byte(BigInteger val) {
		return (val != null) ? convertIntToByte(val.intValue()) : null;
	}
	
	public static Byte flt2Byte(Float val) {
		return (val != null) ? convertIntToByte(val.intValue()) : null;
	}
	
	public static Byte dbl2Byte(Double val) {
		return (val != null) ? convertIntToByte(val.intValue()) : null;
	}
	
	public static Byte bgd2Byte(BigDecimal val) {
		return (val != null) ? convertIntToByte(val.intValue()) : null;
	}
	
	public static Byte bool2Byte(Boolean val) {
		return (val != null) ? convertIntToByte(val ? 1 : 0) : null;
	}
	
    /**
     * Utility for converting an primitive integer to a byte object.
     * 
     * @param val Integer to convert
     * @return A Byte object
     */
	public static Byte convertIntToByte(int val) {
		Byte ret = null;
		
		if (val >= Byte.MAX_VALUE) {
			ret = Byte.MAX_VALUE; // Loss of precision
		} else if (val <= Byte.MIN_VALUE) {
			ret = Byte.MIN_VALUE; // Loss of precision
		} else {
			ret = new Integer(val).byteValue();
		}
		
		return ret;
	}
	
}
