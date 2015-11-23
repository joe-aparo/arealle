package net.jsa.crib.ds.impl;

import java.io.OutputStream;
import java.util.Collection;

import org.slf4j.Logger;

import net.jsa.common.logging.LogUtils;
import net.jsa.crib.ds.api.IDataSetItem;
import net.jsa.crib.ds.api.IDataSetItemWriter;

public abstract class AbstractDataSetItemWriter implements IDataSetItemWriter {

	private Logger log = LogUtils.getLogger();
	private OutputStream outputStream;
	private int itemCount = 0;
	private Collection<String> outputProperties;
	private boolean writeHeader = true;
	
	public AbstractDataSetItemWriter(OutputStream outputStream, Collection<String> outputProperties) {
		this.outputStream = outputStream;
		this.outputProperties = outputProperties;
	}
	
	protected Logger getLog() {
		return log;
	}
	
	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public Collection<String> getOutputProperties() {
		return outputProperties;
	}

	public void setOutputProperties(Collection<String> outputProperties) {
		this.outputProperties = outputProperties;
	}

	public boolean getWriteHeader() {
		return writeHeader;
	}

	public void setWriteHeader(boolean writeHeader) {
		this.writeHeader = writeHeader;
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItemWriter#writeStart(com.jsa.crib.ds.api.IDataSetItemWriterState)
	 */
	@Override
	public void writeStart() {
		// stub
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItemWriter#writeItem(net.jsa.crib.ds.api.IDataSetItem, com.jsa.crib.ds.api.IDataSetItemWriterState)
	 */
	@Override
	public abstract void writeItem(IDataSetItem item);

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItemWriter#writeEnd(com.jsa.crib.ds.api.IDataSetItemWriterState)
	 */
	@Override
	public void writeEnd() {
		// stub
	}
}