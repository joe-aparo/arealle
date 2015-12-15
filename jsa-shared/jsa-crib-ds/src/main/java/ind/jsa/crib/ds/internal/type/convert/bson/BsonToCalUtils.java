package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToCalUtils;

import java.util.Calendar;

import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;
import org.bson.BsonTimestamp;

public class BsonToCalUtils {
    private BsonToCalUtils() {
	}

	public static Calendar str2Cal(BsonString val) {
		return val != null ? ToCalUtils.str2Cal(val.toString()) : null;
	}
	
	public static Calendar int2Cal(BsonInt32 val) {
		return val != null ? ToCalUtils.lng2Cal(val.longValue()) : null;
	}

	public static Calendar lng2Cal(BsonInt64 val) {
		return val != null ? ToCalUtils.lng2Cal(val.longValue()) : null;
	}

	public static Calendar bgi2Cal(BsonDouble val) {
		return val != null ? ToCalUtils.lng2Cal(val.longValue()) : null;
	}

	public static Calendar bgd2Cal(BsonNumber val) {
		return val != null ? ToCalUtils.lng2Cal(val.longValue()) : null;
	}

	public static Calendar dt2Cal(BsonDateTime val) {
		return val != null ? ToCalUtils.lng2Cal(val.getValue()) : null;
	}

	public static Calendar ts2Cal(BsonTimestamp val) {
		return val != null ? ToCalUtils.lng2Cal(Long.valueOf(val.getTime())) : null;
	}
}
