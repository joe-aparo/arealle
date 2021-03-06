package ind.jsa.crib.ds.internal.type.convert.bson;

import java.util.Calendar;

import org.bson.BsonDateTime;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;

public class ToCoreCalUtils {
	private ToCoreCalUtils() {}
	
	public static Calendar bsonStr2Cal(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToCalUtils.str2Cal(((BsonString) val).toString()) : null;
	}
	
	public static Calendar bsonInt2Cal(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToCalUtils.lng2Cal(((BsonInt32) val).longValue()) : null;
	}
	
	public static Calendar bsonLng2Cal(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToCalUtils.lng2Cal(((BsonInt64) val).longValue()) : null;
	}
	
	public static Calendar bsonDt2Cal(BsonDateTime val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToCalUtils.lng2Cal(((BsonDateTime) val).getValue()) : null;
	}
}
