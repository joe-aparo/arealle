package ind.jsa.crib.ds.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetMetaData;
import ind.jsa.crib.ds.api.IDataSetProperty;
import ind.jsa.crib.ds.api.IDataSetResultHandler;
import ind.jsa.crib.ds.api.IKeyGenerator;
import ind.jsa.crib.ds.api.DataSetQuery.FilterExpression;
import ind.jsa.crib.ds.api.DataSetQuery.SortDirection;
import ind.jsa.crib.ds.api.DataSetQuery.FilterOperator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;


/**
 * This DataSet implementation manages a items in memory according to the
 * metadata and options specified in the constructor.
 * 
 */
public class MemoryDataSet extends AbstractDataSet {

    private static final int DEFAULT_INIT_SIZE = 100;
    private static final int STARTING_ID = 100;
    private static final String MEMORY_DOMAIN = "memory";
    
    private IDataSetMetaData metaData;
    private List<DataSetItem> cachedItems = new ArrayList<DataSetItem>(DEFAULT_INIT_SIZE);
    private List<Map<String, Object>> initialItems = null;

    /**
     * Empty data set, without options.
     */
    public MemoryDataSet(String name, DataSetMetaData metaData) {
    	this(name, metaData, null);
    }

    /**
     * Filled data set, with options.
     */
    public MemoryDataSet(
    	String entity, DataSetMetaData metaData, List<Map<String, Object>> initialItems) {
    	super(MEMORY_DOMAIN, entity);
    	
    	this.metaData = metaData;
    	this.initialItems = initialItems;
    }
    
    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.internal.AbstractDataSet#initialize()
     */
    @Override
    public void initialize() {
    	super.initialize();

        if (!CollectionUtils.isEmpty(initialItems)) {
	        for (Map<String, Object> values : initialItems) {
	            create(values);
	        }
        }   	
    }

    /*
     * ](non-Javadoc)
     * @see ind.jsa.crib.ds.internal.AbstractDataSet#initMetaData()
     */
    @Override
    protected IDataSetMetaData initMetaData() {
    	return metaData;
    }
    
