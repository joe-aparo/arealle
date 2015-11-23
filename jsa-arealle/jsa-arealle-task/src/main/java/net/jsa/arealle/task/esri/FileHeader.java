package net.jsa.arealle.task.esri;

import java.io.IOException;
import java.io.InputStream;

import net.jsa.arealle.task.geom.ShapeType;

import com.vividsolutions.jts.geom.GeometryFactory;

public class FileHeader {
	private String fileName;
	private int fileLength;
	private ShapeType shapeType;
	private Box boundingBox;
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public int getFileLength() {
		return fileLength;
	}
	
	public ShapeType getShapeType() {
		return shapeType;
	}
	
	public Box getBoundingBox() {
		return boundingBox;
	}
	
	public void read(InputStream is, GeometryFactory factory) throws IOException {
		fileLength = ReadUtils.readBeInt(is);
		ReadUtils.skip(is, 4); // version
		shapeType = ShapeType.getByCode(ReadUtils.readLeInt(is));
		boundingBox = new Box();
		boundingBox.read(is, factory);
		ReadUtils.skip(is, 32); // trailing stuff we don't need - z,m min/max, etc.
	}
}
