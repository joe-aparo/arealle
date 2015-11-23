package net.jsa.crib.ds.api;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;

public interface IDataSetProperty extends IProperty {

	/**
	 * Get the logical type of the property.
	 * 
	 * @return A logical type
	 */
    LogicalType getType();

    /**
     * Get the dataset for the property.
     * 
     * @return A DataSet
     */
    public IDataSet getDataSet();

	/**
     * Convert a given logical object value to a native value for the property.
	 * 
     * @param  A logical object value
     *
     * @return A corresponding native value
	 */
	Object toNativeValue(Object logicalValue);
	
	/**
     * Convert a given native object value to a logical value for the property.
	 * 
     * @param  A native object value
     *
     * @return A corresponding logical value
	 */
	Object toLogicalValue(Object nativeValue);
	
	Object toLogicalValue(Object nativeValue, LogicalType logicalType);
	Collection<Object> toNativeCollection(Collection<Object> values);
	Collection<Object> toLogicalCollection(Collection<Object> values); 
	String toStringValue(Object value);
	Collection<String> toStringCollection(Collection<Object> values);
	Long toIntegerValue(Object value);
	Collection<Long> toIntegerCollection(Collection<Object> values);
	BigDecimal toDecimalValue(Object value);
	Collection<BigDecimal> toDecimalCollection(Collection<Object> values);
	Calendar toDateTimeValue(Object value);
	Collection<Calendar> toDateTimeCollection(Collection<Object> values);
	Boolean toBooleanValue(Object value);
	Collection<Boolean> toBooleanCollection(Collection<Object> values);

}
