package net.jsa.crib.ds.utils.type;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.jsa.common.logging.LogUtils;

public class ToBoolUtils {
	private ToBoolUtils() {
	}

	public static Boolean str2Bool (String val) {
		Boolean ret = null;
		
        if (val.length() == 1) {
            Character c = Character.toLowerCase(val.charAt(0));
            ret = (c == 'y' || c == 't' || c == 'x');
        } else if (val.length() > 1) {
            try {
                ret = Boolean.parseBoolean(val);
            } catch (NumberFormatException ex) {
    			// Intentionally swallow, log, and return null
    			LogUtils.getLogger().warn("Unable to convert string " + val + " to a boolean value");
            }
        }
        
        return ret;
	}

	public static Boolean byte2Bool (Byte val) {
		return val != null && val != 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	public static Boolean chr2Bool (Character val) {
		return val != null && val != '0' ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public static Boolean shrt2Bool (Short val) {
		return val != null && val != 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public static Boolean int2Bool (Integer val) {
		return val != null && val != 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public static Boolean lng2Bool (Long val) {
		return val != null && val != 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public static Boolean bgi2Bool(BigInteger val) {
		return val != null && val != BigInteger.ZERO ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public static Boolean flt2Bool (Float val) {
		return val != null && val != 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public static Boolean dbl2Bool (Double val) {
		return val != null && val != 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public static Boolean bgd2Bool (BigDecimal val) {
		return val != null && val != BigDecimal.ZERO ? Boolean.TRUE : Boolean.FALSE;
	}
}
