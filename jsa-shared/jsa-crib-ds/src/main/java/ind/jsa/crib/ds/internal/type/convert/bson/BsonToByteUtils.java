package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToByteUtils;

import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class BsonToByteUtils {
	private BsonToByteUtils() {
	}

	public static Byte str2Byte (BsonString val) {
		return val != null ? ToByteUtils.str2Byte(val.getValue()) : null;
	}
	
	public static Byte int2Byte(BsonInt32 val) {
		return val != null ? ToByteUtils.int2Byte(val.getValue()) : null;
	}
	
	public static Byte lng2Byte(BsonInt64 val) {
		return val != null ? ToByteUtils.lng2Byte(val.getValue()) : null;
	}
	
	public static Byte dbl2Byte(BsonDouble val) {
		return val != null ? ToByteUtils.dbl2Byte(val.getValue()) : null;
	}
	
	public static Byte num2Byte(BsonNumber val) {
		return val != null ? ToByteUtils.lng2Byte(val.longValue()) : null;
	}
	
	public static Byte bool2Byte(Boolean val) {
		return val != null ? ToByteUtils.bool2Byte(val.booleanValue()) : null;
	}
}
