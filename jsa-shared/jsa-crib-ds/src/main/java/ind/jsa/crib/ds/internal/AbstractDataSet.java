package ind.jsa.crib.ds.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetMetaData;
import ind.jsa.crib.ds.api.IDataSetProperty;
import ind.jsa.crib.ds.api.IDataSetResultHandler;
import ind.jsa.crib.ds.api.IKeyGenerator;
import ind.jsa.crib.ds.api.ITypeManager;
import ind.jsa.crib.ds.internal.type.DefaultTypeManager;
import net.jsa.common.logging.LogUtils;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

/**
 * Base class for data set implementations.
 */
public abstract class AbstractDataSet implements IDataSet {

	Logger logger = LogUtils.getLogger();
	
	private static final int DFT_PROPERTY_LIST_SIZE = 50;
	
	private IKeyGenerator keyGenerator;
	private IDataSetMetaData metaData;
	private String entity = null;
	private String domain = null;
	private ITypeManager typeManager;
	private Map<String, Object> defaultParamValues = new LinkedHashMap<String, Object>();
	private boolean caseInsensitiveSearch = false;
	private List<String> propertyOrder;
		
	// The following fields are derived on initialization.
	private String idPropertyName = null;
	private Set<String> orderedPropertyNames = new LinkedHashSet<String>(DFT_PROPERTY_LIST_SIZE);
	private Set<String> refIdPropertyNames = new LinkedHashSet<String>(DFT_PROPERTY_LIST_SIZE);
	private Set<String> sortablePropertyNames = new LinkedHashSet<String>(DFT_PROPERTY_LIST_SIZE);
	private Set<String> writablePropertyNames = new LinkedHashSet<String>(DFT_PROPERTY_LIST_SIZE);
	private Set<String> filterablePropertyNames = new LinkedHashSet<String>(DFT_PROPERTY_LIST_SIZE);
	private Map<String, Integer> propertyIndicesByName = new HashMap<String, Integer>();

	public AbstractDataSet(String entity, String domain) {
		this.entity = entity;
		this.domain = domain;
	}
	
