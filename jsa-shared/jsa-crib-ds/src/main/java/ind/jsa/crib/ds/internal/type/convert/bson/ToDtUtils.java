package ind.jsa.crib.ds.internal.type.convert.bson;

import java.util.Date;

import org.bson.BsonDateTime;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;

public class ToDtUtils {
	private ToDtUtils() {}
	
	public static Date bsonStr2Dt(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToDtUtils.str2Dt(((BsonString) val).toString()) : null;
	}
	
	public static Date bsonInt2Dt(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToDtUtils.lng2Dt(((BsonInt32) val).longValue()) : null;
	}
	
	public static Date bsonLng2Dt(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToDtUtils.lng2Dt(((BsonInt64) val).longValue()) : null;
	}
	
	public static Date bsonDt2Dt(BsonDateTime val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToDtUtils.lng2Dt(((BsonDateTime) val).getValue()) : null;
	}
}
