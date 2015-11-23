package net.jsa.arealle.service.solr;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.jsa.arealle.dto.Feature;
import net.jsa.arealle.service.ServiceConstants;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Solr client for indexing geographic features.
 * 
 * Example query(q) 
 *    stateId:"MA" AND townId:"06884716-9bb7-4a60-b773-b060094f2be8" AND typeId:"POND"
 *        
 * Example filter query(fq)
 *     geoWkt:"Intersects(GEOMETRYCOLLECTION(POLYGON((
 *        42.59184894788627 -70.65660536121453,
 *        42.59123940424099 -70.65577481450728,
 *        42.59192283368424 -70.65488458399045, 
 *        42.59258443412237 -70.65597549700298, 
 *        42.59184894788627 -70.65660536121453))))"
 *
 * @author jsaparo
 *
 */
@Component
public class FeatureIndex extends DocumentIndex {
	
	@Resource(name="featureSolrClient")
	private HttpSolrServer solrClient;
	
	/**
	 * Index a given collection of geographic features.
	 * 
	 * @param features A collection of feaures to index
	 */
	public void indexFeatures(List<Feature> features) {
		if (CollectionUtils.isEmpty(features)) {
			return;
		}
		
		getLog().debug("Indexing " + features.size() + " features.");
		
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>(features.size());

		for (Feature feature : features) {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField(ServiceConstants.FLD_SOLR_ID, feature.getId());
			doc.addField(ServiceConstants.FLD_NAME, feature.getName());
			doc.addField(ServiceConstants.FLD_TYPE_ID, feature.getTypeId());
			doc.addField(ServiceConstants.FLD_STATE_ID, feature.getStateId());
			doc.addField(ServiceConstants.FLD_TOWN_ID, feature.getTownId());
			doc.addField(ServiceConstants.FLD_GEO_WKT, feature.getGeoWkt());
			
			docs.add(doc);
		}
		
		addDocuments(docs);
	}
	
	@Override
	public HttpSolrServer getClient() {
		return solrClient;
	}
}
