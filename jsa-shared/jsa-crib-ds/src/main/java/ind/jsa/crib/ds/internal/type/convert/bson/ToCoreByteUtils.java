package ind.jsa.crib.ds.internal.type.convert.bson;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToCoreByteUtils {
	private ToCoreByteUtils() {
	}
	
	public static Byte bsonStr2Byte(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToByteUtils.str2Byte(((BsonString) val).toString()) : null;
	}
	
	public static Byte bsonInt2Byte(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToByteUtils.lng2Byte(((BsonInt32) val).longValue()) : null;
	}
	
	public static Byte bsonLng2Byte(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToByteUtils.lng2Byte(((BsonInt64) val).longValue()) : null;
	}
	
	public static Byte bsonDbl2Byte(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToByteUtils.dbl2Byte(((BsonDouble) val).doubleValue()) : null;
	}
	
	public static Byte bsonNum2Byte(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToByteUtils.dbl2Byte(((BsonNumber) val).doubleValue()) : null;
	}
	
	public static Byte bsonBool2Byte(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToByteUtils.bool2Byte(((BsonBoolean) val).getValue()) : null;
	}
}
