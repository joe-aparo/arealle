package ind.jsa.crib.ds.internal;

import ind.jsa.crib.ds.internal.utils.type.DateTimePattern;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;

import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetMetaData;
import ind.jsa.crib.ds.api.IDataSetProperty;
import ind.jsa.crib.ds.api.ITypeManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Write dataset results to an Excel output stream.
 * 	
 * @author jo26419
 *
 */
public class DataSetItemXlsWriter extends AbstractDataSetItemWriter {

	private SXSSFWorkbook workbook;
	private Sheet sheet;
	private int curRow = 0;
	DataFormat dataFormat;
	CellStyle dateStyle;
	
	public DataSetItemXlsWriter(OutputStream outputStream, Collection<String> outputProperties, boolean writeHeader) {
		super(outputStream, outputProperties);
		setWriteHeader(writeHeader);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.internal.AbstractDataSetItemWriter#writeStart()
	 */
	@Override
	public void writeStart() {
		// Set up new worksheet object
		workbook = new SXSSFWorkbook(100); // set row buffer size
		sheet = workbook.createSheet();
		dataFormat = workbook.createDataFormat();
		dateStyle = workbook.createCellStyle();
		dateStyle.setDataFormat(dataFormat.getFormat(DateTimePattern.UNIVERSAL_DATETIME_PATTERN.toString()));
		
		// no header?
		if (!getWriteHeader()) {
			return;
		}
		
		// Create a new header row
		Row row = sheet.createRow(curRow++); // 0-based row index
		
		// Fill header row object with property names
		int col = 0;
		for (String propName : getOutputProperties()) {
			Cell cell = row.createCell(col++);
			cell.setCellValue(propName);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.internal.AbstractDataSetItemWriter#writeItem(ind.jsa.crib.ds.api.IDataSetItem)
	 */
	@Override
	public void writeItem(IDataSetItem item) {
		// Create a new data row
		Row row = sheet.createRow(curRow++); // 0-based row index
		
		IDataSetMetaData metaData = item.getMetaData();
		ITypeManager typeManager = metaData.getTypeManager();
		
		// Fill the row object with data. Only basic primitive types are supported here.
		int col = 0;
		for (String propName : getOutputProperties()) {
			Cell cell = row.createCell(col++);
			
			IDataSetProperty prop = metaData.getProperty(propName);
			
			if (DefaultTypeManager.isBooleanNature(typeManager.getTypeNature(prop.getType()))) {
				Boolean bool = item.getBoolean(propName);
				if (bool != null) {
					cell.setCellValue(bool);
				}
			} else if (DefaultTypeManager.isDateTimeNature(typeManager.getTypeNature(prop.getType()))) {
				Date dt = item.getDate(propName);
				if (dt != null) {
					cell.setCellValue(dt);
					cell.setCellStyle(dateStyle);
				}
			} else if (DefaultTypeManager.isDecimalNature(typeManager.getTypeNature(prop.getType()))) {
				Double dbl = item.getDouble(propName);
				if (dbl != null) {
					cell.setCellValue(dbl);
				}
			} else if (DefaultTypeManager.isIntegerNature(typeManager.getTypeNature(prop.getType()))) {
				Integer i = item.getInteger(propName);
				if (i != null) {
					cell.setCellValue(i);
				}
			} else { // Assume string
				String str = item.getString(propName);
				if (str != null) {
					cell.setCellValue(str);
				}				
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.internal.AbstractDataSetItemWriter#writeEnd()
	 */
	@Override
	public void writeEnd() {
		// Output the workbook
		try {
			workbook.write(getOutputStream());
			getOutputStream().close();
		} catch (IOException ex) {
			getLog().error("I/O error writing XLS output", ex);
		}
		
		// clean up
		workbook.dispose();
	}
}
