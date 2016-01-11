package ind.jsa.crib.ds.internal.type.convert.sql;

import java.sql.Timestamp;

public class ToLngUtils {
	private ToLngUtils() {}
	
	public static Long sqlDt2Lng(java.sql.Date val) {
		return val != null ? Long.valueOf(val.getTime()) : null;
	}
	
	public static Long ts2Lng(Timestamp val) {
		return val != null ? Long.valueOf(val.getTime()) : null;
	}
}
