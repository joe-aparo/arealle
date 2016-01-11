package ind.jsa.crib.ds.internal.type.convert.std;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Calendar;
import java.util.Date;

import net.jsa.common.logging.LogUtils;

public class ToLngUtils {

	private ToLngUtils() {
	}

	public static Long str2Lng(String val) {
		Long ret = null;
		try {
			ret = Long.parseLong(val);
		} catch (NumberFormatException ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn("Unable to convert string " + val + " to a long value");
		}
		return ret;
	}

	public static Long byte2Lng(Byte val) {
		return val != null ? Long.valueOf(val.longValue()) : null;
	}

	public static Long chr2Lng(Character val) {
		return val != null ? Long.valueOf(Character.getNumericValue(val)) : null;
	}

	public static Long shrt2Lng(Short val) {
		return val != null ? Long.valueOf(val.longValue()) : null;
	}

	public static Long int2Lng(Integer val) {
		return val != null ? Long.valueOf(val.longValue()) : null;
	}

	public static Long bgi2Lng(BigInteger val) {
		return val != null ? Long.valueOf(val.longValue()) : null;
	}

	public static Long flt2Lng(Float val) {
		return val != null ? Long.valueOf(val.longValue()) : null;
	}

	public static Long dbl2Lng(Double val) {
		return val != null ? Long.valueOf(val.longValue()) : null;
	}

	public static Long bgd2Lng(BigDecimal val) {
		return val != null ? Long.valueOf(val.longValue()) : null;
	}

	public static Long cal2Lng(Calendar val) {
		return val != null ? Long.valueOf(val.getTimeInMillis()) : null;
	}

	public static Long dt2Lng(Date val) {
		return val != null ? Long.valueOf(val.getTime()) : null;
	}

	public static Long bool2Lng(Boolean val) {
		return Long.valueOf(val != null && val ? 1 : 0);
	}
}
