package net.jsa.arealle.service.mongo;

import java.util.ArrayList;
import java.util.List;

import net.jsa.arealle.dto.Feature;
import net.jsa.arealle.service.ServiceConstants;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Mongo client for storing geographic feature documents.
 * 
 * @author jsaparo
 *
 */
@Component
public class FeatureStore extends DocumentStore {
	private static final String FEATURE_COLLECTION = "feature";
	
	public FeatureStore() {
		super(FEATURE_COLLECTION);
	}

	public void storeFeatures(List<Feature> features) {
		if (CollectionUtils.isEmpty(features)) {
			return;
		}

		getLog().debug("Storing " + features.size() + " features.");
		
		List<DBObject> docs = new ArrayList<DBObject>(features.size());
		
		for (Feature feature : features) {
			BasicDBObject doc = new BasicDBObject();
			
			doc.put(ServiceConstants.FLD_MONGO_ID, feature.getId());
			doc.put(ServiceConstants.FLD_NAME, feature.getName());
			doc.put(ServiceConstants.FLD_TYPE_ID, feature.getTypeId());
			doc.put(ServiceConstants.FLD_STATE_ID, feature.getStateId());
			doc.put(ServiceConstants.FLD_TOWN_ID, feature.getTownId());
			doc.put(ServiceConstants.FLD_GMAP_JSON, feature.getGmapJson());
			
			docs.add(doc);
		}

		addDocuments(docs);
	}
}
