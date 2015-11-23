package net.jsa.arealle.task.geom;

import java.io.IOException;
import java.io.InputStream;

import net.jsa.arealle.task.esri.ReadUtils;

public class Point {
	private double x;
	private double y;
	private double z;
	private double m;
	
	public Point() {
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public double getM() {
		return m;
	}
	
	public void setM(double m) {
		this.m = m;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str
		.append('{')
		.append("x:")
		.append(x)
		.append(',')
		.append("y:")
		.append(y)
		.append('}');
		
		return str.toString();
	}
	
	public static Point read(InputStream is) throws IOException {
		Point pt = new Point();
		
		pt.setX(ReadUtils.readLeDouble(is));
		pt.setY(ReadUtils.readLeDouble(is));

		return pt;
	}		
}
