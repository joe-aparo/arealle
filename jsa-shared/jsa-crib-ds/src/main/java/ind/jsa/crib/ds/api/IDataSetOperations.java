package ind.jsa.crib.ds.api;

import java.util.List;
import java.util.Map;

import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetResultHandler;

/**
 * Defines methods for working with data defined by a DataSet.
 * 
 * @author jo26419
 *
 */
public interface IDataSetOperations {
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

}
