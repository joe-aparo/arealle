package net.jsa.arealle.task.esri;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.jsa.arealle.task.geom.Point;
import net.jsa.arealle.task.geom.PointPair;

import com.vividsolutions.jts.geom.Coordinate;

public class ReadUtils {
	private static final int XYPOINT_DIM = 2;
	
	public static Point readPoint(InputStream is) throws IOException {
		Point pt = new Point();
		
		pt.setX(readLeDouble(is));
		pt.setY(readLeDouble(is));

		return pt;
	}
	
	public static PointPair readPointPair(InputStream is) throws IOException {
		PointPair pair = new PointPair();
		
		pair.setP1(readPoint(is));
		pair.setP2(readPoint(is));

		return pair;
	}
	
	public static void skip(InputStream is, int ct) throws IOException {
		byte[] buf = new byte[ct];
		is.read(buf);
	}
	
	public static int readLeInt(InputStream is) throws IOException {
		byte [] arr = new byte[4];
		is.read(arr);
		ByteBuffer bb = ByteBuffer.wrap(arr);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		
		return bb.getInt();
	}
	
	public static int readBeInt(InputStream is) throws IOException {
		byte[] arr = new byte[4];
		is.read(arr);
		ByteBuffer bb = ByteBuffer.wrap(arr);
		bb.order(ByteOrder.BIG_ENDIAN);
		
		return bb.getInt();
	}
	
	public static double readLeDouble(InputStream is) throws IOException {
		byte [] arr = new byte[8];
		is.read(arr);
		ByteBuffer bb = ByteBuffer.wrap(arr);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		
		return bb.getDouble();
	}
	
	public static double[] readXYPointOrdinals(InputStream is, int numPoints) throws IOException {
		int readCount = numPoints * XYPOINT_DIM;
		double[] ordinals = new double[readCount];
		
		for (int i = 0; i < readCount; i++) {
			ordinals[i] = readLeDouble(is);
		}
		
		return ordinals;
	}
	
	public static Coordinate readXYCoord(InputStream is) throws IOException {
		Coordinate[] coords = readXYCoords(is, 1);
		return coords[0];
	}
	
	public static Coordinate[] readXYCoords(InputStream is, int numPoints) throws IOException {
		// Read raw ordinals  from stream
		double[] pointOrdinals = readXYPointOrdinals(is, numPoints);
		
		// Array of all points for all lines
		Coordinate[] coords = new Coordinate[numPoints];
		for (int i = 0; i < numPoints; i++) {
			double x = pointOrdinals[i * XYPOINT_DIM];
			double y = pointOrdinals[(i * XYPOINT_DIM ) + 1];

			coords[i] = new Coordinate(x, y);
		}
		
		return coords;
	}	
}
