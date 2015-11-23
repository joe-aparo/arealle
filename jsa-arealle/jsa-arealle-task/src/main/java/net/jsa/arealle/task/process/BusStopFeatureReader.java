package net.jsa.arealle.task.process;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import net.jsa.arealle.task.util.RoadNameNormalizer;

public class BusStopFeatureReader extends DomainFeatureReader {

	private static char ROAD_DELIM_CHR = '@';
	private static String STOP_SEP_STR = " - ";
	private static String ROAD_DELIM_STR = " at ";
	
	@Resource(name="roadNameNormalizer")
	private RoadNameNormalizer roadNameNormalizer;
	
	@Override
	protected String getFeatureName(Map<String, Object> data) {
		String rawName = null;

		Integer stopId = (Integer) data.get("STOP_ID");
		String stopName = (String) data.get("STOP_NAME");
		
		if (stopId != null && !StringUtils.isEmpty(stopName)) {
			int pos = stopName.indexOf(ROAD_DELIM_CHR);
			if (pos != -1) {
				String rd1 = roadNameNormalizer.normalize(stopName.substring(0, pos).trim().toLowerCase());
				String rd2 = roadNameNormalizer.normalize(stopName.substring(pos + 1).trim().toLowerCase());
				
				stopName = rd1 + ROAD_DELIM_STR + rd2;
			}
			
			rawName = stopId + STOP_SEP_STR + stopName; 

		}
		
		return getNameNormalizer().normalize(rawName);
	}

}
