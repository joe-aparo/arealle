package ind.jsa.crib.ds.internal.type.convert.sql;

import java.sql.Timestamp;

public class ToDblUtils {
	private ToDblUtils() {}
	
	public static Double sqlDt2Bgd(java.sql.Date val) {
		return val != null ? Double.valueOf(val.getTime()) : null;
	}

	public static Double ts2Bgd(Timestamp val) {
		return val != null ? Double.valueOf(val.getTime()) : null;
	}
}
