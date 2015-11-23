package net.jsa.arealle.task.process;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.jsa.arealle.service.mongo.PropertyStore;
import net.jsa.arealle.service.solr.PropertyIndex;
import net.jsa.crib.ds.api.DataSetQuery;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetBatchHandler;

@Component
public class PropertyLoader extends DataSetBatchProcessStep {

	private static final int PROPERTY_BATCH_SIZE = 100;

	@Resource(name="dsLoadableProperties")
	IDataSet dsLoadableProperties;
	
	@Resource(name="propertyStore")
	PropertyStore propertyStore;
	
	@Resource(name="propertyIndex")
	PropertyIndex propertyIndex;
	
	@Resource(name="dsLoadablePropertyListings")
	IDataSet dsLoadablePropertyListings;
	
	public PropertyLoader() {
		super("Load properties");
	}

	@Override
	protected IDataSet getDataSet() {
		return dsLoadableProperties;
	}

	@Override
	protected IDataSetBatchHandler getBatchHandler() {
		return new PropertyLoaderBatchHandler(propertyIndex, propertyStore, dsLoadablePropertyListings);
	}

	@Override
	protected int getBatchSize() {
		return PROPERTY_BATCH_SIZE;
	}

	@Override
	protected DataSetQuery getQuery(Map<String, Object> context) {
		DataSetQuery query = new DataSetQuery();
		query.putParam("TOWN_ID", context.get("TOWN_ID"));
		
		return query;
	}
}