    /**
     * Set the items in the data set.
     * 
     * @param items The data items
     */
    public void setItems(List<DataSetItem> items) {
        cachedItems.clear();
        for (DataSetItem item : items) {
            create(item);
        }
    }

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#create(java.util.Map)
	 */
	@Override
	public IDataSetItem create(IDataSetItem values) {
        // Create a new item
        DataSetItem item = new DataSetItem(this, values);

        // If the dataset specifies a key, but no key value has been provided, generate one
        if (item.getId() == null && getKeyGenerator() != null) {
        	item.setId(getKeyGenerator().generateKeyValue(this));
        }
        
        // put the new item in the cached list
        cachedItems.add(item);

		return item;
	}

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.api.IDataSet#retrieve(
     * com.jsa.crib.ds.api.IDataSetResultHandler)
     */
    @Override
    public void retrieve(IDataSetResultHandler handler) {
        processItems(cachedItems, handler);
    }

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IDataSet#retrieve(ind.jsa.crib.ds.api.DataSetQuery, ind.jsa.crib.ds.api.IDataSetResultHandler)
     */
	@Override
	public void retrieve(DataSetQuery query, IDataSetResultHandler handler) {
		processItems(filterItems(query), handler);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#update(ind.jsa.crib.ds.api.DataSetQuery, java.util.Map)
	 */
	@Override
	public void update(DataSetQuery query, IDataSetItem values) {
        // Fetch the list of items to update
        List<DataSetItem> itemsToUpdate = filterItems(query);

        // Apply the given values to each item
        if (itemsToUpdate != null) {
            for (DataSetItem item : itemsToUpdate) {
                for (Entry<String, Object> e : values.entrySet()) {
                    item.put(e.getKey(), e.getValue());
                }
            }
        }
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#update(java.util.Map)
	 */
	@Override
	public IDataSetItem update(IDataSetItem item) {
        // Fetch the matching item
        IDataSetItem updateItem = retrieve(item);

        // Update the item if found
        if (updateItem != null) {
            for (Entry<String, Object> e : item.entrySet()) {
            	updateItem.put(e.getKey(), e.getValue());
            }
        }

        return updateItem;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#delete(java.util.Map)
	 */
	@Override
	public void delete(Map<String, Object> keys) {
        // Get the item refrerence
        IDataSetItem item = retrieve(keys);

        // Remove the item reference from the list
        if (item != null) {
            cachedItems.remove(item);
        }
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getItemCount(ind.jsa.crib.ds.api.DataSetQuery)
	 */
	@Override
	public int getItemCount(DataSetQuery query) {
		
		int count = 0;
		
        // Get all that match
        for (DataSetItem item : cachedItems) {
            if (itemMatchesQuery(item, query)) {
                count++;
            }
        }

		return count;
	}

    /**
     * For the list of given items, apply each one to the given handler.
     * 
     * @param items The items to apply to the handler
     * @param handler The handler for the items
     */
    private void processItems(List<DataSetItem> items, IDataSetResultHandler handler) {
        handler.processStart();

        for (DataSetItem item : items) {
            handler.processItem(item);
        }

        handler.processEnd();
    }

    /**
     * Iterate thru the current set of items and return those that match the given query.
     * 
     * @param query The query to match
     * @return A list of matching items
     */
    private List<DataSetItem> filterItems(DataSetQuery query) {
        List<DataSetItem> filteredItems = new ArrayList<DataSetItem>(cachedItems.size());

        // Get all that match
        for (DataSetItem item : cachedItems) {
            if (itemMatchesQuery(item, query)) {
                filteredItems.add(item);
            }
        }

        // Sort the result
        Collections.sort(filteredItems, new DataSetItemComparator(query.getSorts()));

        // If startrow/maxrows specified, select that subset of rows
        if (query.getStartRow() > 0) {
            int fromIdx = query.getStartRow() - 1;
            int toIdx = query.getMaxRows() > 0 ? fromIdx + query.getMaxRows() : filteredItems.size();

            if (toIdx > filteredItems.size()) {
                toIdx = filteredItems.size();
            }

            // extract sublist
            filteredItems = filteredItems.subList(fromIdx, toIdx);
        }

        return filteredItems;
    }

    /**
     * Return an indicator as to whether a given item matches a given query.
     * 
     * @param item The item to match against the query
     * @param query The query to match the item against
     * @return True if matched, false otherwise
     */
    private boolean itemMatchesQuery(DataSetItem item, DataSetQuery query) {
        boolean match = true;

        // Check for matches between values in the item and any
        // specified parameters for the query
        for (Entry<String, Object> e : query.getParams().entrySet()) {
            if (item.containsKey(e.getKey())) {
                match = itemValueMatchesFilter(
                		getMetaData().getProperty(e.getKey()),
                        item.get(e.getKey()), 
                        e.getValue(), 
                        FilterOperator.EQUAL, null);

                if (!match) {
                    break;
                }
            }
        }

        // Check for matches between values in the item and any specified filters for the query
        if (match) {
            for (Entry<String, FilterExpression> e : query.getFilters().entrySet()) {
                if (item.containsKey(e.getKey())) {
                    match = itemValueMatchesFilter(
                    	getMetaData().getProperty(e.getKey()), 
                    	item.get(e.getKey()), 
                    	e.getValue().getValue(), 
                    	e.getValue().getOperator(), 
                    	e.getValue().getUpperBoundValue());

                    if (!match) {
                        break;
                    }
                }
            }
        }

        return match;
    }

    /**
     * Indicate whether an item value matches a filter value according to a given property specification and
     * filter operator.
     * 
     * @param prop Property spec
     * @param itemVal A value
     * @param filterVal A filter value
     * @param op A filter operator
     * 
     * @return True if matched, false otherwise
     */
    @SuppressWarnings("rawtypes")
    private boolean itemValueMatchesFilter(
    	IDataSetProperty prop, Object itemVal, Object filterVal, FilterOperator op, Object upperFilterVal) {

        if (itemVal == null && filterVal == null && upperFilterVal == null) {
            return true;
        }

        boolean match = false;

        if (op == FilterOperator.CONTAINS) {
            match = itemVal != null && itemVal.toString().indexOf(filterVal.toString()) >= 0;
        } 
        
        if (!match && op == FilterOperator.ONE_OF) {
            if (filterVal instanceof Collection) {
                if (((Collection) filterVal).size() > 0) {
                    match = itemVal != null && isValueInCollection(itemVal, (Collection) filterVal);
                }
            } else {
                op = FilterOperator.EQUAL;
            }
        } 
        
        if (!match && op == FilterOperator.NOT_ONE_OF) {
            if (filterVal instanceof Collection) {
                if (((Collection) filterVal).size() > 0) {
                    match = itemVal != null && !isValueInCollection(itemVal, (Collection) filterVal);
                }
            } else {
                op = FilterOperator.NOT_EQUAL;
            }
        }
        
        if (!match && op == FilterOperator.BETWEEN) {
        	if (filterVal == null && upperFilterVal != null) {
        		op = FilterOperator.LESS_OR_EQUAL;
        	} else if (filterVal != null && upperFilterVal == null) {
        		op = FilterOperator.GREATER_OR_EQUAL;
        	} else if (isNumericProperty(prop.getName())) {
	        	long itemScale = (Long) getTypeManager().convert(itemVal, prop.getVariant(), Long.class, prop.getVariant());
	        	long lowerScale = (Long) getTypeManager().convert(filterVal, prop.getVariant(), Long.class, prop.getVariant());
	        	long upperScale = (Long) getTypeManager().convert(upperFilterVal, prop.getVariant(), Long.class, prop.getVariant());

                match = itemScale >= lowerScale && itemScale <= upperScale;
            }
        }
        
        if (!match && op == FilterOperator.NOT_BETWEEN) {
        	if (filterVal == null && upperFilterVal != null) {
        		op = FilterOperator.GREATER_OR_EQUAL;
        	} else if (filterVal != null && upperFilterVal == null) {
        		op = FilterOperator.LESS_OR_EQUAL;
        	} else if (isNumericProperty(prop.getName())) {
	        	long itemScale = (Long) getTypeManager().convert(itemVal, prop.getVariant(), Long.class, prop.getVariant());
	        	long lowerScale = (Long) getTypeManager().convert(filterVal, prop.getVariant(), Long.class, prop.getVariant());
	        	long upperScale = (Long) getTypeManager().convert(upperFilterVal, prop.getVariant(), Long.class, prop.getVariant());

                match = itemScale <= lowerScale || itemScale >= upperScale;
            }
        }
        
        if (!match && op == FilterOperator.NOT_NULL) {
            match = (itemVal != null && !StringUtils.isEmpty(itemVal.toString()));
        }
        
        if (!match && op == FilterOperator.NULL) {
            match = (itemVal == null || StringUtils.isEmpty(itemVal.toString()));
        }
        
        if (!match) {
            int cmp = getTypeManager().compareValues(itemVal, prop.getVariant(), filterVal, prop.getVariant());

            if (op == FilterOperator.EQUAL) {
                match = (cmp == 0);
            } else if (op == FilterOperator.GREATER) {
                match = (cmp > 0);
            } else if (op == FilterOperator.GREATER_OR_EQUAL) {
                match = (cmp >= 0);
            } else if (op == FilterOperator.LESS) {
                match = (cmp < 0);
            } else if (op == FilterOperator.LESS_OR_EQUAL) {
                match = (cmp <= 0);
            } else if (op == FilterOperator.NOT_EQUAL) {
                match = (cmp != 0);
            }
        }

        return match;
    }

    private boolean isValueInCollection(Object val, Collection<?> col) {
    	if (val == null) {
    		return false;
    	}
    	
        for (Object o : col) {
        	if (getTypeManager().compareValues(val, null, o, null) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * A simple counter based key generator.
     */
    public static class MemoryKeyGenerator implements IKeyGenerator {

        private int currentId = STARTING_ID;

        /*
         * (non-Javadoc)
         * 
         * @see com.jsa.crib.ds.api.IKeyGenerator#generateKeyValue(java.lang.String,
         * java.lang.String)
         */
        @Override
        public Object generateKeyValue(IDataSet dataSet) {
            Integer val = Integer.valueOf(currentId++);
            IDataSetProperty prop = dataSet.getMetaData().getIdProperty();
            if (prop != null) {
            	return dataSet.getTypeManager().convert(val, null, prop.getType(), prop.getVariant());
            }
            
            return null;
        }

        /**
         * Get the current id.
         * 
         * @return An integer id
         */
        public int getCurrentId() {
            return currentId;
        }
    }
    
    /**
     * Used to sort a collection of items according to a given set of sort specification.
     * 
     */
    public class DataSetItemComparator implements Comparator<DataSetItem> {

        private Map<String, SortDirection> sorts;

        /**
         * A given set of sorts determine comparison between items.
         * 
         * @param sorts Sorts used for comparison
         */
        public DataSetItemComparator(Map<String, SortDirection> sorts) {
            this.sorts = sorts;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(DataSetItem item1, DataSetItem item2) {
            int cmp = 0;

            for (Entry<String, SortDirection> e : sorts.entrySet()) {
            	IDataSetProperty prop = getMetaData().getProperty(e.getKey());
                cmp = getTypeManager().compareValues(
                	item1.get(e.getKey()), prop.getVariant(), item2.get(e.getKey()), prop.getVariant());

                if (e.getValue() == SortDirection.DESC) {
                    cmp = -cmp;
                }

                if (cmp != 0) {
                    break;
                }
            }

            return cmp;
        }
    }
}
