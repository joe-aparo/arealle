package net.jsa.arealle.task.process;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.jsa.arealle.task.esri.ShapeEntry;

public class OpenSpaceFeatureReader extends DomainFeatureReader {

	@Override
	protected boolean keepEntry(ShapeEntry shapeEntry, Map<String, Object> data) {
		if (!super.keepEntry(shapeEntry, data)) {
			return false;
		}

		String primPurp = (String) data.get("PRIM_PURP");
		if (StringUtils.isEmpty(primPurp)) {
			return false;
		}
		
		char chr = Character.toUpperCase(primPurp.charAt(0));
		if (chr != 'C' && chr != 'R' && chr != 'B') {
			return false;
		}

		return true;
	}

	@Override
	protected String getFeatureName(Map<String, Object> data) {
		return getNameNormalizer().normalize((String) data.get("SITE_NAME"));
	}
}
