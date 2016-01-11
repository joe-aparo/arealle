package ind.jsa.crib.ds.internal.type.convert.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.jsa.common.logging.LogUtils;

public class ToCalUtils {
    private ToCalUtils() {
	}

	public static Calendar str2Cal(String val) {
		Calendar ret = null;
		String pattern = DateTimePattern.UNIVERSAL_DATETIME_PATTERN.toString();
		
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        try {
            Date dt = fmt.parse(val);
            ret = Calendar.getInstance();
            ret.setTime(dt);
        } catch (Exception ex) {
			// Intentionally swallow, log, and return null
			LogUtils.getLogger().warn("Unable to convert string " + val + " with format " + pattern + " to a calendar value");
        }
		
		return ret;
	}
	
	public static Calendar lng2Cal(Long val) {
		return val != null ? convertLngToCal(val.longValue()) : null;
	}

	public static Calendar bgi2Cal(BigInteger val) {
		return val != null ? convertLngToCal(val.longValue()) : null;
	}

	public static Calendar bgd2Cal(BigDecimal val) {
		return val != null ? convertLngToCal(val.longValue()) : null;
	}

	public static Calendar dt2Cal(Date val) {
		return val != null ? convertLngToCal(val.getTime()) : null;
	}

	public static Calendar sqlDt2Cal(Date val) {
		return val != null ? ind.jsa.crib.ds.internal.type.convert.core.ToCalUtils.convertLngToCal(val.getTime()) : null;
	}

	public static Calendar ts2Cal(Timestamp val) {
		return val != null ? ind.jsa.crib.ds.internal.type.convert.core.ToCalUtils.convertLngToCal(val.getTime()) : null;
	}

	public static Calendar convertLngToCal(long val) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(val));
		
		return cal;
	}	
}
