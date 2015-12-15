package ind.jsa.crib.ds.internal.type.plugins;

import ind.jsa.crib.ds.api.ITypeManager.ITypeManagerPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple typed collection class to maintain a set of type manager plugins.
 * 
 * @author jo26419
 *
 */
public class TypeManagerPluginCollection {
	private static final int PLUGIN_LIST_INIT_SIZE = 5;
	
	List<ITypeManagerPlugin> plugins = new ArrayList<ITypeManagerPlugin>(PLUGIN_LIST_INIT_SIZE);
	
	/**
	 * Set the plugins.
	 * 
	 * @param plugins
	 */
	public void setPlugins(List<ITypeManagerPlugin> plugins) {
		this.plugins.addAll(plugins);
	}
	
	/**
	 * Get the plugins.
	 * 
	 * @return
	 */
	public List<ITypeManagerPlugin> getPlugins() {
		return new ArrayList<ITypeManagerPlugin>(plugins);
	}
}
