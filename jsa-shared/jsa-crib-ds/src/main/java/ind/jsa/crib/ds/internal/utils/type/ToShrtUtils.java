package ind.jsa.crib.ds.internal.utils.type;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.jsa.common.logging.LogUtils;

public class ToShrtUtils {
	private ToShrtUtils() {
	}

	public static Short str2Shrt(String val) {
		Short ret = null;
		try {
			ret = Short.parseShort(val);
		} catch (NumberFormatException ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn("Unable to convert string " + val + " to a short value");
		}
		return ret;
	}

	public static Short byte2Shrt(Byte val) {
		return val != null ? Short.valueOf(val.shortValue()) : null;
	}

	public static Short chr2Shrt(Character val) {
		return val != null ? Short.valueOf((short) Character.getNumericValue(val)) : null;
	}

	public static Short int2Shrt(Integer val) {
		return val != null ? Short.valueOf(val.shortValue()) : null;
	}

	public static Short lng2Shrt(Long val) {
		return val != null ? Short.valueOf(val.shortValue()) : null;
	}
	
	public static Short bgi2Shrt(BigInteger val) {
		return val != null ? Short.valueOf(val.shortValue()) : null;
	}
	
	public static Short flt2Shrt(Float val) {
		return val != null ? Short.valueOf(val.shortValue()) : null;
	}
	
	public static Short dbl2Shrt(Double val) {
		return val != null ? Short.valueOf(val.shortValue()) : null;
	}
	
	public static Short bgd2Shrt(BigDecimal val) {
		return val != null ? Short.valueOf(val.shortValue()) : null;
	}
	
	public static Short bool2Shrt(Boolean val) {
		return Short.valueOf((short) (val != null && val ? 1 : 0));
	}
}