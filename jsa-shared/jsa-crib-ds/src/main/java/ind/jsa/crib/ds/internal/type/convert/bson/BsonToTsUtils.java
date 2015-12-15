package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToTsUtils;

import java.sql.Timestamp;

import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class BsonToTsUtils {
	private BsonToTsUtils() {
	}

	public static Timestamp str2Ts(BsonString val) {
        return val != null ? ToTsUtils.str2Ts(val.toString()) : null;
	}

	public static Timestamp int2Ts(BsonInt32 val) {
		return val != null ? ToTsUtils.lng2Ts(val.longValue()) : null;
	}

	public static Timestamp lng2Ts(BsonInt64 val) {
		return val != null ? ToTsUtils.lng2Ts(val.longValue()) : null;
	}

	public static Timestamp dbl2Ts(BsonDouble val) {
		return val != null ? ToTsUtils.lng2Ts(val.longValue()) : null;
	}

	public static Timestamp num2Ts(BsonNumber val) {
		return val != null ? ToTsUtils.lng2Ts(val.longValue()) : null;
	}

	public static Timestamp dt2Ts(BsonDateTime val) {
		return val != null ? ToTsUtils.lng2Ts(val.getValue()) : null;
	}
}
