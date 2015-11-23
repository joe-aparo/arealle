package net.jsa.arealle.task.process;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.jsa.arealle.service.mongo.FeatureStore;
import net.jsa.arealle.service.solr.FeatureIndex;

import net.jsa.crib.ds.api.DataSetQuery;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetBatchHandler;

@Component
public class FeatureLoader extends DataSetBatchProcessStep {
	
	private static final int FEATURE_BATCH_SIZE = 100;

	@Resource(name="dsLoadableFeatures")
	private IDataSet dsLoadableFeatures;
	
	@Resource
	private FeatureIndex featureIndex;
	
	@Resource
	private FeatureStore featureStore;
	
	public FeatureLoader() {
		super("Load features");
	}

	@Override
	protected IDataSet getDataSet() {
		return dsLoadableFeatures;
	}

	@Override
	protected IDataSetBatchHandler getBatchHandler() {
		return new FeatureLoaderBatchHandler(featureIndex, featureStore);
	}

	@Override
	protected int getBatchSize() {
		return FEATURE_BATCH_SIZE;
	}

	@Override
	protected DataSetQuery getQuery(Map<String, Object> context) {
		DataSetQuery query = new DataSetQuery();
		query.putParam("TOWN_ID", context.get("TOWN_ID"));
		
		return query;
	}
}
