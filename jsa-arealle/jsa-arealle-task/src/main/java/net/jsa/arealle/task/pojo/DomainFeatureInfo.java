package net.jsa.arealle.task.pojo;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public class DomainFeatureInfo {
	private static final int INIT_SHAPE_LIST_SIZE = 10;
	
	private List<Geometry> shapes = new ArrayList<Geometry>(INIT_SHAPE_LIST_SIZE);
	private String geoWkt;
	private String shapeWkt;
	private String gmapJson;

	public List<Geometry> getShapes() {
		return shapes;
	}

	public void setShapes(List<Geometry> shapes) {
		this.shapes = shapes;
	}

	public String getGeoWkt() {
		return geoWkt;
	}
	
	public void setGeoWkt(String geoWkt) {
		this.geoWkt = geoWkt;
	}
	
	public String getShapeWkt() {
		return shapeWkt;
	}
	
	public void setShapeWkt(String shapeWkt) {
		this.shapeWkt = shapeWkt;
	}
	
	public String getGmapJson() {
		return gmapJson;
	}
	
	public void setGmapJson(String gmapJson) {
		this.gmapJson = gmapJson;
	}
}
