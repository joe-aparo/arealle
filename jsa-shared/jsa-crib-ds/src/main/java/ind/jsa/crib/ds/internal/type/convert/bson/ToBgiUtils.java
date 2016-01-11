package ind.jsa.crib.ds.internal.type.convert.bson;

import java.math.BigInteger;

import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToBgiUtils {
	private ToBgiUtils() {}
	
	public static BigInteger bsonStr2Bgi(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgiUtils.str2Bgi(((BsonString) val).toString()) : null;
	}
	
	public static BigInteger bsonInt2Bgi(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgiUtils.lng2Bgi(((BsonInt32) val).longValue()) : null;
	}
	
	public static BigInteger bsonLng2Bgi(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgiUtils.lng2Bgi(((BsonInt64) val).longValue()) : null;
	}
	
	public static BigInteger bsonDbl2Bgi(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgiUtils.dbl2Bgi(((BsonDouble) val).doubleValue()) : null;
	}
	
	public static BigInteger bsonNbr2Bgi(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgiUtils.dbl2Bgi(((BsonNumber) val).doubleValue()) : null;
	}
	
	public static BigInteger bsonDt2Bgi(BsonDateTime val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgiUtils.lng2Bgi(((BsonDateTime) val).getValue()) : null;
	}
	
	public static BigInteger bsonBool2Bgi(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgiUtils.bool2Bgi(((BsonBoolean) val).getValue()) : null;
	}
}
