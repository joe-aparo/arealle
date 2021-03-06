package ind.jsa.crib.ds.internal;

import org.apache.commons.lang3.StringUtils;

/**
 * Miscellaneous property name related utility methods.
 * 
 * @author jo26419
 *
 */
public class PropertyNameUtils {

	/**
	 * Apply standardization to property names.
	 * 
	 * @param name Name to standardize
	 * @return A standardized name string
	 */
	public static String normalizeName(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		
		return name.toLowerCase().trim();
	}
	
	/**
     * Convert a string of the form "UNDER_SCORE_NAME" to "underScoreName".
     * 
     * @param underScoreName The name to convert
     * @return The converted name
     */
    public static String camelCaseUnderScoreName(String underScoreName) {
        if (StringUtils.isEmpty(underScoreName)) {
            return null;
        }

        StringBuilder buf = new StringBuilder(underScoreName.length()); // accumulate result
        char[] chrs = underScoreName.toLowerCase().toCharArray(); // start with all lower case
        boolean cap = false;

        for (int i = 0; i < chrs.length; i++) {
            char chr = chrs[i];

            // Possible syllable markers - ignore, upper case next char, continue
            if (chr == '_' || chr == ' ' || chr == '-' || chr == '.') {
                cap = true;
                continue;
            }

            if (cap) {
                buf.append(Character.toUpperCase(chr));
                cap = false;
            } else {
                buf.append(chr);
            }

        }

        return buf.toString();
    }

    /**
     * Convert a string of the form "underScoreName" to "UNDER_SCORE_NAME".
     * @param camelCaseName The name to convert
     * @return The converted name
     */
    public static String underScoreCamelCaseName(String camelCaseName) {
        if (StringUtils.isEmpty(camelCaseName)) {
            return null;
        }

        StringBuilder buf = new StringBuilder(camelCaseName.length()); // accumulate result
        char[] chrs = camelCaseName.toCharArray(); // start with all lower case

        for (int i = 0; i < chrs.length; i++) {
            char chr = chrs[i];

            // Possible syllable markers - ignore, upper case next char, continue
            if (Character.isUpperCase(chr)) {
                buf.append('_');
            }

            buf.append(Character.toUpperCase(chr));
        }

        return buf.toString();
    }
}
