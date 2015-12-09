package ind.jsa.crib.ds.internal;

import ind.jsa.crib.ds.api.IDataSetMetaData;
import ind.jsa.crib.ds.api.IDataSetProperty;
import ind.jsa.crib.ds.internal.utils.PropertyNameUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Manages a collection of properties that reflect the physical elements of items
 * in a data set, and their natural order.
 * 
 * @author jsaparo
 *
 */
public class DataSetMetaData implements IDataSetMetaData {
	private static final int INIT_PROPERTIES_SIZE = 20;
	
	List<IDataSetProperty> properties = 
		new ArrayList<IDataSetProperty>(INIT_PROPERTIES_SIZE);
	private Map<String, IDataSetProperty> propertyMap = 
		new LinkedHashMap<String, IDataSetProperty>(INIT_PROPERTIES_SIZE);
	private Map<String, Integer> indexMap = 
		new HashMap<String, Integer>(INIT_PROPERTIES_SIZE);

	/**
	 * Add a property to the data set.
	 * 
	 * @param prop A property
	 */
	public void addProperty(IDataSetProperty prop) {
		properties.add(prop);
		String name = PropertyNameUtils.normalizeName(prop.getName());
		propertyMap.put(name, prop);
		indexMap.put(name, indexMap.size());
	}

	/**
	 * Add the given collection of properties.
	 * 
	 * @param properties The properties to add
	 */
	public void setProperties(Collection<IDataSetProperty> properties) {
		clearProperties();
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

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getProperty(java.lang.String)
	 */
	@Override
	public IDataSetProperty getProperty(String propName) {
		return propertyMap.get(PropertyNameUtils.normalizeName(propName));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getProperty(int)
	 */
	@Override
	public IDataSetProperty getProperty(int idx) {
		return idx >= 0 && idx < properties.size() ? properties.get(idx) : null;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getProperties()
	 */
	@Override
	public List<IDataSetProperty> getProperties() {
		return new ArrayList<IDataSetProperty>(properties);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getPropertyiesByName()
	 */
	@Override
	public Map<String, IDataSetProperty> getPropertyiesByName() {
		return new LinkedHashMap<String, IDataSetProperty>(propertyMap);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getPropertyIndex(java.lang.String)
	 */
	@Override
	public int getPropertyIndex(String propName) {
		String name = PropertyNameUtils.normalizeName(propName);
		return indexMap.containsKey(name) ? indexMap.get(name) : -1;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getPropertyName(int)
	 */
	@Override
	public String getPropertyName(int idx) {
		IDataSetProperty prop = getProperty(idx);
		return prop != null ? PropertyNameUtils.normalizeName(prop.getName()) : null;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getIdentityPropertyNames()
	 */
	@Override
	public Collection<String> getIdentityPropertyNames() {
		return filterProperties((IDataSetProperty p) -> p.isIdentity());
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getReferencePropertyNames()
	 */
	@Override
	public Collection<String> getReferencePropertyNames() {
		return filterProperties((IDataSetProperty p) -> p.isReference());
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getWritablePropertyNames()
	 */
	@Override
	public Collection<String> getWritablePropertyNames() {
		return filterProperties((IDataSetProperty p) -> p.isWritable());
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getFilterablePropertyNames()
	 */
	@Override
	public Collection<String> getFilterablePropertyNames() {
		return filterProperties((IDataSetProperty p) -> p.isFilterable());
	}
	
	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getSortablePropertyNames()
	 */
	@Override
	public Collection<String> getSortablePropertyNames() {
		return filterProperties((IDataSetProperty p) -> p.isSortable());
	}
	
	/*
	 * Filter properties into a set of property names.
	 */
	public Collection<String> filterProperties(Function<IDataSetProperty, Boolean> test) {
		Set<String> result = new HashSet<String>(INIT_PROPERTIES_SIZE);
		
		for(IDataSetProperty p : properties) {
			if (test.apply(p)) {
				result.add(p.getName());
			}
		}
		return result;
	}
}
