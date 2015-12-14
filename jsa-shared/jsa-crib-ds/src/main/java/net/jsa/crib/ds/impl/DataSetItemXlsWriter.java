package net.jsa.crib.ds.impl;

import ind.jsa.crib.ds.internal.type.utils.DateTimePattern;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;

import net.jsa.crib.ds.api.IDataSetItem;
import net.jsa.crib.ds.api.IDataSetProperty;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


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

	@Override
	public void writeItem(IDataSetItem item) {
		// Create a new data row
		Row row = sheet.createRow(curRow++); // 0-based row index
		
		// Fill the row object with data
		int col = 0;
		for (String propName : getOutputProperties()) {
			Cell cell = row.createCell(col++);
			IDataSetProperty prop = item.getDataSet().getProperty(propName);
			switch (prop.getType()) {
				case BOOLEAN: {
					Boolean bool = item.getBoolean(propName);
					if (bool != null) {
						cell.setCellValue(bool);
					}
					break;
				}
				case DATETIME: {
					Calendar cal = item.getDateTime(propName);
					if (cal != null) {
						cell.setCellValue(cal);
						cell.setCellStyle(dateStyle);
					}
					break;
				}
				case DECIMAL: {
					BigDecimal dec = item.getDecimal(propName);
					if (dec != null) {
						cell.setCellValue(dec.doubleValue());
					}
					break;
				}
				case INTEGER: {
					Long i = item.getInteger(propName);
					if (i != null) {
						cell.setCellValue(i.doubleValue());
					}
					break;
				}
				default: { // STRING
					String str = item.getString(propName);
					if (str != null) {
						cell.setCellValue(str);
					}
				}
			}
		}
	}

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
