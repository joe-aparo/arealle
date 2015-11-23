package net.jsa.arealle.task.process;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

import net.jsa.arealle.task.util.DateUtils;
import net.jsa.crib.ds.api.IDataSetBatchHandler;

public abstract class AbstractDataSetBatchHandler implements IDataSetBatchHandler {
	protected Integer chkInt(Long val) {
		return val != null ? Integer.valueOf(val.intValue()) : null;
	}
	
	protected Double chkDec(BigDecimal val) {
		return val != null ? Double.valueOf(val.doubleValue()) : null;
	}
	
	protected XMLGregorianCalendar chkDt(Calendar val) {
		return val != null ? DateUtils.createXMLGregorianCalendar(val.getTime()) : null;
	}
}
