package ind.jsa.crib.ds.internal.type.convert.bson;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToByteUtils {
	private ToByteUtils() {
	}
	
	public Byte bsonString2Byte(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToByteUtils.str2Byte(((BsonString) val).toString()) : null;
	}
	
	public Byte bsonInt2Byte(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToByteUtils.lng2Byte(((BsonInt32) val).longValue()) : null;
	}
	
	public Byte bsonLng2Byte(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToByteUtils.lng2Byte(((BsonInt64) val).longValue()) : null;
	}
	
	public Byte bsonDbl2Byte(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToByteUtils.dbl2Byte(((BsonDouble) val).doubleValue()) : null;
	}
	
	public Byte bsonNbr2Byte(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToByteUtils.dbl2Byte(((BsonNumber) val).doubleValue()) : null;
	}
	
	public Byte bsonBool2Byte(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToByteUtils.bool2Byte(((BsonBoolean) val).getValue()) : null;
	}
}
