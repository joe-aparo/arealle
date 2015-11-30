package ind.jsa.crib.ds.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetMetaData;
import ind.jsa.crib.ds.api.IDataSetProperty;
import ind.jsa.crib.ds.api.IDataSetResultHandler;
import ind.jsa.crib.ds.api.IKeyGenerator;
import ind.jsa.crib.ds.api.ITypeManager;

import org.apache.commons.lang3.StringUtils;

/**
 * Base class for data set implementations.
 */
public abstract class AbstractDataSet implements IDataSet {

	/**
	 * Foreign keys are assumed to be suffixed with this string by default.
	 */
	protected static final String FOREIGN_KEY_SUFFIX = "_ID";

	private IDataSetMetaData metaData;
	private String name = null;
	
	private Set<String> keys;
	private Set<String> foreignKeys;
	private String filterProperties = null; // raw comma-delimited filterable property names
	private String propertyOrder = null; // raw comma-delimited list of property names
	private Map<String, Object> defaultParamValues = new LinkedHashMap<String, Object>();
	private Map<String, DataSetProperty> propertiesByName = new LinkedHashMap<String, DataSetProperty>();
	private Set<String> readablePropertyNames = new LinkedHashSet<String>();
	private Set<String> writablePropertyNames = new LinkedHashSet<String>();
	private Set<String> filterablePropertyNames = new LinkedHashSet<String>();
	private Set<String> propertyOrdering = new LinkedHashSet<String>();
	private Map<String, Integer> propertyIndicesByName = new HashMap<String, Integer>();
	private DataSetProperty[] propertiesByIndex; // ordinal-to-name map
	private IKeyGenerator keyGenerator;
	private boolean caseSensitiveSearch;
	private ITypeManager typeManager;

	/**
	 * Get the type registry associated with the data set.
	 * 
	 * @return A type registry instance
	 */
	public IDataSetMetaData getMetaData() {
		return metaData;
	}

	/*
	 * Spring injection / initialization methods follow
	 */

	/**
	 * Set the unique identifying name of the dataset.
	 * 
	 * @param name
	 *            A logical, unique name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Indicate the desired order of properties obtained from a dataset.
	 * 
	 * @param order
	 *            A comma-delimited list of property ids
	 */
	public void setPropertyOrder(String order) {
		this.propertyOrder = order;
	}

	/**
	 * Set the comma-delimited string of names of the properties that may be
	 * filtered on.
	 * 
	 * @param names
	 *            A comma delimited string of property names
	 */
	public void setFilterProperties(String names) {
		this.filterProperties = names;
	}

	/**
	 * Get the comma-delimited string of names of the properties that may be
	 * filtered on.
	 * 
	 * @return A comma delimited string of property names
	 */
	public String getFilterProperties() {
		return filterProperties;
	}

	/**
	 * Indicates whether the DataSet should implement case sensitive filtering.
	 * 
	 * @param caseSensitiveSearch
	 *            An indicator
	 */
	public void setCaseSensitiveSearch(boolean caseSensitiveSearch) {
		this.caseSensitiveSearch = caseSensitiveSearch;
	}

	/**
	 * Get a map of properties by name.
	 * 
	 * @return A collection of properties keyed by name
	 */
	public Map<String, DataSetProperty> getPropertiesByName() {
		return propertiesByName;
	}

	/**
	 * Get current case sensitive search setting.
	 * 
	 * @return An indicator
	 */
	public boolean getCaseSensitiveSearch() {
		return caseSensitiveSearch;
	}

	/**
	 * Initialize the dataset by loading metadata for readable and writable
	 * properties, and setting up property ordering.
	 */
	public void initialize() {
		establishWritableProperties();
		establishReadableProperties();
		establishFilterableProperties();
		establishPropertyOrdering();
	}

