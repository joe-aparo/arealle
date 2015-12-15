package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToBoolUtils;

import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class BsonToBoolUtils {
	private BsonToBoolUtils() {
	}

	public static Boolean str2Bool (BsonString val) {
		return val != null ? ToBoolUtils.str2Bool(val.getValue()) : null;
	}

	public static Boolean int2Bool (BsonInt32 val) {
		return val != null ? ToBoolUtils.int2Bool(val.getValue()) : null;
	}
	
	public static Boolean lng2Bool (BsonInt64 val) {
		return val != null ? ToBoolUtils.lng2Bool(val.getValue()) : null;
	}
	public static Boolean dbl2Bool (BsonDouble val) {
		return val != null ? ToBoolUtils.dbl2Bool(val.getValue()) : null;
	}
	
	public static Boolean bgd2Bool (BsonNumber val) {
		return val != null ? ToBoolUtils.lng2Bool(val.longValue()) : null;
	}
}
