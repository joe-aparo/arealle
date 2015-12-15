package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToShrtUtils;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class BsonToShrtUtils {
	private BsonToShrtUtils() {
	}

	public static Short str2Shrt(BsonString val) {
		return val != null ? ToShrtUtils.str2Shrt(val.toString()) : null;
	}

	public static Short byte2Shrt(Byte val) {
		return val != null ? Short.valueOf(val.shortValue()) : null;
	}

	public static Short int2Shrt(BsonInt32 val) {
		return val != null ? ToShrtUtils.int2Shrt(val.intValue()) : null;
	}

	public static Short lng2Shrt(BsonInt64 val) {
		return val != null ? ToShrtUtils.lng2Shrt(val.longValue()) : null;
	}
	
	public static Short num2Shrt(BsonNumber val) {
		return val != null ? ToShrtUtils.int2Shrt(val.intValue()) : null;
	}
	
	public static Short dbl2Shrt(BsonDouble val) {
		return val != null ? ToShrtUtils.dbl2Shrt(val.doubleValue()) : null;
	}
	
	public static Short bool2Shrt(BsonBoolean val) {
		return val != null ? ToShrtUtils.bool2Shrt(val.getValue()) : null;
	}
}