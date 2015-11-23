package net.jsa.arealle.task.esri;

import java.util.Map;

import net.jsa.arealle.task.geom.IShape;

public interface IShapeFileHandler {
	void onStart(ShapeFileHeader hdr);
	void onShape(ShapeFileEntry entry, IShape shape, Map<String, Object> data);
	
	void onHeader(FileHeader hdr);
	void onEntry(ShapeEntry entry);
	
	void onFinish();
}
