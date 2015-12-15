package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToIntUtils;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;


public class BsonToIntUtils {
	private BsonToIntUtils() {
	}

	public static Integer str2Int(BsonString val) {
		return val != null ? ToIntUtils.str2Int(val.getValue()) : null;
	}

	public static Integer int2Int(BsonInt32 val) {
		return val != null ? Integer.valueOf(val.getValue()) : null;
	}

	public static Integer lng2Int(BsonInt64 val) {
		return val != null ? ToIntUtils.lng2Int(val.longValue()) : null;
	}

	public static Integer dbl2Int(BsonDouble val) {
		return val != null ? ToIntUtils.dbl2Int(val.doubleValue()) : null;
	}

	public static Integer num2Int(BsonNumber val) {
		return val != null ? ToIntUtils.dbl2Int(val.doubleValue()) : null;
	}

	public static Integer bool2Int(BsonBoolean val) {
		return val != null ? ToIntUtils.bool2Int(val.getValue()) : null;
	}
}
