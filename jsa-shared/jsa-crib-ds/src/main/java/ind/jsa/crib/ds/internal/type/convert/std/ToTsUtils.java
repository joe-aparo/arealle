package ind.jsa.crib.ds.internal.type.convert.std;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.jsa.common.logging.LogUtils;

public class ToTsUtils {
	private ToTsUtils() {
	}

	public static Timestamp str2Ts(String val) {
		Timestamp ret = null;
		String pattern = DateTimePattern.UNIVERSAL_TIMESTAMP_PATTERN.toString();
		
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        try {
            Date dt = fmt.parse(val);
            ret = new Timestamp(dt.getTime());
        } catch (Exception ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn(
				"Unable to convert string " + val + " with format " + pattern + " to a timestamp value");
        }
        return ret;
	}

	public static Timestamp lng2Ts(Long val) {
		return val != null ? convertLngToTs(val.longValue()) : null;
	}

	public static Timestamp bgi2Ts(BigInteger val) {
		return val != null ? convertLngToTs(val.longValue()) : null;
	}

	public static Timestamp bgd2Ts(BigDecimal val) {
		return val != null ? convertLngToTs(val.longValue()) : null;
	}

	public static Timestamp cal2Ts(Calendar val) {
		return val != null ? convertLngToTs(val.getTimeInMillis()) : null;
	}

	public static Timestamp dt2Ts(Date val) {
		return val != null ? convertLngToTs(val.getTime()) : null;
	}
	
	private static Timestamp convertLngToTs(long val) {
		return new Timestamp(new Date(val).getTime());
	}
}
