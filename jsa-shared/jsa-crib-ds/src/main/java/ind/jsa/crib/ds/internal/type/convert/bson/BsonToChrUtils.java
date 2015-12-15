package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToChrUtils;

import org.bson.BsonBoolean;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class BsonToChrUtils {
	private BsonToChrUtils() {
	}

	public static Character str2Chr(BsonString val) {
		return val != null ? ToChrUtils.str2Chr(val.toString()) : null;
	}
	
	public static Character int2Chr(BsonInt32 val) {
		return val != null ? ToChrUtils.int2Chr(val.getValue()) : null;
	}
	
	public static Character lng2Chr(BsonInt64 val) {
		return val != null ? ToChrUtils.lng2Chr(val.longValue()) : null;
	}
	
	public static Character dbl2Chr(BsonDouble val) {
		return val != null ? ToChrUtils.dbl2Chr(val.doubleValue()) : null;
	}
	
	public static Character num2Chr(BsonNumber val) {
		return val != null ? ToChrUtils.dbl2Chr(val.doubleValue()) : null;
	}
	
	public static Character bool2Chr(BsonBoolean val) {
		return val != null ? ToChrUtils.bool2Chr(val.getValue()) : null;
	}
}
