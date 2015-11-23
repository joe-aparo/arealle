package net.jsa.arealle.task.esri.impl;

import java.io.IOException;
import java.io.InputStream;

import net.jsa.arealle.task.esri.ReadUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class PointReader extends AbstractGeometryReader  {

	@Override
	public Geometry read(InputStream is, GeometryFactory factory) throws IOException {
		Coordinate coord = ReadUtils.readXYCoord(is);
		
		return factory.createPoint(coord);
	}
	
	public boolean hasBoundingBox() {
		return false;
	}
}
