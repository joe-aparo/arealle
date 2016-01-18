package ind.jsa.crib.ds.internal.type.convert.bson;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToCoreIntUtils {
	private ToCoreIntUtils() {}
	
	public static Integer bsonStr2Int(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.str2Int(((BsonString) val).toString()) : null;
	}
	
	public static Integer bsonInt2Int(BsonInt32 val) {
		return val != null ? Integer.valueOf(((BsonInt32) val).getValue()) : null;
	}
	
	public static Integer bsonLng2Int(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.lng2Int(((BsonInt64) val).longValue()) : null;
	}
	
	public static Integer bsonDbl2Int(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.dbl2Int(((BsonDouble) val).doubleValue()) : null;
	}
	
	public static Integer bsonNum2Int(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.dbl2Int(((BsonNumber) val).doubleValue()) : null;
	}
	
	public static Integer bsonBool2Int(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.bool2Int(((BsonBoolean) val).getValue()) : null;
	}
}
