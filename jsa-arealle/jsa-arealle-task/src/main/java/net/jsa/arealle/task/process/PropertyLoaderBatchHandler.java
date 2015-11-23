package net.jsa.arealle.task.process;

import java.util.List;

import net.jsa.arealle.dto.Property;
import net.jsa.arealle.service.mongo.PropertyStore;
import net.jsa.arealle.service.solr.PropertyIndex;
import net.jsa.crib.ds.api.IDataSet;

public class PropertyLoaderBatchHandler extends LoadablePropertyBatchHandler {
	private PropertyIndex propertyIndex;
	private PropertyStore propertyStore;
	
	public PropertyLoaderBatchHandler(
		PropertyIndex propertyIndex, PropertyStore propertyStore, IDataSet dsLoadablePropertyListings) {
		super(dsLoadablePropertyListings);
		
		this.propertyIndex = propertyIndex;
		this.propertyStore = propertyStore;
	}

	@Override
	protected void handleProperties(List<Property> properties) {
		propertyIndex.indexProperties(properties);
		propertyStore.storeProperties(properties);
	}
}
