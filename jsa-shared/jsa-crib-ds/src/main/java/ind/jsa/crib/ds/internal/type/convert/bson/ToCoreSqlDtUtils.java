package ind.jsa.crib.ds.internal.type.convert.bson;

import java.sql.Date;

import org.bson.BsonDateTime;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;

public class ToCoreSqlDtUtils {
	private ToCoreSqlDtUtils() {}
	
	public static Date bsonStr2SqlDt(BsonString val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToSqlDtUtils.str2SqlDt(((BsonString) val).toString()) : null;
	}
	
	public static Date bsonInt2SqlDt(BsonInt32 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToSqlDtUtils.lng2SqlDt(((BsonInt32) val).longValue()) : null;
	}
	
	public static Date bsonLng2SqlDt(BsonInt64 val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToSqlDtUtils.lng2SqlDt(((BsonInt64) val).longValue()) : null;
	}
	
	public static Date bsonDt2SqlDt(BsonDateTime val) {
		return val != null ? 
			ind.jsa.crib.ds.internal.type.convert.core.ToSqlDtUtils.lng2SqlDt(((BsonDateTime) val).getValue()) : null;
	}
}
