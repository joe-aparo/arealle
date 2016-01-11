package ind.jsa.crib.ds.internal.type.convert.sql;

import java.math.BigInteger;
import java.sql.Timestamp;

public class ToBgiUtils {
	private ToBgiUtils() {}
	
	public static BigInteger sqlDt2Bgi(java.sql.Date val) {
		return val != null ? BigInteger.valueOf(val.getTime()) : null;
	}
	
	public static BigInteger ts2Bgi(Timestamp val) {
		return val != null ? BigInteger.valueOf(val.getTime()) : null;
	}
}
