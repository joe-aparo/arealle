package ind.jsa.crib.ds.internal.type.convert.bson;

import java.util.Date;

import org.bson.BsonBinary;
import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToCoreStrUtils {
	private ToCoreStrUtils() {}
	
	public static String bsonStr2Str(BsonString val) {
		return val != null ? val.getValue() : null;
	}

	public static String bsonInt2Str(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.lng2Str(((BsonInt32) val).longValue()) : null;
	}
	
	public static String bsonLng2Str(BsonInt64 val) {
		return val != null ?
			ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.lng2Str(((BsonInt64) val).longValue()) : null;
	}
	
	public static String bsonDbl2Str(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.dbl2Str(((BsonDouble) val).doubleValue()) : null;
	}
	
	public static String bsonNum2Str(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.dbl2Str(((BsonNumber) val).doubleValue()) : null;
	}
	
	public static String bsonDtToStr(BsonDateTime val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.dt2Str(new Date(((BsonDateTime) val).getValue())) : null;
	}
	
	public static String bsonBool2Str(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils.bool2Str(((BsonBoolean) val).getValue()) : null;
	}
	
	public static String bsonBinary2Str(BsonBinary val) {
		return val != null ? val.asString().toString() : null;
	}
}
