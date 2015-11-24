package ind.jsa.crib.ds.api;

/**
 * Callback handler for items obtained from a DataSet.
 * 
 */
public interface IDataSetResultHandler {

    /**
     * Called before items rows are retrieved.
     */
    void processStart();

    /**
     * Called as each item is retrieved.
     * 
     * @param item The item retrieved
     */
    void processRow(IDataSetItem item);

    /**
     * Called after all items have been retrieved.
     */
    void processEnd();

    /**
     * Get the number of items processed during the retrieve.
     * @return A count
     */
    int getProcessCount();
}
