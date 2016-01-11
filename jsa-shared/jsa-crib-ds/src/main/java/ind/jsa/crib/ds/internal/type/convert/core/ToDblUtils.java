package ind.jsa.crib.ds.internal.type.convert.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

import net.jsa.common.logging.LogUtils;

public class ToDblUtils {
	private ToDblUtils() {
	}

	public static Double str2Dbl(String val) {
		Double ret = null;
		try {
			ret = Double.parseDouble(val);
		} catch (NumberFormatException ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn("Unable to convert string " + val + " to a double value");
		}
		return ret;
	}

	public static Double byte2Dbl(Byte val) {
		return val != null ? Double.valueOf(val.doubleValue()) : null;
	}

	public static Double chr2Dbl(Character val) {
		return val != null ? Double.valueOf(Character.getNumericValue(val)) : null;
	}

	public static Double shrt2Dbl(Short val) {
		return val != null ? Double.valueOf(val.intValue()) : null;
	}

	public static Double int2Dbl(Integer val) {
		return val != null ? Double.valueOf(val.intValue()) : null;
	}

	public static Double lng2Dbl(Long val) {
		return val != null ? Double.valueOf(val.longValue()) : null;
	}

	public static Double bgi2Dbl(BigInteger val) {
		return val != null ? Double.valueOf(val.longValue()) : null;
	}

	public static Double flt2Dbl(Float val) {
		return val != null ? Double.valueOf(val.floatValue()) : null;
	}

	public static Double bgd2Dbl(BigDecimal val) {
		return val != null ? Double.valueOf(val.doubleValue()) : null;
	}

	public static Double bool2Dbl(Boolean val) {
		return Double.valueOf(val != null && val ? 1 : 0);
	}

	public static Double sqlDt2Bgd(java.sql.Date val) {
		return val != null ? Double.valueOf(val.getTime()) : null;
	}

	public static Double ts2Bgd(Timestamp val) {
		return val != null ? Double.valueOf(val.getTime()) : null;
	}
}
