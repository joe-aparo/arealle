package ind.jsa.crib.ds.internal;


import java.util.ArrayList;
import java.util.List;

import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;

/**
 * Class for extracting a collection of identifiers from the results of items retrieved from a DataSet. 
 */
public class IdListDataSetResultHandler extends AbstractDataSetResultHandler {

    /**
     * Default initial size of the internal list of items.
     */
    private static final int DEFAULT_INIT_ROW_SIZE = 500;

    private List<Object> ids;
    private String idProp;

    /**
     * This Constructor sets required members, with a default id property name and a default initial size.
     * 
     * @param dataSet The DataSet for the handler
     * @param query The query for the handler
     */
    public IdListDataSetResultHandler(IDataSet dataSet, DataSetQuery query) {
        this(dataSet, query, IDataSet.DEFAULT_ID_PROPERTY);
    }

    /**
     * This Constructor sets required members, with a specified id property name and a default initial size.
     * 
     * @param dataSet The DataSet for the handler
     * @param query The query for the handler
     * @param idProp The name of the id property
     */
    public IdListDataSetResultHandler(IDataSet dataSet, DataSetQuery query, String idProp) {
        this(dataSet, query, IDataSet.DEFAULT_ID_PROPERTY, DEFAULT_INIT_ROW_SIZE);
    }

    /**
     * This Constructor sets required members, with a specified id property name and a specified initial size.
     * 
     * @param dataSet The DataSet for the handler
     * @param query The query for the handler
     * @param idProp The name of the id property
     * @param initSize The initial size of the internal list of items
     */
    public IdListDataSetResultHandler(IDataSet dataSet, DataSetQuery query, String idProp, int initSize) {
    	super(dataSet, query);
        ids = new ArrayList<Object>(initSize);

        this.idProp = idProp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.jsa.crib.ds.impl.AbstractDataSetResultHandler#processRow(com.jsa.crib.ds.api
     * .DataSetItem)
     */
    @Override
    public void processItem(IDataSetItem item) {
        super.processItem(item);
        ids.add(item.get(idProp));
    }

    /**
     * Get the list of accumulated ids.
     * 
     * @return A list of id objects
     */
    public List<Object> getIds() {
        return ids;
    }
}
