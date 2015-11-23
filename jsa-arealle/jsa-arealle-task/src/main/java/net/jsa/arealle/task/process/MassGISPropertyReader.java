package net.jsa.arealle.task.process;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import net.jsa.arealle.dto.Property;
import net.jsa.arealle.dto.PropertyListing;
import net.jsa.arealle.task.util.AcreCalc;
import net.jsa.arealle.task.util.INameNormalizer;
import net.jsa.arealle.task.util.StrUtils;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.utils.type.ToIntUtils;

@Component
public class MassGISPropertyReader extends DbfProcessStep {
	private static final String MASSGIS_SOURCE_ID = "MASSGIS";
	
	// MassGIS LAND USE code descriptions. These are converted
	// to Arealle equivalent types and unit counts when applicable
	/*
		101		Single Family
		1010	Single Family
		1012	Single Family
		1013	Single Family
		102		Condominium
		1020	Condominium
		103		Mobile Home
		1030	Mobile Home
		104		Two Family
		1040	Two Family
		105		Three Family
		1050	Three Family
		106		Improvement
		1060	Improvement
		109		Multi-Unit
		1090	Multi-Unit
		111		Multi-Unit
		1110	Multi-Unit
		112		Multi-Unit
		1120	Multi-Unit
		130		Land
		1300	Land
		1310	Land
		1320	Land
		3221	Retail Condo
	 */
	
	private static Map<String, PropertyType> landUse2PropType = new HashMap<String, PropertyType>();
	private static Map<String, Integer> landUse2Units = new HashMap<String, Integer>();
	
	static {
		landUse2PropType.put("101", PropertyType.SINGLE);
		landUse2PropType.put("1010", PropertyType.SINGLE);
		landUse2PropType.put("1012", PropertyType.SINGLE);
		landUse2PropType.put("1013", PropertyType.SINGLE);
		landUse2PropType.put("102", PropertyType.CONDO);
		landUse2PropType.put("1020", PropertyType.CONDO);
		landUse2PropType.put("103", PropertyType.MOBILE);
		landUse2PropType.put("1030", PropertyType.MOBILE);
		landUse2PropType.put("104", PropertyType.MULTI);
		landUse2PropType.put("1040", PropertyType.MULTI);
		landUse2PropType.put("105", PropertyType.MULTI);
		landUse2PropType.put("1050", PropertyType.MULTI);
		landUse2PropType.put("106", PropertyType.OTHER);
		landUse2PropType.put("1060", PropertyType.OTHER);
		landUse2PropType.put("109", PropertyType.MULTI);
		landUse2PropType.put("1090", PropertyType.MULTI);
		landUse2PropType.put("111", PropertyType.MULTI);
		landUse2PropType.put("1110", PropertyType.MULTI);
		landUse2PropType.put("112", PropertyType.MULTI);
		landUse2PropType.put("1120", PropertyType.MULTI);
		landUse2PropType.put("130", PropertyType.LAND);
		landUse2PropType.put("1300", PropertyType.LAND);
		landUse2PropType.put("1310", PropertyType.LAND);
		landUse2PropType.put("1320", PropertyType.LAND);
		landUse2PropType.put("3221", PropertyType.OTHER);
		
		landUse2Units.put("101", Integer.valueOf(1));
		landUse2Units.put("1010", Integer.valueOf(1));
		landUse2Units.put("1012", Integer.valueOf(1));
		landUse2Units.put("1013", Integer.valueOf(1));
		landUse2Units.put("102", Integer.valueOf(1));
		landUse2Units.put("1020", Integer.valueOf(1));
		landUse2Units.put("103", Integer.valueOf(1));
		landUse2Units.put("1030", Integer.valueOf(2));
		landUse2Units.put("104", Integer.valueOf(2));
		landUse2Units.put("1040", Integer.valueOf(2));
		landUse2Units.put("105", Integer.valueOf(3));
		landUse2Units.put("1050", Integer.valueOf(3));		
	}
	
