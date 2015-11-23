package net.jsa.arealle.task.esri;

import java.io.IOException;
import java.io.InputStream;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class Box {
	private Coordinate bottomLeft;
	private Coordinate topRight;

	public Coordinate getBottomLeft() {
		return bottomLeft;
	}

	public void setBottomLeft(Coordinate bottomLeft) {
		this.bottomLeft = bottomLeft;
	}

	public Coordinate getTopRight() {
		return topRight;
	}

	public void setTopRight(Coordinate topRight) {
		this.topRight = topRight;
	}
	
	public void read(InputStream is, GeometryFactory factory) throws IOException {
		bottomLeft = ReadUtils.readXYCoord(is);
		topRight = ReadUtils.readXYCoord(is);
	}
}
