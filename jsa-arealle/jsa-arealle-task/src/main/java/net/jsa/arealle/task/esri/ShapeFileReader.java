package net.jsa.arealle.task.esri;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import net.jsa.arealle.task.esri.impl.PointReader;
import net.jsa.arealle.task.esri.impl.PolyLineReader;
import net.jsa.arealle.task.esri.impl.PolygonReader;
import net.jsa.arealle.task.geom.ShapeType;
import net.jsa.common.logging.LogUtils;
import com.vividsolutions.jts.geom.GeometryFactory;

public class ShapeFileReader {
	private static final Map<ShapeType, IGeometryReader> shapeReaders;

	static {
		shapeReaders = new HashMap<ShapeType, IGeometryReader>();
		shapeReaders.put(ShapeType.POLYGON, new PolygonReader());
		shapeReaders.put(ShapeType.POLYLINE, new PolyLineReader());
		shapeReaders.put(ShapeType.POINT, new PointReader());
	}

	private static final int FILE_HEADER_LEN = 50;
	private static final int PRELUDE_LEN = 24;
	private static final int ENTRY_HEADER_LEN = 4;
	
	private Logger log = LogUtils.getLogger();
	private GeometryFactory geometryFactory = new GeometryFactory();
	private boolean isOpen = false;
	private FileHeader header;
	private int byteCt;
	private InputStream istream;
	private IGeometryReader geometryReader;
	
	public void open(String shapeFileName) throws IOException {
		if (isOpen) {
			return;
		}
		
		// Assume ESRI shape file name (*.shp")
		istream = new FileInputStream(shapeFileName);
		
		// Skip prelude - no need
		ReadUtils.skip(istream, PRELUDE_LEN);
		
		// Read/handle header
		header = readHeader(shapeFileName);

		// Establish the reader for shapes in this file. All shape entries should be of the same type.
		geometryReader = shapeReaders.get(header.getShapeType());
		if (geometryReader == null) {
			throw new RuntimeException("Unable to locate reader for shape type: " + header.getShapeType());
		}

		// Advance byte count past header
		byteCt = FILE_HEADER_LEN;

		isOpen = true;
	}
	
	public ShapeEntry readNext() throws IOException {
		if (!isOpen || byteCt >= header.getFileLength()) {
			return null;
		}
		
		ShapeEntry entry = readShapeEntry();
		
		byteCt += (entry.getLength() + ENTRY_HEADER_LEN);
		
		return entry;
	}
	
	public void close() {
		if (!isOpen) {
			return;
		}
		
		try {
			istream.close();
		} catch (IOException e) {
			log.error("Error closeing shapefile: " + header.getFileName());
		}
		
		isOpen = false;
	}
	
	public FileHeader getHeader() {
		return header;
	}

	private FileHeader readHeader(String fileName) throws IOException {
		FileHeader header = new FileHeader();
		header.setFileName(fileName);
		header.read(istream, geometryFactory);
		
		return header;
	}
	
	private ShapeEntry readShapeEntry() throws IOException {
		ShapeEntry entry = new ShapeEntry();
		entry.read(istream, geometryFactory, geometryReader);
		
		return entry;
	}

}