	/*
	 * Public DataSet interface methods follow
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ds.DataSet#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ds.DataSet#getKeys()
	 */
	@Override
	public Set<String> getKeys() {
		return keys != null ? keys : getDefaultKeys();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ds.DataSet#getProperties()
	 */
	@Override
	public List<IDataSetProperty> getProperties() {
		List<IDataSetProperty> props = new ArrayList<IDataSetProperty>(
				propertiesByName.size());

		for (String propname : propertyOrdering) {
			props.add(propertiesByName.get(propname));
		}

		return props;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ds.DataSet#getPropertyNames()
	 */
	@Override
	public Set<String> getPropertyNames() {
		Set<String> names = new LinkedHashSet<String>(propertyOrdering.size());

		for (String propname : propertyOrdering) {
			names.add(propname);
		}

		return names;
	}

	/**
	 * Add a property to the data set.
	 * 
	 * @param prop
	 *            A property
	 */
	protected void addProperty(DataSetProperty prop) {
		propertiesByName.put(prop.getName(), prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ds.DataSet#getProperty(java.lang.String)
	 */
	@Override
	public DataSetProperty getProperty(String propname) {
		return propertiesByName.get(propname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ds.DataSet#getPropertyIndex(java.lang.String)
	 */
	@Override
	public int getPropertyIndex(String propname) {
		return propertyIndicesByName.containsKey(propname) ? propertyIndicesByName
				.get(propname) : -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ds.DataSet#getReadablePropertyNames()
	 */
	@Override
	public Set<String> getReadablePropertyNames() {
		return orderPropertyNames(readablePropertyNames);
	}

	/**
	 * Identify a specified property as readable.
	 * 
	 * @param propName
	 *            The property to consider readable
	 */
	protected void addReadableProperty(String propName) {
		readablePropertyNames.add(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsa.crib.ds.api.IDataSet#isReadableProperty(java.lang.String)
	 */
	@Override
	public boolean isReadableProperty(String propName) {
		return readablePropertyNames.contains(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ds.DataSet#getWritablePropertyNames()
	 */
	@Override
	public Set<String> getWritablePropertyNames() {
		return orderPropertyNames(writablePropertyNames);
	}

	/**
	 * Identify a specified property as writable.
	 * 
	 * @param propName
	 *            The property to consider writable
	 */
	protected void addWritableProperty(String propName) {
		writablePropertyNames.add(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsa.crib.ds.api.IDataSet#isWritableProperty(java.lang.String)
	 */
	@Override
	public boolean isWritableProperty(String propName) {
		return writablePropertyNames.contains(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ds.DataSet#getFilterablePropertyNames()
	 */
	@Override
	public Set<String> getFilterablePropertyNames() {
		return orderPropertyNames(filterablePropertyNames);
	}

	/**
	 * Set the collection of filterable property names.
	 * 
	 * @param names
	 *            A collection of property names
	 */
	public void setFilterablePropertyNames(Set<String> names) {
		filterablePropertyNames = names;
	}

	/**
	 * Identify a specified property as filterable.
	 * 
	 * @param propName
	 *            The property to consider filterable
	 */
	protected void addFilterableProperty(String propName) {
		filterablePropertyNames.add(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jsa.crib.ds.api.IDataSet#isFilterableProperty(java.lang.String)
	 */
	@Override
	public boolean isFilterableProperty(String propName) {
		return filterablePropertyNames.contains(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ds.DataSet#getProperty(int)
	 */
	@Override
	public DataSetProperty getProperty(int idx) {
		return idx >= 0 && idx < propertiesByIndex.length ? propertiesByIndex[idx]
				: null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.copyright.ds.DataSet#getPropertyName(int)
	 */
	@Override
	public String getPropertyName(int idx) {
		DataSetProperty prop = getProperty(idx);
		return prop != null ? prop.getName() : null;
	}

	/**
	 * Set the key generator for with the DataSet. If set, the generator will be
	 * invoked once per registered key, each time passing the key name and the
	 * DataSetName to the generateKeyValue method of the generator.
	 * 
	 * @param keyGenerator
	 *            The key generator to use
	 */
	public void setKeyGenerator(IKeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	/**
	 * Get the currently set key generator.
	 * 
	 * @return A key generator instance
	 */
	public IKeyGenerator getKeyGenerator() {
		return keyGenerator;
	}

	/**
	 * Specify the set of identifiers representing the properties that are
	 * considered primary keys for the dataset.
	 * 
	 * @param keyIds
	 *            The specified property identifiers
	 */
	public void setKeys(Set<String> keyIds) {
		if (keyIds == null) {
			this.keys = null;
		} else {
			this.keys = new LinkedHashSet<String>(keyIds.size());

			for (String k : keyIds) {
				String[] vals = StringUtils.split(k, ","); // in case multivalue
															// string
				for (String x : vals) {
					this.keys.add(x.toUpperCase().trim());
				}
			}
		}
	}

	/**
	 * Specify the set of identifiers representing the properties that are
	 * considered to be foreign keys to other entities.
	 * 
	 * @param keyIds
	 *            The specified property identifiers
	 */
	public void setForeignKeys(Set<String> keyIds) {
		if (keyIds == null) {
			this.foreignKeys = null;
		} else {
			this.foreignKeys = new LinkedHashSet<String>(keyIds.size());

			for (String k : keyIds) {
				this.foreignKeys.add(k.toUpperCase());
			}
		}
	}

	/**
	 * Set the default value for the specified property.
	 * 
	 * @param propname
	 *            Name of the property
	 * @param value
	 *            The default value for the property
	 */
	public void setDefaultParameterValue(String propname, Object value) {
		if (!StringUtils.isNotEmpty(propname) || value == null) {
			return;
		}

		if (defaultParamValues == null) {
			defaultParamValues = new HashMap<String, Object>();
		}

		defaultParamValues.put(propname, value);
	}

	/**
	 * Get the currently set default parameter values for the dataset.
	 * 
	 * @return The map of default parameters
	 */
	public Map<String, Object> getDefaultParameterValues() {
		Map<String, Object> params = new HashMap<String, Object>();

		if (defaultParamValues != null && defaultParamValues.size() > 0) {
			defaultParamValues.putAll(params);
		}

		return params;
	}

	/**
	 * Set the default parameters for this dataset.
	 * 
	 * @param defaultParams
	 *            Default parameter values
	 */
	public void setDefaultParameterValues(Map<String, Object> defaultParams) {
		if (defaultParams != null && defaultParams.size() > 0) {
			defaultParamValues = new HashMap<String, Object>();
			defaultParams.putAll(defaultParamValues);
		} else {
			defaultParamValues = null;
		}
	}

	/**
	 * Get the default key values for this dataset.
	 * 
	 * @return The set of default keys
	 */
	protected Set<String> getDefaultKeys() {
		Set<String> keyIds = new LinkedHashSet<String>();

		// if no keys have been explicitly specified and the dataset appears
		// to have the default key field, assume that is the key
		if (writablePropertyNames.contains(IDataSet.DEFAULT_ID_PROPERTY)) {
			keyIds.add(IDataSet.DEFAULT_ID_PROPERTY);
		}

		return keyIds;
	}

	/**
	 * Get the default foreign keys for the dataset.
	 * 
	 * @return The set of foreign keys
	 */
	protected Set<String> getDefaultForeignKeys() {
		Set<String> keyIds = new LinkedHashSet<String>();

		for (String k : writablePropertyNames) {
			if (k.endsWith(FOREIGN_KEY_SUFFIX)) {
				keyIds.add(k);
			}
		}

		return keyIds;
	}

	/**
	 * Load the writable properties based on the table currently associated with
	 * the dataset.
	 */
	protected abstract void establishWritableProperties();

	/**
	 * Initialize the readable properties for the DataSet. Readable properties
	 * are those corresponding to the retrieve command if specified. Otherwise
	 * they are considered to be the same as the currently set writable
	 * properties.
	 */
	protected abstract void establishReadableProperties();

	/**
	 * Initialize the filterable properties for the DataSet. Unless specifically
	 * configured, filterable properties are assumed to be the same as the
	 * currently set filterable properties.
	 */
	protected abstract void establishFilterableProperties();

	/**
	 * Initialize property ordering for the dataset. All properties are ordered.
	 * Order is first determined by specific configuration, followed by the
	 * ordering implied by the retrieval command, followed by the ordering
	 * implied by the update command.
	 */
	protected void establishPropertyOrdering() {
		// establish property ordering
		propertyOrdering.clear();

		// use specified order to the extent possible
		if (propertyOrder != null) {
			String[] names = propertyOrder.split(",");
			for (String propname : names) {
				propertyOrdering.add(propname.trim());
			}
		}

		// apply readable order to the extent possible
		for (String propname : readablePropertyNames) {
			if (!propertyOrdering.contains(propname)) {
				propertyOrdering.add(propname);
			}
		}

		// for all remaining, apply natural table order
		for (String propname : writablePropertyNames) {
			if (!propertyOrdering.contains(propname)) {
				propertyOrdering.add(propname);
			}
		}

		// Accumulate final ordering in collections accessible by index or name
		int idx = 0;
		propertiesByIndex = new DataSetProperty[propertyOrdering.size()];
		for (String propname : propertyOrdering) {
			propertiesByIndex[idx] = propertiesByName.get(propname);
			propertyIndicesByName.put(propname, idx++);
		}
	}

	/**
	 * Get the total number of items defined by the data set.
	 * 
	 * @return A count
	 */
	public int getItemCount() {
		return getItemCount(new DataSetQuery());
	}

	/**
	 * Retrieve all items from a data set.
	 * 
	 * @return A list of items
	 */
	public List<IDataSetItem> retrieve() {
		DataSetQuery query = new DataSetQuery();
		
		ListDataSetResultHandler handler = new ListDataSetResultHandler(this, query);

		retrieve(query, handler);

		return handler.getItems();
	}

    /*
     * (non-Javadoc)
     * 
     * @see com.copyright.ds.DataSet#retrieve(com.copyright.ds.DataSetResultHandler)
     */
    @Override
    public void retrieve(IDataSetResultHandler handler) {
        retrieve(new DataSetQuery(), handler); // blank query - retrieve all
    }

    /**
	 * Retrieve all items from a data set.
	 * 
	 * @return A list of items
	 */
	public List<IDataSetItem> retrieve(DataSetQuery query) {
		ListDataSetResultHandler handler = new ListDataSetResultHandler(this, query);

		retrieve(query, handler);

		return handler.getItems();
	}

	/**
	 * Retrieve the n-th item from the data set.
	 * 
	 * @return A single item
	 * @param index
	 *            The 1-based index of the item to retrieve
	 */
	public IDataSetItem retrieve(int index) {
		DataSetQuery query = new DataSetQuery();
		query.setStartRow(index);
		query.setMaxRows(1);

		ListDataSetResultHandler handler = new ListDataSetResultHandler(this,
				query);
		retrieve(query, handler);

		return handler.getItems().size() > 0 ? handler.getItems().get(0) : null;
	}

	/**
	 * Utility method for converting a given a set of property names to a new
	 * set ordered according to the currently established property ordering.
	 * 
	 * @param setToOrder
	 *            The set to order
	 * @return A new ordered set
	 */
	private Set<String> orderPropertyNames(Set<String> setToOrder) {
		Set<String> names = new LinkedHashSet<String>(setToOrder.size());
		for (String propName : propertyOrdering) {
			if (setToOrder.contains(propName)) {
				names.add(propName);
			}
		}

		return names;
	}
}
