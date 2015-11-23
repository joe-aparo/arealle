package net.jsa.arealle.task.util;

import java.util.Collection;

import org.springframework.util.CollectionUtils;

import net.jsa.arealle.task.geom.IShape;

public class WktUtil {

	public static String toWktCollection(Collection<IShape> shapes) {
		
		if (CollectionUtils.isEmpty(shapes)) {
			return null;
		}
		
		StringBuilder wkt = new StringBuilder(1000);
		
		// init collection string
		wkt.append("GEOMETRYCOLLECTION(");
		
		// Append individual shaped wkt strings to collection string
		boolean delim = false;
		for (IShape shape : shapes) {
			if (delim) {
				wkt.append(',');
			}
			delim = true;
			
			wkt.append(shape.toWkt());
		}
		
		// terminate collection string
		wkt.append(")");
		
		return wkt.toString();
	}
}
