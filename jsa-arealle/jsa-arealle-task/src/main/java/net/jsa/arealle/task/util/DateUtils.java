package net.jsa.arealle.task.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

/**
 * Common routines for formatting and parsing dates.
 * @author jsaparo
 *
 */
public class DateUtils {
	
	public static Logger log = Logger.getLogger(DateUtils.class);
	
	// Stock set of formats
	public static final String SHORT_US_DATE = "MM/dd/yyyy";
	public static final String SHORT_US_DATETIME = "MM/dd/yyyy HH:mm:ss";
	public static final String UNIVERSAL_DATE = "yyyy-MM-dd";
	public static final String UNIVERSAL_DATETIME = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String MEDIUM_US_DATE = "MMMMM d, yyyy";
	public static final String COMPRESSED_DATE = "yyyyMMdd";
	public static final String COMPRESSED_DATETIME = "yyyyMMddHH:mm:ss";
	
	public static String formatShortUsDate() {
		return format(SHORT_US_DATE);
	}
	
	public static String formatShortUsDate(Date dt) {
		return format(dt, SHORT_US_DATE);
	}
	
	public static String formatShortUsDateTime() {
		return format(SHORT_US_DATETIME);
	}
	
	public static String formatShortUsDateTime(Date dt) {
		return format(dt, SHORT_US_DATETIME);
	}
	
	public static String formatUniversalDateTime() {
		return format(UNIVERSAL_DATETIME);
	}
	
	public static String formatUniversalDateTime(Date dt) {
		return format(dt, UNIVERSAL_DATETIME);
	}
	
	public static String formatUniversalDate() {
		return format(UNIVERSAL_DATE);
	}
	
	public static String formatUniversalDate(Date dt) {
		return format(dt, UNIVERSAL_DATE);
	}
	
	public static String formatMediumUsDate() {
		return format(MEDIUM_US_DATE);
	}
	
	public static String formatMediumUsDate(Date dt) {
		return format(dt, MEDIUM_US_DATE);
	}
	
	public static String formatCompressedDate() {
		return format(COMPRESSED_DATE);
	}
	
	public static String formatCompressedDate(Date dt) {
		return format(dt, COMPRESSED_DATE);
	}
	
	public static String formatCompressedDateTime() {
		return format(COMPRESSED_DATETIME);
	}
	
	public static String formatCompressedDateTime(Date dt) {
		return format(dt, COMPRESSED_DATETIME);
	}
	
	public static String format(String format) {
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		
		return fmt.format(new Date());
	}
	
	public static String format(Date dt, String format) {
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		
		return fmt.format(dt);
	}
	
	/**
	 * Utility method for instantiating an XMLGregorianCalendar object
	 * initialized to a specified date.
	 * 
	 * @param date The date to set the calendar to
	 * @return An XMLGregorianCalendar object
	 */
	public static XMLGregorianCalendar createXMLGregorianCalendar(Date date) 
    {
        DatatypeFactory datatypeFactory = null;

		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException ex) {
			log.warn("Error creating xml calendar object", ex);
			return null;
		}
        
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        
        return datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
    }
	
	/**
	 * Utility method for instantiating an XMLGregorianCalendar object.
	 * 
	 * @return An XMLGregorianCalendar object
	 */
	public static XMLGregorianCalendar createXMLGregorianCalendar() 
    {
		return createXMLGregorianCalendar(new Date());
    }
	
	
	public static Date shortStrToDate(String dtStr) {
		
		if (dtStr == null || dtStr.length() != 8) {
			return null;
		}
		
		try {
			int yr = Integer.parseInt(dtStr.substring(0, 4));
			int mo = Integer.parseInt(dtStr.substring(4, 6));
			int dy = Integer.parseInt(dtStr.substring(6));
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, yr);
			cal.set(Calendar.DAY_OF_MONTH, dy);
			cal.set(Calendar.MONTH, mo - 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			return new java.sql.Date(cal.getTimeInMillis());
			
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar xcal) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, xcal.getYear());
		cal.set(Calendar.MONTH, xcal.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, xcal.getDay());
		cal.set(Calendar.HOUR, xcal.getHour());
		cal.set(Calendar.MINUTE,  xcal.getMinute());
		cal.set(Calendar.SECOND, xcal.getSecond());
		
		return cal.getTime();
	}
	
}
