package net.jsa.arealle.task.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;

import net.jsa.common.logging.LogUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.io.gml2.GMLWriter;

/**
 * Geometric utility methods.
 * 
 * @author jsaparo
 *
 */
public class GeomUtils {
	private static int XYDIM_SIZE = 2;
	private static String LAT_LNG_CRS_ID = "EPSG:4326";
	private static int INIT_JSON_BUF_SIZE = 1000;
	
	private static Map<String, CoordinateReferenceSystem> crsMap = new HashMap<String, CoordinateReferenceSystem>();

	/**
	 * Fetch the coordinate system object for the given system id.
	 * 
	 * @param crsId A coordinate reference system identifer
	 * @return A coordinate reference system object
	 */
	public static CoordinateReferenceSystem getCrs(String crsId) {

		CoordinateReferenceSystem crs = crsMap.get(crsId);

		if (crs == null) {
			try {
				crs = CRS.decode(crsId);
				crsMap.put(crsId, crs);
			} catch (NoSuchAuthorityCodeException e) {
				throw new RuntimeException("Invalid authority");
			} catch (FactoryException e) {
				throw new RuntimeException("Error initializing CRS");
			}			
		}

		return crs;
	}
	
	/**
	 * Convert given values from a source coordinate reference system, to a
	 * target system.
	 * 
	 * @param inVals Values to convert
	 * @param sourceCrsId Source reference system
	 * @param targetSrcId Target reference system
	 * @return An array of converted values.
	 */
	public static double[] convert(double[] inVals, String sourceCrsId, String targetSrcId) {
		int numPts = inVals.length / 2;
		
		CoordinateReferenceSystem sourceCrs = getCrs(sourceCrsId);
		CoordinateReferenceSystem targetCrs = getCrs(targetSrcId);

		try {
	    	DefaultCoordinateOperationFactory cof = new DefaultCoordinateOperationFactory(); 
	    	CoordinateOperation	op = cof.createOperation(sourceCrs, targetCrs);
	    	MathTransform xfrm = op.getMathTransform();
	    	
	        xfrm.transform(inVals, 0, inVals, 0, numPts);
		} catch (Exception ex) {
			throw new RuntimeException("Error converting to LatLng", ex);
		}
        
        return inVals;
	}
	
	/**
	 * Convert a given geometric shape from a source coordinate reference system, to a
	 * target system.
	 * 
	 * @param geometry A geometric shape to convert
	 * @param sourceCrsId Source reference system
	 * @param targetSrcId Target reference system
	 * @return A converted gometric shape.
	 */	public static Geometry convert(Geometry geometry, String sourceCrsId, String targetCrsId) {
		Coordinate[] coords = geometry.getCoordinates();
		
		// If already converted, y coordinate will be negative.
		if (coords[0].y < 0) {
			return geometry;
		}
		
		double[] ordinals = new double[coords.length * XYDIM_SIZE];
		for (int i = 0; i < coords.length; i++) {
			Coordinate coord = coords[i];
			ordinals[i * XYDIM_SIZE] = coord.x;
			ordinals[(i * XYDIM_SIZE) + 1] = coord.y;
		}
		
		GeomUtils.convert(ordinals, sourceCrsId, targetCrsId);

		for (int i = 0; i < coords.length; i++) {
			Coordinate coord = coords[i];
			coord.x = ordinals[i * XYDIM_SIZE];
			coord.y = ordinals[(i * XYDIM_SIZE) + 1];
		}
		
		geometry.geometryChanged();
		
		return geometry;
	}
	 
	
	/**
	 * Convert given values to lat/long geo coordinates.
	 * @param inVals The values to convert
	 * @param sourceCrsId The source coordinate reference system id.
	 * @return A converted set of values
	 */
	public static double[] convertToLatLng(double[] inVals, String sourceCrsId) {
		return convert(inVals, sourceCrsId, LAT_LNG_CRS_ID);
	}
	
	/**
	 * Convert given geometric shape to lat/long geo coordinates.
	 * @param geometry The geometric shape to convert
	 * @param sourceCrsId The source coordinate reference system id.
	 * @return A converted geometric shape
	 */
	public static Geometry convertToLatLng(Geometry geometry, String sourceCrsId) {
		return convert(geometry, sourceCrsId, LAT_LNG_CRS_ID);
	}

	/**
	 * Conver a given WKT string to a geometric shape.
	 * @param wkt The WKT string to convert
	 * @return A geometric shape
	 */
	public static Geometry fromWkt(String wkt) {
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader reader = new WKTReader( geometryFactory );

        Geometry geom = null;
        
        try {
        	geom = reader.read(wkt);
		} catch (ParseException e) {
			LogUtils.getLogger().warn("Error parsing WKT: " + wkt);
		}
        
        return geom;
	}
	
	/**
	 * Convert a geometric shape to a WKT string.
	 * @param geom The shape to convert
	 * @return A WKT string
	 */
	public static String toWkt(Geometry geom) {
		String wkt = null;
        WKTWriter writer = new WKTWriter();
        
		wkt = writer.write(geom);
        
		return wkt;		
	}
	
	/**
	 * Convert a geometric shape to a GML string.
	 * @param geom The shape to convert
	 * @return A GML string
	 */
	public static String toGML(Geometry geom) {
		String kml = null;
		
		GMLWriter writer = new GMLWriter();
		
		kml = writer.write(geom);
		
		return kml;
	}

	/**
	 * Convert a given collection of shapes to a GeometryCollection.
	 * 
	 * @param shapes The collection of shapes to convert
	 * @param factory A geometry factory to facilitate conversion
	 * @return A GeometryCollection
	 */
	public static GeometryCollection getShapeCollection(List<Geometry> shapes, GeometryFactory factory) {
		if (shapes.size() == 0) {
			return null;
		}
		
		Geometry[] geoms = new Geometry[shapes.size()];
		geoms = shapes.toArray(geoms);
		
		return new GeometryCollection(geoms, factory);
	}
	/**
	 * Convert a given geometric shape to GoogleMaps API JSON equivalent
	 * @param geom
	 * @return
	 */
	
	public static String toGmapJson(Geometry geom) {
		StringBuilder json = new StringBuilder(INIT_JSON_BUF_SIZE);
		
		addJsonPrefix(json);
		addJsonBody(geom, json);
		addJsonSuffix(json);
		
		return json.toString();
	}

	private static void addJsonBody(Geometry geom, StringBuilder json) {
		if (geom instanceof GeometryCollection) {
			GeometryCollection coll = (GeometryCollection) geom;
			int ct = coll.getNumGeometries();
			for (int i = 0; i < ct; i++) {
				if (i > 0) {
					json.append(','); // object separator
				}
				
				addJsonBody(coll.getGeometryN(i), json);
			}
		} else {
			if (geom instanceof Point) {
				json.append(GmapJsonUtils.toJsonPoint((Point) geom));
			}
			else if (geom instanceof LineString) {
				json.append(GmapJsonUtils.toJsonPolyline((LineString) geom));
			}
			else if (geom instanceof Polygon) {
				json.append(GmapJsonUtils.toJsonPolygon((Polygon) geom));
			}
		}
	}

	private static void addJsonPrefix(StringBuilder json) {
		json.append("{shapes : [");
	}
	
	private static void addJsonSuffix(StringBuilder json) {
		json.append("]}");
	}
}
