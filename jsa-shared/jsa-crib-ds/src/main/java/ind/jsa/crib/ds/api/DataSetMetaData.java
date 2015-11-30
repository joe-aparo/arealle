package ind.jsa.crib.ds.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Manages a collection of properties that define elements of items in a data set. Also
 * contains a reference to a type manager for ultimately managing item values.
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
		propertyMap.put(prop.getName(), prop);
		indexMap.put(prop.getName(), indexMap.size());
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
	}

    /**
     * Get a property associated with the DataSet by name.
     *
     * @param name The name of the property to get
     * @return A property or null if not available
     */
	public IDataSetProperty getProperty(String propname) {
		return propertyMap.get(propname);
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
		return filterProperties((IDataSetProperty p) -> true);
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
	public int getPropertyIndex(String propname) {
		return indexMap.containsKey(propname) ? indexMap.get(propname) : -1;
	}

    /**
     * Get the name of the property for a given ordinal index.
     * 
     * @param idx The ordinal index of the property
     * @return A property name, or null if not available
     */
	public String getPropertyName(int idx) {
		IDataSetProperty prop = getProperty(idx);
		return prop != null ? prop.getName() : null;
	}

    /**
     * Get the collection of readable properties for the DataSet.
     * 
     * @return A list of properties
     */
	public List<IDataSetProperty> getReadableProperties() {
		return filterProperties((IDataSetProperty p) -> p.isInstanceId());
	}

    /**
     * Get the collection of writable properties for the DataSet.
     * 
     * @return A list of properties
     */
	public List<IDataSetProperty> getWritableProperties() {
		return filterProperties((IDataSetProperty p) -> p.isWritable());
	}

    /**
     * Get the collection of filterable properties for the DataSet.
     * 
     * @return A list of properties
     */
	public List<IDataSetProperty> getFilterableProperties() {
		return filterProperties((IDataSetProperty p) -> p.isFilterable());
	}

	/**
	 * Get the collection of instance identifier properties.
	 * @return
	 */
	public List<IDataSetProperty> getInstanceIdProperties() {
		return filterProperties((IDataSetProperty p) -> p.isInstanceId());		
	}
	
	/**
	 * Get the collection of reference identifier properties.
	 * @return
	 */
	public List<IDataSetProperty> getReferenceIdProperties() {
		return filterProperties((IDataSetProperty p) -> p.isReferenceId());		
	}
	
	/*
	 * General function for filtering properties.
	 */
	private List<IDataSetProperty> filterProperties(Predicate<IDataSetProperty> func) {
		List<IDataSetProperty> props = new ArrayList<IDataSetProperty>(propertyMap.size());
		for (IDataSetProperty prop : properties) {
			if (func.test(prop)) {
				props.add(prop);
			}
		}
		
		return props;
	}
}
