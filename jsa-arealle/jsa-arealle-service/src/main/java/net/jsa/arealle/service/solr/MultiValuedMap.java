package net.jsa.arealle.service.solr;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Represents a simple cache for representing multiple values for a solr field.
 * 
 * @author jsaparo
 *
 */
public class MultiValuedMap {
	private static final int INIT_MAP_SIZE = 10;
	
	Map<String, Set<Object>> valueMap = new LinkedHashMap<String, Set<Object>>(INIT_MAP_SIZE);
	
	public void clear() {
		valueMap.clear();
	}
	
	public void put(String field, Object value) {
		if (value == null) {
			return;
		}
		
		Set<Object> values = valueMap.get(field);
		if (values == null) {
			values = new LinkedHashSet<Object>();
			valueMap.put(field, values);
		}
		
		values.add(value);
	}
	
	public Set<Entry<String, Set<Object>>> getEntries() {
		return valueMap.entrySet();
	}
}
