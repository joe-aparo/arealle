package ind.jsa.crib.ds.api;

import java.util.Collection;
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
	 * Get the names of properties representing the
	 * item identifier (typically only one).
	 * 
	 * @return A collection of property names, or null if not specified.
	 */
	Collection<String> getIdPropertyNames();
	
	/**
	 * Get the names of properties that represent
	 * identifiers to other entities.
	 * 
	 * @return A list of property names, or null if not specified.
	 */
	Collection<String> getIdRefPropertyNames();
	
	/**
	 * Get the names of properties that may be written back
	 * to the source if modified. If not specified, all
	 * properties should be assumed to be able to be updated.
	 * 
	 * @return A list of property names, or null if not specified.
	 */
	Collection<String> getWritablePropertyNames();
	
	/**
	 * Get the names of properties that may be used to
	 * filter items. If not specified, all properties 
	 * should be assumed to be able to be used to filter items.
	 * 
	 * @return A list of property names, or null if not specified.

	 */
	Collection<String> getFilterablePropertyNames();
	
	/**
	 * Get the names of properties that may be used to
	 * sort items. If not specified, all properties 
	 * should be assumed to be able to be used to sort items.
	 * 
	 * @return A list of property names, or null if not specified.
	 */
	Collection<String> getSortablePropertyNames();
}