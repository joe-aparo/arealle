package ind.jsa.crib.ds.internal.type.convert.bson;

import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToCoreLngUtils {
	private ToCoreLngUtils() {}
	
	public static Long bsonStr2Lng(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.str2Lng(((BsonString) val).toString()) : null;
	}
	
	public static Long bsonInt2Lng(BsonInt32 val) {
		return val != null ? Long.valueOf(((BsonInt32) val).longValue()) : null;
	}
	
	public static Long bsonLng2Lng(BsonInt64 val) {
		return val != null ? Long.valueOf(((BsonInt64) val).longValue()) : null;
	}
	
	public static Long bsonDbl2Lng(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.dbl2Lng(((BsonDouble) val).doubleValue()) : null;
	}
	
	public static Long bsonNbr2Lng(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.dbl2Lng(((BsonNumber) val).doubleValue()) : null;
	}
	
	public static Long bsonDtToLng(BsonDateTime val) {
		return val != null ? Long.valueOf(((BsonDateTime) val).getValue()) : null;
	}
	
	public static Long bsonBool2Lng(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.bool2Lng(((BsonBoolean) val).getValue()) : null;
	}
}
