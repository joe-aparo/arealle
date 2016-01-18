package ind.jsa.crib.ds.internal.type.convert.bson;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToCoreShrtUtils {
	private ToCoreShrtUtils() {
	}
	
	public static Short bsonStr2Shrt(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToShrtUtils.str2Shrt(((BsonString) val).toString()) : null;
	}
	
	public static Short bsonInt2Shrt(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToShrtUtils.lng2Shrt(((BsonInt32) val).longValue()) : null;
	}
	
	public static Short bsonLng2Shrt(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToShrtUtils.lng2Shrt(((BsonInt64) val).longValue()) : null;
	}
	
	public static Short bsonDbl2Shrt(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToShrtUtils.dbl2Shrt(((BsonDouble) val).doubleValue()) : null;
	}
	
	public static Short bsonNbr2Shrt(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToShrtUtils.dbl2Shrt(((BsonNumber) val).doubleValue()) : null;
	}
	
	public static Short bsonBool2Shrt(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToShrtUtils.bool2Shrt(((BsonBoolean) val).getValue()) : null;
	}
}
