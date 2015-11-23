package net.jsa.arealle.task.process;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import net.jsa.arealle.task.esri.ShapeEntry;
import net.jsa.arealle.task.util.FeatureNameNormalizer;
import net.jsa.arealle.task.util.INameNormalizer;

/**
 * Loads general "features" from a shape file into the global processing context
 * via a feature cache.
 *
 * The presumption is that numerous instances of this will run, followed by 
 * some process that would further process the contents of the cache., e.g. load
 * the info into a database.
 * 
 * @author jsaparo
 *
 */
@Component
public abstract class DomainFeatureReader extends ShapeFileProcessStep {
	
	// IOC
	private String featureType;
	private boolean pushDomain;

	private FeatureCache featureCache;
	
	@Resource(name="featureNameNormalizer")
	private FeatureNameNormalizer nameNormalizer;
	
	public DomainFeatureReader() {
		super("Read domain features");
	}

	public void setPushDomain(boolean pushDomain) {
		this.pushDomain = pushDomain;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	/*
	 * (non-Javadoc)
	 * @see net.jsa.arealle.task.ShapeFileProcessStep#start(java.util.Map)
	 */
	@Override
	public boolean start(Map<String, Object> context) {
		if (!super.start(context)) {
			return false;
		}
		
		featureCache = FeatureCache.fetchFromContext(context);
		if (featureCache == null) {
			getLog().warn("Feature cache not found in context");
			return false;
		}
		
		getLog().debug("Reading features for type: " + featureType);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.jsa.arealle.task.AbstractProcessStep#finish()
	 */
	@Override
	public void finish() {
		if (pushDomain) {
			featureCache.pushDomain(featureType);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.jsa.arealle.task.ShapeFileProcessStep#handleEntry(net.jsa.arealle.esri.ShapeEntry, java.util.Map)
	 */
	@Override
	protected void handleEntry(ShapeEntry shapeEntry, Map<String, Object> data) {
		// Skip row if we shouldn't keep it
		if (!keepEntry(shapeEntry, data)) {
			return;
		}
		
		// Get feature name
		String featureName = getFeatureName(data);

		// Get feature attributes
		Map<String, String> featureAttrs = getFeatureAttrs(data);
		
		// Can't have an empty name
		if (StringUtils.isEmpty(featureName)) {
			getLog().warn("Empty feature name. Ignoring record.");
			return;
		}

		// If operating within a domain, attempt to associate this feature with one
		// or more features of that domain.
		if (featureCache.getDomainFeatures() != null) {
			
			// Figure out which domain features should be associated with this entry/shape
			Set<String> domainIds = featureCache.determineDomainFeatureIdsForEntry(shapeEntry.getShape(), featureName);
	
			if (!CollectionUtils.isEmpty(domainIds)) {
				// Associate the feature/shape with all indicated domain features
				for (String domainId : domainIds) {
					if (isValidDomainId(domainId, data)) {
						getLog().debug("Associating " + featureName + " with domain id: " + domainId);
						featureCache.assertFeatureInfo(featureType, null, featureName, domainId, shapeEntry.getShape(), featureAttrs);
					}
				}
			} else {
				// otherwise discard the entry
				getLog().debug("Discarding orphaned feature: " + featureType + "/" + featureName);
			}
			
		// Otherwise this feature is not associated with a domain feature. Indicate with null domain id.
		} else {
			featureCache.assertFeatureInfo(featureType, null, featureName, null, shapeEntry.getShape(), featureAttrs);
		}
	}

	/**
	 * Indicates whether the current feature should be skipped.
	 * 
	 * @param data A data row
	 * @return Flag indicating whether to skip
	 */
	protected boolean keepEntry(ShapeEntry shapeEntry, Map<String, Object> data) {
		return true;
	}
	
	/**
	 * Get an attribute string for the current feature. 
	 * @param data The raw data from which to derive attributes
	 * 
	 * @return A formatted name/value pair string, or null if there are no attributes.
	 */
	protected Map<String, String> getFeatureAttrs(Map<String, Object> data) {
		return null;
	}
	
	/**
	 * Return the currently set name normalizer for the reader.
	 * 
	 * @return A name normalizer instance
	 */
	protected INameNormalizer getNameNormalizer() {
		return nameNormalizer;
	}
	
	/**
	 * Determine the name of a feature given its data row
	 * 
	 * @param data The data row
	 * 
	 * @return A field name
	 */
	protected abstract String getFeatureName(Map<String, Object> data);
	
	/**
	 * Allows sub-classes to restrict association with domains.
	 * 
	 * @param domainId The domain id to check
	 * @param The data record of the object being associated with the domain
	 * 
	 * @return A validity indicator
	 */
	protected boolean isValidDomainId(String domainId, Map<String, Object> data) {
		return true;
	}
	
	protected FeatureCache getFeatureCache() {
		return featureCache;
	}
}