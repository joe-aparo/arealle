package net.jsa.arealle.task.geom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.jsa.arealle.task.esri.ReadUtils;

public class Polygon extends AbstractShape {
	List<Point> outerRing;
	List<List<Point>> innerRings = new ArrayList<List<Point>>();
	
	public void read(InputStream is) throws IOException {
		super.read(is);
		
		// Number of rings in the polygon
		int numRings = ReadUtils.readLeInt(is);
		
		// Total number of points for all rings
		int numPoints = ReadUtils.readLeInt(is);
		
		// For each ring, the index to its first point in the point array
		int[] indices = new int[numRings];
		for (int i = 0; i < numRings; i++) {
			indices[i] = ReadUtils.readLeInt(is);
		}
		
		// Array of all points for all rings
		Point[] pts = new Point[numPoints];
		for (int i = 0; i < numPoints; i++) {
			pts[i] = ReadUtils.readPoint(is);
		}
		
		// Convert what was read to an outer ring and and inner rings
		for (int i = 0; i < numRings; i++) {
			int ptIdx1 = indices[i];
			int ptIdx2 = (i+1) < numRings ? indices[i+1] - 1 : pts.length - 1;

			List<Point> ptList = new ArrayList<Point>(ptIdx2 - ptIdx1 + 1);
			while (ptIdx1 <= ptIdx2) {
				ptList.add(pts[ptIdx1++]);
			}
			
			if (i == 0) {
				outerRing = ptList;
			} else {
				innerRings.add(ptList);
			}
		}
	}

	public List<Point> getOuterRing() {
		return outerRing;
	}

	public List<List<Point>> getInnerRings() {
		return innerRings;
	}
	
	public String toWkt() {
		StringBuilder wkt = new StringBuilder(500);

		//e.g. POLYGON ((10 10, 10 20, 20 20, 20 15, 10 10))
		wkt.append("POLYGON(");

		writePointList(getOuterRing(), wkt);

		if (getOuterRing().size() > 0) {
			for (List<Point> pts : getInnerRings()) {
				wkt.append(',');
				writePointList(pts, wkt);
			}
		}
		
		wkt.append(')');
		
		return wkt.toString();
	}
}
