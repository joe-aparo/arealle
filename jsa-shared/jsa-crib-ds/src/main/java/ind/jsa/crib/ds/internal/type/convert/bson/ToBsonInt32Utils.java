package ind.jsa.crib.ds.internal.type.convert.bson;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.bson.BsonInt32;

public class ToBsonInt32Utils {
	private ToBsonInt32Utils() {
	}

	public static BsonInt32 str2Int(String val) {
		Integer x = ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.str2Int(val);
		return x != null ? new BsonInt32(x) : null;
	}

	public static BsonInt32 byte2Int(Byte val) {
		Integer x = ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.byte2Int(val);
		return x != null ? new BsonInt32(x) : null;
	}

	public static BsonInt32 chr2Int(Character val) {
		Integer x = ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.chr2Int(val);
		return x != null ? new BsonInt32(x) : null;
	}

	public static BsonInt32 shrt2Int(Short val) {
		Integer x = ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.shrt2Int(val);
		return x != null ? new BsonInt32(x) : null;
	}

	public static BsonInt32 int2Int(Integer val) {
		return val != null ? new BsonInt32(val) : null;
	}
	
	public static BsonInt32 lng2Int(Long val) {
		Integer x = ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.lng2Int(val);
		return x != null ? new BsonInt32(x) : null;
	}

	public static BsonInt32 flt2Int(Float val) {
		Integer x = ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.flt2Int(val);
		return x != null ? new BsonInt32(x) : null;
	}

	public static BsonInt32 dbl2Int(Double val) {
		Integer x = ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.dbl2Int(val);
		return x != null ? new BsonInt32(x) : null;
	}

	public static BsonInt32 bgi2Int(BigInteger val) {
		Integer x = ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.bgi2Int(val);
		return x != null ? new BsonInt32(x) : null;
	}

	public static BsonInt32 bgd2Int(BigDecimal val) {
		Integer x = ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.bgd2Int(val);
		return x != null ? new BsonInt32(x) : null;
	}

	public static BsonInt32 bool2Int(Boolean val) {
		Integer x = ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils.bool2Int(val);
		return x != null ? new BsonInt32(x) : null;
	}
}
