package net.jsa.arealle.task.process;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.jsa.arealle.dto.Property;
import net.jsa.arealle.dto.PropertyListing;
import net.jsa.crib.ds.api.IDataSet;

@Component
public class PropertyStager extends CountedProcessStep {

	@Resource(name="dsProperties")
	private IDataSet dsProperties;
	
	@Resource(name="dsPropertyListings")
	private IDataSet dsPropertyListings;
	
	private Property[] properties;
	
	public PropertyStager() {
		super("Load scraped listings");
	}
	
	@Override
	public boolean start(Map<String, Object> context) {
		if (!super.start(context)) {
			return false;
		}

		// Get cached property objects
		PropertyCache cache = PropertyCache.fetchFromContext(getContext());
		properties = cache.getProperties();
			
		setTotalItems(properties.length);
		
		return true;
	}
	
	@Override
	public boolean nextItem() {
		if (!super.nextItem()) {
			return false;
		}

		Property prop = properties[getCurrentItem() - 1];
		
		if (prop.getListings().size() > 1) {
			int x = 1;
			
			if (x == 1) x = 2;
		}

		Map<String, Object> params = new HashMap<String, Object>(20);

		try {
			// Create property record
			params.put("PROPERTY_ID", prop.getPropertyId());
			params.put("STATE_ID", prop.getStateId());
			params.put("TOWN_ID", prop.getTownId());
			params.put("PARCEL_FEATURE_NAME", prop.getParcelFeatureName());
			params.put("ROAD_NAME", prop.getRoadName());
			params.put("ADDR_NUM", prop.getAddrNum());
			params.put("ADDR_STR", prop.getAddrStr());
			params.put("PROPERTY_TYPE_ID", prop.getType());
			params.put("UNITS", prop.getUnits());
			
			dsProperties.create(params);
	
		} catch (RuntimeException ex) {
			getLog().error("Error creating property: " + prop.getAddrStr() + "; " + prop.getRoadName(), ex);
		}
		
		// Write property listing records
		for (PropertyListing listing : prop.getListings()) {
			try {
				params.clear();

				params.put("PROPERTY_LISTING_ID", listing.getPropertyListingId());
				params.put("PROPERTY_ID", prop.getPropertyId());
				params.put("LISTING_SOURCE_ID", listing.getListingSourceId());
				params.put("SOURCE_PROPERTY_ID", listing.getSourcePropertyId());
				params.put("DETAIL_URL", listing.getDetailUrl());
				params.put("IMG_URL", listing.getImgUrl());
				params.put("UNIT", listing.getUnit());
				params.put("LAND_AREA_SQFT", listing.getLandAreaSqFt());
				params.put("LAND_AREA_ACRES", listing.getLandAreaAcres());
				params.put("PRICE", listing.getPrice());
				params.put("YEAR_BUILT", listing.getYearBuilt());
				params.put("LIVING_AREA", listing.getLivingArea());
				params.put("STYLE", listing.getStyle());
				params.put("FLOORS", listing.getFloors());
				params.put("ROOMS", listing.getRooms());
				params.put("BED_ROOMS", listing.getBedRooms());
				params.put("BATH_ROOMS", listing.getBathRooms());
				params.put("GARAGE_BAYS", listing.getGarageBays());
				params.put("CREATED_ON", new Date());
				
				dsPropertyListings.create(params);
			} catch (RuntimeException ex) {
				getLog().error("Error creating property listings " + prop.getAddrStr() + "; " + 
					prop.getRoadName() + "; " + listing.getUnit(), ex);
			}
		}

		return true;
	}
}
