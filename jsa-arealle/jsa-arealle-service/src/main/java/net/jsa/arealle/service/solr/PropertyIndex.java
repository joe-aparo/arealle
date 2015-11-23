package net.jsa.arealle.service.solr;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.jsa.arealle.dto.Property;
import net.jsa.arealle.dto.PropertyListing;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Solr client for indexing and retrieving property records.
 * 
 * @author jsaparo
 *
 */
@Component
public class PropertyIndex extends DocumentIndex {
	@Resource(name="propertySolrClient")
	private HttpSolrServer solrClient;
	
	/**
	 * Index the given collection of properties.
	 * 
	 * @param properties A collection of properties
	 */
	public void indexProperties(List<Property> properties) {
		if (CollectionUtils.isEmpty(properties)) {
			return;
		}

		getLog().debug("Indexing " + properties.size() + " properpties.");

		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>(properties.size());

		// Multi-valued element support
		MultiValuedMap valueMap = new MultiValuedMap();

		for (Property property : properties) {
			if (!CollectionUtils.isEmpty(property.getListings())) {

				valueMap.clear();

				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", property.getPropertyId());
				doc.addField("stateId",  property.getStateId());
				doc.addField("townId", property.getTownId());
				doc.addField("parcelFeatureName", property.getParcelFeatureName());
				doc.addField("parcelId", property.getParcelId());
				doc.addField("roadName", property.getRoadName());
				doc.addField("addrNum", property.getAddrNum());
				doc.addField("addrStr", property.getAddrStr());
				doc.addField("type", property.getType());
				doc.addField("units", property.getUnits());

				// Accumulate multi-valued elements from listings
				for (PropertyListing listing : property.getListings()) {
					valueMap.put("listingSourceId", listing.getListingSourceId());
					valueMap.put("detailUrl", listing.getDetailUrl());
					valueMap.put("imgUrl", listing.getImgUrl());
					valueMap.put("unit", listing.getUnit());
					valueMap.put("price", listing.getPrice());
					valueMap.put("landAreaSqFt", listing.getLandAreaSqFt());
					valueMap.put("landAreaAcres", listing.getLandAreaAcres());
					valueMap.put("yearBuilt", listing.getYearBuilt());
					valueMap.put("livingArea", listing.getLivingArea());
					valueMap.put("style", listing.getStyle());
					valueMap.put("floors", listing.getFloors());
					valueMap.put("rooms", listing.getRooms());
					valueMap.put("bedRooms", listing.getBedRooms());
					valueMap.put("bathRooms", listing.getBathRooms());
					valueMap.put("garageBays", listing.getGarageBays());
					valueMap.put("broker", listing.getBroker());
				}

				applyMultiValuedElementsToDocument(valueMap, doc);

				docs.add(doc);
			}
		}

		if (!CollectionUtils.isEmpty(docs)) {
			addDocuments(docs);
		}
	}

	@Override
	public HttpSolrServer getClient() {
		return solrClient;
	}
}
