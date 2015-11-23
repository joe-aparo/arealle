package net.jsa.arealle.task.geom;

import java.io.IOException;
import java.io.InputStream;

import net.jsa.arealle.task.esri.ReadUtils;

public class PointPair {
	private Point p1 = new Point();
	private Point p2 = new Point();
	
	public Point getP1() {
		return p1;
	}
	
	public void setP1(Point p1) {
		this.p1 = p1;
	}
	
	public Point getP2() {
		return p2;
	}
	
	public void setP2(Point p2) {
		this.p2 = p2;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str
		.append("v1:" + p1.toString() + ", ")
		.append("v2:" + p2.toString());
		
		return str.toString();
	}
	
	public static PointPair read(InputStream is) throws IOException {
		PointPair rect = new PointPair();
		
		rect.setP1(ReadUtils.readPoint(is));
		rect.setP2(ReadUtils.readPoint(is));

		return rect;
	}

	public Point getCentroid() {
		return new Point(
			p1.getX() + ((p2.getX() - p1.getX()) / 2),
			p1.getY() + ((p2.getY() - p1.getY()) / 2));		
	}

}
