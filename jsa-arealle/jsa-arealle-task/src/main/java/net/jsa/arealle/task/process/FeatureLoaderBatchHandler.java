package net.jsa.arealle.task.process;

import java.util.List;

import net.jsa.arealle.dto.Feature;
import net.jsa.arealle.service.mongo.FeatureStore;
import net.jsa.arealle.service.solr.FeatureIndex;

public class FeatureLoaderBatchHandler extends LoadableFeatureBatchHandler {
	private FeatureIndex featureIndex;
	private FeatureStore featureStore;
	
	public FeatureLoaderBatchHandler(FeatureIndex featureIndex, FeatureStore featureStore) {
		this.featureIndex = featureIndex;
		this.featureStore = featureStore;
	}

	@Override
	protected void handleFeatures(List<Feature> features) {
		featureIndex.indexFeatures(features);
		featureStore.storeFeatures(features);
	}
}
