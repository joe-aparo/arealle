package ind.jsa.crib.ds.internal.type.convert.bson;

import java.sql.Timestamp;

import org.bson.BsonDateTime;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;

public class ToTsUtils {
	private ToTsUtils() {}
	
	public static Timestamp bsonStr2Ts(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToTsUtils.str2Ts(((BsonString) val).toString()) : null;
	}
	
	public static Timestamp bsonInt2Ts(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToTsUtils.lng2Ts(((BsonInt32) val).longValue()) : null;
	}
	
	public static Timestamp bsonLng2Ts(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToTsUtils.lng2Ts(((BsonInt64) val).longValue()) : null;
	}
	
	public static Timestamp bsonDt2Ts(BsonDateTime val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToTsUtils.lng2Ts(((BsonDateTime) val).getValue()) : null;
	}
}
