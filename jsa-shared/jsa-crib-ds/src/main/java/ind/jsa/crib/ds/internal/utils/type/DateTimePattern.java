package ind.jsa.crib.ds.internal.utils.type;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains a collection of common date/time formats.
 * 
 * @author jsaparo
 *
 */
public enum DateTimePattern {
    /**
     * Medium date/time pattern
     */
    UNIVERSAL_DATETIME_PATTERN("UDTP", "yyyy-MM-dd HH:mm:ss"),
    /**
     * Universal timestamp
     */
    UNIVERSAL_TIMESTAMP_PATTERN("UTSP", "yyyy-MM-dd HH:mm:ss.SSS"),
    /**
     * Universal short date
     */
    UNIVERSAL_DATE_PATTERN("UDP", "yyyy-MM-dd"),
    /**
     * US short date
     */
    US_SHORT_DATE_PATTERN("UDP", "MM/dd/yyyy"),
    /**
     * US short date
     */
    MEDIUM_DATETIME_PATTERN("MDTP", "EEE, MMM d yyyy HH:mm:ss");
    
    private String code;
    private String pattern;

    private static final Map<String, DateTimePattern> PATTERNS_BY_CODE = new HashMap<String, DateTimePattern>();

    static {
    	PATTERNS_BY_CODE.put(UNIVERSAL_DATETIME_PATTERN.getCode(), UNIVERSAL_DATETIME_PATTERN);
    	PATTERNS_BY_CODE.put(UNIVERSAL_TIMESTAMP_PATTERN.getCode(), UNIVERSAL_TIMESTAMP_PATTERN);
    	PATTERNS_BY_CODE.put(UNIVERSAL_DATE_PATTERN.getCode(), UNIVERSAL_DATE_PATTERN);
    	PATTERNS_BY_CODE.put(US_SHORT_DATE_PATTERN.getCode(), US_SHORT_DATE_PATTERN);
    	PATTERNS_BY_CODE.put(MEDIUM_DATETIME_PATTERN.getCode(), MEDIUM_DATETIME_PATTERN);
    }

    /**
     * Constructor.
     * @param code Pattern code
     * @param pattern Date/time pattern
     */
    DateTimePattern(String code, String pattern) {
        this.code = code;
        this.pattern = pattern;
    }

    /**
     * Get date/time pattern.
     * @return A pattern string 
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Get current direction code.
     * @return A direction code
     */
    public String getCode() {
        return code;
    }

    /**
     * Get an enumeration value by code.
     * @param code Pattern code
     * @return A DateTimePattern enumeration
     */
    public static DateTimePattern getByCode(String code) {
        return PATTERNS_BY_CODE.get(code);
    }

    /**
     * As a pattern string.
     * 
     * @return A string
     */
    @Override
    public String toString() {
        return pattern;
    }
}
