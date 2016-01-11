package ind.jsa.crib.ds.internal.type.convert.bson;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToChrUtils {
	public Character bsonStr2Chr(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToChrUtils.str2Chr(((BsonString) val).toString()) : null;
	}

	public Character bsonInt2Chr(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToChrUtils.lng2Chr(((BsonInt32) val).longValue()) : null;
	}
	
	public Character bsonLng2Chr(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToChrUtils.lng2Chr(((BsonInt64) val).longValue()) : null;
	}
	
	public Character bsonDbl2Chr(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToChrUtils.dbl2Chr(((BsonDouble) val).doubleValue()) : null;
	}
	
	public Character bsonNbr2Chr(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToChrUtils.dbl2Chr(((BsonNumber) val).doubleValue()) : null;
	}
	
	public Character bsonBool2Chr(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToChrUtils.bool2Chr(((BsonBoolean) val).getValue()) : null;
	}
}
