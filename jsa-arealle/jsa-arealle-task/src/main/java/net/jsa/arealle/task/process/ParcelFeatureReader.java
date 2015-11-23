package net.jsa.arealle.task.process;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import net.jsa.arealle.task.esri.ShapeEntry;
import net.jsa.arealle.task.pojo.FeatureInfo;

@Component
public class ParcelFeatureReader extends DomainFeatureReader {
	private static final int MAX_PARCEL_POINTS = 1000;

	@Override
	protected boolean keepEntry(ShapeEntry shapeEntry, Map<String, Object> data) {
		String polyType = (String) data.get("POLY_TYPE");
		
		if (StringUtils.isEmpty(polyType) || 
			(!polyType.equals("FEE") && !polyType.equals("TAX"))) {
			
			return false;
		}
		
		int numPoints = shapeEntry.getShape().getNumPoints();
		if (numPoints > MAX_PARCEL_POINTS) {
			getLog().warn(
				"Too many points (" + numPoints + ") for parcel with id: " + data.get("LOC_ID"));
		}
		
		return numPoints <= MAX_PARCEL_POINTS;
	}

	@Override
	protected String getFeatureName(Map<String, Object> data) {
		return (String) data.get("LOC_ID");
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.jsa.arealle.task.DomainFeatureReader#isValidDomainId(java.lang.String, java.util.Map)
	 */
	protected boolean isValidDomainId(String domainId, Map<String, Object> data) {
		
		// Only allow association with a town domain id if that id maps to the same
		// source town id in the given data object.
		Integer townId = (Integer) data.get("TOWN_ID");
		if (townId == null) {
			return false;
		}
		
		String srcTownId = townId.toString();
		boolean valid = false;
		
		// Fetch the town feature object for the given domain (town) id. It should be in the cache.
		FeatureInfo townInfo = getFeatureCache().getFeatureInfoForId("TOWN", domainId);
		if (townInfo != null) {
			String featureTownSrcId = townInfo.getAttrs().get("SOURCE_ID");
			valid = (featureTownSrcId != null && srcTownId.equals(featureTownSrcId));
		} else {
			valid = false;
		}
		
		return valid;
	}
}
