package ind.jsa.crib.ds.internal.type.convert.bson;

import java.util.Date;

import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToStrUtils {
	private ToStrUtils() {}
	
	public static String bsonStr2Str(BsonString val) {
		return val != null ? val.getValue() : null;
	}

	public static String bsonInt2Str(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToStrUtils.lng2Str(((BsonInt32) val).longValue()) : null;
	}
	
	public static String bsonLng2Str(BsonInt64 val) {
		return val != null ?
			ind.jsa.crib.ds.internal.type.convert.std.ToStrUtils.lng2Str(((BsonInt64) val).longValue()) : null;
	}
	
	public static String bsonDbl2Str(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToStrUtils.dbl2Str(((BsonDouble) val).doubleValue()) : null;
	}
	
	public static String bsonNbr2Str(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToStrUtils.dbl2Str(((BsonNumber) val).doubleValue()) : null;
	}
	
	public static String bsonDtToStr(BsonDateTime val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToStrUtils.dt2Str(new Date(((BsonDateTime) val).getValue())) : null;
	}
	
	public static String bsonBool2Str(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToStrUtils.bool2Str(((BsonBoolean) val).getValue()) : null;
	}
}