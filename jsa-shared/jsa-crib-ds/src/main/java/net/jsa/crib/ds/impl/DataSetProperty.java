package net.jsa.crib.ds.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.slf4j.Logger;

import net.jsa.common.logging.LogUtils;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetProperty;
import net.jsa.crib.ds.api.ILogicalTypeConverter;
import net.jsa.crib.ds.api.ILogicalTypeRegistry;
import net.jsa.crib.ds.api.LogicalType;

/**
 * An item of data in a dataset. A collection of these represents the schema for the dataset.
 * DataSet properties are associated with a specified dataset and logical type.
 * 
 */
public class DataSetProperty extends Property implements IDataSetProperty {

    private IDataSet dataSet;
    private Logger log = LogUtils.getLogger();
    
    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.api.IAttribute#getType()
     */
    public LogicalType getType() {
    	ILogicalTypeRegistry registry = dataSet.getTypeRegistry();
    	
    	LogicalType typ = registry.getLogicalTypeForClass(
        		registry.makeClassKey(getClassType(), getVariant()));
    	
    	return typ;
    }

    /**
     * Public constructor - properties must be associated with a dataset.
     * 
     * @param dataSet The dataset associated with the property.
     */
    public DataSetProperty(IDataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Get the dataset for the property.
     * 
     * @return A DataSet
     */
    public IDataSet getDataSet() {
        return dataSet;
    }


	@Override
	public Object toNativeValue(Object givenValue) {
		
	   	if (givenValue == null) {
    		return null;
    	}

	   	ILogicalTypeRegistry registry = dataSet.getTypeRegistry();
	   	
	   	// Determine the logical type associated with the given value
	   	String cls = registry.makeClassKey(givenValue.getClass(), getVariant());	   	
    	LogicalType logicalType = registry.getLogicalTypeForClass(cls);
    	if (logicalType == null) {
    		log.warn(
    			"No logical type for class '" + cls + "'. Returning null.");
    		
    		return null;
    	}
    	
    	// Coerce the given value into a logical value
		Object logicalValue = toLogicalValue(givenValue, logicalType);	
    	
		// Get the converter for the native class type of this property with respect to the logical type
    	cls = registry.makeClassKey(getClassType(), getVariant());
    	ILogicalTypeConverter converter = registry.getConverter(cls, logicalType);
    	if (converter == null) {
    		log.warn(
       			"No registered converter for class '" + cls + "' and logical type '" + 
       			logicalType.toString() + "'. Returning null.");
        		
       		return null;
    	}
    	
    	// Convert the logical value to the native value of this property
    	return converter.toNativeValue(logicalValue);
	}
	
	@Override
	public Object toLogicalValue(Object nativeValue) {
		return toLogicalValue(nativeValue, getType());
	}
	
	@Override
	public Object toLogicalValue(Object givenValue, LogicalType logicalType) {
    	if (givenValue == null) {
    		return null;
    	}

	   	ILogicalTypeRegistry registry = dataSet.getTypeRegistry();
	   	String cls = registry.makeClassKey(givenValue.getClass(), getVariant());
    	ILogicalTypeConverter converter = registry.getConverter(cls, logicalType);
    	
    	if (converter == null) {
    		log.warn(
           		"No registered converter for class '" + cls + "' and logical type '" + 
           		logicalType.toString() + "'. Returning null.");
        		
       		return null;
    	}
    	
    	return converter.toLogicalValue(givenValue);
	}
	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toNativeCollection(java.util.Collection, java.lang.Class, com.jsa.crib.ds.utils.type.TypeContext)
	 */
	@Override
	public Collection<Object> toNativeCollection(Collection<Object> values) {
		if (values == null) {
			return null;
		}
		
		// convert values into new collection
		Collection<Object> coll = new ArrayList<Object>();
		for (Object val : values) {
			coll.add(toNativeValue(val));
		}
		
		return coll;
	}

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toLogicalCollection(java.util.Collection, com.jsa.crib.ds.utils.type.TypeContext)
	 */
	@Override
	public Collection<Object> toLogicalCollection(Collection<Object> values) {
		if (values == null) {
			return null;
		}
		
		// convert values into new collection
		Collection<Object> coll = new ArrayList<Object>();
		for (Object val : values) {
			coll.add(toLogicalValue(val));
		}
		
		return coll;
	}

    /**
     * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toStringValue(java.lang.Object)
     */
	@Override
	public String toStringValue(Object value) {
		return (String) toLogicalValue(value, LogicalType.STRING);
	}

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toStringCollection(java.util.Collection, com.jsa.crib.ds.utils.type.TypeContext)
	 */
	@Override
	public Collection<String> toStringCollection(Collection<Object> values) {
		if (values == null) {
			return null;
		}
		
		// convert values into new collection
		Collection<String> coll = new ArrayList<String>();
		for (Object val : values) {
			coll.add(toStringValue(val));
		}
		
		return coll;
	}

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toIntegerValue(java.lang.Object)
	 */
	@Override
	public Long toIntegerValue(Object value) {
		return (Long) toLogicalValue(value, LogicalType.INTEGER);
	}

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toIntegerCollection(java.util.Collection, com.jsa.crib.ds.utils.type.TypeContext)
	 */
	@Override
	public Collection<Long> toIntegerCollection(Collection<Object> values) {
		if (values == null) {
			return null;
		}
		
		// convert values into new collection
		Collection<Long> coll = new ArrayList<Long>();
		for (Object val : values) {
			coll.add(toIntegerValue(val));
		}
		
		return coll;
	}

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toDecimalValue(java.lang.Object)
	 */
	@Override
	public BigDecimal toDecimalValue(Object value) {
		return (BigDecimal) toLogicalValue(value, LogicalType.DECIMAL);
	}

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toDecimalCollection(java.util.Collection, com.jsa.crib.ds.utils.type.TypeContext)
	 */
	@Override
	public Collection<BigDecimal> toDecimalCollection(Collection<Object> values) {
		if (values == null) {
			return null;
		}
		
		// convert values into new collection
		Collection<BigDecimal> coll = new ArrayList<BigDecimal>();
		for (Object val : values) {
			coll.add(toDecimalValue(val));
		}
		
		return coll;
	}

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toDateTimeValue(java.lang.Object)
	 */
	@Override
	public Calendar toDateTimeValue(Object value) {
		return (Calendar) toLogicalValue(value, LogicalType.DATETIME);
	}

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toDateTimeCollection(java.util.Collection, com.jsa.crib.ds.utils.type.TypeContext)
	 */
	@Override
	public Collection<Calendar> toDateTimeCollection(Collection<Object> values) {
		if (values == null) {
			return null;
		}
		
		// convert values into new collection
		Collection<Calendar> coll = new ArrayList<Calendar>();
		for (Object val : values) {
			coll.add(toDateTimeValue(val));
		}
		
		return coll;
	}

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toBooleanValue(java.lang.Object)
	 */
	@Override
	public Boolean toBooleanValue(Object value) {
		return (Boolean) toLogicalValue(value, LogicalType.BOOLEAN);
	}

	/**
	 * @see net.jsa.crib.ds.api.ILogicalTypeRegistry#toBooleanCollection(java.util.Collection, com.jsa.crib.ds.utils.type.TypeContext)
	 */
	@Override
	public Collection<Boolean> toBooleanCollection(Collection<Object> values) {
		if (values == null) {
			return null;
		}
		
		// convert values into new collection
		Collection<Boolean> coll = new ArrayList<Boolean>();
		for (Object val : values) {
			coll.add(toBooleanValue(val));
		}
		
		return coll;
	}
}
