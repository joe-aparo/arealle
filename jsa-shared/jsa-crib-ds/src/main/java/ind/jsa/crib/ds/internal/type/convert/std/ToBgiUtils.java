package ind.jsa.crib.ds.internal.type.convert.std;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import net.jsa.common.logging.LogUtils;

public class ToBgiUtils {
	private ToBgiUtils() {
	}

	public static BigInteger str2Bgi(String val) {
		BigInteger ret = null;

		try {
			ret = new BigInteger(val);
		} catch (NumberFormatException ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn("Unable to convert string " + val + " to a big integer value");
		}
		
		return ret;
	}

	public static BigInteger byte2Bgi(Byte val) {
		return val != null ? BigInteger.valueOf(val.longValue()) : null;
	}

	public static BigInteger chr2Bgi(Character val) {
		return val != null ? BigInteger.valueOf(Character.getNumericValue(val)) : null;
	}

	public static BigInteger shrt2Bgi(Short val) {
		return val != null ? BigInteger.valueOf(val.shortValue()) : null;
	}

	public static BigInteger int2Bgi(Integer val) {
		return val != null ? BigInteger.valueOf(val.intValue()) : null;
	}

	public static BigInteger lng2Bgi(Long val) {
		return val != null ? BigInteger.valueOf(val.longValue()) : null;
	}

	public static BigInteger flt2Bgi(Float val) {
		return val != null ? BigInteger.valueOf(val.longValue()) : null;
	}

	public static BigInteger dbl2Bgi(Double val) {
		return val != null ? BigInteger.valueOf(val.longValue()) : null;
	}

	public static BigInteger bgd2Bgi(BigDecimal val) {
		return val != null ? BigInteger.valueOf(val.longValue()) : null;
	}
	
	public static BigInteger bool2Bgi(Boolean val) {
		return val != null ? BigInteger.valueOf(val ? 1 : 0) : null;
	}
	
	public static BigInteger cal2Bgi(Calendar val) {
		return val != null ? BigInteger.valueOf(val.getTimeInMillis()) : null;
	}
	
	public static BigInteger dt2Bgi(Date val) {
		return val != null ? BigInteger.valueOf(val.getTime()) : null;
	}
	public static BigInteger sqlDt2Bgi(java.sql.Date val) {
		return val != null ? BigInteger.valueOf(val.getTime()) : null;
	}
	
	public static BigInteger ts2Bgi(Timestamp val) {
		return val != null ? BigInteger.valueOf(val.getTime()) : null;
	}
}
