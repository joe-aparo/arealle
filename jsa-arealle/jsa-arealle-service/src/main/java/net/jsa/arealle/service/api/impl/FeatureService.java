package net.jsa.arealle.service.api.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;

import net.jsa.arealle.dto.Feature;
import net.jsa.arealle.service.ServiceConstants;
import net.jsa.arealle.service.api.IFeatureService;
import net.jsa.arealle.service.mongo.FeatureStore;
import net.jsa.arealle.service.solr.FeatureIndex;
import net.jsa.common.logging.LogUtils;

/**
 * Main feature service implementation.
 * 
 * @author jsaparo
 *
 */
@Component
public class FeatureService implements IFeatureService {
	
	private static final String FEATURE_TYPE_SEARCH_PATTERN = "typeId:%s AND name:%s*";

	private Logger log = LogUtils.getLogger();
	
	@Resource(name="featureIndex")
	FeatureIndex featureIndex;
	
	@Resource(name="featureStore")
	FeatureStore featureStore;
	
	/*
	 * (non-Javadoc)
	 * @see net.jsa.arealle.service.api.IFeatureService#getSearchedFeature(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Feature> searchFeaturesByName(String typeId, String term, int start, int count) {

		SolrQuery query = new SolrQuery();
	    query.setQuery(String.format(FEATURE_TYPE_SEARCH_PATTERN, typeId, term));
	    query.setStart(start);
	    query.setRows(count);
	    query.setRows(Integer.MAX_VALUE);

	    List<Feature> features = new ArrayList<Feature>(count);
	    
	    try {
		    QueryResponse response = featureIndex.getClient().query(query);
		    
		    SolrDocumentList results = response.getResults();
		    Iterator<SolrDocument> iter = results.iterator();
		    while (iter.hasNext()) {
		    	SolrDocument doc = iter.next();
		    	Feature feature = new Feature();
		    	
		    	feature.setId(doc.getFieldValue(ServiceConstants.FLD_SOLR_ID).toString());
		    	feature.setName(doc.getFieldValue(ServiceConstants.FLD_NAME).toString());
		    	feature.setTypeId(doc.getFieldValue(ServiceConstants.FLD_TYPE_ID).toString());
		    	feature.setStateId(doc.getFieldValue(ServiceConstants.FLD_STATE_ID).toString());
		    	feature.setTownId(doc.getFieldValue(ServiceConstants.FLD_TOWN_ID).toString());
		    	
		    	features.add(feature);
		    }
	    } catch (SolrServerException ex) {
	    	log.error("Error getting features of type: " + typeId, ex);
	    }
	    
		return features;
	}

	/*
	 * (non-Javadoc)
	 * @see net.jsa.arealle.service.api.IFeatureService#retrieveFeature(java.lang.String)
	 */
	@Override
	public Feature retrieveFeatureById(String id) {
		Feature feature = null;
		DBObject doc = featureStore.getDocumentById(id);
		
		if (doc != null) {
			feature = new Feature();
			feature.setId((String) doc.get(ServiceConstants.FLD_MONGO_ID));
			feature.setName((String) doc.get(ServiceConstants.FLD_NAME));
			feature.setTypeId((String) doc.get(ServiceConstants.FLD_TYPE_ID));
	    	feature.setStateId((String) doc.get(ServiceConstants.FLD_STATE_ID));
	    	feature.setTownId((String) doc.get(ServiceConstants.FLD_TOWN_ID));
	    	feature.setGmapJson((String) doc.get(ServiceConstants.FLD_GMAP_JSON));
		}

		return feature;
	}
}
