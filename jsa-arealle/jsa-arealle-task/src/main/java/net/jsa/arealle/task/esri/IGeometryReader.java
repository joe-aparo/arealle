package net.jsa.arealle.task.esri;

import java.io.IOException;
import java.io.InputStream;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public interface IGeometryReader {
	boolean hasBoundingBox();
	Geometry read(InputStream is, GeometryFactory factory)  throws IOException;
}
