package ind.jsa.crib.ds.internal;

import ind.jsa.crib.ds.api.IDataSetMetaData;
import ind.jsa.crib.ds.api.IDataSetProperty;
import ind.jsa.crib.ds.api.ITypeManager;
import ind.jsa.crib.ds.internal.utils.NameUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Manages a collection of properties that reflect the physical elements of items in a data set, and their natural order.
 * 
 * @author jsaparo
 *
 */
public class DataSetMetaData implements IDataSetMetaData {
	private static final int INIT_PROPERTIES_SIZE = 20;
	
	private ITypeManager typeManager;
	
	List<IDataSetProperty> properties = new ArrayList<IDataSetProperty>(INIT_PROPERTIES_SIZE);
	private Map<String, IDataSetProperty> propertyMap = new LinkedHashMap<String, IDataSetProperty>(INIT_PROPERTIES_SIZE);
	private Map<String, Integer> indexMap = new HashMap<String, Integer>(INIT_PROPERTIES_SIZE);

	@Autowired
	public void setTypeManager(ITypeManager typeManager) {
		this.typeManager = typeManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getTypeManager()
	 */
	@Override
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
		return propertyMap.get(NameUtils.normalizePropertyName(propName));
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
		String name = NameUtils.normalizePropertyName(propName);
		return indexMap.containsKey(name) ? indexMap.get(name) : -1;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetMetaData#getPropertyName(int)
	 */
	@Override
	public String getPropertyName(int idx) {
		IDataSetProperty prop = getProperty(idx);
		return prop != null ? NameUtils.normalizePropertyName(prop.getName()) : null;
	}
}
