package net.jsa.arealle.task.esri;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;

import net.jsa.common.logging.LogUtils;

public class ShapeDbaseReader {
	private String dbaseFileName;
	private FileInputStream istream = null;
	boolean isOpen = false;
	private DbaseFileReader dbr;
	int totalItems;
	int curItem;
	private Logger log = LogUtils.getLogger();
	
	public void open(String fileName) throws IOException {
		if (isOpen) {
			return;
		}
		
		// Assume ESRI shape file name (*.shp")
		dbaseFileName = fileName;
		istream = new FileInputStream(dbaseFileName);
		dbr = new DbaseFileReader(istream.getChannel(), false, Charset.defaultCharset());
		totalItems = dbr.getHeader().getNumRecords();
		curItem = 0;
		
		isOpen = true;
	}
	
	public Map<String, Object> readNext() throws IOException {
		if (!isOpen || curItem >= totalItems || !dbr.hasNext()) {
			return null;
		}
		
		curItem++;
		
		DbaseFileHeader dbfHeader = dbr.getHeader();
		int numFields = dbfHeader.getNumFields();
		Map<String, Object> map = new LinkedHashMap<String, Object>(numFields);
		DbaseFileReader.Row	row = dbr.readRow(); 
		
		for (int i = 0; i < numFields; i++) {
			map.put(dbfHeader.getFieldName(i), row.read(i));
		}
		
		return map;
	}

	public void close() {
		if (!isOpen) {
			return;
		}
		
		try {
			istream.close();
		} catch (IOException e) {
			log.error("Error closeing dbase file: " + dbaseFileName);
		}
		
		isOpen = false;
		
	}
	
	public int getTotalItems() {
		return totalItems;
	}

	public int getCurItem() {
		return curItem;
	}
}
