package net.jsa.arealle.task.process;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import net.jsa.arealle.dto.Property;
import net.jsa.arealle.dto.PropertyListing;
import net.jsa.crib.ds.api.DataSetQuery;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetItem;

public abstract class LoadablePropertyBatchHandler extends AbstractDataSetBatchHandler {
	IDataSet dsLoadablePropertyListings;
	
	public LoadablePropertyBatchHandler(IDataSet dsLoadablePropertyListings) {
		this.dsLoadablePropertyListings = dsLoadablePropertyListings;
	}

	@Override
	public void handleBatch(List<IDataSetItem> items) {

		List<Property> properties = new ArrayList<Property>(items.size());
		DataSetQuery query = new DataSetQuery();
		
		for (IDataSetItem propItem : items) {
			Property prop = new Property();
			
			prop.setPropertyId(propItem.getString("PROPERTY_ID"));
			prop.setStateId(propItem.getString("STATE_ID"));
			prop.setTownId(propItem.getString("TOWN_ID"));
			prop.setParcelFeatureName(propItem.getString("PARCEL_FEATURE_NAME"));
			prop.setParcelId(propItem.getString("PARCEL_ID"));
			prop.setRoadName(propItem.getString("ROAD_NAME"));
			prop.setAddrNum(propItem.getInteger("ADDR_NUM").intValue());
			prop.setAddrStr(propItem.getString("ADDR_STR"));
			prop.setType(propItem.getString("PROPERTY_TYPE_ID"));
			prop.setUnits(chkInt(propItem.getInteger("UNITS")));
			
			query.getParams().put("PROPERTY_ID", prop.getPropertyId());		
			List<IDataSetItem> listings = dsLoadablePropertyListings.retrieve(query);
			
			if (!CollectionUtils.isEmpty(listings)) {
				for (IDataSetItem lstgItem : listings) {
					PropertyListing listing = new PropertyListing();
					
					listing.setListingSourceId(lstgItem.getString("LISTING_SOURCE_ID"));
					listing.setPropertyListingId(lstgItem.getString("PROPERTY_LISTING_ID"));
					listing.setDetailUrl(lstgItem.getString("DETAIL_URL"));
					listing.setImgUrl(lstgItem.getString("IMG_URL"));
					listing.setUnit(lstgItem.getString("UNIT"));
					listing.setPrice(chkInt(lstgItem.getInteger("PRICE")));
					listing.setLandAreaSqFt(chkInt(lstgItem.getInteger("LAND_AREA_SQFT")));
					listing.setLandAreaAcres(chkDec(lstgItem.getDecimal("LAND_AREA_ACRES")));
					listing.setYearBuilt(chkInt(lstgItem.getInteger("YEAR_BUILT")));
					listing.setLivingArea(chkInt(lstgItem.getInteger("LIVING_AREA")));
					listing.setStyle(lstgItem.getString("STYLE"));
					listing.setFloors(chkInt(lstgItem.getInteger("FLOORS")));
					listing.setRooms(chkInt(lstgItem.getInteger("ROOMS")));
					listing.setBedRooms(chkInt(lstgItem.getInteger("BED_ROOMS")));
					listing.setBathRooms(chkDec(lstgItem.getDecimal("BATH_ROOMS")));
					listing.setGarageBays(chkInt(lstgItem.getInteger("GARAGE_BAYS")));
					listing.setBroker(lstgItem.getString("BROKER"));
					
					prop.getListings().add(listing);
				}
			}

			if (!CollectionUtils.isEmpty(prop.getListings())) {
				properties.add(prop);
			}
		}
		
		handleProperties(properties);
	}
	
	protected abstract void handleProperties(List<Property> properties);
}
