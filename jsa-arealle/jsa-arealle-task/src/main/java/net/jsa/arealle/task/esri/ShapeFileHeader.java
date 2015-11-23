package net.jsa.arealle.task.esri;

import java.io.IOException;
import java.io.InputStream;

import net.jsa.arealle.task.geom.PointPair;
import net.jsa.arealle.task.geom.ShapeType;

public class ShapeFileHeader {
	private String fileName;
	private int fileLength;
	private ShapeType shapeType;
	private PointPair box;

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getFileLength() {
		return fileLength;
	}
	public void setFileLength(int fileLength) {
		this.fileLength = fileLength;
	}
	public ShapeType getShapeType() {
		return shapeType;
	}
	public void setShapeType(ShapeType shapeType) {
		this.shapeType = shapeType;
	}
	
	public PointPair getBox() {
		return box;
	}
	public void setBox(PointPair box) {
		this.box = box;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("fileLength:" + fileLength + ", ")
		.append("shapeType:" + shapeType + ", ")
		.append("box:" + box.toString());
		
		return str.toString();
	}
	
	public void read(InputStream is) throws IOException {
		setFileLength(ReadUtils.readBeInt(is));
		ReadUtils.skip(is, 4); // version
		setShapeType(ShapeType.getByCode(ReadUtils.readLeInt(is)));
		setBox(ReadUtils.readPointPair(is));
		ReadUtils.skip(is, 32); // z,m min/max
	}		
}
