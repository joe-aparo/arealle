package net.jsa.crib.ds.utils.type;

import java.math.BigDecimal;

import net.jsa.common.logging.LogUtils;

public class ToIntUtils {
	private ToIntUtils() {
	}

	public static Integer str2Int(String val) {
		Integer ret = null;
		try {
			ret = Integer.parseInt(val);
		} catch (NumberFormatException ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn("Unable to convert string " + val + " to an integer value");
		}
		return ret;
	}

	public static Integer byte2Int(Byte val) {
		return val != null ? Integer.valueOf(val.intValue()) : null;
	}

	public static Integer chr2Int(Character val) {
		return val != null ? Integer.valueOf(Character.getNumericValue(val)) : null;
	}

	public static Integer shrt2Int(Short val) {
		return val != null ? Integer.valueOf(val.intValue()) : null;
	}

	public static Integer lng2Int(Long val) {
		return val != null ? Integer.valueOf(val.intValue()) : null;
	}

	public static Integer flt2Int(Float val) {
		return val != null ? Integer.valueOf(val.intValue()) : null;
	}

	public static Integer dbl2Int(Double val) {
		return val != null ? Integer.valueOf(val.intValue()) : null;
	}

	public static Integer bgd2Int(BigDecimal val) {
		return val != null ? Integer.valueOf(val.intValue()) : null;
	}

	public static Integer bool2Int(Boolean val) {
		return Integer.valueOf(val != null && val ? 1 : 0);
	}
}
