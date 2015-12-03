package ind.jsa.crib.ds.internal;

import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetBatchHandler;
import ind.jsa.crib.ds.api.IDataSetItem;

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

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.internal.ListDataSetResultHandler#processItem(ind.jsa.crib.ds.api.IDataSetItem)
	 */
	@Override
	public void processItem(IDataSetItem item) {
		super.processItem(item);
		
		// If we've read the batch size, handle the batch and reset
		if (this.getProcessCount() >= batchSize) {
			batchHandler.handleBatch(getItems());
			reset();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.internal.AbstractDataSetResultHandler#processEnd()
	 */
	@Override
	public void processEnd() {
		super.processEnd();
		
		// Handle straggling batch
		batchHandler.handleBatch(getItems());
	}
}
