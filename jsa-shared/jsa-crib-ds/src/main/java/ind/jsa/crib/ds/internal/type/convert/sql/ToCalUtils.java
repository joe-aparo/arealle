package ind.jsa.crib.ds.internal.type.convert.sql;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class ToCalUtils {
	private ToCalUtils() {}
	
	public static Calendar sqlDt2Cal(Date val) {
		return val != null ? ind.jsa.crib.ds.internal.type.convert.std.ToCalUtils.convertLngToCal(val.getTime()) : null;
	}

	public static Calendar ts2Cal(Timestamp val) {
		return val != null ? ind.jsa.crib.ds.internal.type.convert.std.ToCalUtils.convertLngToCal(val.getTime()) : null;
	}
}
