package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToDtUtils;

import java.util.Date;

import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;
import org.bson.BsonTimestamp;

public class BsonToDtUtils {
	private BsonToDtUtils() {
	}

	public static Date str2Dt(BsonString val) {
		return val != null ? ToDtUtils.str2Dt(val.toString()) : null;
	}

	public static Date lng2Dt(BsonInt32 val) {
		return val != null ? ToDtUtils.lng2Dt(val.longValue()) : null;
	}

	public static Date bgi2Dt(BsonInt64 val) {
		return val != null ? ToDtUtils.lng2Dt(val.longValue()) : null;
	}

	public static Date bgi2Dt(BsonDouble val) {
		return val != null ? new Date(val.longValue()) : null;
	}

	public static Date bgd2Dt(BsonNumber val) {
		return val != null ? new Date(val.longValue()) : null;
	}

	public static Date dt2Dt(BsonDateTime val) {
		return val != null ? new Date(val.getValue()) : null;
	}

	public static Date ts2Dt(BsonTimestamp val) {
		return val != null ? new Date(val.getTime()) : null;
	}
}
