package ind.jsa.crib.ds.internal.type.convert.sql;

import java.sql.Timestamp;
import java.util.Date;

public class ToDtUtils {
	private ToDtUtils() {}
	
	public static Date sqlDt2Dt(java.sql.Date val) {
		return val != null ? new Date(val.getTime()) : null;
	}
	
	public static Date ts2Dt(Timestamp val) {
		return val != null ? new Date(val.getTime()) : null;
	}
}
