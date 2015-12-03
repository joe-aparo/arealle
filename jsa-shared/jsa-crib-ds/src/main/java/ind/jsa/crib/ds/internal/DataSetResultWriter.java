package ind.jsa.crib.ds.internal;

import org.slf4j.Logger;

import net.jsa.common.logging.LogUtils;
import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetItemWriter;

/**
 * Base class for streaming data set results to a writer.
 * 
 * @author jsaparo
 *
 */
public class DataSetResultWriter extends AbstractDataSetResultHandler {
	private boolean writeHeader = true;
	private Logger log = LogUtils.getLogger();
	private IDataSetItemWriter itemWriter;

	/**
	 * Constructor takes output object and a writer for formatting written items.
	 * 
	 * @param resultWriter Result output
	 * @param itemWriter Object for writing an item to the result output
	 */
	public DataSetResultWriter(IDataSet dataSet, DataSetQuery query, IDataSetItemWriter itemWriter) {
		super(dataSet, query);
		this.itemWriter = itemWriter;
	}
	
	protected Logger getLog() {
		return log;
	}

	public boolean getWriteHeaders() {
		return writeHeader;
	}

	public void setWriteHeaders(boolean writeHeaders) {
		this.writeHeader = writeHeaders;
	}

	/**
	 * @see net.jsa.crib.ds.impl.AbstractDataSetResultHandler#processStart()
	 */
	@Override
	public void processStart() {
       	itemWriter.writeStart();
	}
	
	/**
	 * @see net.jsa.crib.ds.impl.AbstractDataSetResultHandler#processRow(net.jsa.crib.ds.api.IDataSetItem)
	 */
    @Override
    public void processItem(IDataSetItem item) {
        super.processItem(item);
       	itemWriter.writeItem(item);
    }

    /**
     * @see net.jsa.crib.ds.impl.AbstractDataSetResultHandler#processEnd()
     */
	@Override
    public void processEnd() {
    	super.processEnd();
       	itemWriter.writeEnd();
    }
}
