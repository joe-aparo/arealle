package net.jsa.crib.ds.impl;

import net.jsa.crib.ds.api.DataSetQuery;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetBatchHandler;
import net.jsa.crib.ds.api.IDataSetItem;

/**
 * Result handler that allows the interjection of another handler to
 * be invoked whenever a certain number of items are read. This handler
 * will clear the currently accumulated list of items after invoking
 * the configured handler for the current batch of items.
 * 
 * @author jsaparo
 *
 */
public class BatchedDataSetResultHandler extends ListDataSetResultHandler {
	private int batchSize;
	IDataSetBatchHandler batchHandler;
	
	public BatchedDataSetResultHandler(
		IDataSet dataSet, DataSetQuery query, int batchSize, IDataSetBatchHandler batchHandler) {
		
		super(dataSet, query);
		this.batchSize = batchSize;
		this.batchHandler = batchHandler;
	}

	@Override
	public void processRow(IDataSetItem item) {
		super.processRow(item);
		
		// If we've read the batch size, handle the batch and reset
		if (this.getProcessCount() >= batchSize) {
			batchHandler.handleBatch(getItems());
			reset();
		}
	}
	
	public void processEnd() {
		super.processEnd();
		
		// Handle straggling batch
		batchHandler.handleBatch(getItems());
	}
}
