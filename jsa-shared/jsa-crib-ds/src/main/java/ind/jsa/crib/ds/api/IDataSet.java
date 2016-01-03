package ind.jsa.crib.ds.api;

import java.util.List;
import java.util.Map;

public interface IDataSet {
	public static final String DEFAULT_ID_PROPERTY = "id";

	/**
	 * Get the name of the entity that dataset represents.
	 * 
	 * @return A string
	 */
	String getEntity();
	
	/**
	 * Get the name of the domain to which the entity pertains.
	 * 
	 * @return A string
	 */
	String getDomain();
	
	/**
	 * Get the type registry associated with the data set.
	 * 
	 * @return A type registry instance
	 */
	ITypeManager getTypeManager() ;

	/**
	 * Get basic metadata object associated with the dataset.
	 * 
	 * @return A meta data object
	 */
	IDataSetMetaData getMetaData();
	
	/**
	 * Get the key generator associated with data set.
	 * 
	 * @return A key generator
	 */
	IKeyGenerator getKeyGenerator();
	
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
     * Retrieve all items using a specified handler.
     * 
     * @param handler The handler that will process the results.
     */
    void retrieve(IDataSetResultHandler handler);

    /**
     * Retrieve a single item from the DataSet. Only 1 or 0 items will be returned.
     * 
     * @param params Criteria for the item to fetch
     * @return A single item from the DataSet, or null if no result
     */
    IDataSetItem retrieve(Map<String, Object> params);

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
     * Instantiate a blank item consistent with the dataset. The item is not stored
     * and exists in memory only.
     * 
     * @return An item consistent with the dataset.
     */
    IDataSetItem blankItem();
    
    /**
     * Instantiate an item consistent with the dataset initialized with values. The 
     * item is not stored and exists in memory only.
     * 
     * @return An item with values, consistent with the dataset.
     */
    IDataSetItem exampleItem(Map<String, Object> values);

    /**
     * Create a single item in the DataSet. The given values are presumed to represent the intended set of
     * values for the newly created item.
     * 
     * @param values The values used used to create a new item
     * @return A map of values representing the newly created item.
     */
    IDataSetItem create(Map<String, Object> values);

    /**
     * Create a single item in the DataSet. The given values are presumed to represent the intended set of
     * values for the newly created item.
     * 
     * @param item An item representing the set of values to use for the newly created item.
     * @return The item to create
     */
    IDataSetItem create(IDataSetItem item);

    /**
     * Update items in the DataSet that match a given query.
     * 
     * @param query Query used to select the records to update
     * @param values A map of values to set for the selected records
     */
    void update(DataSetQuery query, Map<String, Object> values);

    /**
     * Update items in the DataSet that match a given query.
     * 
     * @param query Query used to select the records to update
     * @param item An item representing values to set for the selected records
     */
    void update(DataSetQuery query, IDataSetItem item);

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
     * Update a single item in the DataSet. The given values are presumed to contain a key value as well as
     * the properties to be updated.
     * 
     * @param item An item representing values to update in the dataset.
     * 
     * @return A single object representing the updated item.
     */
    IDataSetItem update(IDataSetItem item);

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
     * Get a list of all property names for the dataset, in proper order.
     * 
     * @return A list of data set properties
     */
    List<String> getOrderedPropertyNames();
    
    /**
     * Get a list of all sortable property names for the dataset, in proper order.
     * 
     * @return A list of data set properties
     */
    List<String> getSortablePropertyNames();
    
    /**
     * Get a list of all writable property names for the dataset, in proper order.
     * 
     * @return A list of data set properties
     */
    List<String> getWritablePropertyNames();
    
    /**
     * Get a list of all filterable property names for the dataset, in proper order.
     * 
     * @return A list of data set properties
     */
    List<String> getFilterablePropertyNames();
    
    /**
     * Get the identity property name for the dataset.
     * 
     * @return A list of data set properties
     */
    String getIdPropertyName();
    
    /**
     * Get a list of all reference property names for the dataset, in proper order.
     * 
     * @return A list of data set propertyNames
     */
    List<String> getRefIdPropertyNames();
    
    /**
     * Determine whether the specified property is a key value.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isIdProperty(String name);
    
    /**
     * Determine whether the specified property is a reference value.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isIdRefProperty(String name);
    
    /**
     * Determine whether the specified property is writable.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isWritableProperty(String name);
    
    /**
     * Determine whether the specified property is a filterable.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isFilterableProperty(String name);
    
    /**
     * Determine whether the specified property is sortable.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isSortableProperty(String name);
    
    /**
     * Get a collection of default parameter values for CRUD calls if not specified.
     * 
     * @return A map of values
     */
    Map<String, Object> getDefaultParameterValues();
    
    /**
     * Determine whether case-insenitive searches are allowable for the database .
     * 
     * @return An indicator
     */
    boolean isCaseInsensitiveSearch();
    
    /**
     * Determine whether the given property is atomic in nature.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isAtomicProperty(String name);
  

    /**
     * Determine whether the given property is a String type.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isStringProperty(String name);
  
    /**
     * Determine whether the given property represents an numeric value.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isNumericProperty(String name);
    
    
    /**
     * Determine whether the given property represents an numeric,
     * non-fractional value.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isIntegerProperty(String name);
    
    /**
     * Determine whether the given property is a numeric,
     * fractional value.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isDecimalProperty(String name);   
    
    /**
     * Determine whether the given property is a data/time type.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isDateTimeProperty(String name);  
    
    /**
     * Determine whether the given property is a boolean type.
     * 
     * @param name A property name
     * @return An indicator
     */
    boolean isBooleanProperty(String name);
}
