package ind.jsa.crib.ds.internal.type.convert.bson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;

import org.bson.BsonDateTime;

public class ToBsonDtUtils {
	private ToBsonDtUtils() {}
	
	public static BsonDateTime str2Dt(String val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.str2Lng(val);
		return x != null ? new BsonDateTime(x) : null;
	}

	public static BsonDateTime lng2Dt(Long val) {
		return val != null ? new BsonDateTime(val) : null;
	}

	public static BsonDateTime bgi2Dt(BigInteger val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.bgi2Lng(val);
		return x != null ? new BsonDateTime(x) : null;
	}

	public static BsonDateTime bgd2Dt(BigDecimal val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.bgd2Lng(val);
		return x != null ? new BsonDateTime(x) : null;
	}

	public static BsonDateTime cal2Dt(Calendar val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.cal2Lng(val);
		return x != null ? new BsonDateTime(x) : null;
	}

	public static BsonDateTime sqlDt2Dt(java.sql.Date val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.sqlDt2Lng(val);
		return x != null ? new BsonDateTime(x) : null;
	}
	
	public static BsonDateTime ts2Dt(Timestamp val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.ts2Lng(val);
		return x != null ? new BsonDateTime(x) : null;
	}

}
