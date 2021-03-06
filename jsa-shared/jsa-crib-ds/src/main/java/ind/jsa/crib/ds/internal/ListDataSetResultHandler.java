package ind.jsa.crib.ds.internal;


import java.util.ArrayList;
import java.util.List;

import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;

/**
 * Simple List result handler implementation that accumulates results in a list of DataSetItems in memory.
 */
public class ListDataSetResultHandler extends AbstractDataSetResultHandler {

    /**
     * Default initial size of the internal list of items.
     */
    private static final int DEFAULT_INIT_ROW_SIZE = 100;

    private List<IDataSetItem> items;

    /**
     * This Constructor sets required members, with a default query and a default initial size.
     * 
     * @param dataSet The DataSet for the handler
     */
    public ListDataSetResultHandler(IDataSet dataSet, DataSetQuery query) {
        this(dataSet, query, DEFAULT_INIT_ROW_SIZE);
    }

    /**
     * This Constructor sets required members, with a specified query and a default initial size.
     * 
     * @param dataSet The DataSet for the handler
     * @param query The query for the handler
     * @param initSize The initial size of the internal list of items
     */
    public ListDataSetResultHandler(IDataSet dataSet, DataSetQuery query, int initSize) {
    	super(dataSet, query);
        items = new ArrayList<IDataSetItem>(initSize);
    }

    @Override
    public void processItem(IDataSetItem item) {
        super.processItem(item);
        items.add(item);
    }

    /**
     * Get the accumulated list of items.
     * 
     * @return A list of items
     */
    public List<IDataSetItem> getItems() {
        return items;
    }
    
    /**
     * Clear items in the event that caller wants to re-use same handler.
     * 
     */
    public void reset() {
    	items.clear();
    	setProcessCount(0);
    }
}
