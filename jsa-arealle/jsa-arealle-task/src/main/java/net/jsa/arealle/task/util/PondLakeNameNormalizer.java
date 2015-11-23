package net.jsa.arealle.task.util;

import org.springframework.stereotype.Component;

/**
 * Pond/lake name normalizer
 * @author jsaparo
 *
 */
@Component
public class PondLakeNameNormalizer extends BasicNameNormalizer  {
	
	public PondLakeNameNormalizer() {
		addDirectionalLookups();
		
		addLookup("is", "island");
		addLookup("isl", "island");
		addLookup("pk", "park");
		addLookup("pd", "pond");
		addLookup("pnd", "pond");
		addLookup("pt", "point");
		addLookup("bk", "brook");
		addLookup("brk", "brook");
	}
}
