package net.jsa.arealle.task.util;

import org.springframework.stereotype.Component;

/**
 * Normalize town names.
 * 
 * @author jsaparo
 *
 */
@Component
public class TownNameNormalizer extends BasicNameNormalizer {
	public TownNameNormalizer() {
		addDirectionalLookups();
	}
}
