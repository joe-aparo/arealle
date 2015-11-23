package net.jsa.arealle.task.process;

import java.util.List;
import java.util.Map;

import net.jsa.crib.ds.api.DataSetQuery;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetBatchHandler;
import net.jsa.crib.ds.api.IDataSetItem;
import net.jsa.crib.ds.impl.BatchedDataSetResultHandler;

public abstract class DataSetBatchProcessStep extends CountedProcessStep {
	
	private DataSetQuery query;
	
	public DataSetBatchProcessStep(String name) {
		super(name);
	}

	@Override
	public boolean start(Map<String, Object> context) {
		super.start(context);

		query = getQuery(context);
		
		setTotalItems(getDataSet().getItemCount(query));
		
		return true;
	}
	
	@Override
	public boolean nextItem() {
		super.nextItem();
		
		// Wrap caller-provided handler within a generic batch handler 
		BatchedDataSetResultHandler handler = new BatchedDataSetResultHandler ( 
			getDataSet(), query, getBatchSize(), new ProcessStepDataSetBatchHandler(this, getBatchHandler()));
		
		// Main loop blocks on this call. There will only be one call to nextItem
		// by the main processing loop.
		getDataSet().retrieve(query, handler);		

		// Batch is complete - no more work to do
		return false;
	}
	
	public class ProcessStepDataSetBatchHandler implements IDataSetBatchHandler {
		IDataSetBatchHandler delegate;
		CountedProcessStep processStep;
		
		public ProcessStepDataSetBatchHandler(CountedProcessStep processStep, IDataSetBatchHandler delegate) {
			this.processStep = processStep;
			this.delegate = delegate;
		}
		
		@Override
		public void handleBatch(List<IDataSetItem> items) {
			for (int i = 0; i < items.size(); i++) {
				// increment item in super class to support progress feedback
				processStep.advanceCurrentItem();
			}
			
			// Process the batch with the caller provided handler
			delegate.handleBatch(items);
		}
	}

	/**
	 * Get the data set associated with the step.
	 * 
	 * @return A data set
	 */
	protected abstract IDataSet getDataSet();
	
	/**
	 * Get the batch handler associated with the step.
	 * 
	 * @return A batch handler
	 */
	protected abstract IDataSetBatchHandler getBatchHandler();
	
	/**
	 * Get the batch size for the step.
	 * 
	 * @return A batch size
	 */
	protected abstract int getBatchSize();
	
	/**
	 * Get the query for the data set.
	 * 
	 * @return A query
	 */
	protected abstract DataSetQuery getQuery(Map<String, Object> context);
	
}
