package net.jsa.arealle.task.process;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class TStopFeatureReader extends DomainFeatureReader {;
	private static final String STATION_LABEL = "station";
	
	@Override
	protected String getFeatureName(Map<String, Object> data) {
		String tmp = (String) data.get("STATION");
		String line = (String) data.get("LINE");
		if (!StringUtils.isEmpty(tmp)) {
			tmp = tmp.toLowerCase();
			if (!tmp.endsWith(STATION_LABEL)) {
				tmp = tmp + ' ' + STATION_LABEL;
			}
		}
		
		tmp = getNameNormalizer().normalize(tmp);
		
		if (!StringUtils.isEmpty(line)) {
			tmp = tmp + " (" + line + ")";
		}
		
		return tmp;
	}
}
