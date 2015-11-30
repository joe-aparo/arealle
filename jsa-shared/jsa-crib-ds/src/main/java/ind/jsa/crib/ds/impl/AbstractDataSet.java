package ind.jsa.crib.ds.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ind.jsa.crib.ds.api.DataSetMetaData;
import ind.jsa.crib.ds.api.DataSetOptions;
import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetProperty;
import ind.jsa.crib.ds.api.IDataSetResultHandler;
import ind.jsa.crib.ds.api.IKeyGenerator;
import ind.jsa.crib.ds.utils.NameUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

/**
 * Base class for data set implementations.
 */
public abstract class AbstractDataSet implements IDataSet {

	private static final int DFT_PROPERTY_LIST_SIZE = 50;
	
	private IKeyGenerator keyGenerator;
	private DataSetMetaData metaData;
	private String name = null;
	private DataSetOptions options;
	
	// The following support variables are derived by combining the metadata and options specified in the
	// constructor. Initializing these variables in construction simplifies related logic and is more efficient.
	private List<IDataSetProperty> orderedProperties = new ArrayList<IDataSetProperty>(DFT_PROPERTY_LIST_SIZE);
	private List<IDataSetProperty> identityProperties = new ArrayList<IDataSetProperty>(DFT_PROPERTY_LIST_SIZE);
	private List<IDataSetProperty> referenceProperties = new ArrayList<IDataSetProperty>(DFT_PROPERTY_LIST_SIZE);
	private List<IDataSetProperty> readableProperties = new ArrayList<IDataSetProperty>(DFT_PROPERTY_LIST_SIZE);
	private List<IDataSetProperty> writableProperties = new ArrayList<IDataSetProperty>(DFT_PROPERTY_LIST_SIZE);
	private List<IDataSetProperty> filterableProperties = new ArrayList<IDataSetProperty>(DFT_PROPERTY_LIST_SIZE);
	private Map<String, Integer> propertyIndicesByName = new HashMap<String, Integer>();
	private String[] propertyNamesByIndex;

	public AbstractDataSet(String name, DataSetMetaData metaData, DataSetOptions options) {
		this.name = name;
		this.metaData = metaData;
		this.options = options;
		
		initDataSet();
	}
	
