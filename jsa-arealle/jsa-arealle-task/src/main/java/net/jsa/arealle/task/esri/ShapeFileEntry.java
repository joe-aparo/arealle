package net.jsa.arealle.task.esri;

import java.io.IOException;
import java.io.InputStream;

import net.jsa.arealle.task.geom.ShapeType;

public class ShapeFileEntry {
	private int id;
	private int length;
	private ShapeType type;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public ShapeType getType() {
		return type;
	}

	public void setType(ShapeType type) {
		this.type = type;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str
		.append("id:" + id + ", ")
		.append("length:" + length + ", ")
		.append("type:" + type);
		
		return str.toString();
	}

	public void read(InputStream is) throws IOException {
		setId(ReadUtils.readBeInt(is));
		setLength(ReadUtils.readBeInt(is));
		setType(ShapeType.getByCode(ReadUtils.readLeInt(is)));
	}
}
