package net.jsa.arealle.task.process;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import net.jsa.arealle.task.pojo.DomainFeatureInfo;
import net.jsa.arealle.task.pojo.FeatureInfo;
import net.jsa.arealle.task.util.GeomUtils;
import net.jsa.arealle.task.util.StrUtils;
import net.jsa.crib.ds.api.IDataSet;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

@Component
public class FeatureStager extends CountedProcessStep {
	private GeometryFactory factory = new GeometryFactory();
	private FeatureCache featureCache;
	private FeatureInfo[] features;
	private String featureType;
	
	public FeatureStager() {
		super("Store cached features in database");
	}

	@Resource(name="dsFeatures")
	private IDataSet dsFeatures;
	
	@Resource(name="dsDomainFeatures")
	private IDataSet dsDomainFeatures;
	
	public void setFeatureType(String type) {
		featureType = type;
	}
	
	@Override
	public boolean start(Map<String, Object> context) {
		super.start(context);

		featureCache = FeatureCache.fetchFromContext(context, false); 
		if (featureCache == null) {
			getLog().warn("Feature cache found in context");
			return false;
		}
		
		Map<String, FeatureInfo> featureMap = featureCache.assertFeatureMapsByName(featureType);
		
		getLog().debug("Storing features for type: " + featureType);
		
		features = new FeatureInfo[featureMap.size()];
		features = featureMap.values().toArray(features);

		setTotalItems(featureMap.size());

		return true;
	}

	@Override
	public boolean nextItem() {
		if (!super.nextItem()) {
			return false;
		}
		
		FeatureInfo featureInfo = features[getCurrentItem() - 1];
		
		// IDs pre-generated by reader
		String featureId = featureInfo.getId(); 
		
		// Store the state feature
		Map<String, Object> feature = new HashMap<String, Object>();
		feature.put("FEATURE_ID", featureId); 
		feature.put("FEATURE_NAME", featureInfo.getName());
		feature.put("FEATURE_TYPE_ID", featureType);
		feature.put("STATE_ID", getContext().get("STATE_ID")); // From global context		
		if (!CollectionUtils.isEmpty(featureInfo.getAttrs())) {
			feature.put("ATTRS", 
				StrUtils.mapToString(featureInfo.getAttrs()));
		}
		
		// Create db record
		dsFeatures.create(feature);

		// Add domain feature db entries
		for (Entry<String, DomainFeatureInfo> domainFeatureEntry : featureInfo.getDomainFeatureInfo().entrySet()) {
			String domainFeatureId = domainFeatureEntry.getKey();
			DomainFeatureInfo domainFeatureInfo = domainFeatureEntry.getValue();

			// Convert list of shapes to a geometry collection
			GeometryCollection shapeCollection = GeomUtils.getShapeCollection(domainFeatureInfo.getShapes(), factory);

			// Convert geometry collection to WKT
			String wkt = shapeCollection.toText();
			
			// Store town-specific shapes for the state feature
			Map<String, Object> domainFeature = new HashMap<String, Object>();
			domainFeature.put("FEATURE_ID", featureId);
			domainFeature.put("DOMAIN_FEATURE_ID", domainFeatureId);
			domainFeature.put("SHAPES", wkt);
			
			// Create db record
			dsDomainFeatures.create(domainFeature);
		}

		return true;
	}
}
