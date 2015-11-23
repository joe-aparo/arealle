package net.jsa.arealle.task.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import net.jsa.arealle.task.pojo.DomainFeatureInfo;
import net.jsa.arealle.task.pojo.FeatureInfo;
import net.jsa.common.logging.LogUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.TopologyException;

/**
 * A memory structure for holding FeatureInfo instances. The structure is
 * a Map with the following organization:
 * 
 * Map<[featureTypeId], Map<[featureName], FeatureInfo>>k
 * 
 * @author jsaparo
 *
 */
public class FeatureCache {
	private static final String FEATURE_CACHE_KEY = "feature.cache";
	private static final int INIT_TYPE_MAP_SIZE = 10;
	private static final int INIT_FEATURE_MAP_SIZE = 5000;
	private static final int CLOSE_PROXIMITY = 50;
	
	private Logger log = LogUtils.getLogger();
	private Map<String, Map<String, FeatureInfo>> featuresByTypeAndName;
	private Map<String, Map<String, FeatureInfo>> featuresByTypeAndId;
	private Map<String, FeatureInfo> domainFeaturesByName;
	
	public static FeatureCache fetchFromContext(Map<String, Object> context) {
		return fetchFromContext(context, true);
	}
	
	public static FeatureCache fetchFromContext(Map<String, Object> context, boolean create) {
		FeatureCache cache = (FeatureCache) context.get(FEATURE_CACHE_KEY);
		if (cache == null && create) {
			cache = new FeatureCache();
			context.put(FEATURE_CACHE_KEY, cache);
		}

		return cache;
	}
	
	/**
	 * Place a shape, and its corresponding attributes, into the feature
	 * map for a specified feature type, name, and domain.
	 * 
	 * @param featureType The feature type
	 * @param featureId The id of the feature if known, otherwise null
	 * @param featureName The unique feature name within the type
	 * @param domainId The id of the associated domain feature
	 * @param shape The shape itself
	 * @param attrs An attribute map
	 */
	public void assertFeatureInfo(
		String featureType, String featureId, String featureName, String domainId, 
		Geometry shape, Map<String, String> attrs) {

		log.debug("Recording domain feature info for: " + featureType + ":" + featureName + ":" + domainId);
		
		FeatureInfo featureInfo = assertFeatureInfoForName(featureType, featureId, featureName);
		if (attrs != null) {
			featureInfo.getAttrs().putAll(attrs);
		}
		
		DomainFeatureInfo domainFeatureInfo = assertDomainFeatureInfo(
			featureType, featureId, featureName, domainId);
		
		domainFeatureInfo.getShapes().add(shape);
		
		Map<String, FeatureInfo> featuresById = assertFeaturesByIdForType(featureType);
		featuresById.put(featureInfo.getId(), featureInfo);
	}

	/**
	 * Get domain feature object for a given feature type, name, and domain id. Create the
	 * entry if it does not exist.
	 * 
	 * @param featureType The feature type
	 * @param featureId The id of the feature if known, otherwise null
	 * @param featureName The unique feature name within the type
	 * @param domainId The id of the associated domain feature
	 * 
	 * @return A domain feature object
	 */
	public DomainFeatureInfo assertDomainFeatureInfo(
		String featureType, String featureId, String featureName, String domainId) {
		
		FeatureInfo featureInfo = assertFeatureInfoForName(featureType, featureId, featureName);
		
		// If the feature is not associated with with any domain feature, the
		// domain feature id is considered to be the same as that of the feature itself.
		if (domainId == null) {
			domainId = featureInfo.getId();
		}
		
		// Get map of domain feature object keyed by domain id
		Map<String, DomainFeatureInfo> featureInfoByDomain = featureInfo.getDomainFeatureInfo();
		
		// Fetch the domain feature info the for domain id
		DomainFeatureInfo domainFeatureInfo = featureInfoByDomain.get(domainId);
		
		// Create new domain feature instance if it doesn't exist
		if (domainFeatureInfo == null) {
			domainFeatureInfo = new DomainFeatureInfo();
			featureInfoByDomain.put(domainId,  domainFeatureInfo);
		}
		
		return domainFeatureInfo;
	}

	/**
	 * Get feature info for a given type and id.
	 * 
	 * @param featureType The type of feature to get
	 * @param featureId The id of the feature info to get
	 * 
	 * @return A feature object, or null if not found
	 */
	public FeatureInfo getFeatureInfoForId(String featureType, String featureId) {
		Map<String, FeatureInfo> featuresById = assertFeaturesByIdForType(featureType);
		
		return featuresById.get(featureId);
	}
	
	/**
	 * Get map of features mapped by feature id for the given feature type.
	 * 
	 * @param featureType The type of feature to retrieve the map for
	 * 
	 * @return A map of features keyed by id
	 */
	public Map<String, FeatureInfo> assertFeaturesByIdForType(String featureType) {
		
		Map<String, Map<String, FeatureInfo>> featuresByType = this.assertFeaturesByTypeAndId();
		
		Map<String, FeatureInfo> featuresById = featuresByType.get(featureType);
		if (featuresById == null) {
			featuresById = new HashMap<String, FeatureInfo>(INIT_FEATURE_MAP_SIZE);
			featuresByType.put(featureType, featuresById);
		}
		
		return featuresById;
	}
	
