package ind.jsa.crib.ds.internal.type.convert.bson;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;

public class ToDblUtils {
	private ToDblUtils() {}
	
	public static Double bsonStr2Dbl(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToDblUtils.str2Dbl(((BsonString) val).toString()) : null;
	}
	
	public static Double bsonInt2Dbl(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToDblUtils.lng2Dbl(((BsonInt32) val).longValue()) : null;		
	}
	
	public static Double bsonLng2Dbl(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToDblUtils.lng2Dbl(((BsonInt64) val).longValue()) : null;
	}
	
	public static Double bsonDouble2Dbl(BsonDouble val) {
		return val != null ? Double.valueOf(((BsonDouble) val).getValue()) : null;
	}

	public static Double bsonBool2Dbl(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToDblUtils.bool2Dbl(((BsonBoolean) val).getValue()) : null;
	}
}
