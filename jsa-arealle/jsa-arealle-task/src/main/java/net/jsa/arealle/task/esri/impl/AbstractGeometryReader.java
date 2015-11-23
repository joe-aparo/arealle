package net.jsa.arealle.task.esri.impl;

import org.slf4j.Logger;

import net.jsa.arealle.task.esri.IGeometryReader;
import net.jsa.common.logging.LogUtils;

public abstract class AbstractGeometryReader implements IGeometryReader {
	private Logger log = LogUtils.getLogger();
	
	public boolean hasBoundingBox() {
		return true;
	}

	protected Logger getLog() {
		return log;
	}
}
