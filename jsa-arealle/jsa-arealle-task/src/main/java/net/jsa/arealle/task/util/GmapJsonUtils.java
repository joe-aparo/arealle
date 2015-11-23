package net.jsa.arealle.task.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Utility class for converting shapes to GoogleMaps API JSON equivalents.
 * 
 * @author jsaparo
 *
 */
public class GmapJsonUtils {
	public static final String LATLNG_PATTERN = "new google.maps.LatLng(%f,%f)";
	public static final int INIT_JSON_BUF_SIZE = 1000;

	public static String toJsonPoint(Coordinate coord) {
		return String.format(LATLNG_PATTERN, coord.x, coord.y);
	}
	
	public static String toJsonPoint(Point point) {
		return toJsonPoint(point.getCoordinate());
	}
	
	public static String toJsonPolygon(Polygon polygon) {
		StringBuilder json = new StringBuilder(INIT_JSON_BUF_SIZE);
		
		json.append("new google.maps.Polygon({");
		json.append("paths:[");
		
		addJsonPoints(polygon.getCoordinates(), json);
		
		json.append("],");
		json.append("geodesic:true");
		json.append("})");
		
		return json.toString();
	}
	
	public static String toJsonPolyline(LineString polyline) {
		StringBuilder json = new StringBuilder(INIT_JSON_BUF_SIZE);
		
		json.append("new google.maps.Polyline({");
		json.append("path:[");
		
		addJsonPoints(polyline.getCoordinates(), json);
		
		json.append("],");
		json.append("geodesic:true");
		json.append("})");
		
		return json.toString();
	}
	
	private static void addJsonPoints(Coordinate[] coords, StringBuilder json) {
		int i = 0;
		for (Coordinate coord : coords) {
			if (i++ > 0) {
				json.append(',');
			}

			json.append(toJsonPoint(coord));
		}
	}
}
