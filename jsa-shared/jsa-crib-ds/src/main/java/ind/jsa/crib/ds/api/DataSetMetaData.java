package ind.jsa.crib.ds.api;

import ind.jsa.crib.ds.utils.NameUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Manages a collection of properties that define elements of items in a data set.
 * 
 * @author jsaparo
 *
 */
public class DataSetMetaData {
	private static final int INIT_PROPERTIES_SIZE = 20;
	
	private ITypeManager typeManager;
	
	List<IDataSetProperty> properties = new ArrayList<IDataSetProperty>(INIT_PROPERTIES_SIZE);
	private Map<String, IDataSetProperty> propertyMap = new LinkedHashMap<String, IDataSetProperty>(INIT_PROPERTIES_SIZE);
	private Map<String, Integer> indexMap = new HashMap<String, Integer>(INIT_PROPERTIES_SIZE);

	@Autowired
	public void setTypeManager(ITypeManager typeManager) {
		this.typeManager = typeManager;
	}
	
	/**
	 * Get the type registry associated with the data set.
	 * 
	 * @return A type registry instance
	 */
	public ITypeManager getTypeManager() {
		return typeManager;
	}

	/**
	 * Add a property to the data set.
	 * 
	 * @param prop A property
	 */
	protected void addProperty(IDataSetProperty prop) {
		properties.add(prop);
		String name = NameUtils.normalizePropertyName(prop.getName());
		propertyMap.put(name, prop);
		indexMap.put(name, indexMap.size());
	}

	/**
	 * Add the given collection of properties.
	 * 
	 * @param properties The properties to add
	 */
	public void addProperties(Collection<IDataSetProperty> properties) {
		for (IDataSetProperty prop : properties) {
			addProperty(prop);
		}
	}
	
	/**
	 * Remove all properties.
	 */
	public void clearProperties() {
		properties.clear();
		propertyMap.clear();
		indexMap.clear();
	}

    /**
     * Get a property associated with the DataSet by name.
     *
     * @param propName The name of the property to get
     * @return A property or null if not available
     */
	public IDataSetProperty getProperty(String propName) {
		return propertyMap.get(NameUtils.normalizePropertyName(propName));
	}

    /**
     * Get a property associated with the DataSet by ordinal index.
     *
     * @param idx The ordinal index of the property to get
     * @return A property or null if not available
     */
	public IDataSetProperty getProperty(int idx) {
		return idx >= 0 && idx < properties.size() ? properties.get(idx) : null;
	}

	/**
	 * Get a list of all properties.
	 * 
	 * @return A list of properties
	 */
	public List<IDataSetProperty> getProperties() {
		return new ArrayList<IDataSetProperty>(properties);
	}

	/**
	 * Get a map of all properties by name.
	 * 
	 * @return A collection of properties keyed by name
	 */
	public Map<String, IDataSetProperty> getPropertyiesByName() {
		return new LinkedHashMap<String, IDataSetProperty>(propertyMap);
	}

    /**
     * Get a property associated with the DataSet by ordinal index.
     *
     * @param idx The ordinal index of the property to get
     * @return A property or null if not available
     */
	public int getPropertyIndex(String propName) {
		String name = NameUtils.normalizePropertyName(propName);
		return indexMap.containsKey(name) ? indexMap.get(name) : -1;
	}

    /**
     * Get the name of the property for a given ordinal index.
     * 
     * @param idx The ordinal index of the property
     * @return A property name, or null if not available
     */
	public String getPropertyName(int idx) {
		IDataSetProperty prop = getProperty(idx);
		return prop != null ? NameUtils.normalizePropertyName(prop.getName()) : null;
	}
}
