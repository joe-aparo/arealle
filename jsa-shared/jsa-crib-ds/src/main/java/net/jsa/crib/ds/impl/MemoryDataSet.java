package net.jsa.crib.ds.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jsa.crib.ds.api.DataSetQuery;
import net.jsa.crib.ds.api.FilterExpression;
import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetItem;
import net.jsa.crib.ds.api.IDataSetProperty;
import net.jsa.crib.ds.api.IDataSetResultHandler;
import net.jsa.crib.ds.api.IKeyGenerator;
import net.jsa.crib.ds.api.IProperty;
import net.jsa.crib.ds.api.DataSetQuery.SortDirection;
import net.jsa.crib.ds.api.FilterExpression.FilterOperator;

import org.apache.commons.lang3.StringUtils;


/**
 * This implementation manages a list of DataSetItems in memory. This type of data set must be initialized
 * with a given list of IAttribute objects. Records in memory will be managed according to the set of given
 * attributes.
 * 
 */
public class MemoryDataSet extends AbstractDataSet {

    private static final int DEFAULT_INIT_SIZE = 100;
    private static final int STARTING_ID = 100;

    private List<IProperty> attributes;
    private List<Map<String, Object>> initialItems = null;
    private List<DataSetItem> cachedItems = new ArrayList<DataSetItem>(DEFAULT_INIT_SIZE);

    /**
     * Empty data set structure.
     */
    public MemoryDataSet() {
        setKeyGenerator(new MemoryKeyGenerator());
    }

    /**
     * DataSet initialized with attributes.
     * 
     * @param attributes The attributes
     */
    public MemoryDataSet(List<IProperty> attributes) {
        this(attributes, null);
    }

    /**
     * DataSet initialized with attributes and data.
     * 
     * @param attributes The attributes
     * @param items An initial set of data items
     */
    public MemoryDataSet(List<IProperty> attributes, List<Map<String, Object>> items) {
        setAttributes(attributes);
        this.initialItems = items;
    }

    /**
     * Load initial data items after standard initialization.
     */
    @Override
    public void initialize() {
        super.initialize();

        if (initialItems != null) {
            for (Map<String, Object> values : initialItems) {
                create(values);
            }
        }
    }

