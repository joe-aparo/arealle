package ind.jsa.crib.ds.internal.type.convert.bson;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.bson.BsonInt64;

public class ToBsonInt64Utils {
	private ToBsonInt64Utils() {
	}

	public static BsonInt64 str2Lng(String val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.str2Lng(val);
		return x != null ? new BsonInt64(x) : null;
	}

	public static BsonInt64 byte2Lng(Byte val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.byte2Lng(val);
		return x != null ? new BsonInt64(x) : null;
	}

	public static BsonInt64 chr2Lng(Character val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.chr2Lng(val);
		return x != null ? new BsonInt64(x) : null;
	}

	public static BsonInt64 shrt2Lng(Short val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.shrt2Lng(val);
		return x != null ? new BsonInt64(x) : null;
	}

	public static BsonInt64 lng2Lng(Long val) {
		return val != null ? new BsonInt64(val) : null;
	}

	public static BsonInt64 flt2Lng(Float val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.flt2Lng(val);
		return x != null ? new BsonInt64(x) : null;
	}

	public static BsonInt64 dbl2Lng(Double val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.dbl2Lng(val);
		return x != null ? new BsonInt64(x) : null;
	}

	public static BsonInt64 bgi2Lng(BigInteger val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.bgi2Lng(val);
		return x != null ? new BsonInt64(x) : null;
	}

	public static BsonInt64 bgd2Lng(BigDecimal val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.bgd2Lng(val);
		return x != null ? new BsonInt64(x) : null;
	}

	public static BsonInt64 bool2Lng(Boolean val) {
		Long x = ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils.bool2Lng(val);
		return x != null ? new BsonInt64(x) : null;
	}
}
