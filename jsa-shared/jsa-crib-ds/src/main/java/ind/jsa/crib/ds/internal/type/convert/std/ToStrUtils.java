package ind.jsa.crib.ds.internal.type.convert.std;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToStrUtils {

	private ToStrUtils() {
	}

	public static String byte2Str(Byte val) {
		return val != null ? val.toString() : null;
	}
	
	public static String chr2Str(Character val) {
		return val != null ? val.toString() : null;
	}
	
	public static String shrt2Str(Short val) {
		return val != null ? val.toString() : null;
	}
	
	public static String int2Str(Integer val) {
		return val != null ? val.toString() : null;
	}
	
	public static String lng2Str(Long val) {
		return val != null ? val.toString() : null;
	}
	
	public static String bgi2Str(BigInteger val) {
		return val != null ? val.toString() : null;
	}
	
	public static String flt2Str(Float val) {
		return val != null ? val.toString() : null;
	}
	
	public static String dbl2Str(Double val) {
		return val != null ? val.toString() : null;
	}
	
	public static String bgd2Str(BigDecimal val) {
		return val != null ? val.toString() : null;
	}
	
	public static String cal2Str(Calendar val) {
		return convertDateToStr(val.getTime());
	}
	
	public static String dt2Str(Date val) {
		return convertDateToStr(val);
	}
	
	public static String sqlDt2Str(Date val) {
		return convertDateToStr(val);
	}
	
	public static String ts2Str(Timestamp val) {
		return convertDateToStr(val);
	}
	
	public static String bool2Str(Boolean val) {
		return val != null ? val.toString() : null;
	}

	/**
	 * Utility to convert a date object to a string.
	 * 
	 * @param dt A date object to convert
	 * @param pattern A conversion pattern
	 * @return A formatted date string
	 */
	public static String convertDateToStr(Date dt) {
		if (dt == null) {
			return null;
		}
		
		String pattern = DateTimePattern.UNIVERSAL_DATETIME_PATTERN.toString();
        return new SimpleDateFormat(pattern).format(dt);		
	}
}
