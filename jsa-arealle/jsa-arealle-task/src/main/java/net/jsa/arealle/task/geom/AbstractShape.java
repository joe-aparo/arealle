package net.jsa.arealle.task.geom;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;

import net.jsa.arealle.task.esri.ReadUtils;

public abstract class AbstractShape implements IShape {
	private static CoordinateReferenceSystem sourceCrs;
	private static CoordinateReferenceSystem targetCrs;

	static {
		try {
			sourceCrs = CRS.decode("EPSG:26986");
			targetCrs = CRS.decode("EPSG:4326");
		} catch (NoSuchAuthorityCodeException e) {
			throw new RuntimeException("Invalid authority");
		} catch (FactoryException e) {
			throw new RuntimeException("Error initializing CRS");
		}
	}

	private boolean isNull;
	private PointPair boundingBox;
	
	public boolean getIsNull() {
		return isNull;
	}
	public void setIsNull(boolean isNull) {
		this.isNull = isNull;
	}
	
	public PointPair getBoundingBox() {
		return boundingBox;
	}
	
	public void setBoundingBox(PointPair boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("isNull:" + isNull + ", ")
		.append("box:" + boundingBox.toString());
		
		return str.toString();
	}
	
	public void read(InputStream is) throws IOException {
		boundingBox = ReadUtils.readPointPair(is);
	}
	
	public Point getCentroid() {
		return boundingBox.getCentroid();		
	}
	
	public static void writePointList(List<Point> points, StringBuilder wkt) {
		boolean delim = false;
		
		wkt.append('(');
		
		for (Point pt : points) {
			if (delim) {
				wkt.append(',');
			}

			writePoint(pt, wkt);
			delim = true;
		}
		
		wkt.append(')');		
	}
	
	public static void writePoint(Point pt, StringBuilder wkt) {
		double[] inCoords = new double[2];
		inCoords[0] = pt.getX();
		inCoords[1] = pt.getY();
		
		// convert to Lat/Lng
		double[] outCoords = null;
		outCoords = toLatLng(inCoords);
		
		// Write WKT point
		wkt.append(outCoords[0]);
		wkt.append(' ');
		wkt.append(outCoords[1]);
	}
	
	
	public static String toWkt(Point pt) {
		StringBuilder wkt = new StringBuilder(20);
		writePoint(pt, wkt);
		
		return "POINT(" + wkt + ")";
	}
	
	public static double[] toLatLng(double[] inVals) {
		double[] outVals = new double[inVals.length];
		
		try {
	    	DefaultCoordinateOperationFactory cof = new DefaultCoordinateOperationFactory(); 
	    	CoordinateOperation	op = cof.createOperation(sourceCrs, targetCrs);
	    	MathTransform xfrm = op.getMathTransform();
	    	
	        xfrm.transform(inVals, 0, outVals, 0, 1);
		} catch (Exception ex) {
			throw new RuntimeException("Error converting to LatLng", ex);
		}
        
        return outVals;
	}
}
