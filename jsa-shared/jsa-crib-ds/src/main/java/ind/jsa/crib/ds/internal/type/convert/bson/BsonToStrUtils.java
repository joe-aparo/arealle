package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToStrUtils;

import java.sql.Timestamp;
import java.util.Date;

import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;
import org.bson.BsonTimestamp;

public class BsonToStrUtils {

	private BsonToStrUtils() {
	}
	
	public static String str2Str(BsonString val) {
		return val != null ? val.toString() : null;
	}
	
	public static String int2Str(BsonInt32 val) {
		return val != null ? val.toString() : null;
	}
	
	public static String long2Str(BsonInt64 val) {
		return val != null ? val.toString() : null;
	}
	
	public static String dbl2Str(BsonDouble val) {
		return val != null ? val.toString() : null;
	}
	
	public static String num2Str(BsonNumber val) {
		return val != null ? val.toString() : null;
	}
	
	public static String dt2Str(BsonDateTime val) {
		return ToStrUtils.dt2Str(new Date(val.getValue()));
	}
	
	public static String ts2Str(BsonTimestamp val) {
		return ToStrUtils.ts2Str(new Timestamp(val.getTime()));
	}
	
	public static String bool2Str(BsonBoolean val) {
		return val != null ? val.toString() : null;
	}
}
