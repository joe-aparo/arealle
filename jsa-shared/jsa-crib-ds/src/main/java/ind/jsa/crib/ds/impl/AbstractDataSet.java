package ind.jsa.crib.ds.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ind.jsa.crib.ds.api.DataSetMetaData;
import ind.jsa.crib.ds.api.DataSetOptions;
import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetProperty;
import ind.jsa.crib.ds.api.IDataSetResultHandler;
import ind.jsa.crib.ds.api.IKeyGenerator;

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
	// constructor. Initializing these variables in construction simplifies related logic and is more efficient
	// than repeatedly executing the initializing logic to get the same info.
	private List<String> orderedPropertyNames = new ArrayList<String>(DFT_PROPERTY_LIST_SIZE);
	private List<String> identityPropertyNames = new ArrayList<String>(DFT_PROPERTY_LIST_SIZE);
	private List<String> referencePropertyNames = new ArrayList<String>(DFT_PROPERTY_LIST_SIZE);
	private List<String> readablePropertyNames = new ArrayList<String>(DFT_PROPERTY_LIST_SIZE);
	private List<String> writablePropertyNames = new ArrayList<String>(DFT_PROPERTY_LIST_SIZE);
	private List<String> filterablePropertyNames = new ArrayList<String>(DFT_PROPERTY_LIST_SIZE);
	private Map<String, Integer> propertyIndicesByName = new HashMap<String, Integer>();

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
	 * @see ind.jsa.crib.ds.api.IDataSet#getMetaData()
	 */
	public DataSetMetaData getMetaData() {
		return metaData;
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
	public List<String> getOrderedPropertyNames() {
		return new ArrayList<String>(orderedPropertyNames);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getReadableProperties()
	 */
	@Override
	public List<String> getReadablePropertyNames() {
		return new ArrayList<String>(readablePropertyNames);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getWritableProperties()
	 */
	@Override
	public List<String> getWritablePropertyNames() {
		return new ArrayList<String>(writablePropertyNames);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getFilterableProperties()
	 */
	@Override
	public List<String> getFilterablePropertyNames() {
		return new ArrayList<String>(filterablePropertyNames);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getIdentityProperties()
	 */
	@Override
	public List<String> getIdentityPropertyNames() {
		return new ArrayList<String>(identityPropertyNames);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getIdentityProperties()
	 */
	@Override
	public List<String> getReferencePropertyNames() {
		return new ArrayList<String>(referencePropertyNames);
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
			identityPropertyNames.addAll(orderPropertyNames(options.getIdentityProperties()));
		}
		
		if (options != null && !CollectionUtils.isEmpty(options.getReferenceProperties())) {
			referencePropertyNames.addAll(orderPropertyNames(options.getReferenceProperties()));
		}
		
		if (options != null && !CollectionUtils.isEmpty(options.getReadableProperties())) {
			readablePropertyNames.addAll(orderPropertyNames(options.getReadableProperties()));			
		} else {
			// Assume all properties are readable
			readablePropertyNames.addAll(orderedPropertyNames);
		}

		if (options != null && !CollectionUtils.isEmpty(options.getWritableProperties())) {
			writablePropertyNames.addAll(orderPropertyNames(options.getWritableProperties()));						
		} else {
			// Assume all properties are writable
			writablePropertyNames.addAll(orderedPropertyNames);
		}

		if (options != null && !CollectionUtils.isEmpty(options.getFilterableProperties())) {
			filterablePropertyNames.addAll(orderPropertyNames(options.getFilterableProperties()));			
		} else {
			// Assume all properties are filterable
			filterablePropertyNames.addAll(orderedPropertyNames);
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
		
		// use specified order to the extent possible
		if (options != null && !CollectionUtils.isEmpty(options.getPropertyOrdering())) {
			for (String propName : options.getPropertyOrdering()) {
				prop = metaData.getProperty(propName);
				if (prop != null) {
					orderedPropertyNames.add(prop.getName());
				}
			}
		}

		// apply readable order to the extent possible
		if (options != null && !CollectionUtils.isEmpty(options.getReadableProperties())) {
			for (String propName : options.getReadableProperties()) {
				prop = metaData.getProperty(propName);
				if (prop != null && !propertyIndicesByName.containsKey(prop.getName())) {
					orderedPropertyNames.add(prop.getName());
				}
			}
		}

		// apply writable order to the extent possible
		if (options != null && !CollectionUtils.isEmpty(options.getWritableProperties())) {
			for (String propName : options.getWritableProperties()) {
				prop = metaData.getProperty(propName);
				if (prop != null && !propertyIndicesByName.containsKey(prop.getName())) {
					orderedPropertyNames.add(prop.getName());
				}
			}
		}

		// for all remaining, apply natural order to the extent possible
		for (IDataSetProperty p : metaData.getProperties()) {
			if (!propertyIndicesByName.containsKey(p.getName())) {
				orderedPropertyNames.add(p.getName());
			}
		}
	}
	
	/*
	 * For a given collection of property names, return a list of those
	 * names ordered in the currently established order for the dataset.
	 */
	private List<String> orderPropertyNames(Set<String> nameSet) {
		int sz = !CollectionUtils.isEmpty(nameSet) ? nameSet.size() : 0;
		
		List<String> orderedNames = new ArrayList<String>(sz);
		
		if (sz != 0) {
			for (String propName : orderedPropertyNames) {
				if (nameSet.contains(propName)) {
					orderedNames.add(propName);
				}
			}
		}
		
		return orderedNames;
	}
}
