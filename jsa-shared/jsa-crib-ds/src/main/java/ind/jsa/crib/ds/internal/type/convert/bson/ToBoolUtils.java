package ind.jsa.crib.ds.internal.type.convert.bson;

import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToBoolUtils {
	private ToBoolUtils() {}
	
	public static Boolean bsonStr2Bool(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBoolUtils.str2Bool(((BsonString) val).toString()) : null;
	}
	
	public static Boolean bsonInt2Bool(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBoolUtils.lng2Bool(((BsonInt32) val).longValue()) : null;
	}
	
	public static Boolean bsonLng2Bool(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBoolUtils.lng2Bool(((BsonInt64) val).longValue()) : null;
	}
	
	public static Boolean bsonDbl2Bool(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBoolUtils.dbl2Bool(((BsonDouble) val).doubleValue()) : null;
	}
	
	public static Boolean bsonNbr2Bool(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBoolUtils.dbl2Bool(((BsonNumber) val).doubleValue()) : null;
	}
}
