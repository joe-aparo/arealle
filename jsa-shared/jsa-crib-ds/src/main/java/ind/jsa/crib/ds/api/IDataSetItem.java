package ind.jsa.crib.ds.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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
	 * Get metadata associated with the item.
	 * 
	 * @return Item metadata
	 */
	IDataSetMetaData getMetaData();
	
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
     * Get the named property value as a String.
     * 
     * @param name Property name
     * @return A String value, or null if unavailable
     */
	String getString(String name);
	
	/**
     * Get the named property value as a Byte.
     * 
     * @param name Property name
	 * @return A Byte value, or null if unavailable
	 */
	Byte getByte(String name);
	
	/**
     * Get the named property value as a Character.
     * 
     * @param name Property name
	 * @return A Character value, or null if unavailable
	 */
	Character getCharacter(String name);
	
	/**
     * Get the named property value as a Short.
     * 
     * @param name Property name
	 * @return A Short value, or null if unavailable
	 */
	Short getShort(String name);
	
	/**
     * Get the named property value as an Integer.
     * 
     * @param name Property name
	 * @return An integer value, or null if unavailable
	 */
	Integer getInteger(String name);
	
	/**
     * Get the named property value as a Long.
     * 
     * @param name Property name
	 * @return A Long value, or null if unavailable
	 */
	Long getLong(String name);
	
	/**
     * Get the named property value as a BigInteger.
     * 
     * @param name Property name
	 * @return A BigInteger value, or null if unavailable
	 */
	BigInteger getBigInteger(String name);
	
	/**
     * Get the named property value as a Float.
	 * 
     * @param name Property name
	 * @return A Float value, or null if unavailable
	 */
	Float getFloat(String name);
	
	/**
     * Get the named property value as a Double.
	 * 
     * @param name Property name
	 * @return A Double value, or null if unavailable
	 */
	Double getDouble(String name);
	
	/**
     * Get the named property value as a BigDecimal.
	 * 
     * @param name Property name
	 * @return A BigDecimal value, or null if unavailable
	 */
	BigDecimal getBigDecimal(String name);
	
	/**
     * Get the named property value as a Calendar.
	 * 
     * @param name Property name
	 * @return A Calendar value, or null if unavailable
	 */
	Calendar getCalendar(String name);
	
	/**
     * Get the named property value as a Date.
	 * 
     * @param name Property name
	 * @return A Date value, or null if unavailable
	 */
	Date getDate(String name);
	
	/**
     * Get the named property value as a Timestamp.
	 * 
     * @param name Property name
	 * @return A Timestamp value, or null if unavailable
	 */
	Timestamp getTimestamp(String name);
	
	/**
     * Get the named property value as a Boolean.
	 * 
     * @param name Property name
	 * @return A Boolean value, or null if unavailable
	 */
	Boolean getBoolean(String name);
	
	/**
     * Get the indexed property value as a String.
     * 
	 * @param idx Property index
     * @return A String value, or null if unavailable
	 */
	String getString(int idx);

	/**
     * Get the indexed property value as a Byte.
	 * 
	 * @param idx Property index
	 * @return A Byte value, or null if unavailable
	 */
	Byte getByte(int idx);

	/**
     * Get the indexed property value as a Character.
	 * 
	 * @param idx Property index
	 * @return A Byte value, or null if unavailable
	 */
	Character getCharacter(int idx);

	/**
     * Get the indexed property value as a Short.
	 * 
	 * @param idx Property index
	 * @return A Short value, or null if unavailable
	 */
	Short getShort(int idx);

	/**
     * Get the indexed property value as an Integer.
	 * 
	 * @param idx Property index
	 * @return An Integer value, or null if unavailable
	 */
	Integer getInteger(int idx);

	/**
     * Get the indexed property value as a Long.
	 * 
	 * @param idx Property index
	 * @return A Long value, or null if unavailable
	 */
	Long getLong(int idx);

	/**
     * Get the indexed property value as a BigInteger.
	 * 
	 * @param idx Property index
	 * @return A BigInteger value, or null if unavailable
	 */
	BigInteger getBigInteger(int idx);

	/**
     * Get the indexed property value as a Float.
	 * 
	 * @param idx Property index
     * @return A Float value, or null if unavailable
	 */
	Float getFloat(int idx);

	/**
     * Get the indexed property value as a Double.
	 * 
	 * @param idx Property index
     * @return A Double value, or null if unavailable
	 */
	Double getDouble(int idx);

	/**
     * Get the indexed property value as a BigDecimal.
	 * 
	 * @param idx Property index
     * @return A BigDecimal value, or null if unavailable
	 */
	BigDecimal getBigDecimal(int idx);

	/**
     * Get the indexed property value as a Date.
	 * 
	 * @param idx Property index
     * @return A Date value, or null if unavailable
	 */
	Date getDate(int idx);

	/**
     * Get the indexed property value as a Calendar.
	 * 
	 * @param idx Property index
     * @return A Calendar value, or null if unavailable
	 */
	Calendar getCalendar(int idx);
	
	/**
     * Get the indexed property value as a Timestamp.
	 * 
	 * @param idx Property index
     * @return A Timestamp value, or null if unavailable
	 */
	Timestamp getTimestamp(int idx);

	/**
     * Get the indexed property value as a Boolean.
	 * 
	 * @param idx Property index
     * @return A Boolean value, or null if unavailable
	 */
	Boolean getBoolean(int idx);
}