	@Resource(name="dsLandUseLookup")
	private IDataSet dsLandUseLookup;
	
	@Resource(name="roadNameNormalizer")
	INameNormalizer roadNameNormalizer;
	
	@Resource(name="unitNormalizer")
	INameNormalizer unitNormalizer;
	
	private PropertyCache propertyCache;
	
	public MassGISPropertyReader() {
		super("MassGIS Assesors Property loader");
	}
	
	@Override
	public boolean start(Map<String, Object> context) {
		if (!super.start(context)) {
			return false;
		}
		
		propertyCache = PropertyCache.fetchFromContext(getContext());
		
		return true;
	}
	
	@Override
	protected void handleItem(Map<String, Object> row) {
		// Filter records that don't have required values
		
		String srcPropId = (String) row.get("PROP_ID");	
		if (StringUtils.isEmpty(srcPropId)) {
			getLog().warn("No source identifier provided for property: " + row.get("SITE_ADDR"));
			return;			
		}
		
		String fullAddr = (String) row.get("SITE_ADDR");	
		if (StringUtils.isEmpty(fullAddr)) {
			getLog().warn("No address provided for property: " + row.get("PROP_ID"));
			return;			
		}
		
		String parcelFeatureName = (String) row.get("LOC_ID");
		if (StringUtils.isEmpty(parcelFeatureName)) {
			getLog().warn("No parcel name/identifier provided for: " + fullAddr);
			return;
		}
		
		String addrStr = (String) row.get("ADDR_NUM");
		if (StringUtils.isEmpty(addrStr)) {
			getLog().warn("No address number provided for: " + fullAddr);
			return;
		}
		
		String useCode = (String) row.get("USE_CODE");
		PropertyType propType = null;
		if (useCode != null) {
			propType = landUse2PropType.get(useCode);
		}
		
		if (propType == null) {
			getLog().warn("Invalid land use code (" + useCode + ") provided for: " + fullAddr);
			return;
		}
		
		// Attempt to derive unites from use code. Otherwise use what the record says
		Integer units = landUse2Units.get("useCode");
		if (units == null) {
			units = (Integer) row.get("UNITS");
		}

		// Set up property component
		Property prop = new Property();
		prop.setStateId((String) getContext().get("STATE_ID"));
		prop.setTownId((String) getContext().get("TOWN_ID"));
		prop.setParcelFeatureName(parcelFeatureName);
		prop.setRoadName(roadNameNormalizer.normalize((String) row.get("FULL_STR")));
		prop.setAddrNum(new Integer(StrUtils.extractNum(addrStr)));
		prop.setAddrStr(addrStr.toUpperCase());
		prop.setType(propType.toString());
		prop.setUnits(units);
		
		// Set up listing component
		PropertyListing listing = new PropertyListing();
		listing.setListingSourceId(MASSGIS_SOURCE_ID);
		listing.setSourcePropertyId(srcPropId);
		listing.setLandAreaSqFt(AcreCalc.calcSqFeet((Double) row.get("LOT_SIZE"), (String) row.get("LOT_UNITS")));
		listing.setLandAreaAcres(AcreCalc.calcAcres((Double) row.get("LOT_SIZE"), (String) row.get("LOT_UNITS")));
		listing.setPrice((Integer) row.get("TOTAL_VAL"));
		listing.setUnit(unitNormalizer.normalize((String) row.get("LOCATION")));
		listing.setYearBuilt((Integer) row.get("YEAR_BUILT"));
		listing.setStyle((String) row.get("STYLE"));
		listing.setLivingArea((Integer) row.get("RES_AREA"));
		listing.setRooms((Integer) row.get("NUM_ROOMS"));
		listing.setFloors(ToIntUtils.str2Int((String) row.get("STORIES")));

		propertyCache.putProperty(prop, listing);
	}
}
