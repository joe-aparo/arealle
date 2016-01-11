package ind.jsa.crib.ds.internal.type.convert.bson;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToFltUtils {
	private ToFltUtils() {}
	
	public static Float bsonStr2Flt(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToFltUtils.str2Flt(((BsonString) val).toString()) : null;
	}
	
	public static Float bsonInt2Flt(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToFltUtils.lng2Flt(((BsonInt32) val).longValue()) : null;
	}
	
	public static Float bsonLng2Flt(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToFltUtils.lng2Flt(((BsonInt64) val).longValue()) : null;
	}
	
	public static Float bsonDbl2Flt(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToFltUtils.dbl2Flt(((BsonDouble) val).doubleValue()) : null;	
	}
	
	public static Float bsonNbr2Flt(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToFltUtils.dbl2Flt(((BsonNumber) val).doubleValue()) : null;
	}
	
	public static Float bsonBool2Flt(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToFltUtils.bool2Flt(((BsonBoolean) val).getValue()) : null;
	}
}
