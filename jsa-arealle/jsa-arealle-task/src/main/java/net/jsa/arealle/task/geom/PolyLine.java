package net.jsa.arealle.task.geom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.jsa.arealle.task.esri.ReadUtils;

public class PolyLine extends AbstractShape {
	List<List<Point>> lines = new ArrayList<List<Point>>();
	
	public void read(InputStream is) throws IOException {
		super.read(is);
		
		// Number of lines in the polyline
		int numLines = ReadUtils.readLeInt(is);
		
		// Total number of points for all lines
		int numPoints = ReadUtils.readLeInt(is);
		
		// For each ring, the index to its first point in the point array
		int[] indices = new int[numLines];
		for (int i = 0; i < numLines; i++) {
			indices[i] = ReadUtils.readLeInt(is);
		}
		
		// Array of all points for all rings
		Point[] pts = new Point[numPoints];
		for (int i = 0; i < numPoints; i++) {
			pts[i] = ReadUtils.readPoint(is);
		}
		
		// Convert what was read to a collection of lines
		for (int i = 0; i < numLines; i++) {
			int ptIdx1 = indices[i];
			int ptIdx2 = (i+1) < numLines ? indices[i+1] - 1 : pts.length - 1;

			List<Point> ptList = new ArrayList<Point>(ptIdx2 - ptIdx1 + 1);
			while (ptIdx1 <= ptIdx2) {
				ptList.add(pts[ptIdx1++]);
			}
			
			lines.add(ptList);
		}
	}

	public List<List<Point>> getLines() {
		return lines;
	}
	
	public String toWkt() {
		StringBuilder wkt = new StringBuilder(500);
		
		// e.g. MULTILINESTRING ((10 10, 20 20, 10 40),	(40 40, 30 30, 40 20, 30 10))
		wkt.append("MULTILINESTRING (");
		
		int ct = 0;
		
		for (List<Point> line : lines) {
			if (ct++ > 0) {
				wkt.append(',');
			}
			writePointList(line, wkt);
		}
				
		wkt.append(')');
		
		return wkt.toString();
	}
}
