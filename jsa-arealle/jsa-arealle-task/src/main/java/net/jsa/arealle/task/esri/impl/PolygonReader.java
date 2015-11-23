package net.jsa.arealle.task.esri.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jsa.arealle.task.esri.ReadUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class PolygonReader extends AbstractGeometryReader {

	@Override
	public Geometry read(InputStream is, GeometryFactory factory) throws IOException {

		// Number of rings in the polygon
		int numRings = ReadUtils.readLeInt(is);
		
		// Total number of points for all rings
		int numPoints = ReadUtils.readLeInt(is);
		
		// For each ring, the index to its first point in the point array
		int[] indices = new int[numRings];
		for (int i = 0; i < numRings; i++) {
			indices[i] = ReadUtils.readLeInt(is);
		}

		// Read line coordinates, converting to lat/lng
		Coordinate[] coords = ReadUtils.readXYCoords(is, numPoints);
		
		Polygon[] polygons = new Polygon[numRings];
		
		// Generate polygons from rings
		for (int i = 0; i < numRings; i++) {
			int ptIdx1 = indices[i];
			int ptIdx2 = (i+1) < numRings ? indices[i+1] - 1 : coords.length - 1;

			int subCoordLen = ptIdx2 - ptIdx1 + 1; // Extra slot to allow 'closing' the ring
			Coordinate[] subCoords = new Coordinate[subCoordLen];

			int idx;
			for (idx = 0; idx <= subCoordLen - 1; idx++) {
				subCoords[idx] = coords[ptIdx1++];
			}
			
			polygons[i] = factory.createPolygon(dedupCoords(subCoords));
		}
		
		MultiPolygon multiPoly = factory.createMultiPolygon(polygons);
		
		return multiPoly;
	}
	
	/**
	 * There are cases when duplicate coordinates are present within a set of polygon coordinates.
	 * This logic removes them with the help of a temporary lookup hash.
	 * 
	 * @param inCoords The coordinates to de-dup
	 * @return A de-duped set of coorinates
	 */
	protected Coordinate[] dedupCoords(Coordinate[] inCoords) {
		int len = inCoords.length;
		List<Coordinate> tmp = new ArrayList<Coordinate>(len);
		Set<String> lkp = new HashSet<String>(len);
		
		int ix = 0;
		for (Coordinate coord : inCoords) {
			if (ix == 0 || ix == (len - 1)) { // Disregard 1st/last - they will equal in a closed polygon
				tmp.add(coord);
			} else {  // No intervening dups
				String k = coord.x + ":" + coord.y;
				if (!lkp.contains(k)) {
					tmp.add(coord);
					lkp.add(k);
				}
			}

			ix++;
		}
		
		Coordinate[] outCoords = new Coordinate[tmp.size()];
		outCoords = tmp.toArray(outCoords);
		
		return outCoords;
	}
}
