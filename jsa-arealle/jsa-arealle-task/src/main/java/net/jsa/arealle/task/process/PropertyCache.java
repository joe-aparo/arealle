package net.jsa.arealle.task.process;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import net.jsa.arealle.dto.Property;
import net.jsa.arealle.dto.PropertyListing;
import net.jsa.common.logging.LogUtils;

public class PropertyCache {
	private static final String KEY_DELIM = "|";
	private static final String PROPERTY_CACHE_KEY = "property.cache";
	private static final int INIT_MAP_SIZE = 15000;
	
	private Map<String, Property> propertyMap = new HashMap<String, Property>(INIT_MAP_SIZE);
	private Logger log = LogUtils.getLogger();
	
	public static PropertyCache fetchFromContext(Map<String, Object> context) {
		return fetchFromContext(context, true);
	}
	
	public static PropertyCache fetchFromContext(Map<String, Object> context, boolean create) {
		PropertyCache cache = (PropertyCache) context.get(PROPERTY_CACHE_KEY);
		if (cache == null && create) {
			cache = new PropertyCache();
			context.put(PROPERTY_CACHE_KEY, cache);
		}

		return cache;
	}

	public Property putProperty(Property property, PropertyListing listing) {
		
		String k = makePropertyKey(property);
		
		Property prop = propertyMap.get(k);
		
		if (prop == null && 
			!StringUtils.isEmpty(property.getParcelFeatureName()) && 
			!StringUtils.isEmpty(property.getTownId()) &&
			!StringUtils.isEmpty(property.getStateId()) &&
			!StringUtils.isEmpty(property.getRoadName()) &&
			!StringUtils.isEmpty(property.getAddrStr()) &&
			!StringUtils.isEmpty(property.getType())
			) {

			prop = property;

			if (StringUtils.isEmpty(property.getPropertyId())) {
				property.setPropertyId(UUID.randomUUID().toString());
			}
			
			propertyMap.put(k, prop);
			
			log.debug("Cached property: " + prop.getPropertyId() + " - " + k);
		}
		
		if (prop != null) {
			if (StringUtils.isEmpty(listing.getPropertyListingId())) {
				listing.setPropertyListingId(UUID.randomUUID().toString());
			}
			
			prop.getListings().add(listing);
			log.debug("Cached listing: " + listing.getListingSourceId() + " for property: " + k);
		} else {
			log.warn("Property could not be cached for: " + k);
		}
		
		return prop;
	}
	
	public Property[] getProperties() {
		Property[] props = new Property[propertyMap.size()];
		return propertyMap.values().toArray(props);
	}

	private String makePropertyKey(Property property) {
		return property.getAddrStr() + KEY_DELIM + property.getRoadName();
	}
}