package ind.jsa.crib.ds.internal.type.convert.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.jsa.common.logging.LogUtils;

public class ToDtUtils {
	private ToDtUtils() {
	}

	public static Date str2Dt(String val) {
		Date ret = null;
		String pattern = DateTimePattern.UNIVERSAL_DATETIME_PATTERN.toString();
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        try {
            ret = fmt.parse(val);
        } catch (Exception ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn("Unable to convert string " + val + " with format " + pattern + " to a date value");
        }
        return ret;
	}

	public static Date lng2Dt(Long val) {
		return val != null ? new Date(val.longValue()) : null;
	}

	public static Date bgi2Dt(BigInteger val) {
		return val != null ? new Date(val.longValue()) : null;
	}

	public static Date bgd2Dt(BigDecimal val) {
		return val != null ? new Date(val.longValue()) : null;
	}

	public static Date cal2Dt(Calendar val) {
		return val != null ? new Date(val.getTimeInMillis()) : null;
	}

	public static Date sqlDt2Dt(java.sql.Date val) {
		return val != null ? new Date(val.getTime()) : null;
	}
	
	public static Date ts2Dt(Timestamp val) {
		return val != null ? new Date(val.getTime()) : null;
	}
}
