package net.jsa.arealle.task.esri;

import java.io.IOException;
import java.io.InputStream;

import net.jsa.arealle.task.geom.ShapeType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class ShapeEntry {
	private int id;
	private int length;
	private ShapeType shapeType;
	private Box boundingBox;
	private Geometry shape;
	
	public int getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

	public ShapeType getShapeType() {
		return shapeType;
	}

	public Box getBoundingBox() {
		return boundingBox;
	}
	
	public Geometry getShape() {
		return shape;
	}
	
	public void read(InputStream is, GeometryFactory factory, IGeometryReader geometryReader) throws IOException {
		id = (ReadUtils.readBeInt(is));
		length = ReadUtils.readBeInt(is);
		shapeType = ShapeType.getByCode(ReadUtils.readLeInt(is));

		// Point-type shapes don't tend to have bounding boxes, while other types of shapes do.
		// This is determined by the reader implementation
		if (geometryReader.hasBoundingBox()) {
			boundingBox = new Box();
			boundingBox.read(is, factory);
		}

		shape = geometryReader.read(is, factory);
	}
}
