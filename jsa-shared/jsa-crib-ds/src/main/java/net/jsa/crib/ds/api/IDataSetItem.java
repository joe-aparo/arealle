package net.jsa.crib.ds.api;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

/**
 * Represents an instance of an item within a DataSet. An item maintains values for the item via an array of
 * object references. This array of values is managed and exposed in a manner consistent with the names and
 * order of the properties of the associated DataSet.
 * 
 * This interface extends the Map<String, Object> interface because it can be logically thought of as one.
 */
public interface IDataSetItem extends Map<String, Object> {

    /**
     * Get the owning DataSet for the item.
     * 
     * @return The DataSet
     */
    IDataSet getDataSet();

    /**
     * Get the value for a specified property index.
     * 
     * @param idx The ordinal index of the property
     * @return The value, or null
     * 
     */
    Object get(int idx);

    /**
     * Set the value for a specified property index.
     * 
     * @param idx The property index
     * @param val The value to set
     * 
     * @return Reference to this item
     */
    Map<String, Object> put(int idx, Object val);
    

    /**
     * Remove an item from the set by named key.
     * 
     * @param key The key of the item to remove
     * @return The removed object
     */
    Object remove(String key);
    
    /**
     * Remove an item from the set by ordinal index.
     * 
     * @param idx The ordinal index of the item to remove
     * @return The removed object
     */
    Object remove(int idx);
    
	/**
	 * Get the native value for a specified property name.
	 * 
     * @param name Property name
	 * @return A native value
	 */
	Object getNativeValue(String name);
	
	/**
	 * Get the native value for a specified property name.
	 * 
     * @param name Property name
	 * @return A native value
	 */
	Object getLogicalValue(String name, LogicalType type);
	
    /**
     * Get the named property value as a String.
     * 
     * @param name Property name
     * @return A String value
     */
	String getString(String name);
	
	/**
     * Get the named property value as a Long.
     * 
     * @param name Property name
	 * @return A Long value
	 */
	Long getInteger(String name);
	
	/**
     * Get the named property value as a BigDecimal.
	 * 
     * @param name Property name
	 * @return A BigDecimal value
	 */
	BigDecimal getDecimal(String name);
	
	/**
     * Get the named property value as a Calendar.
	 * 
     * @param name Property name
	 * @return A Calendar value
	 */
	Calendar getDateTime(String name);
	
	/**
     * Get the named property value as a Boolean.
	 * 
     * @param name Property name
	 * @return A Boolean value
	 */
	Boolean getBoolean(String name);

	/**
     * Get the indexed property value as a String.
     * 
	 * @param idx Property index
     * @return A String value
	 */
	String getString(int idx);

	/**
     * Get the indexed property value as a Long.
	 * 
	 * @param idx Property index
	 * @return A Long value
	 */
	Long getInteger(int idx);

	/**
     * Get the indexed property value as a BigDecimal.
	 * 
	 * @param idx Property index
     * @return A BigDecimal value
	 */
	BigDecimal getDecimal(int idx);

	/**
     * Get the indexed property value as a Calendar.
	 * 
	 * @param idx Property index
     * @return A Calendar value
	 */
	Calendar getDateTime(int idx);

	/**
     * Get the indexed property value as a Boolean.
	 * 
	 * @param idx Property index
     * @return A Boolean value
	 */
	Boolean getBoolean(int idx);
	
	/**
	 * Get the native of of a specified property index.
	 * 
	 * @param idx Property index
	 * @return A native value
	 */
	Object getNativeValue(int idx);

	Object getLogicalValue(int idx, LogicalType type);
	

}
