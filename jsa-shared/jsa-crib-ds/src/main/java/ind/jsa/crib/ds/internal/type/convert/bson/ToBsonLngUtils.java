package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.std.ToLngUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import net.jsa.common.logging.LogUtils;

import org.bson.BsonInt64;

public class ToBsonLngUtils {
	private ToBsonLngUtils() {
	}

	public static BsonInt64 str2Lng(String val) {
		return val != null ? new BsonInt64(ToLngUtils.str2Lng(val)) : null;
	}

	public static BsonInt64 byte2Lng(Byte val) {
		return val != null ? new BsonInt64(ToLngUtils.byte2Lng(val)) : null;
	}

	public static BsonInt64 chr2Lng(Character val) {
		return val != null ? new BsonInt64(ToLngUtils.chr2Lng(val)) : null;
	}

	public static BsonInt64 shrt2Lng(Short val) {
		return val != null ? new BsonInt64(ToLngUtils.shrt2Lng(val)) : null;
	}

	public static BsonInt64 int2Lng(Integer val) {
		return val != null ? new BsonInt64(Long.valueOf(val.longValue())) : null;
	}

	public static BsonInt64 bgi2Lng(BigInteger val) {
		return val != null ? new BsonInt64(Long.valueOf(val.longValue())) : null;
	}

	public static BsonInt64 flt2Lng(Float val) {
		return val != null ? new BsonInt64(Long.valueOf(val.longValue())) : null;
	}

	public static BsonInt64 dbl2Lng(Double val) {
		return val != null ? new BsonInt64(Long.valueOf(val.longValue())) : null;
	}

	public static BsonInt64 bgd2Lng(BigDecimal val) {
		return val != null ? new BsonInt64(Long.valueOf(val.longValue())) : null;
	}

	public static BsonInt64 cal2Lng(Calendar val) {
		return val != null ? new BsonInt64(Long.valueOf(val.getTimeInMillis())) : null;
	}

	public static BsonInt64 dt2Lng(Date val) {
		return val != null ? new BsonInt64(Long.valueOf(val.getTime())): null;
	}

	public static BsonInt64 ts2Lng(Timestamp val) {
		return val != null ? new BsonInt64(Long.valueOf(val.getTime())) : null;
	}

	public static BsonInt64 bool2Lng(Boolean val) {
		return new BsonInt64(Long.valueOf(val != null && val ? 1 : 0));
	}

}
