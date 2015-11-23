package net.jsa.crib.ds.api;

/**
 * Mechanism for extending the logical type registry
 * via initialization.
 * 
 * @author jsaparo
 *
 */
public interface ILogicalTypeRegistryInitializer {
	/**
	 * Perform initialization
	 * @param registry Registry to initialize
	 */
	public void initialize(ILogicalTypeRegistry registry);
}
