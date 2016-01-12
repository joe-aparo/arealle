package ind.jsa.crib.ds.internal.type.convert.bson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

import org.bson.BsonDouble;

public class ToBsonDblUtils {
	private ToBsonDblUtils() {}
	
	public static BsonDouble str2Dbl(String val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.str2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}

	public static BsonDouble byte2Dbl(Byte val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.byte2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}

	public static BsonDouble chr2Dbl(Character val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.chr2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}

	public static BsonDouble shrt2Dbl(Short val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.shrt2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}

	public static BsonDouble int2Dbl(Integer val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.int2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}

	public static BsonDouble lng2Dbl(Long val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.lng2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}

	public static BsonDouble bgi2Dbl(BigInteger val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.bgi2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}

	public static BsonDouble flt2Dbl(Float val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.flt2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}

	public static BsonDouble bgd2Dbl(BigDecimal val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.bgd2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}

	public static BsonDouble bool2Dbl(Boolean val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.bool2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}

	public static BsonDouble sqlDt2Dbl(java.sql.Date val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.sqlDt2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}

	public static BsonDouble ts2Dbl(Timestamp val) {
		Double x = ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils.ts2Dbl(val);
		return x != null ? new BsonDouble(x) : null;
	}
}
