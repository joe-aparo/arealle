package ind.jsa.crib.ds.internal.type.convert.core;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.jsa.common.logging.LogUtils;

public class ToFltUtils {
	private ToFltUtils() {
	}
	
	public static Float str2Flt(String val) {
		Float ret = null;
		try {
			ret = Float.parseFloat(val);
		} catch (NumberFormatException ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn("Unable to convert string " + val + " to a float value");
		}
		return ret;
	}

	public static Float byte2Flt(Byte val) {
		return val != null ? Float.valueOf(val.floatValue()) : null;
	}

	public static Float chr2Flt(Character val) {
		return val != null ? Float.valueOf(Character.getNumericValue(val)) : null;
	}

	public static Float shrt2Flt(Short val) {
		return val != null ? Float.valueOf(val.intValue()) : null;
	}

	public static Float int2Flt(Integer val) {
		return val != null ? Float.valueOf(val.intValue()) : null;
	}

	public static Float lng2Flt(Long val) {
		return val != null ? Float.valueOf(val.longValue()) : null;
	}

	public static Float bgi2Flt(BigInteger val) {
		return val != null ? Float.valueOf(val.longValue()) : null;
	}

	public static Float dbl2Flt(Double val) {
		return val != null ? Float.valueOf(val.floatValue()) : null;
	}

	public static Float bgd2Flt(BigDecimal val) {
		return val != null ? Float.valueOf(val.floatValue()) : null;
	}

	public static Float bool2Flt(Boolean val) {
		return Float.valueOf(val != null && val ? 1 : 0);
	}
}
