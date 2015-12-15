package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToDblUtils;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class BsonToDblUtils {
	private BsonToDblUtils() {
	}

	public static Double str2Dbl(BsonString val) {
		return val != null ? ToDblUtils.str2Dbl(val.toString()) : null;
	}

	public static Double int2Dbl(BsonInt32 val) {
		return val != null ? ToDblUtils.int2Dbl(val.getValue()) : null;
	}

	public static Double lng2Dbl(BsonInt64 val) {
		return val != null ? ToDblUtils.lng2Dbl(val.longValue()) : null;
	}

	public static Double bgi2Dbl(BsonDouble val) {
		return val != null ? Double.valueOf(val.doubleValue()) : null;
	}

	public static Double flt2Dbl(BsonNumber val) {
		return val != null ? Double.valueOf(val.doubleValue()) : null;
	}

	public static Double bool2Dbl(BsonBoolean val) {
		return val != null ? ToDblUtils.bool2Dbl(val.getValue()) : null;
	}
}
