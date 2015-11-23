package net.jsa.arealle.task.process;

import java.io.IOException;
import java.util.Map;

import net.jsa.arealle.task.esri.ShapeDbaseReader;
import net.jsa.arealle.task.esri.ShapeEntry;
import net.jsa.arealle.task.esri.ShapeFileReader;

public abstract class ShapeFileProcessStep extends CountedProcessStep {

	private String shapeFileName;
	ShapeFileReader shapeFileReader;
	ShapeDbaseReader shapeDbaseReader;
	
	public ShapeFileProcessStep(String name) {
		super(name);
		
		shapeFileReader = new ShapeFileReader();
		shapeDbaseReader = new ShapeDbaseReader();
	}

	public void setShapeFileName(String shapeFileName) {
		this.shapeFileName = shapeFileName;
	}
	
	@Override
	public boolean start(Map<String, Object> context) {
		super.start(context);
		
		String dbfName = shapeFileName.substring(0, shapeFileName.indexOf('.')) + ".dbf";
		
		try {
			shapeFileReader.open(shapeFileName);
			shapeDbaseReader.open(dbfName);
			
			setTotalItems(shapeDbaseReader.getTotalItems());
		} catch (IOException ex) {
			getLog().error("Error opening shape data.", ex);
			return false;
		}

		return true;
	}
	
	@Override
	public boolean nextItem() {
		if (!super.nextItem()) {
			return false;
		}
		
		try {
			Map<String, Object> data = shapeDbaseReader.readNext();
			ShapeEntry entry = shapeFileReader.readNext();
			
			handleEntry(entry, data);			
		} catch (IOException ex) {
			getLog().error("Error reading shape data.", ex);
			return false;
		}
		
		return true;
	}

	protected abstract void handleEntry(ShapeEntry shapeEntry, Map<String, Object> data);
}