	@Autowired
	public void setTypeManager(ITypeManager typeManager) {
		this.typeManager = typeManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getTypeManager()
	 */
	@Override
	public ITypeManager getTypeManager() {
		return typeManager;
	}
	
	/*
	 * Utility call for initializing dataset. Invoked from constructor.
	 * Separated for read-ability.
	 */
    @PostConstruct
	public void initialize() {
    	metaData = initMetaData();
    	
		// First establish overall property ordering
		establishPropertyOrdering();
		
		// Now set other collections based on established ordered properties
		idPropertyName = metaData.getIdPropertyName();
		refIdPropertyNames.addAll(orderPropertyNames(metaData.getIdRefPropertyNames(), false));
		writablePropertyNames.addAll(orderPropertyNames(metaData.getWritablePropertyNames(), true));						
		filterablePropertyNames.addAll(orderPropertyNames(metaData.getFilterablePropertyNames(), true));			
		sortablePropertyNames.addAll(orderPropertyNames(metaData.getSortablePropertyNames(), true));			
	}

    protected abstract IDataSetMetaData initMetaData();
    
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
	public IDataSetMetaData getMetaData() {
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
	public String getEntity() {
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getDomain()
	 */
	@Override
	public String getDomain() {
		return domain;
	}

	/**
	 * Specify the desired ordering of properties associated
	 * with the dataset. Given names should correspond to
	 * properties in the metadata. Those that don't will be
	 * ignored.
	 * 
	 * @param propertyOrder A list of property names, in the
	 * desired order.
	 */
	public void setPropertyOrder(List<String> propertyOrder) {
		this.propertyOrder = propertyOrder;
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
	 * @see ind.jsa.crib.ds.api.IDataSet#getWritableProperties()
	 */
	@Override
	public List<String> getWritablePropertyNames() {
		return new ArrayList<String>(writablePropertyNames);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getSortablePropertyNames()
	 */
	@Override
	public List<String> getSortablePropertyNames() {
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
	 * @see ind.jsa.crib.ds.api.IDataSet#getIdPropertyName()
	 */
	@Override
	public String getIdPropertyName() {
		return idPropertyName;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getIdentityProperties()
	 */
	@Override
	public List<String> getRefIdPropertyNames() {
		return new ArrayList<String>(refIdPropertyNames);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#isIdentityProperty(java.lang.String)
	 */
	@Override
    public boolean isIdProperty(String name) {
    	return idPropertyName != null && idPropertyName.equals(name);
    }

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#isReferenceProperty(java.lang.String)
     */
	@Override
    public boolean isIdRefProperty(String name) {
    	return refIdPropertyNames.contains(name);
    }
    
    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#isWritableProperty(java.lang.String)
     */
	@Override
    public boolean isWritableProperty(String name) {
    	return writablePropertyNames.contains(name);
    }
    
    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#isFilterableProperty(java.lang.String)
     */
	@Override
    public boolean isFilterableProperty(String name) {
    	return filterablePropertyNames.contains(name);
    }
    
    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#isSortableProperty(java.lang.String)
     */
	@Override
    public boolean isSortableProperty(String name) {
    	return sortablePropertyNames.contains(name);
    }

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getDefaultParameterValues()
	 */
	@Override
	public Map<String, Object> getDefaultParameterValues() {
		return new HashMap<String, Object> (defaultParamValues);
	}

	/**
	 * Indicate whether search logic should be case insensitive w/respect
	 * to string values. By default, searches are case-sensitive.
	 * 
	 * @param caseInsensitiveSearch An indicator
	 */
	public void setCaseInsensitiveSearch(boolean caseInsensitiveSearch) {
		this.caseInsensitiveSearch = caseInsensitiveSearch;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#isCaseInsensitiveSearch()
	 */
	@Override
	public boolean isCaseInsensitiveSearch() {
		return caseInsensitiveSearch;
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
	 * @see ind.jsa.crib.ds.api.IDataSet#blankItem()
	 */
	@Override
	public IDataSetItem blankItem() {
		return new DataSetItem(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#create(java.util.Map)
	 */
	@Override
    public IDataSetItem create(Map<String, Object> values) {
    	return create(new DataSetItem(this, values));
    }

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#update(ind.jsa.crib.ds.api.DataSetQuery, java.util.Map)
     */
    @Override
    public void update(DataSetQuery query, Map<String, Object> values) {
    	update(new DataSetItem(this, values));
    }

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#update(java.util.Map)
     */
    @Override
    public IDataSetItem update(Map<String, Object> values) {
    	return update(new DataSetItem(this, values));
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
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#isAtomicProperty(java.lang.String)
     */
    public boolean isAtomicProperty(String name) {
    	return propertyIsOfNature(name, DefaultTypeManager.ATOMIC_NATURE);
    }

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#isStringProperty(java.lang.String)
     */
    @Override
    public boolean isStringProperty(String name) {
    	return propertyIsOfNature(name, DefaultTypeManager.STRING_NATURE);
    }
    
    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#isNumericProperty(java.lang.String)
     */
    @Override
    public boolean isNumericProperty(String name) {
       	return propertyIsOfNature(name, DefaultTypeManager.NUMERIC_NATURE);
    }

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#isIntegerProperty(java.lang.String)
     */
    @Override
    public boolean isIntegerProperty(String name) {
       	return propertyIsOfNature(name, DefaultTypeManager.INTEGER_NATURE);
    }

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#isDecimalProperty(java.lang.String)
     */
    @Override
    public boolean isDecimalProperty(String name) {
       	return propertyIsOfNature(name, DefaultTypeManager.DECIMAL_NATURE);
    }
    
    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#isDateTimeProperty(java.lang.String)
     */
    @Override
    public boolean isDateTimeProperty(String name) {
       	return propertyIsOfNature(name, DefaultTypeManager.DATETIME_NATURE);
    }
    
    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#isBooleanProperty(java.lang.String)
     */
    @Override
    public boolean isBooleanProperty(String name) {
       	return propertyIsOfNature(name, DefaultTypeManager.BOOLEAN_NATURE);
    }

    /**
     * Get the currently set default parameter values.
     * 
     * @return A map of values
     */
	public Map<String, Object> getDefaultParamValues() {
		return new HashMap<String, Object>(defaultParamValues);
	}

    /**
     * Set the currently set default parameter values.
     * 
     * @param defaultParamValues A map of values
     */	
	public void setDefaultParamValues(Map<String, Object> defaultParamValues) {
		defaultParamValues.clear();
		defaultParamValues.putAll(defaultParamValues);
	}

    /**
     * Get the logger for the dataset.
     * 
     * @return A logger
     */
    protected Logger getLogger() {
    	return logger;
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
		if (!CollectionUtils.isEmpty(propertyOrder)) {
			for (String propName : propertyOrder) {
				prop = metaData.getProperty(propName);
				if (prop != null) {
					orderedPropertyNames.add(prop.getName());
				}
			}
		}

		// for all remaining, apply natural order
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
	private Collection<String> orderPropertyNames(Collection<String> names, boolean defaultAll) {
		int sz = !CollectionUtils.isEmpty(names) ? names.size() : 0;
		
		List<String> orderedNames = new ArrayList<String>(sz);
		
		if (sz != 0) {
			for (String propName : orderedPropertyNames) {
				if (names.contains(propName)) {
					orderedNames.add(propName);
				}
			}
		} else if (defaultAll) {
			orderedNames = getOrderedPropertyNames();
		}
		
		return orderedNames;
	}
	
	/*
	 * Determine whether the given property is of the given nature.
	 *  
	 * @param name The name of the property to check
	 * @param nature The nature to check
	 * 
	 * @return An indicator
	 */
	private boolean propertyIsOfNature(String name, long nature) {
        IDataSetProperty prop = getMetaData().getProperty(name);
        long propNature = getTypeManager().getTypeNature(prop.getType());
        
        return (propNature & nature) != 0;
 	}
}
