package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToLngUtils;

import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;
import org.bson.BsonTimestamp;

public class BsonToLngUtils {

	private BsonToLngUtils() {
	}

	public static Long str2Lng(BsonString val) {
		return val != null ? ToLngUtils.str2Lng(val.toString()) : null;
	}

	public static Long int2Lng(BsonInt32 val) {
		return val != null ? ToLngUtils.int2Lng(val.intValue()) : null;
	}

	public static Long lng2Lng(BsonInt64 val) {
		return val != null ? Long.valueOf(val.longValue()) : null;
	}

	public static Long dbl2Lng(BsonDouble val) {
		return val != null ? ToLngUtils.dbl2Lng(val.doubleValue()) : null;
	}

	public static Long num2Lng(BsonNumber val) {
		return val != null ? ToLngUtils.dbl2Lng(val.doubleValue()) : null;
	}

	public static Long dt2Lng(BsonDateTime val) {
		return val != null ? Long.valueOf(val.getValue()) : null;
	}

	public static Long ts2Lng(BsonTimestamp val) {
		return val != null ? Long.valueOf(val.getTime()) : null;
	}

	public static Long bool2Lng(BsonBoolean val) {
		return val != null ? ToLngUtils.bool2Lng(val.getValue()) : null;
	}
}
