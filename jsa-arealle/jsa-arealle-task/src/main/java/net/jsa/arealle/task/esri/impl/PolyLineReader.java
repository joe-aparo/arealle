package net.jsa.arealle.task.esri.impl;

import java.io.IOException;
import java.io.InputStream;

import net.jsa.arealle.task.esri.ReadUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class PolyLineReader extends AbstractGeometryReader {

	@Override
	public Geometry read(InputStream is, GeometryFactory factory) throws IOException {
		// Number of lines in the polyline
		int numLines = ReadUtils.readLeInt(is);
		
		// Total number of points for all lines
		int numPoints = ReadUtils.readLeInt(is);
		
		// For each line, the index to its first point in the point array
		int[] indices = new int[numLines];
		for (int i = 0; i < numLines; i++) {
			indices[i] = ReadUtils.readLeInt(is);
		}
		
		// Read line coordinates, converting to lat/lng
		Coordinate[] coords = ReadUtils.readXYCoords(is, numPoints);
		
		// Convert what was read to a collection of lines
		LineString lineStrings[] = new LineString[numLines];
		for (int i = 0; i < numLines; i++) {
			int ptIdx1 = indices[i];
			int ptIdx2 = (i+1) < numLines ? indices[i+1] - 1 : coords.length - 1;

			int subCoordLen = ptIdx2 - ptIdx1 + 1; 
			Coordinate[] subCoords = new Coordinate[subCoordLen];

			for (int j = 0; j < subCoordLen; j++) {
				subCoords[j] = coords[ptIdx1++];
			}

			lineStrings[i] = factory.createLineString(subCoords);
		}
		
		return factory.createMultiLineString(lineStrings);
	}
}
