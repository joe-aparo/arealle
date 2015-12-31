package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToFltUtils;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class BsonToFltUtils {
	private BsonToFltUtils() {
	}
	
	public static Float str2Flt(BsonString val) {
		return val != null ? ToFltUtils.str2Flt(val.toString()) : null;
	}

	public static Float int2Flt(BsonInt32 val) {
		return val != null ? ToFltUtils.int2Flt(val.getValue()) : null;
	}

	public static Float lng2Flt(BsonInt64 val) {
		return val != null ? ToFltUtils.lng2Flt(val.longValue()) : null;
	}

	public static Float dbl2Flt(BsonDouble val) {
		return val != null ? ToFltUtils.dbl2Flt(val.doubleValue()) : null;
	}

	public static Float num2Flt(BsonNumber val) {
		return val != null ? ToFltUtils.dbl2Flt(val.doubleValue()) : null;
	}

	public static Float bool2Flt(BsonBoolean val) {
		return val != null ? ToFltUtils.bool2Flt(val.getValue()) : null;
	}
}
