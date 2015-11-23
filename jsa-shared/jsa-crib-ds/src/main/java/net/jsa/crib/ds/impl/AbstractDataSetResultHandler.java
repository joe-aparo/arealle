package net.jsa.crib.ds.impl;

import net.jsa.crib.ds.api.DataSetQuery;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetItem;
import net.jsa.crib.ds.api.IDataSetResultHandler;

/**
 * Common handler from which to derive specific handlers.
 * 
 */
public abstract class AbstractDataSetResultHandler implements IDataSetResultHandler {

    private int processCount = 0;
    private IDataSet dataSet;
    private DataSetQuery query;
    
    /**
     * Constructor associates data set with handler.
     * 
     * @param dataSet A DataSet
     */
    public AbstractDataSetResultHandler(IDataSet dataSet, DataSetQuery query) {
    	this.dataSet = dataSet;
    	this.query = query;
    }
    
    /**
     * Get the data set associated with the handler.
     * 
     * @return A data set
     */
    public IDataSet getDataSet() {
    	return dataSet;
    }

    /**
     * Get the query associated with the handler
     * 
     * @return A dataset query
     */
    public DataSetQuery getQuery() {
		return query;
	}

	/*
     * (non-Javadoc)
     * @see com.jsa.crib.ds.api.IDataSetResultHandler#processStart()
     */
    @Override
    public void processStart() {
        processCount = 0;
    }

    /*
     * (non-Javadoc)
     * @see com.jsa.crib.ds.api.IDataSetResultHandler#processRow(com.jsa.crib.ds.api.DataSetItem)
     */
    @Override
    public void processRow(IDataSetItem item) {
        processCount++;
    }

    /*
     * (non-Javadoc)
     * @see com.jsa.crib.ds.api.IDataSetResultHandler#processEnd()
     */
    @Override
    public void processEnd() {
        // No-op adapter method
    }

    /*
     * (non-Javadoc)
     * @see com.jsa.crib.ds.api.IDataSetResultHandler#getProcessCount()
     */
    @Override
    public int getProcessCount() {
        return processCount;
    }

    /**
     * Set the current process count.
     * @param count A count
     */
    public void setProcessCount(int count) {        
        processCount = count;
    }
}
