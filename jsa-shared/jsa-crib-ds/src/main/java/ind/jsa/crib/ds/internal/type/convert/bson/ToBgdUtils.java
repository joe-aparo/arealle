package ind.jsa.crib.ds.internal.type.convert.bson;

import java.math.BigDecimal;

import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToBgdUtils {
	public ToBgdUtils() {}
	
	public static BigDecimal bsonStr2Bgd(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBgdUtils.str2Bgd(((BsonString) val).toString()) : null;
	}
	
	public static BigDecimal bsonInt2Bgd(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBgdUtils.lng2Bgd(((BsonInt32) val).longValue()) : null;		
	}
	
	public static BigDecimal bsonLng2Bgd(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBgdUtils.lng2Bgd(((BsonInt64) val).longValue()) : null;
	}
	
	public static BigDecimal bsonDbl2Bgd(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBgdUtils.dbl2Bgd(((BsonDouble) val).doubleValue()) : null;
	}
	
	public static BigDecimal bsonNbr2Bgd(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBgdUtils.dbl2Bgd(((BsonNumber) val).doubleValue()) : null;
	}
	
	public static BigDecimal bsonDt2Bgd(BsonDateTime val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBgdUtils.lng2Bgd(((BsonDateTime) val).getValue()) : null;
	}
	
	public static BigDecimal bsonBool2Bgd(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.std.ToBgdUtils.bool2Bgd(((BsonBoolean) val).getValue()) : null;
	}
}
