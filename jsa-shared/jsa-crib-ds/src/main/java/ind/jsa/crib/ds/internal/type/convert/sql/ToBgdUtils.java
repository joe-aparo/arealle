package ind.jsa.crib.ds.internal.type.convert.sql;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ToBgdUtils {
	private ToBgdUtils() {}
	
	public static BigDecimal sqlDt2Bgd(java.sql.Date val) {
		return val != null ? BigDecimal.valueOf(val.getTime()) : null;
	}

	public static BigDecimal ts2Bgd(Timestamp val) {
		return val != null ? BigDecimal.valueOf(val.getTime()) : null;
	}
}