    /**
     * Set the Attributes for the dataset. The initialize method must be called after this call.
     * 
     * @param attrs The list of attributes for the dataset
     */
    public void setAttributes(List<IProperty> attrs) {
        cachedItems.clear();
        attributes = attrs;
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
     * 
     * @see com.jsa.crib.ds.api.IDataSet#retrieve( com.jsa.crib.ds.api.DataSetQuery,
     * com.jsa.crib.ds.api.IDataSetResultHandler)
     */
    @Override
    public void retrieve(DataSetQuery query, IDataSetResultHandler handler) {
        processItems(filterItems(query), handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.api.IDataSet#retrieve(java.util.Map)
     */
    @Override
    public DataSetItem retrieve(Map<String, Object> keyMap) {

        // convert values to map types specified by dataset
        Map<String, Object> convertedValues = convertToNativeValues(keyMap);

        // assume empty query
        DataSetQuery query = new DataSetQuery();

        // the item to return
        DataSetItem item = null;

        // only use configured keys
        for (Entry<String, Object> e : convertedValues.entrySet()) {
            if (getKeys().contains(e.getKey())) {
                query.putParam(e.getKey(), e.getValue());
            }
        }

        // If we have keys to fetch on, look for items
        if (query.getParams().size() > 0) {
            // filter the items
            List<DataSetItem> items = filterItems(query);

            // grab the first one if > 0
            item = items.size() > 0 ? items.get(0) : null;
        }

        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.api.IDataSet#create(java.util.Map)
     */
    @Override
    public DataSetItem create(Map<String, Object> values) {
        // Create a new item given the keys
        DataSetItem item = new DataSetItem(this);

        for (Entry<String, Object> e : values.entrySet()) {
            item.put(e.getKey(), e.getValue());
        }

        // attempt to seed the values with a set of newly generated keys
        Set<String> keyIds = getKeys();
        if (keyIds != null) {
            Map<String, Object> newKeys = getNewKeyValues(values);
            if (newKeys != null) {
                for (Entry<String, Object> e : newKeys.entrySet()) {
                    Object givenKey = values.get(e.getKey());
                    Object generatedKey = e.getValue();

                    if (givenKey == null || StringUtils.isEmpty(givenKey.toString())) {
                        item.put(e.getKey(), generatedKey);
                    }
                }
            }
        }

        // put it in the list
        cachedItems.add(item);

        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.api.IDataSet#update( com.jsa.crib.ds.api.DataSetQuery,
     * java.util.Map)
     */
    @Override
    public void update(DataSetQuery query, Map<String, Object> values) {
        // Fetch the list of items to update
        List<DataSetItem> updatedItems = filterItems(query);

        // Apply the given values to each item
        if (updatedItems != null) {
            for (DataSetItem item : updatedItems) {
                for (Entry<String, Object> e : values.entrySet()) {
                    item.put(e.getKey(), e.getValue());
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.api.IDataSet#update(java.util.Map)
     */
    @Override
    public DataSetItem update(Map<String, Object> values) {
        // convert values to map types specified by dataset
        Map<String, Object> convertedValues = convertToNativeValues(values);

        // Fetch the matching item
        DataSetItem item = retrieve(convertedValues);

        // Update the item if found
        if (item != null) {
            for (Entry<String, Object> e : convertedValues.entrySet()) {
                item.put(e.getKey(), e.getValue());
            }
        }

        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.api.IDataSet#delete(java.util.Map)
     */
    @Override
    public void delete(Map<String, Object> key) {
        // Get the item refrerence
        DataSetItem item = retrieve(key);

        // Remove the item reference from the list
        if (item != null) {
            cachedItems.remove(item);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.api.IDataSet#getItemCount( com.jsa.crib.ds.api.DataSetQuery)
     */
    @Override
    public int getItemCount(DataSetQuery query) {
        // temporary handler
        ListDataSetResultHandler handler = new ListDataSetResultHandler(this, query);

        // fetch items into handler
        retrieve(query, handler);

        // return number of items in handler
        return handler.getItems().size();
    }

    /**
     * Retrieve data from a given dataset for a given query and cache the result in a memory-backed data set.
     * 
     * @param dataSet The dataset from which to retrieve rows of data
     * 
     * @return A MemoryDataSet representing the selected rows.
     */
    public static MemoryDataSet cache(IDataSet dataSet) {
    	return cache(dataSet, null);
    }
    
    /**
     * Clone a dataset and cache the results of the specified query if not null.
     * 
     * @param dataSet The dataset to clone as a memory backed data set
     * @param query The query controlling the rows to retireve. If null, no rows will be retrieved
     * 
     * @return A MemoryDataSet representing the given dataset containing selected rows implied by the query.
     */
    public static MemoryDataSet cache(IDataSet dataSet, DataSetQuery query) {
        ListDataSetResultHandler handler = new ListDataSetResultHandler(dataSet, query);

        // Collect dataset properties as IAttributes
        List<IProperty> attrs = new ArrayList<IProperty>(dataSet.getProperties().size());
        for (String name : dataSet.getReadablePropertyNames()) {
            attrs.add(dataSet.getProperty(name));
        }

        List<Map<String, Object>> items = null;
        
        if (query != null) {
	        // Fetch data into a handler
	        dataSet.retrieve(query, handler);
	
	        // Collect dataset items as a list of maps
	        items = new ArrayList<Map<String, Object>>(handler.getItems().size());
	
	        for (IDataSetItem item : handler.getItems()) {
	            items.add(item);
	        }
        }

        // Initialize memory dataset with attrs and items
        MemoryDataSet ds = new MemoryDataSet(attrs, items);
        ds.setName(dataSet.getName());
        ds.setKeys(dataSet.getKeys());

        ds.initialize();

        return ds;
    }

    /*
     * 
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.impl.AbstractDataSet#establishWritableProperties()
     */
    @Override
    protected void establishWritableProperties() {
        // All attributes are considered writable
        for (IProperty attr : attributes) {
            DataSetProperty prop = new DataSetProperty(this);
            prop.setName(attr.getName()); // upper case for consistency
            prop.setSize(attr.getSize());
            prop.setClassName(attr.getClassName());

            addProperty(prop);
            addWritableProperty(prop.getName());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.impl.AbstractDataSet#establishReadableProperties()
     */
    @Override
    protected void establishReadableProperties() {
        // All writable properties are assumed readable
        for (String propName : getPropertiesByName().keySet()) {
            addReadableProperty(propName);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jsa.crib.ds.impl.AbstractDataSet#establishFilterableProperties()
     */
    @Override
    protected void establishFilterableProperties() {
        // All readable properties are assumed filterable
        for (String propName : getPropertiesByName().keySet()) {
            if (isReadableProperty(propName)) {
                addFilterableProperty(propName);
            }
        }
    }

    /**
     * For the list of given items, apply each one to the given handler.
     * 
     * @param items The items to apply to the handler
     * @param handler The handler for the items
     */
    protected void processItems(List<DataSetItem> items, IDataSetResultHandler handler) {
        handler.processStart();

        for (DataSetItem item : items) {
            handler.processRow(item);
        }

        handler.processEnd();
    }

    /**
     * Iterate thru the current set of items and return those that match the given query.
     * 
     * @param query The query to match
     * @return A list of matching items
     */
    protected List<DataSetItem> filterItems(DataSetQuery query) {
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
    protected boolean itemMatchesQuery(DataSetItem item, DataSetQuery query) {
        boolean match = true;

        // Check for matches between values in the item and any
        // specified parameters for the query
        for (Entry<String, Object> e : query.getParams().entrySet()) {
            if (item.containsKey(e.getKey())) {
                match = itemValueMatchesFilter(item.getDataSet().getProperty(e.getKey()),
                        item.get(e.getKey()), e.getValue(), FilterOperator.EQUAL, null);

                if (!match) {
                    break;
                }
            }
        }

        // Check for matches between values in the item and any specified filters for the query
        if (match) {
            for (Entry<String, FilterExpression> e : query.getFilters().entrySet()) {
                if (item.filterContainsKey(e.getKey())) {
                    match = itemValueMatchesFilter(item.getDataSet().getProperty(e.getKey()), item.get(e
                            .getKey()), e.getValue().getValue(), e.getValue().getOperator(), e.getValue()
                            .getUpperBoundValue());

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
    private boolean itemValueMatchesFilter(IDataSetProperty prop, Object itemVal, Object filterVal,
            FilterOperator op, Object upperFilterVal) {

        if (itemVal == null && filterVal == null) {
            return true;
        }

        boolean match = false;

        if (op == FilterOperator.CONTAINS) {
            match = itemVal != null && itemVal.toString().indexOf(filterVal.toString()) >= 0;
        } else if (op == FilterOperator.ONE_OF) {
            if (filterVal instanceof Collection) {
                if (((Collection) filterVal).size() > 0) {
                    match = itemVal != null && isValueInCollection(itemVal, (Collection) filterVal);
                } else {
                    match = true; // no constraint
                }
            } else {
                match = false;
            }
        } else if (op == FilterOperator.NOT_ONE_OF) {
            if (filterVal instanceof Collection) {
                if (((Collection) filterVal).size() > 0) {
                    match = itemVal != null && !isValueInCollection(itemVal, (Collection) filterVal);
                } else {
                    match = true; // no constraint
                }
            } else {
                match = false;
            }
        } else if (op == FilterOperator.BETWEEN) {
            if (getTypeRegistry().hasScalarValue(itemVal, prop.getVariant())) {
                long itemScale = getTypeRegistry().getScalarValue(itemVal, prop.getVariant());
                long lowerScale = getTypeRegistry().getScalarValue(filterVal, prop.getVariant());
                long upperScale = getTypeRegistry().getScalarValue(upperFilterVal, prop.getVariant());

                match = itemScale >= lowerScale && itemScale <= upperScale;
            } else {
                match = false;
            }
        } else if (op == FilterOperator.NOT_BETWEEN) {
            if (getTypeRegistry().hasScalarValue(itemVal, prop.getVariant())) {
                long itemScale = getTypeRegistry().getScalarValue(itemVal, prop.getVariant());
                long lowerScale = getTypeRegistry().getScalarValue(filterVal, prop.getVariant());
                long upperScale = getTypeRegistry().getScalarValue(upperFilterVal, prop.getVariant());

                match = itemScale <= lowerScale || itemScale >= upperScale;
            } else {
                match = false;
            }
        } else if (op == FilterOperator.NOT_NULL) {
            match = (itemVal != null && !StringUtils.isEmpty(itemVal.toString()));
        } else if (op == FilterOperator.NULL) {
            match = (itemVal == null || StringUtils.isEmpty(itemVal.toString()));
        } else {
            int cmp = getTypeRegistry().compareValues(itemVal, filterVal, prop.getVariant());

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
        	if (getTypeRegistry().compareValues(val, o, null) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Generate a new key.
     * 
     * @param params Input parameters
     * @return A name/value key pair
     */
    protected Map<String, Object> getNewKeyValues(Map<String, Object> params) {
        Map<String, Object> keyValues = new HashMap<String, Object>();

        Set<String> keyIds = getKeys();
        if (keyIds != null && keyIds.size() > 0 && getKeyGenerator() != null) {
            for (String k : keyIds) {
                keyValues.put(k, getKeyGenerator().generateKeyValue(getName(), k));
            }
        }

        return keyValues;
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
            	IDataSetProperty prop = item1.getDataSet().getProperty(e.getKey());
                cmp = getTypeRegistry().compareValues(item1.get(e.getKey()), item2.get(e.getKey()), prop.getVariant());

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
        public Object generateKeyValue(String dataSetName, String keyField) {
            return Integer.valueOf(currentId++);
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
}
