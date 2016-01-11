package ind.jsa.crib.ds.internal.type.convert.bson;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.bson.BsonBinary;
import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonValue;

public class ToBsonDocUtils {
	private ToBsonDocUtils() {
	}
	
	public static BsonDocument jsonToBsonDoc(String json) {
		return null;
	}
	
	public static BsonDocument mapToBsonDoc(Map<String,Object> map) {
		BsonDocument toDoc = new BsonDocument();
		
		loadBsonDocFromMap(toDoc, map);
		
		return toDoc;
	}
	
	private static void loadBsonDocFromMap(BsonDocument toDoc, Map<String, Object> map) {
		Set<String> keys = map.keySet();
		
		for (String key : keys) {
			Object value = map.get(key);
			
			if (value instanceof Map<?,?>) {
				// Set up new map
				Map<String, Object> innerMap = (Map<String, Object>) value;
				BsonDocument innerDoc = new BsonDocument();
				toDoc.put(key, innerDoc);
				
				// recurse
				loadBsonDocFromMap(innerDoc, innerMap);
			} else {
				toDoc.put(key, convertNativeValToBsonVal(value));
			}
		}
		
	}
	
	private static BsonValue convertNativeValToBsonVal(Object value) {

		if (value instanceof Byte[]) {
			return new BsonBinary((byte[]) value);
		} else if (value instanceof Boolean) {
			return new BsonBoolean((Boolean) value);			
		} else if (value instanceof Date) {
			return new BsonDateTime(((Date) value).getTime());
		} /* else if (bsonValue instanceof BsonDouble) {
			return Double.valueOf(((BsonDouble) bsonValue).getValue());
		} else if (bsonValue instanceof BsonInt32) {
			return Integer.valueOf(((BsonInt32) bsonValue).getValue());
		} else if (bsonValue instanceof BsonInt64) {
			return Long.valueOf(((BsonInt64) bsonValue).getValue());
		} else if (bsonValue instanceof BsonString) {
			return ((BsonString) bsonValue).getValue();
		} else {
			return null; // unsupported
		}
		*/
		return null;
	}

}
