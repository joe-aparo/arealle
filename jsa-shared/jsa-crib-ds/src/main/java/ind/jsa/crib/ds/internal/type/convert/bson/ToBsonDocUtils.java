package ind.jsa.crib.ds.internal.type.convert.bson;

import ind.jsa.crib.ds.internal.type.convert.core.ToMapUtils;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.bson.BsonBinary;
import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNull;
import org.bson.BsonString;
import org.bson.BsonValue;

public class ToBsonDocUtils {
	private ToBsonDocUtils() {
	}
	
	public static BsonDocument jsonToBsonDoc(String json) {
		return mapToBsonDoc(ToMapUtils.jsonToMap(json));
	}
	
	public static BsonDocument mapToBsonDoc(Map<String,Object> map) {
		if (map == null) {
			return null;
		}
		
		BsonDocument toDoc = new BsonDocument();
		
		loadBsonDocFromMap(toDoc, map);
		
		return toDoc;
	}
	
	@SuppressWarnings("unchecked")
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

		if (value == null) {
			return new BsonNull();
		}
		
		if (value instanceof Byte[]) {
			return new BsonBinary((byte[]) value);
		} else if (value instanceof Boolean) {
			return new BsonBoolean((Boolean) value);			
		} else if (value instanceof Date) {
			return new BsonDateTime(((Date) value).getTime());
		} else if (value instanceof Double) {
			return new BsonDouble((Double) value);
		} else if (value instanceof Integer) {
			return new BsonInt32((Integer) value);
		} else if (value instanceof Long) {
			return new BsonInt64((Long) value);
		} else if (value instanceof String) {
			return new BsonString((String) value);
		} else {
			return new BsonNull(); // unsupported
		}
	}

}
