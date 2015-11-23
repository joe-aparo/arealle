package net.jsa.arealle.task.process;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.jsa.arealle.task.esri.ShapeEntry;

public class PondLakeFeatureReader extends DomainFeatureReader {

	@Override
	protected boolean keepEntry(ShapeEntry shapeEntry, Map<String, Object> data) {
		if (!super.keepEntry(shapeEntry, data)) {
			return false;
		}
		
		String tmp = getFeatureName(data);
		if (tmp != null) {
			tmp = tmp.toLowerCase();
		}

		return !StringUtils.isEmpty(tmp) && 
			(tmp.contains("pond") || tmp.contains("lake") || tmp.contains("reservoir"));
	}

	@Override
	protected String getFeatureName(Map<String, Object> data) {
		return getNameNormalizer().normalize((String) data.get("NAME"));
	}
}
