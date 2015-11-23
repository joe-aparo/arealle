package net.jsa.arealle.service.mongo;

import java.util.ArrayList;
import java.util.List;

import net.jsa.arealle.dto.Property;
import net.jsa.arealle.dto.PropertyListing;
import net.jsa.arealle.service.ServiceConstants;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Mongo client for storing property documents.
 * 
 * @author jsaparo
 *
 */
@Component
public class PropertyStore extends DocumentStore {
	private static final String PROPERTY_COLLECTION = "property";
	
	public PropertyStore() {
		super(PROPERTY_COLLECTION);
	}
	
	public void storeProperties(List<Property> properties) {
		List<DBObject> docs = new ArrayList<DBObject>(properties.size());
		
		getLog().debug("Storing " + properties.size() + " properpties.");

		for (Property property : properties) {
			if (!CollectionUtils.isEmpty(property.getListings())) {
				
				BasicDBObject doc = new BasicDBObject();

				doc.put(ServiceConstants.FLD_MONGO_ID, property.getPropertyId());
				doc.put(ServiceConstants.FLD_PROPERTY_ID,  property.getPropertyId());
				doc.put(ServiceConstants.FLD_STATE_ID,  property.getStateId());
				doc.put(ServiceConstants.FLD_TOWN_ID, property.getTownId());
				doc.put(ServiceConstants.FLD_PARCEL_FEATURE_NAME, property.getParcelFeatureName());
				doc.put(ServiceConstants.FLD_PARCEL_ID, property.getParcelId());
				doc.put(ServiceConstants.FLD_ROAD_NAME, property.getRoadName());
				doc.put(ServiceConstants.FLD_ADDR_NUM, property.getAddrNum());
				doc.put(ServiceConstants.FLD_ADDR_STR, property.getAddrStr());
				doc.put(ServiceConstants.FLD_TYPE, property.getType());
				doc.put(ServiceConstants.FLD_UNITS, property.getUnits());
			
				List<BasicDBObject> childDocs = new ArrayList<BasicDBObject>(property.getListings().size());
				
				for (PropertyListing listing : property.getListings()) {
					BasicDBObject childDoc = new BasicDBObject();
					
					childDoc.put(ServiceConstants.FLD_LISTING_SOURCE_ID, listing.getListingSourceId());
					childDoc.put(ServiceConstants.FLD_DETAIL_URL, listing.getDetailUrl());
					childDoc.put(ServiceConstants.FLD_IMG_URL, listing.getImgUrl());
					childDoc.put(ServiceConstants.FLD_UNIT, listing.getUnit());
					childDoc.put(ServiceConstants.FLD_PRICE, listing.getPrice());
					childDoc.put(ServiceConstants.FLD_LAND_AREA_SQFT, listing.getLandAreaSqFt());
					childDoc.put(ServiceConstants.FLD_LAND_AREA_ACRES, listing.getLandAreaAcres());
					childDoc.put(ServiceConstants.FLD_YEAR_BUILT, listing.getYearBuilt());
					childDoc.put(ServiceConstants.FLD_LIVING_AREA, listing.getLivingArea());
					childDoc.put(ServiceConstants.FLD_STYLE, listing.getStyle());
					childDoc.put(ServiceConstants.FLD_FLOORS, listing.getFloors());
					childDoc.put(ServiceConstants.FLD_ROOMS, listing.getRooms());
					childDoc.put(ServiceConstants.FLD_BED_ROOMS, listing.getBedRooms());
					childDoc.put(ServiceConstants.FLD_BATH_ROOMS, listing.getBathRooms());
					childDoc.put(ServiceConstants.FLD_GARAGE_BAYS, listing.getGarageBays());
					childDoc.put(ServiceConstants.FLD_BROKER, listing.getBroker());
					
					childDocs.add(childDoc);
				}
				
				doc.put(ServiceConstants.FLD_LISTINGS, childDocs);
				
				docs.add(doc);
			}
		}
		
		addDocuments(docs);
	}

}
