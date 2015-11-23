package net.jsa.crib.ds.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import net.jsa.common.logging.LogUtils;
import net.jsa.crib.ds.api.ILogicalTypeConverter;
import net.jsa.crib.ds.api.ILogicalTypeRegistry;
import net.jsa.crib.ds.api.ILogicalTypeRegistryInitializer;
import net.jsa.crib.ds.api.LogicalType;

/**
 * Standard implementation of a logical type registry.
 * 
 * @author jsaparo
 *
 */
public final class LogicalTypeRegistry implements ILogicalTypeRegistry {
    public static final int DEFAULT_SCALE = 3;
    private static final char VARIANT_DELIM = '/';
    
	private final Logger log = LogUtils.getLogger();
	
    private Map<String, ILogicalTypeConverter> converterMap = new HashMap<String, ILogicalTypeConverter>(100);
    private Map<String, LogicalType> classToLogicalTypeMap = new HashMap<String, LogicalType>(100);
    private Map<LogicalType, Set<String>> logicalTypeToClassesMap = new HashMap<LogicalType, Set<String>>(100);
    private List<ILogicalTypeRegistryInitializer> initializers = new ArrayList<ILogicalTypeRegistryInitializer>();

	/**
	 * Constructor add default registry initialization.
	 */
	public LogicalTypeRegistry() {
    	initializers.add(new DefaultTypeRegistryInitializer());
    }
    
	/**
	 * Constructor accepts custom registry initializers, in addition
	 * to the default initializer.
	 * 
	 * @param inits Initializers
	 */
    public LogicalTypeRegistry(List<ILogicalTypeRegistryInitializer> inits) {
    	this();
    	for (ILogicalTypeRegistryInitializer init : inits) {
    		this.initializers.add(init);
    	}
    }

