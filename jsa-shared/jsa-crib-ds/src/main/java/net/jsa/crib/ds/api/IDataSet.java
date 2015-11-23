package net.jsa.crib.ds.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A conceptual set of data items. All items share a common set of ordered attributes. Support is provided for
 * property metadata, CRUD semantics, procedural querying, and result processing.
 * 
 */
public interface IDataSet {

    /**
     * Default property name for unique id. 
     */
    public static final String DEFAULT_ID_PROPERTY = "ID";

    /**
     * Get the type registry associated with the dataset
     * 
     * @return A type registry
     */
    public ILogicalTypeRegistry getTypeRegistry();
    
    /**
     * Get the logical name of the data set. A dataset should always have a name and the name should be unique
     * within a logical context.
     * 
     * @return A string
     */
    String getName();

    /**
     * Get the names of the properties representing the logical keys for the DataSet. Keys are not required,
     * but should be used when known. This can help control automated logic in the implementation.
     * 
     * @return A string collection
     */
    Set<String> getKeys();

    /**
     * Get the names of the properties representing keys that reference other DataDets. Reference keys are not
     * required, but should be used when known. This can help control automated logic in the implementation.
     * 
     * @return A string collection
     */
    Set<String> getReferenceKeys();

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

    /**
     * Retrieve all items from the DataSet, processing results via the specified handler.
     * 
     * @param handler The handler that will process the results.
     */
    void retrieve(IDataSetResultHandler handler);

    /**
     * Retrieve specified items from the DataSet, returning results as a list
     * 
     * @param query The query to apply
     * @return A list of items
     */
    List<IDataSetItem> retrieve(DataSetQuery query);
    
    
    /**
     * Retrieve items from the DataSet according to the specified query.
     * 
     * @param query The query to use to retrieve data.
     * @param handler The handler that will process the results.
     */
    void retrieve(DataSetQuery query, IDataSetResultHandler handler);

    /**
     * Retrieve a single item from the DataSet. Only 1 or 0 items will be returned. The given key values will
     * be used to identify the item in question.
     * 
     * @param keys The values used retrieve the single item
     * @return A single item from the DataSet, or null if no result
     */
    IDataSetItem retrieve(Map<String, Object> keys);

    /**
     * Retrieve all items from the dataset as a list.
     * 
     * @return A list of items
     */
    List<IDataSetItem> retrieve();

    /**
     * From all items in the DataSet, retrieve the item at the specified ordinal index.
     * 
     * @param index The 1-based ordinal index of the item to retrieve
     * 
     * @return An item, or null if not available.
     */
    IDataSetItem retrieve(int index);

    /**
     * Create a single item in the DataSet. The given values are presumed to represent the intended set of
     * values for the newly created item.
     * 
     * @param values The values used used to create a new item
     * @return A single object representing the newly created item.
     */
    IDataSetItem create(Map<String, Object> values);

    /**
     * Update items in the DataSet that match a given query.
     * 
     * @param query Query used to select the records to update
     * @param values The values to set for the selected records
     */
    void update(DataSetQuery query, Map<String, Object> values);

    /**
     * Update a single item in the DataSet. The given values are presumed to contain a key value as well as
     * the properties to be updated.
     * 
     * @param values The values used to identify the item to update, and the properties used for the update.
     * 
     * @return A single object representing the updated item.
     */
    IDataSetItem update(Map<String, Object> values);

    /**
     * Delete a single item from the DataSet. The given values are presumed to contain enough information to
     * identify the item to delete.
     * 
     * @param keys The values to use to determine which item to delete.
     */
    void delete(Map<String, Object> keys);

    /**
     * Get the number of items in the DataSet according to the specified query.
     * 
     * @param query The query to apply to the DataSet
     * 
     * @return A count
     */
    int getItemCount(DataSetQuery query);

    /**
     * Get the count of all items in the data set.
     * 
     * @return A count
     */
    int getItemCount();

    /**
     * Get the name of the property representing the referring key to a parent entity.
     * 
     * @return A string
     */
    String getParentKey();
}
