package net.jsa.common.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

	// Static methods only - no public constructor
    private LogUtils() {
    }
    
    /**
     * Returns a logger for the calling class.
     * 
     * @return A logger
     */
    public static Logger getLogger() {
        final Throwable t = new Throwable();
        t.fillInStackTrace();
        final String clazz = t.getStackTrace()[1].getClassName();
        return LoggerFactory.getLogger(clazz);
    }
}
