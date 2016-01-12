package ind.jsa.crib.ds.internal.type.convert.bson;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.bson.BsonBoolean;

public class ToBsonBoolUtils {
	private ToBsonBoolUtils() {}
	
	public static BsonBoolean str2Bool (String val) {
		Boolean x = ind.jsa.crib.ds.internal.type.convert.core.ToBoolUtils.str2Bool(val);
		return x != null ? new BsonBoolean(x) : null;
	}

	public static BsonBoolean byte2Bool (Byte val) {
		Boolean x = ind.jsa.crib.ds.internal.type.convert.core.ToBoolUtils.byte2Bool(val);
		return x != null ? new BsonBoolean(x) : null;
	}

	public static BsonBoolean chr2Bool (Character val) {
		Boolean x = ind.jsa.crib.ds.internal.type.convert.core.ToBoolUtils.chr2Bool(val);
		return x != null ? new BsonBoolean(x) : null;
	}
	
	public static BsonBoolean shrt2Bool (Short val) {
		Boolean x = ind.jsa.crib.ds.internal.type.convert.core.ToBoolUtils.shrt2Bool(val);
		return x != null ? new BsonBoolean(x) : null;
	}
	
	public static BsonBoolean int2Bool (Integer val) {
		Boolean x = ind.jsa.crib.ds.internal.type.convert.core.ToBoolUtils.int2Bool(val);
		return x != null ? new BsonBoolean(x) : null;
	}
	
	public static BsonBoolean lng2Bool (Long val) {
		Boolean x = ind.jsa.crib.ds.internal.type.convert.core.ToBoolUtils.lng2Bool(val);
		return x != null ? new BsonBoolean(x) : null;
	}
	
	public static BsonBoolean bgi2Bool(BigInteger val) {
		Boolean x = ind.jsa.crib.ds.internal.type.convert.core.ToBoolUtils.bgi2Bool(val);
		return x != null ? new BsonBoolean(x) : null;
	}
	
	public static BsonBoolean flt2Bool (Float val) {
		Boolean x = ind.jsa.crib.ds.internal.type.convert.core.ToBoolUtils.flt2Bool(val);
		return x != null ? new BsonBoolean(x) : null;
	}
	
	public static BsonBoolean dbl2Bool (Double val) {
		Boolean x = ind.jsa.crib.ds.internal.type.convert.core.ToBoolUtils.dbl2Bool(val);
		return x != null ? new BsonBoolean(x) : null;
	}
	
	public static BsonBoolean bgd2Bool (BigDecimal val) {
		Boolean x = ind.jsa.crib.ds.internal.type.convert.core.ToBoolUtils.bgd2Bool(val);
		return x != null ? new BsonBoolean(x) : null;
	}
}
