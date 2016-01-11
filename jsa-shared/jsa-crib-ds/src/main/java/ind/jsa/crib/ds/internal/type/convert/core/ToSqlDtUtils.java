package ind.jsa.crib.ds.internal.type.convert.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

import net.jsa.common.logging.LogUtils;

public class ToSqlDtUtils {
	private ToSqlDtUtils() {
	}

	public static Date str2SqlDt(String val) {
		Date ret = null;
		String pattern = DateTimePattern.UNIVERSAL_DATETIME_PATTERN.toString();
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        try {
            ret = new Date(fmt.parse(val).getTime());
        } catch (Exception ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn("Unable to convert string " + val + " with format " + pattern + " to a date value");
        }
        return ret;
	}

	public static Date lng2SqlDt(Long val) {
		return val != null ? new Date(val.longValue()) : null;
	}

	public static Date bgi2SqlDt(BigInteger val) {
		return val != null ? new Date(val.longValue()) : null;
	}

	public static Date bgd2SqlDt(BigDecimal val) {
		return val != null ? new Date(val.longValue()) : null;
	}

	public static Date cal2SqlDt(Calendar val) {
		return val != null ? new Date(val.getTimeInMillis()) : null;
	}

	public static Date dt2SqlDt(java.util.Date val) {
		return val != null ? new Date(val.getTime()) : null;
	}
	
	public static Date ts2SqlDt(Timestamp val) {
		return val != null ? new Date(val.getTime()) : null;
	}
}
