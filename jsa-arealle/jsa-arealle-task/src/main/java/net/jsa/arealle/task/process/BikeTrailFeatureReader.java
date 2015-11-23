package net.jsa.arealle.task.process;

import java.util.Map;

public class BikeTrailFeatureReader extends DomainFeatureReader {

	@Override
	protected String getFeatureName(Map<String, Object> data) {
		return (String) data.get("TRAILNAME");
	}

}
