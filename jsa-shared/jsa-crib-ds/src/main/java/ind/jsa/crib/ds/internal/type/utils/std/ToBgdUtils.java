package ind.jsa.crib.ds.internal.type.utils.std;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import net.jsa.common.logging.LogUtils;

public class ToBgdUtils {
	private ToBgdUtils() {
	}

	public static BigDecimal str2Bgd(String val) {
		BigDecimal ret = null;

		try {
			ret = new BigDecimal(val);
		} catch (NumberFormatException ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn("Unable to convert string " + val + " to a big decimal value");
		}
		
		return ret;
	}

	public static BigDecimal byte2Bgd(Byte val) {
		return val != null ? BigDecimal.valueOf(val) : null;
	}

	public static BigDecimal chr2Bgd(Character val) {
		return val != null ? BigDecimal.valueOf(Long.valueOf(val.toString())) : null;
	}

	public static BigDecimal shrt2Bgd(Short val) {
		return val != null ? BigDecimal.valueOf(val) : null;
	}

	public static BigDecimal int2Bgd(Integer val) {
		return val != null ? BigDecimal.valueOf(val) : null;
	}

	public static BigDecimal lng2Bgd(Long val) {
		return val != null ? BigDecimal.valueOf(val) : null;
	}

	public static BigDecimal bgi2Bgd(BigInteger val) {
		return val != null ? BigDecimal.valueOf(val.longValue()) : null;
	}
	
	public static BigDecimal flt2Bgd(Float val) {
		if (val == null) {
			return null;
		}
		
		// Force the scale of the result to match the decimal point location of the float
		// value represented as a string.
		return BigDecimal.valueOf(val.doubleValue()).setScale(calcScale(val.toString()), BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal dbl2Bgd(Double val) {
		return val != null ? BigDecimal.valueOf(val) : null;
	}

	public static BigDecimal cal2Bgd(Calendar val) {
		return val != null ? BigDecimal.valueOf(val.getTimeInMillis()) : null;
	}

	public static BigDecimal dt2Bgd(Date val) {
		return val != null ? BigDecimal.valueOf(val.getTime()) : null;
	}

	public static BigDecimal ts2Bgd(Timestamp val) {
		return val != null ? BigDecimal.valueOf(val.getTime()) : null;
	}

	public static BigDecimal bool2Bgd(Boolean val) {
		return val != null ? BigDecimal.valueOf(val ? 1 : 0) : null;
	}
	
	private static int calcScale(String decStr) {
		if (StringUtils.isEmpty(decStr)) {
			return 0;
		}
		
		int pos = decStr.indexOf('.');
		if (pos == -1) {
			return 0;
		}
		
		return decStr.length() - (pos + 1);
	}
}