	/**
	 * Set the key generator associated with data set.
	 * 
	 * @param keyGenerator A key generator
	 */
	@Autowired
	public void setKeyGenerator(IKeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getKeyGenerator()
	 */
	@Override
	public IKeyGenerator getKeyGenerator() {
		return keyGenerator;
	}
	
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
	 * @see ind.jsa.crib.ds.api.IDataSet#getProperties()
	 */
	@Override
	public List<IDataSetProperty> getProperties() {
		return new ArrayList<IDataSetProperty>(orderedProperties);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getReadableProperties()
	 */
	@Override
	public List<IDataSetProperty> getReadableProperties() {
		return new ArrayList<IDataSetProperty>(readableProperties);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getWritableProperties()
	 */
	@Override
	public List<IDataSetProperty> getWritableProperties() {
		return new ArrayList<IDataSetProperty>(writableProperties);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getFilterableProperties()
	 */
	@Override
	public List<IDataSetProperty> getFilterableProperties() {
		return new ArrayList<IDataSetProperty>(filterableProperties);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getIdentityProperties()
	 */
	@Override
	public List<IDataSetProperty> getIdentityProperties() {
		return new ArrayList<IDataSetProperty>(identityProperties);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getDefaultParameterValues()
	 */
	@Override
	public Map<String, Object> getDefaultParameterValues() {
		return options != null ? options.getDefaultParamValues() : null;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#isCaseInsensitiveSearch()
	 */
	@Override
	public boolean isCaseInsensitiveSearch() {
		return options != null ? options.isCaseInsensitiveSearch() : false;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getItemCount()
	 */
	@Override
	public int getItemCount() {
		return getItemCount(new DataSetQuery());
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#retrieve()
	 */
	@Override
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

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#retrieve(ind.jsa.crib.ds.api.DataSetQuery)
     */
    @Override
	public List<IDataSetItem> retrieve(DataSetQuery query) {
		ListDataSetResultHandler handler = new ListDataSetResultHandler(this, query);

		retrieve(query, handler);

		return handler.getItems();
	}

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#retrieve(int)
     */
    @Override
	public IDataSetItem retrieve(int index) {
		DataSetQuery query = new DataSetQuery();
		query.setStartRow(index);
		query.setMaxRows(1);

		ListDataSetResultHandler handler = new ListDataSetResultHandler(this,
				query);
		retrieve(query, handler);

		return handler.getItems().size() > 0 ? handler.getItems().get(0) : null;
	}

	/*
	 * Utility call for initializing dataset. Invoked from constructor.
	 * Separated for read-ability.
	 */
	private void initDataSet() {
		// First establish overall property ordering
		establishPropertyOrdering();
		
		// Now set other collections based on established ordered properties
		
		if (options != null && !CollectionUtils.isEmpty(options.getIdentityProperties())) {
			identityProperties.addAll(orderProperties(options.getIdentityProperties()));
		}
		
		if (options != null && !CollectionUtils.isEmpty(options.getReferenceProperties())) {
			referenceProperties.addAll(orderProperties(options.getReferenceProperties()));
		}
		
		if (options != null && !CollectionUtils.isEmpty(options.getReadableProperties())) {
			readableProperties.addAll(orderProperties(options.getReadableProperties()));			
		} else {
			// Assume all properties are readable
			readableProperties.addAll(orderedProperties);
		}

		if (options != null && !CollectionUtils.isEmpty(options.getWritableProperties())) {
			writableProperties.addAll(orderProperties(options.getWritableProperties()));						
		} else {
			// Assume all properties are writable
			writableProperties.addAll(orderedProperties);
		}

		if (options != null && !CollectionUtils.isEmpty(options.getFilterableProperties())) {
			filterableProperties.addAll(orderProperties(options.getFilterableProperties()));			
		} else {
			// Assume all properties are writable
			filterableProperties.addAll(orderedProperties);
		}
	}
	
	/*
	 * Initialize property ordering for the dataset given the current state
	 * of the propertyOrdering field. All returned property names are ordered
	 * according to this central ordering scheme. Order is first determined by
	 * explicit property names. Properties not explicitly ordered will be done
	 * so according to the order they were added to the dataset with a preference
	 * to the order in which readable & writable properties are specified.
	 */
	private void establishPropertyOrdering() {

		IDataSetProperty prop;
		int idx = 0;
		
		// use specified order to the extent possible
		if (options != null && !CollectionUtils.isEmpty(options.getPropertyOrdering())) {
			for (String propName : options.getPropertyOrdering()) {
				prop = metaData.getProperty(propName);
				if (prop != null) {
					addOrderedProperty(prop, idx++);
				}
			}
		}

		// apply readable order to the extent possible
		if (options != null && !CollectionUtils.isEmpty(options.getReadableProperties())) {
			for (String propName : options.getReadableProperties()) {
				prop = metaData.getProperty(propName);
				if (prop != null && !propertyIndicesByName.containsKey(prop.getName())) {
					addOrderedProperty(prop, idx++);
				}
			}
		}

		// apply writable order to the extent possible
		if (options != null && !CollectionUtils.isEmpty(options.getWritableProperties())) {
			for (String propName : options.getWritableProperties()) {
				prop = metaData.getProperty(propName);
				if (prop != null && !propertyIndicesByName.containsKey(prop.getName())) {
					addOrderedProperty(prop, idx++);
				}
			}
		}

		// for all remaining, apply natural order to the extent possible
		for (IDataSetProperty p : metaData.getProperties()) {
			if (!propertyIndicesByName.containsKey(p.getName())) {
				addOrderedProperty(p, idx++);
			}
		}
	}
	
	/*
	 * Register an ordered property.
	 * 
	 * @param prop The property to register
	 * @param idx The sequential index of the property
	 */
	private void addOrderedProperty(IDataSetProperty prop, int idx) {
		orderedProperties.add(prop);
		propertyNamesByIndex[idx] = prop.getName();
		propertyIndicesByName.put(prop.getName(), idx++);
		
	}
	
	/*
	 * For a given collection of property names, return a list of those
	 * names ordered in the currently established order for the dataset.
	 */
	private List<IDataSetProperty> orderProperties(Collection<String> names) {
		int sz = !CollectionUtils.isEmpty(names) ? names.size() : 0;
		
		List<IDataSetProperty> orderedProperties = new ArrayList<IDataSetProperty>(sz);
		
		if (sz != 0) {
			TreeMap<Integer, IDataSetProperty> orderedMap = new TreeMap<Integer, IDataSetProperty>();
			
			for (String name : names) {
				String propName = NameUtils.normalizePropertyName(name);
				Integer idx = propertyIndicesByName.get(propName);
				if (idx != null) {
					orderedMap.put(idx, metaData.getProperty(propName));
				}
			}
			
			orderedProperties.addAll(orderedMap.values());
		}
		
		return orderedProperties;
	}
}
