package net.jsa.arealle.task.process;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;

public abstract class DbfProcessStep extends CountedProcessStep {
	private String fileNamePattern;
	private FileInputStream isDbf;
	private DbaseFileReader dbr;
	private String dbfName;
	
	public DbfProcessStep(String name) {
		super(name);
	}
	
	public String getFileNamePattern() {
		return fileNamePattern;
	}

	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	protected String makeDbfName() {
		return getFileNamePattern();
	}

	@Override
	public boolean start(Map<String, Object> ctx) {
		super.start(ctx);
		
		dbfName = makeDbfName();
		
		try {
			isDbf = new FileInputStream(dbfName);
			dbr = new DbaseFileReader(isDbf.getChannel(), false, Charset.defaultCharset());
			this.setTotalItems(dbr.getHeader().getNumRecords());
		} catch (Exception ex) {
			getLog().error("Error reading dbf file: " + dbfName, ex);
			
			isDbf = null;
			dbr = null;
		}	
		
		return true;
	}

	@Override
	public boolean nextItem() {
		if (!dbr.hasNext()) {
			return false;
		}
		
		if (!super.nextItem()) {
			return false;
		}
		
		try {
			handleItem(readDbfRow(dbr));
		} catch (IOException ex) {
			getLog().error("Error reading dbf file: " + dbfName, ex);
			return false;
		}
		
		return true;
	}

	@Override
	public void finish() {
		super.finish();
		
		try {
			isDbf.close();
		} catch (IOException ex) {
			getLog().error("Error closing dbf file: " + dbfName, ex);
		}
	}

	protected abstract void handleItem(Map<String, Object> row);

	private Map<String, Object> readDbfRow(DbaseFileReader dbr) throws IOException {
		if (dbr == null) {
			return null;
		}
		
		if (!dbr.hasNext()) {
			getLog().error("DBF at EOF");
			return null;
		}
		
		DbaseFileHeader dbfHeader = dbr.getHeader();
		int numFields = dbfHeader.getNumFields();
		Map<String, Object> map = new LinkedHashMap<String, Object>(numFields);
		DbaseFileReader.Row	row = dbr.readRow(); 
		
		for (int i = 0; i < numFields; i++) {
			map.put(dbfHeader.getFieldName(i), row.read(i));
		}
		
		return map;
	}
}
