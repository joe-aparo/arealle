package net.jsa.arealle.task.util;

/**
 * Common name normalization call.
 * @author jsaparo
 *
 */
public interface INameNormalizer {
	/**
	 * Normalize a given name.
	 * @param nameIn A given name
	 * @return A normalized version of the given name
	 */
	String normalize(String nameIn);
}
