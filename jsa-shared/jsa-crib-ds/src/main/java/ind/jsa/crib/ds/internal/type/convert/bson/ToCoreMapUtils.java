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

public class ToCoreMapUtils {
	private ToCoreMapUtils() {
	}
		
	public static Map<String, Object> bsonDocToMap(BsonDocument doc) {
		Map<String, Object> toMap = getMapForDoc(doc);
		
		loadMapFromBsonDoc(getMapForDoc(doc), doc);
		
		return toMap;
	}
	
	private static void loadMapFromBsonDoc(Map<String, Object> toMap, BsonDocument doc) {
		Map<String, BsonValue> map = doc;
		Set<String> keys = map.keySet();
		
		for (String key : keys) {
			BsonValue bsonValue = doc.get(key);
			
			if (bsonValue instanceof BsonDocument) {
				// Set up new map
				BsonDocument innerDoc = (BsonDocument) bsonValue;
				Map<String, Object> innerMap = getMapForDoc(innerDoc);
				toMap.put(key, innerMap);
				
				// recurse
				loadMapFromBsonDoc(innerMap, innerDoc);
			} else {
				toMap.put(key, convertBsonValToNativeVal(bsonValue));
			}
		}
		
	}
	
	private static Map<String, Object> getMapForDoc(BsonDocument doc) {
		return new LinkedHashMap<String, Object>(((Map<String, BsonValue>) doc).size());
	}

	private static Object convertBsonValToNativeVal(BsonValue bsonValue) {
		if (bsonValue instanceof BsonBinary) {
			return ((BsonBinary) bsonValue).getData();
		} else if (bsonValue instanceof BsonBoolean) {
			return Boolean.valueOf(((BsonBoolean) bsonValue).getValue());			
		} else if (bsonValue instanceof BsonDateTime) {
			return new Date(((BsonDateTime) bsonValue).getValue());
		} else if (bsonValue instanceof BsonDouble) {
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
	}
}
