package net.jsa.crib.ds.impl;

import ind.jsa.crib.ds.internal.type.convert.std.PassThru;
import ind.jsa.crib.ds.internal.type.convert.std.ToBgdUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToBgiUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToBoolUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToByteUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToCalUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToChrUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToDblUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToDtUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToFltUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToIntUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToLngUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToShrtUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToStrUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToTsUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.Date;

import net.jsa.crib.ds.api.ILogicalTypeConverter;
import net.jsa.crib.ds.api.ILogicalTypeRegistry;
import net.jsa.crib.ds.api.ILogicalTypeRegistryInitializer;
import net.jsa.crib.ds.api.LogicalType;


/**
 * Default logical type registry initializer. This registers common associations
 * between logical types and native types.
 * 
 * @author jsaparo
 *
 */
public class DefaultTypeRegistryInitializer implements ILogicalTypeRegistryInitializer {

	@Override
	public void initialize(ILogicalTypeRegistry registry) {
		
    	// Map common native classes to their default primary logical type
		registry.setLogicalTypeForClass(registry.makeClassKey(String.class, null), LogicalType.STRING);
    	registry.setLogicalTypeForClass(registry.makeClassKey(Character.class, null), LogicalType.STRING);
    	registry.setLogicalTypeForClass(registry.makeClassKey(Byte.class, null), LogicalType.INTEGER);
    	registry.setLogicalTypeForClass(registry.makeClassKey(Short.class, null), LogicalType.INTEGER);
    	registry.setLogicalTypeForClass(registry.makeClassKey(Integer.class, null), LogicalType.INTEGER);
    	registry.setLogicalTypeForClass(registry.makeClassKey(Long.class, null), LogicalType.INTEGER);
    	registry.setLogicalTypeForClass(registry.makeClassKey(BigInteger.class, null), LogicalType.INTEGER);
    	registry.setLogicalTypeForClass(registry.makeClassKey(Float.class, null), LogicalType.DECIMAL);
    	registry.setLogicalTypeForClass(registry.makeClassKey(Double.class, null), LogicalType.DECIMAL);
    	registry.setLogicalTypeForClass(registry.makeClassKey(BigDecimal.class, null), LogicalType.DECIMAL);
    	registry.setLogicalTypeForClass(registry.makeClassKey(GregorianCalendar.class, null), LogicalType.DATETIME);
    	registry.setLogicalTypeForClass(registry.makeClassKey(Date.class, null), LogicalType.DATETIME);
    	registry.setLogicalTypeForClass(registry.makeClassKey(Timestamp.class, null), LogicalType.DATETIME);
    	registry.setLogicalTypeForClass(registry.makeClassKey(Boolean.class, null), LogicalType.BOOLEAN);
    	
    	// Register common conversions between native types and logical string values
    	registry.registerConverter(registry.makeClassKey(String.class, null), LogicalType.STRING, PassThru.instance());
    	registry.registerConverter(registry.makeClassKey(Byte.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToByteUtils.str2Byte((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.byte2Str((Byte) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Character.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToChrUtils.str2Chr((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.chr2Str((Character) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Short.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToShrtUtils.str2Shrt((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.shrt2Str((Short) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Integer.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToIntUtils.str2Int((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.int2Str((Integer) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Long.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToLngUtils.str2Lng((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.lng2Str((Long) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(BigInteger.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToBgiUtils.str2Bgi((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.bgi2Str((BigInteger) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Float.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToFltUtils.str2Flt((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.flt2Str((Float) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Double.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToDblUtils.str2Dbl((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.dbl2Str((Double) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(BigDecimal.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToBgdUtils.str2Bgd((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.bgd2Str((BigDecimal) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(GregorianCalendar.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToCalUtils.str2Cal((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.cal2Str((GregorianCalendar) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Date.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToDtUtils.str2Dt((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.dt2Str((Date) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Timestamp.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToTsUtils.str2Ts((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.ts2Str((Timestamp) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Boolean.class, null), LogicalType.STRING, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToBoolUtils.str2Bool((String) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToStrUtils.bool2Str((Boolean) nativeValue);
            	}
    		});
    	
    	// Register common conversions between native types and logical integer values
    	registry.registerConverter(registry.makeClassKey(Long.class, null), LogicalType.INTEGER, PassThru.instance());
    	registry.registerConverter(registry.makeClassKey(String.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		if (logicalValue instanceof Long) {
            			return ToStrUtils.lng2Str((Long) logicalValue);
            		} else if (logicalValue instanceof Long) {
                		return ToStrUtils.lng2Str((Long) logicalValue);
            		} else {
            			return ToStrUtils.lng2Str((Long) logicalValue);
            		}
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.str2Lng((String) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Byte.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToByteUtils.lng2Byte((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.byte2Lng((Byte) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Character.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToChrUtils.lng2Chr((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.chr2Lng((Character) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Short.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToShrtUtils.lng2Shrt((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.shrt2Lng((Short) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Integer.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToIntUtils.lng2Int((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.int2Lng((Integer) nativeValue);
            	}
    		});  	
    	registry.registerConverter(registry.makeClassKey(BigInteger.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToBgiUtils.lng2Bgi((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.bgi2Lng((BigInteger) nativeValue);
            	}
    		});  	
    	registry.registerConverter(registry.makeClassKey(Float.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToFltUtils.lng2Flt((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.flt2Lng((Float) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Double.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToDblUtils.lng2Dbl((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.dbl2Lng((Double) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(BigDecimal.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToBgdUtils.lng2Bgd((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.bgd2Lng((BigDecimal) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(GregorianCalendar.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToCalUtils.lng2Cal((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.cal2Lng((GregorianCalendar) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Date.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToDtUtils.lng2Dt((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.dt2Lng((Date) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Timestamp.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToTsUtils.lng2Ts((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.ts2Lng((Timestamp) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Boolean.class, null), LogicalType.INTEGER, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToBoolUtils.lng2Bool((Long) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToLngUtils.bool2Lng((Boolean) nativeValue);
            	}
    		});
    	
    	// Register common conversions between native types and logical decimal values
    	registry.registerConverter(registry.makeClassKey(BigDecimal.class, null), LogicalType.DECIMAL, PassThru.instance());
    	registry.registerConverter(registry.makeClassKey(String.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToStrUtils.bgd2Str((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.str2Bgd((String) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Byte.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToByteUtils.bgd2Byte((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.byte2Bgd((Byte) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Character.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToChrUtils.bgd2Chr((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.chr2Bgd((Character) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Short.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToShrtUtils.bgd2Shrt((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.shrt2Bgd((Short) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Integer.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToIntUtils.bgd2Int((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.int2Bgd((Integer) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Long.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToLngUtils.bgd2Lng((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.lng2Bgd((Long) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(BigInteger.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToBgiUtils.bgd2Bgi((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.bgi2Bgd((BigInteger) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Float.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToFltUtils.bgd2Flt((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.flt2Bgd((Float) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Double.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToDblUtils.bgd2Dbl((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.dbl2Bgd((Double) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(GregorianCalendar.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToCalUtils.bgd2Cal((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.cal2Bgd((GregorianCalendar) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Date.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToDtUtils.bgd2Dt((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.dt2Bgd((Date) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Timestamp.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToTsUtils.bgd2Ts((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.ts2Bgd((Timestamp) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Boolean.class, null), LogicalType.DECIMAL, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToBoolUtils.bgd2Bool((BigDecimal) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBgdUtils.bool2Bgd((Boolean) nativeValue);
            	}
    		});
    	
    	// Register common conversions between native types and logical date/time values
    	registry.registerConverter(registry.makeClassKey(GregorianCalendar.class, null), LogicalType.DATETIME, PassThru.instance());
    	registry.registerConverter(registry.makeClassKey(String.class, null), LogicalType.DATETIME, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToStrUtils.cal2Str((GregorianCalendar) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToCalUtils.str2Cal((String) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Long.class, null), LogicalType.DATETIME, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToLngUtils.cal2Lng((GregorianCalendar) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToCalUtils.lng2Cal((Long) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(BigDecimal.class, null), LogicalType.DATETIME, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToBgdUtils.cal2Bgd((GregorianCalendar) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToCalUtils.bgd2Cal((BigDecimal) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Date.class, null), LogicalType.DATETIME, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToDtUtils.cal2Dt((GregorianCalendar) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToCalUtils.dt2Cal((Date) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Timestamp.class, null), LogicalType.DATETIME, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToTsUtils.cal2Ts((GregorianCalendar) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToCalUtils.ts2Cal((Timestamp) nativeValue);
            	}
    		});
    	
    	// Register common conversions between native types and logical boolean values
    	registry.registerConverter(registry.makeClassKey(Boolean.class, null), LogicalType.BOOLEAN, PassThru.instance());
    	registry.registerConverter(registry.makeClassKey(String.class, null), LogicalType.BOOLEAN, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToStrUtils.bool2Str((Boolean) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBoolUtils.str2Bool((String) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Byte.class, null), LogicalType.BOOLEAN, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToByteUtils.bool2Byte((Boolean) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBoolUtils.byte2Bool((Byte) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Character.class, null), LogicalType.BOOLEAN, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToChrUtils.bool2Chr((Boolean) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBoolUtils.chr2Bool((Character) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Short.class, null), LogicalType.BOOLEAN, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToShrtUtils.bool2Shrt((Boolean) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBoolUtils.shrt2Bool((Short) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Integer.class, null), LogicalType.BOOLEAN, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToIntUtils.bool2Int((Boolean) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBoolUtils.int2Bool((Integer) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Long.class, null), LogicalType.BOOLEAN, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToLngUtils.bool2Lng((Boolean) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBoolUtils.lng2Bool((Long) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(BigInteger.class, null), LogicalType.BOOLEAN, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToBgiUtils.bool2Bgi((Boolean) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBoolUtils.bgi2Bool((BigInteger) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Float.class, null), LogicalType.BOOLEAN, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToFltUtils.bool2Flt((Boolean) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBoolUtils.flt2Bool((Float) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Double.class, null), LogicalType.BOOLEAN, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToDblUtils.bool2Dbl((Boolean) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBoolUtils.dbl2Bool((Double) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(BigDecimal.class, null), LogicalType.BOOLEAN, 
    		new ILogicalTypeConverter() {
            	public Object toNativeValue(Object logicalValue) {
            		return ToBgdUtils.bool2Bgd((Boolean) logicalValue);
            	}
            	public Object toLogicalValue(Object nativeValue) {
            		return ToBoolUtils.bgd2Bool((BigDecimal) nativeValue);
            	}
    		});
    	registry.registerConverter(registry.makeClassKey(Long.class, null), LogicalType.BOOLEAN, 
        		new ILogicalTypeConverter() {
                	public Object toNativeValue(Object logicalValue) {
                		return ToLngUtils.bool2Lng((Boolean) logicalValue);
                	}
                	public Object toLogicalValue(Object nativeValue) {
                		return ToBoolUtils.lng2Bool((Long) nativeValue);
                	}
        		});
	}

}
