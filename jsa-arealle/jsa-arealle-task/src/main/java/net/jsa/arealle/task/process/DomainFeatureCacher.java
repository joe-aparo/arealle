package net.jsa.arealle.task.process;

import java.util.Map;

import javax.annotation.Resource;

import net.jsa.arealle.task.util.GeomUtils;
import net.jsa.arealle.task.util.StrUtils;
import net.jsa.crib.ds.api.DataSetQuery;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetItem;
import net.jsa.crib.ds.impl.ListDataSetResultHandler;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * Loads features from the database into the global processing context
 * via a feature cache.
 *
 * The presumption is that numerous instances of this will run, followed by 
 * some process that would further process the contents of the cache., e.g. load
 * the info into a database.
 * 
 * @author jsaparo
 *
 */
public class DomainFeatureCacher extends CountedProcessStep {
	private FeatureCache featureCache;
	private String featureType;
	private ListDataSetResultHandler resultHandler;
	private boolean pushDomain;
	
	@Resource(name="dsCacheableFeatures")
	private IDataSet dsCacheableFeatures;
	
	public DomainFeatureCacher() {
		super("Cache feature type objects from database");
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public void setPushDomain(boolean pushDomain) {
		this.pushDomain = pushDomain;
	}

	public boolean start(Map<String, Object> context) {
		
		featureCache = FeatureCache.fetchFromContext(context, true);
		
		DataSetQuery query = new DataSetQuery();
		query.putParam("STATE_ID", context.get("STATE_ID"));
		query.putParam("FEATURE_TYPE_ID", featureType);
		
		resultHandler = new ListDataSetResultHandler(dsCacheableFeatures, query);
		dsCacheableFeatures.retrieve(query, resultHandler);
		
		setTotalItems(resultHandler.getItems().size());
		
		return super.start(context);
	}

	@Override
	public boolean nextItem() {
		if (!super.nextItem()) {
			return false;
		}
		
		IDataSetItem item = resultHandler.getItems().get(getCurrentItem() - 1);
		
		String featureId = item.getString("FEATURE_ID");
		String featureName = item.getString("FEATURE_NAME");
		String domainId = item.getString("DOMAIN_FEATURE_ID");
		GeometryCollection shapes = (GeometryCollection) GeomUtils.fromWkt(item.getString("SHAPES"));
		String attrStr = item.getString("ATTRS");
		Map<String, String> attrs = StrUtils.stringToMap(attrStr);
		
		for (int i = 0; i < shapes.getNumGeometries(); i++) {
			featureCache.assertFeatureInfo(
				featureType, featureId, featureName, domainId, shapes.getGeometryN(i), attrs);
		}

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
}
