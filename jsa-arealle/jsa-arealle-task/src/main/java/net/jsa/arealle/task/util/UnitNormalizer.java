package net.jsa.arealle.task.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Normalize property unit designations.
 * @author jsaparo
 *
 */
@Component
public class UnitNormalizer implements INameNormalizer {

	public static final String EMPTY_UNIT_VALUE = "-";
	@Override
	public String normalize(String nameIn) {
		// U:1
		// #1
		
		if (StringUtils.isEmpty(nameIn)) {
			return EMPTY_UNIT_VALUE;
		}

		if (nameIn.startsWith("U:")) {
			return nameIn.substring(2);
		} else if (nameIn.startsWith("#")) {
			return nameIn.substring(1);
		} else {
			return EMPTY_UNIT_VALUE;
		}
	}

}
