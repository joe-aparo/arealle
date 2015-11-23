package net.jsa.arealle.task.process;

import java.util.Map;

import net.jsa.arealle.task.esri.ShapeEntry;

import com.vividsolutions.jts.geom.MultiLineString;

public class CoastLineFeatureReader extends DomainFeatureReader  {
	private static final String COMMON_COAST_LINE_NAME = "Coast Line";
	private static final int MAX_ACCEPTABLE_LINE_POINTS = 1000;

	@Override
	protected String getFeatureName(Map<String, Object> data) {
		return COMMON_COAST_LINE_NAME;
	}
	
	@Override
	protected boolean keepEntry(ShapeEntry shapeEntry, Map<String, Object> data) {
		if (!super.keepEntry(shapeEntry, data)) {
			return false;
		}
		
		Integer typ = (Integer) data.get("TYPE");
		
		if (typ == null || typ.intValue() != 1) {
			return false;
		}
		
		MultiLineString  line = (MultiLineString) shapeEntry.getShape();
		
		boolean tooBig = line.getNumPoints() > MAX_ACCEPTABLE_LINE_POINTS;
		
		if (tooBig) {
			getLog().debug("Line is too big... ignoring");
		}
		
		return !tooBig;
	}
}
