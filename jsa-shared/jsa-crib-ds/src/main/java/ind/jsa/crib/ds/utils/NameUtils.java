package ind.jsa.crib.ds.utils;

import org.apache.commons.lang3.StringUtils;

public class NameUtils {

	/**
	 * Apply standardization to property names.
	 * 
	 * @param name Name to standardize
	 * @return A standardized name string
	 */
	public static String normalizePropertyName(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		
		return name.toLowerCase().trim();
	}
}
