package ind.jsa.crib.ds.internal.type.convert.core;

import java.util.Map;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

public class ToMapUtils {
	
	private static ObjectMapper mapper = JsonFactory.create();
	
	private ToMapUtils() {
		
	}

	public static Map<String, Object> jsonToMap(String json) {
		return mapper.fromJson(json, Map.class);
	}
}
