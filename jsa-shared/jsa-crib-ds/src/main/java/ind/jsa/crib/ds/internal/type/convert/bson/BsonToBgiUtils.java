package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToBgiUtils;

import java.math.BigInteger;

import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;
import org.bson.BsonTimestamp;

public class BsonToBgiUtils {
	private BsonToBgiUtils() {
	}

	public static BigInteger str2Bgi(BsonString val) {
		return val != null ? ToBgiUtils.str2Bgi(val.toString()) : null;
	}

	public static BigInteger int2Bgi(BsonInt32 val) {
		return val != null ? ToBgiUtils.int2Bgi(val.getValue()) : null;
	}

	public static BigInteger lng2Bgi(BsonInt64 val) {
		return val != null ? ToBgiUtils.lng2Bgi(val.getValue()) : null;
	}

	public static BigInteger dbl2Bgi(BsonDouble val) {
		return val != null ? ToBgiUtils.dbl2Bgi(val.getValue()) : null;
	}

	public static BigInteger num2Bgi(BsonNumber val) {
		return val != null ? ToBgiUtils.lng2Bgi(val.longValue()) : null;
	}
	
	public static BigInteger bool2Bgi(BsonBoolean val) {
		return val != null ? ToBgiUtils.bool2Bgi(val.getValue()) : null;
	}
	
	public static BigInteger dt2Bgi(BsonDateTime val) {
		return val != null ? ToBgiUtils.lng2Bgi(val.getValue()) : null;
	}
	
	public static BigInteger ts2Bgi(BsonTimestamp val) {
		return val != null ? ToBgiUtils.int2Bgi(val.getTime()) : null;
	}
}