	/**
     * Initialize the registry.
     */
    public void initialize() {
    	for (ILogicalTypeRegistryInitializer initializer : initializers) {
    		initializer.initialize(this);
    	}
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#setLogicalTypeForClass(java.lang.Class, net.jsa.crib.ds.api.LogicalType)
     */
    public void setLogicalTypeForClass(String cls, LogicalType type) {
    	log.debug("Setting logical type " + type + " for class " + cls);
    	
    	classToLogicalTypeMap.put(cls, type);
    	Set<String> classes = logicalTypeToClassesMap.get(type);
    	if (classes == null) {
    		classes = new LinkedHashSet<String>(10);
    		logicalTypeToClassesMap.put(type, classes);
    	}
    	classes.add(cls);
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#getLogicalTypeForClass(java.lang.Class)
     */
    public LogicalType getLogicalTypeForClass(String cls) {
    	LogicalType type = classToLogicalTypeMap.get(cls);

    	// If not found check for variant in name
    	if (type == null) {
    		int pos = cls.indexOf(VARIANT_DELIM);
    		if (pos != -1) {
    			// Strip variant and attempt to resolve by class name alone
    			type = classToLogicalTypeMap.get(cls.substring(0, pos));
    		}
    	}
    	
    	return type;
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#getMappedLogicalTypes()
     */
    public Set<LogicalType> getMappedLogicalTypes() {
    	return  logicalTypeToClassesMap.keySet();
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#getMappedNativeTypes()
     */
    public Set<String> getMappedNativeTypes() {
    	return  classToLogicalTypeMap.keySet();
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#getClassesForLogicalType(net.jsa.crib.ds.api.LogicalType)
     */
    public Collection<String> getClassesForLogicalType(LogicalType type) {
    	return logicalTypeToClassesMap.get(type);
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#registerConverter(java.lang.Class, net.jsa.crib.ds.api.LogicalType, net.jsa.crib.ds.api.ILogicalTypeConverter)
     */
    public void registerConverter (String cls, LogicalType type, ILogicalTypeConverter converter) {
    	log.debug("Registering converter for type " + type + " and class " + cls + ": " + converter.getClass().getName());
    	 
    	converterMap.put(makeConverterMapKey(cls, type), converter);
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#getConverter(java.lang.Class, net.jsa.crib.ds.api.LogicalType)
     */
    public ILogicalTypeConverter getConverter(String cls, LogicalType type) {
    	
    	ILogicalTypeConverter converter = converterMap.get(makeConverterMapKey(cls, type));
    	
    	// If not found check for variant in name
    	if (converter == null) {
    		int pos = cls.indexOf(VARIANT_DELIM);
    		if (pos != -1) {
    			// Strip variant and attempt to resolve by class name alone
    			converter = converterMap.get(makeConverterMapKey(cls.substring(0, pos), type));
    		}
    	}
    	
    	return converter;
    }

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#isNumericValue(java.lang.Object)
	 */
	@Override
    public boolean isNumericValue(Object val, String variant) {
    	LogicalType typ = getLogicalTypeForClass(makeClassKey(val.getClass(), variant));
    	return typ != null & (typ == LogicalType.INTEGER || typ == LogicalType.DECIMAL);
    }

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#hasScalarValue(java.lang.Object)
	 */
    @Override
    public boolean hasScalarValue(Object val, String variant) {
    	if (val == null) {
    		return false;
    	}
    	
    	return isScalarType(makeClassKey(val.getClass(), variant));
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#isScalarType(java.lang.Class)
     */
    @Override
    public boolean isScalarType(String cls) {
    	LogicalType typ = getLogicalTypeForClass(cls);
    	return typ != null & (
    		typ == LogicalType.INTEGER || typ == LogicalType.DECIMAL || 
    		typ == LogicalType.DATETIME || typ == LogicalType.BOOLEAN);
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#getScalarValue(java.lang.Object)
     */
    @Override
    public long getScalarValue(Object val, String variant) {
        long scalar = 0;
        String cls = makeClassKey(val.getClass(), variant);
        
        if (val != null) {
	    	LogicalType typ = getLogicalTypeForClass(cls);
	        ILogicalTypeConverter converter = this.getConverter(cls, typ);
	        Object logicalValue = converter.toLogicalValue(val);
	        
	    	if (typ == LogicalType.DATETIME) {	    		
	    		Calendar cal = (Calendar) logicalValue;
	    		scalar = cal.getTimeInMillis();
	    	}
	    	else if (typ == LogicalType.INTEGER) {
	        	BigInteger i = (BigInteger) logicalValue;
	            scalar = i.intValue();
	        } 
	    	else if (typ == LogicalType.DECIMAL) {
	        	BigDecimal d =  (BigDecimal) logicalValue;
	            scalar = d.longValue();
	        }
	    	else if (typ == LogicalType.BOOLEAN) {
	        	Boolean b =  (Boolean) logicalValue;
	            scalar = b ? 1 : 0;
	        }
        }
    	
        return scalar;
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#isNumericString(java.lang.String)
     */
    @Override
    public boolean isNumericString(String str) {
        return StringUtils.isNotEmpty(str) && str.matches("(-?\\d*)?(\\.\\d+)?");
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#compareValues(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compareValues(Object val1, Object val2, String variant) {
        int cmp = 0;

        if (val2 == null || val2 == null) {
        	cmp = cmpNull(val1, val2); // punt
        }
        else if (hasScalarValue(val1, variant) && hasScalarValue(val2, variant)) {
        	long sc1 = getScalarValue(val1, variant);
        	long sc2 = getScalarValue(val2, variant);
        	return sc1 < sc2 ? -1 : (sc1 > sc2 ? 1 : 0);
        } else { // treat as strings
        	String str1 = val1.toString();
        	String str2 = val2.toString();
        	
        	cmp = str1.compareTo(str2);
        }
        
        return cmp;
    }

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#makeClassKey(java.lang.Class, java.lang.String)
     */
	@Override
	public String makeClassKey(Class<?> type, String variant) {
		return !StringUtils.isEmpty(variant) ? type.getName() + VARIANT_DELIM + variant : type.getName();
	}
    
    private static String makeConverterMapKey(String cls, LogicalType type) {
    	return cls + ":" + type.toString();
    }
    
    private int cmpNull(Object o1, Object o2) {
    	int cmp = 0;
    	
        if (o1 == null && o2 == null) {
            cmp = 0;
        } else if (o1 == null && o2 != null) {
            cmp = -1;
        } else if (o1 != null && o2 == null) {
            cmp = 1;
        }
        
        return cmp;
    }
}
