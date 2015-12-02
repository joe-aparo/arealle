package ind.jsa.crib.ds.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ind.jsa.crib.ds.api.IDataSetItem;

public class DataSetItemCsvWriter extends AbstractDataSetItemWriter {

	private static final char DEFAULT_SEP_CHAR = ',';
	private static final char DEFAULT_QUOTE_CHAR = '"';
	private static final String DEFAULT_LINE_END_STR=System.getProperty("line.separator");
	
	private char sepChar = DEFAULT_SEP_CHAR;
	private char quoteChar = DEFAULT_QUOTE_CHAR;
	private String lineEndStr = DEFAULT_LINE_END_STR;
	OutputStreamWriter osWriter;

	public DataSetItemCsvWriter(OutputStream outputStream, Collection<String> outputProperties, boolean writeHeader) {
		super(outputStream, outputProperties);
		setWriteHeader(writeHeader);
	}
	/**
	 * Set the default separator character.
	 * 
	 * @param sepChar A character
	 */
	public void setSepChar(char sepChar) {
		this.sepChar = sepChar;
	}

	/**
	 * Set the default quote character.
	 * 
	 * @param sepChar A character
	 */
	public void setQuoteChar(char quoteChar) {
		this.quoteChar = quoteChar;
	}

	/**
	 * Set the default line ending string.
	 * 
	 * @param sepChar A string
	 */
	public void setLineEndStr(String lineEndStr) {
		this.lineEndStr = lineEndStr;
	}

	/**
	 * @see net.jsa.crib.ds.impl.AbstractDataSetItemWriter#writeStart()
	 */
	@Override
	public void writeStart() {
		osWriter = new OutputStreamWriter(getOutputStream());
		
		if (!getWriteHeader()) {
			return;
		}
		
		String[] vals = new String[getOutputProperties().size()];
		int i = 0;
		for (String fld : getOutputProperties()) {
			vals[i++] = fld;
		}
		
		writeRow(vals);
	}

	/**
	 * @see net.jsa.crib.ds.impl.AbstractDataSetItemWriter#writeItem(net.jsa.crib.ds.api.IDataSetItem)
	 */
	@Override
	public void writeItem(IDataSetItem item) {
		String[] vals = new String[getOutputProperties().size()];
		int i = 0;
		for (String fld : getOutputProperties()) {
			vals[i++] = item.getString(fld);
		}

		// Write a single record, but API requires list
		List<String[]> records = new ArrayList<String[]>(1);
		records.add(vals);

		writeRow(vals);
	}

	/**
	 * @see net.jsa.crib.ds.impl.AbstractDataSetItemWriter#writeEnd()
	 */
	@Override
	public void writeEnd() {
		try {
			osWriter.close();
		} catch (IOException ex) {
			getLog().error("Error close CSV output stream", ex);
		}
	}

	private void writeRow(String[] vals) {
		try {
			for (int i = 0; i < vals.length; i++) {
				if (i > 0) {
					osWriter.write(Character.toString(sepChar));
				}
				osWriter.write(Character.toString(quoteChar));
				osWriter.write(vals[i] != null ? vals[i] : "");
				osWriter.write(Character.toString(quoteChar));
			}
			osWriter.write(lineEndStr);
		} catch (IOException ex) {
			getLog().error("Error writing to CSV output stream", ex);
		}
	}
}	
