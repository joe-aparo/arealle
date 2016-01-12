package ind.jsa.crib.ds.internal.type.convert.bson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.bson.BsonString;

public class ToBsonStringUtils {
	public ToBsonStringUtils() {}

	public static BsonString str2Str(String val) {
		return val != null ? new BsonString(val.toString()) : null;
	}
	
	public static BsonString byte2Str(Byte val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.byte2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString chr2Str(Character val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.chr2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString shrt2Str(Short val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.shrt2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString int2Str(Integer val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.int2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString lng2Str(Long val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.lng2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString bgi2Str(BigInteger val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.bgi2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString flt2Str(Float val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.flt2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString dbl2Str(Double val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.dbl2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString bgd2Str(BigDecimal val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.bgd2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString cal2Str(Calendar val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.cal2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString dt2Str(Date val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.dt2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString sqlDt2Str(Date val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.sqlDt2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString ts2Str(Timestamp val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.ts2Str(val);
		return x != null ? new BsonString(x) : null;
	}
	
	public static BsonString bool2Str(Boolean val) {
		String x = ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.bool2Str(val);
		return x != null ? new BsonString(x) : null;
	}
}