	/**
	 * Get a feature object for a given feature type and name. Create the
	 * entry if it doesn't exist.
	 * 
	 * @param featureType The feature type
	 * @param featureId The id of the feature if known, otherwise null
	 * @param featureName The unique feature name within the type
	 * 
	 * @return A top-level feature info object
	 */
	public FeatureInfo assertFeatureInfoForName(String featureType, String featureId, String featureName) {
		// Get map of features for the specified type, mapped by unique feature name
		Map<String, FeatureInfo> featureMapsByName = assertFeatureMapsByName(featureType);
		
		// Fetch feature info for specified unique feature name
		FeatureInfo featureInfo = featureMapsByName.get(featureName);
		
		// Create feature info instance if it doesn't exist
		if (featureInfo == null) {
			featureInfo = new FeatureInfo();
			featureInfo.setId(StringUtils.isEmpty(featureId) ? UUID.randomUUID().toString() : featureId);
			featureInfo.setName(featureName);
			featureInfo.setTypeId(featureType);
			
			featureMapsByName.put(featureName, featureInfo);
		}
		
		return featureInfo;
	}
	
	/**
	 * For a given feature type, get the corresponding map of feature 
	 * maps, keyed by name. Create it if it's not there.
	 *  
	 * @param featureType The feature type
	 * 
	 * @return A map of feature object collections keyed by by feature type
	 */
	public Map<String, FeatureInfo> assertFeatureMapsByName(String featureType) {
		// Get global map of feature info, keyed by feature type
		Map<String, Map<String, FeatureInfo>> featureMapsByType = assertFeaturesByTypeAndName();
		
		// Get the map of features keyed by unique feature name for the specified type
		Map<String, FeatureInfo> featuresByName = 
			(Map<String, FeatureInfo>) featureMapsByType.get(featureType);
		
		// Create the named feature map if it doesn't exist
		if (featuresByName == null) {
			featuresByName = new LinkedHashMap<String, FeatureInfo>(INIT_FEATURE_MAP_SIZE);
			featureMapsByType.put(featureType, featuresByName);
		}
		
		return featuresByName;
	}
	
	/**
	 * Get the map of feature collections, keyed by feature type, from the context.
	 * Create it if it's not there.
	 *  
	 * @return A map of feature object collections keyed by by feature type
	 */
	public Map<String, Map<String, FeatureInfo>> assertFeaturesByTypeAndName() {
		
		// Create global map of feature info if it doesn't exist.
		if (featuresByTypeAndName == null) {
			featuresByTypeAndName = new LinkedHashMap<String, Map<String, FeatureInfo>>(INIT_TYPE_MAP_SIZE);
		}
		
		return featuresByTypeAndName;
	}
	
	/**
	 * Set the features that will be used as the current domain for other processed features.
	 * 
	 * @param featuresByNameMap A map of feature objects keyed by feature name;
	 */
	public void pushDomain(String featureType) {
		domainFeaturesByName = (StringUtils.isEmpty(featureType)) ? null : assertFeatureMapsByName(featureType);
	}

	/**
	 * Get the features that will be used as the current domain for other processed features.
	 * 
	 * @return A map of feature objects keyed by feature name;
	 */
	public Map<String, FeatureInfo> getDomainFeatures() {
		return domainFeaturesByName;
	}

	/**
	 * Get features by type and id
	 * 
	 */
	public Map<String, Map<String, FeatureInfo>> assertFeaturesByTypeAndId() {
		if(featuresByTypeAndId == null) {
			featuresByTypeAndId = new HashMap<String, Map<String, FeatureInfo>>(INIT_TYPE_MAP_SIZE);
		}
		
		return featuresByTypeAndId;
	}

	/**
	 * Return the ids of those domain features that are geographically connected,
	 * i.e. that intersect or are within a designated distance,  to a given feature.
	 * 
	 * @param entryShape A feature shape
	 * @param entryFeatureName The name of the feature owning the shape
	 * 
	 * @return A list of town identifiers
	 */
	public Set<String> determineDomainFeatureIdsForEntry(Geometry entryShape, String entryFeatureName) {
		// Get currently set colllection of domain features
		Map<String, FeatureInfo> domainFeatures = getDomainFeatures();
		
		// If no domain features are specified, there's nothing to map
		if (CollectionUtils.isEmpty(domainFeatures)) {
			return null;
		}
		
		// The collection of ids to return
		Set<String> ids = new HashSet<String>();

		// Visit each feature in the domain looking for matches
		for (Entry<String, FeatureInfo> e : domainFeatures.entrySet()) {
			FeatureInfo domainFeature = e.getValue();
			
			// For a feature designated as a domain feature, there is typically only one iteration below.
			// However the logic will consider multiple domain feature entries if they exist.
			for (Entry<String, DomainFeatureInfo> e2 : domainFeature.getDomainFeatureInfo().entrySet()) {
				DomainFeatureInfo domainInfo = e2.getValue();

				try {
					// Associate the given shape with domain shape if they're geographically connected 
					for (Geometry domainShape : domainInfo.getShapes()) {
						if (entryShape.intersects(domainShape) || 
							entryShape.isWithinDistance(domainShape, CLOSE_PROXIMITY)) {
							
							// Add feature id
							ids.add(domainFeature.getId());
							
							log.debug(
								"Feature: " + entryFeatureName + 
								" is within domain feature: " + domainFeature.getTypeId() + "/" + domainFeature.getName());
							
							// no need to check further for this feature
							break;
						}
					}
				} catch (TopologyException ex) {
					log.error("Error determining intersecting feature for shape: " + 
						domainFeature.getId() + "/" + domainFeature.getName(), ex);
					
					// When this exception happens, the shapes actually intersect,
					// but in a way that JTS occasionally has some issue with.
					ids.add(domainFeature.getId());
				}
			}
		}

		return ids;
	}		
}
