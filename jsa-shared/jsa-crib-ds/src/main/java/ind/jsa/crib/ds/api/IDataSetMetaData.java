package ind.jsa.crib.ds.api;

import java.util.List;
import java.util.Map;

/**
 * Access a collection of properties that reflect the physical elements of 
 * items in a data set, and their natural order.
 * 
 * @author jsaparo
 *
 */
public interface IDataSetMetaData {
    /**
     * Get a property associated with the DataSet by name.
     *
     * @param propName The name of the property to get
     * @return A property or null if not available
     */
	IDataSetProperty getProperty(String propName);

    /**
     * Get a property associated with the DataSet by ordinal index.
     *
     * @param idx The ordinal index of the property to get
     * @return A property or null if not available
     */
	IDataSetProperty getProperty(int idx);

	/**
	 * Get a list of all properties.
	 * 
	 * @return A list of properties
	 */
	List<IDataSetProperty> getProperties();

	/**
	 * Get a map of all properties by name.
	 * 
	 * @return A collection of properties keyed by name
	 */
	Map<String, IDataSetProperty> getPropertyiesByName();

    /**
     * Get a property associated with the DataSet by ordinal index.
     *
     * @param idx The ordinal index of the property to get
     * @return A property or null if not available
     */
	int getPropertyIndex(String propName);

    /**
     * Get the name of the property for a given ordinal index.
     * 
     * @param idx The ordinal index of the property
     * @return A property name, or null if not available
     */
	String getPropertyName(int idx);
	
	/**
	 * 
	 * @return
	 */
	List<String> getIdentityPropertyNames();
	
	/**
	 * 
	 * @return
	 */
	List<String> getReferencePropertyNames();
	
	/**
	 * 
	 * @return
	 */
	List<String> getReadablePropertyNames();
	
	/**
	 * 
	 * @return
	 */
	List<String> getWritablePropertyNames();
	
	/**
	 * 
	 * @return
	 */
	List<String> getFilterablePropertyNames();
}