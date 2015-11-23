package net.jsa.arealle.task.process;

import java.util.ArrayList;
import java.util.List;

import net.jsa.arealle.dto.Feature;
import net.jsa.arealle.task.util.GeomUtils;
import net.jsa.crib.ds.api.IDataSetItem;
import com.vividsolutions.jts.geom.Geometry;

public abstract class LoadableFeatureBatchHandler extends AbstractDataSetBatchHandler  {

	@Override
	public void handleBatch(List<IDataSetItem> items) {
		
		List<Feature> features = new ArrayList<Feature>(items.size());
		
		for (IDataSetItem item : items) {
			Feature feature = new Feature();
			feature.setId(item.getString("FEATURE_ID"));
			feature.setTypeId(item.getString("FEATURE_TYPE_ID"));
			feature.setName(item.getString("FEATURE_NAME"));
			feature.setStateId(item.getString("STATE_ID"));
			feature.setTownId(item.getString("DOMAIN_FEATURE_ID"));

			// Convert raw WKT to to Lat/Lng equivalent
			Geometry geom = GeomUtils.fromWkt(item.getString("SHAPES"));
			GeomUtils.convertToLatLng(geom, item.getString("CRS_ID"));
			
			// Set GEO Wkt used for geo-spatial searching
			feature.setGeoWkt(GeomUtils.toWkt(geom));
			
			// Set Google map json used for display
			feature.setGmapJson(GeomUtils.toGmapJson(geom));
			
			features.add(feature);
		}
		
		handleFeatures(features);
	}
	
	protected abstract void handleFeatures(List<Feature> features);
}
