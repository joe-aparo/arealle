package ind.jsa.crib.ds.internal.type.convert.bson;

import java.math.BigDecimal;

import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

public class ToCoreBgdUtils {
	public ToCoreBgdUtils() {}
	
	public static BigDecimal bsonStr2Bgd(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgdUtils.str2Bgd(((BsonString) val).toString()) : null;
	}
	
	public static BigDecimal bsonInt2Bgd(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgdUtils.lng2Bgd(((BsonInt32) val).longValue()) : null;		
	}
	
	public static BigDecimal bsonLng2Bgd(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgdUtils.lng2Bgd(((BsonInt64) val).longValue()) : null;
	}
	
	public static BigDecimal bsonDbl2Bgd(BsonDouble val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgdUtils.dbl2Bgd(((BsonDouble) val).doubleValue()) : null;
	}
	
	public static BigDecimal bsonNum2Bgd(BsonNumber val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgdUtils.dbl2Bgd(((BsonNumber) val).doubleValue()) : null;
	}
	
	public static BigDecimal bsonDt2Bgd(BsonDateTime val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgdUtils.lng2Bgd(((BsonDateTime) val).getValue()) : null;
	}
	
	public static BigDecimal bsonBool2Bgd(BsonBoolean val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToBgdUtils.bool2Bgd(((BsonBoolean) val).getValue()) : null;
	}
}
