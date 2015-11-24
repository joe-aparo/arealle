package ind.jsa.crib.ds.api;

import java.util.List;
import java.util.Set;

import ind.jsa.crib.ds.api.IDataSetProperty;

/**
 * Defines methods for working with DataSet metadata.
 * 
 * @author jo26419
 *
 */
public interface IDataSetMetaData {
	/**
	 * Get the type manager associated with the data set.
	 * @return
	 */
	ITypeManager getTypeManager();
	
    /**
     * Get the logical name of the data set. A dataset should always have a name and the name should be unique
     * within a logical context.
     * 
     * @return A string
     */
    String getName();

    /**
     * Get the names of the properties representing the logical keys for the DataSet.
     * 
     * @return A string collection
     */
    Set<String> getKeys();

    /**
     * Get the complete list of properties associated with the DataSet.
     * 
     * @return A property collection
     */
    List<IDataSetProperty> getProperties();
    /**
     * Get the complete collection of property names associated with the DataSet.
     * 
     * @return A string collection
     */
    Set<String> getPropertyNames();

    /**
     * Get a property associated with the DataSet by name.
     *
     * @param name The name of the property to get
     * @return A property or null if not available
     */
    IDataSetProperty getProperty(String name);

    /**
     * Get a property associated with the DataSet by ordinal index.
     *
     * @param idx The ordinal index of the property to get
     * @return A property or null if not available
     */
    IDataSetProperty getProperty(int idx);

    /**
     * Get the ordinal index of a given property name.
     * 
     * @param name The name of the property
     * @return >=0 if available, -1 if not available
     */
    int getPropertyIndex(String name);

    /**
     * Get the name of the property for a given ordinal index.
     * 
     * @param idx The ordinal index of the property
     * @return A property name, or null if not available
     */
    String getPropertyName(int idx);

    /**
     * Get the collection of readable property names for the DataSet. These will be the same as writable
     * properties in the case of simple single-table dataset.
     * 
     * @return A string collection
     */
    Set<String> getReadablePropertyNames();

    /**
     * Determine whether a specified property is considered readable.
     * 
     * @param propName The property to check
     * @return An indicator
     */
    boolean isReadableProperty(String propName);

    /**
     * Get the collection of writable property names for the DataSet. These will be the same as readable
     * properties in the case of simple single-table dataset.
     * 
     * @return A string collection
     */
    Set<String> getWritablePropertyNames();

    /**
     * Determine whether a specified property is considered writable.
     * 
     * @param propName The property to check
     * @return An indicator
     */
    boolean isWritableProperty(String propName);

    /**
     * Get the collection of filterable property names for the DataSet. These will be the same as readable
     * properties if not explicitly specified.
     * 
     * @return A string collection
     */
    Set<String> getFilterablePropertyNames();

    /**
     * Determine whether a specified property is considered filterable.
     * 
     * @param propName The property to check
     * @return An indicator
     */
    boolean isFilterableProperty(String propName);
}
