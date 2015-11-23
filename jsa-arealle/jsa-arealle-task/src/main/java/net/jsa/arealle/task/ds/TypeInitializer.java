package net.jsa.arealle.task.ds;

import java.sql.Date;
import java.util.Calendar;

import org.slf4j.Logger;

import net.jsa.common.logging.LogUtils;
import net.jsa.crib.ds.api.ILogicalTypeConverter;
import net.jsa.crib.ds.api.ILogicalTypeRegistry;
import net.jsa.crib.ds.api.ILogicalTypeRegistryInitializer;
import net.jsa.crib.ds.api.LogicalType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Class for integrating MySQL Geometric types
 *
 * 
 * @author jsaparo
 *
 */
public class TypeInitializer implements ILogicalTypeRegistryInitializer {

	private Logger log = LogUtils.getLogger();

	@Override
	public void initialize(ILogicalTypeRegistry registry) {

		String cls = registry.makeClassKey(byte[].class, "GEOMETRY");
		
		registry.setLogicalTypeForClass(cls, LogicalType.STRING);

		registry.registerConverter(cls, LogicalType.STRING,
				new ILogicalTypeConverter() {
					public Object toNativeValue(Object logicalValue) {
						return str2Geom((String) logicalValue);
					}

					public Object toLogicalValue(Object nativeValue) {
						return geom2Str((byte[]) nativeValue);
					}
				});
		
		cls = registry.makeClassKey(java.sql.Date.class, "DATE");
		
		registry.setLogicalTypeForClass(cls, LogicalType.DATETIME);

		registry.registerConverter(cls, LogicalType.DATETIME,
				new ILogicalTypeConverter() {
					public Object toNativeValue(Object logicalValue) {
						return cal2SqlDt((Calendar) logicalValue);
					}

					public Object toLogicalValue(Object nativeValue) {
						return sqlDt2Cal((java.sql.Date) nativeValue);
					}
				});

	}

	/**
	 * Convert string value in WKT format to well-known byte format.
	 * 
	 * @param val WKT string
	 * 
	 * @return An array of bytes in WKB format
	 */
	public byte[] str2Geom(String val) {
		byte[] retVal = null;
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader reader = new WKTReader( geometryFactory );

        try {
			Geometry shape = reader.read(val);
			WKBWriter writer = new WKBWriter();
			retVal = writer.write(shape);
		} catch (ParseException e) {
			log.warn("Error parsing WKT: " + val);
		}
        
		return retVal;
	}
	
	/**
	 * Convert byte array in WKB format to well-known text format.
	 * 
	 * @param val WKB byte array
	 * 
	 * @return An string in WKT format
	 */
	public String geom2Str(byte[] val) {
		String retVal = null;
        GeometryFactory geometryFactory = new GeometryFactory();
        WKBReader reader = new WKBReader( geometryFactory );
        
        try {
//        	val[1] = (byte) 1;
			Geometry shape = reader.read(val);
			WKTWriter writer = new WKTWriter();
			retVal = writer.write(shape);
		} catch (ParseException e) {
			log.warn("Error parsing shape: " + e.getMessage());
		}
        
		return retVal;
	}
	
	protected Object sqlDt2Cal(Date logicalValue) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date(logicalValue.getTime()));
		
		return cal;
	}

	protected Object cal2SqlDt(Calendar nativeValue) {
		return new java.sql.Date(nativeValue.getTimeInMillis());
	}
}
