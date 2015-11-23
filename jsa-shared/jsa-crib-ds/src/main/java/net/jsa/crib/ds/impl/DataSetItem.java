package net.jsa.crib.ds.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jsa.crib.ds.api.IDataSet;
import net.jsa.crib.ds.api.IDataSetItem;
import net.jsa.crib.ds.api.IDataSetProperty;
import net.jsa.crib.ds.api.LogicalType;

/**
 * An instance of an item within a DataSet. An item maintains values for the item via an array of
 * object references. This array of values is managed and exposed in a manner consistent with the names and
 * order of the properties of the associated DataSet.
 */
public class DataSetItem implements IDataSetItem {
    private IDataSet dataSet;
    private Object[] values;

    /**
     * An item is connected to its containing DataSet in order to manage and expose an internal array of
     * values for the item.
     * 
     * This constructor initializes a blank item.
     * 
     * @param dataSet The owning DataSet
     */
    public DataSetItem(IDataSet dataSet) {
        this(dataSet, null);
    }

    /**
     * An item is connected to its containing DataSet in order to manage and expose an internal array of
     * values for the item.
     * 
     * This constructor initializes a new item with a give set of values.
     * 
     * @param dataSet The owning DataSet
     * @param vals A collection of initialization values
     */
    public DataSetItem(IDataSet dataSet, Map<String, Object> vals) {
        this.values = new Object[dataSet.getProperties().size()];
        this.dataSet = dataSet;

        if (vals != null) {
            putAll(vals);
        }
    }

    /**
     * Get the owning DataSet for the item.
     * @return The DataSet
     */
    public IDataSet getDataSet() {
        return dataSet;
    }

    /**
     * Clear the current set of values.
     */
    @Override
    public void clear() {
        for (int i = 0; i < values.length; i++) {
            values[i] = null;
        }
    }

    /**
     * Get the value for a specified property name.
     * 
     * @param name The name of the property
     * @return The value, or null
     * 
     */
    public Object get(String name) {
        return get(dataSet.getPropertyIndex(name));
    }

    /**
     * Get the value for a specified property index.
     * 
     * @param idx The ordinal index of the property
     * @return The value, or null
     * 
     */
    public Object get(int idx) {
        return idx >= 0 && idx < values.length ? values[idx] : null;
    }

    /**
     * Set the value for a specified property name.
     * 
     * @param name The property name
     * @param val The value to set
     * 
     * @return Reference to this item
     */
    @Override
    public Map<String, Object> put(String name, Object val) {
        put(dataSet.getPropertyIndex(name), val);

        return this;
    }

    /**
     * Set the value for a specified property index.
     * 
     * @param idx The property index
     * @param val The value to set
     * 
     * @return Reference to this item
     */
    public Map<String, Object> put(int idx, Object val) {
        if (idx >= 0 && idx < values.length) {
        	IDataSetProperty p = dataSet.getProperty(idx);
        	if (p != null && val != null) {
        		values[idx] = p.toLogicalValue(val);
        	}
        }

        return this;
    }

    /**
     * Return the number of properties that have a non-null value assigned.
     * 
     * @return A size
     */
    @Override
    public int size() {
    	return values.length;
    }

    /**
     * Return an indicator as to whether there is a value assigned for the given key.
     *
     * @param key The key to check
     * @return An indicator
     */
    @Override
    public boolean containsKey(Object key) {
        int idx = dataSet.getPropertyIndex(key.toString());
        return idx != -1 && values[idx] != null;
    }

    /**
     * Return an indicator as to whether there is a value assigned for the given key.
     * (Special for filter)
     * 
     * @param key The key to check
     * @return An indicator
     */
    public boolean filterContainsKey(Object key) {
        int idx = dataSet.getPropertyIndex(key.toString());
        return idx != -1;
    }

    /**
     * Return an indicator as to whether the given value equals any value currently assigned to this item.
     *
     * @param value The value to check
     * @return An indicator
     */
    @Override
   public boolean containsValue(Object value) {
        for (int i = 0; i < values.length; i++) {
            if (value.equals(values[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the set of entries.
     * @return An Entry set
     */
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                map.put(dataSet.getPropertyName(i), values[i]);
            }
        }

        return map.entrySet();
    }

    /**
     * Get the object for the property whose name matches the string-ified representation of the given object.
     *
     * @param key The key of the object to get
     * @return The located object, or null if not present
     */
    @Override
    public Object get(Object key) {
        return get(key.toString());
    }

    /**
     * Returns an indicator as to whether no values have been assigned to the item.
     * 
     * @return True if no values assigned, False otherwise.
     */
    @Override
    public boolean isEmpty() {
        for (Object val : values) {
            if (val != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the set of keys for the properties associated with the this item that have values assigned.
     * Properties that do not have a value assigned are skipped.
     * 
     * The set is ordered according to the DataSet.
     * 
     * @return An entry set
     */
    @Override
    public Set<String> keySet() {
        Set<String> keys = new LinkedHashSet<String>();
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                keys.add(dataSet.getPropertyName(i));
            }
        }

        return keys;
    }

    /**
     * Set values for this item that match the given set of values.
     * 
     * @param m The map of values to put
     */
    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        for (Map.Entry<? extends String, ? extends Object> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Remove an item from the set.
     * 
     * @param key The key of the item to remove
     * @return The removed object
     */
   @Override
   public Object remove(Object key) {
        if (key == null) {
            return null;
        }

        return remove(key.toString());
    }

    /**
     * Remove an item from the set.
     * 
     * @param key The key of the item to remove
     * @return The removed object
     */
    public Object remove(String key) {
        if (key == null) {
            return null;
        }

        return remove(dataSet.getPropertyIndex(key));
    }

    /**
     * Remove an item from the set.
     * 
     * @param idx The ordinal index of the item to remove
     * @return The removed object
     */
    public Object remove(int idx) {
        Object tmp = null;

        if (idx >= 0 && idx < values.length) {
            tmp = values[idx];
            values[idx] = null;
        }

        return tmp;
    }

    /**
     * Get a collection of values for those properties that have assigned values.
     * 
     * @return The values in the collection 
     */
    @Override
    public Collection<Object> values() {
        List<Object> vals = new ArrayList<Object>();
        for (Object val : values) {
            if (val != null) {
                vals.add(val);
            }
        }

        return vals;
    }

    /**
     * @see net.jsa.crib.ds.api.IDataSetItem#getString(java.lang.String)
     */
	@Override
	public String getString(String name) {
		return getString(dataSet.getPropertyIndex(name));
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getInteger(java.lang.String)
	 */
	@Override
	public Long getInteger(String name) {
		return getInteger(dataSet.getPropertyIndex(name));
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getDecimal(String name) {
		return getDecimal(dataSet.getPropertyIndex(name));
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getDateTime(java.lang.String)
	 */
	@Override
	public Calendar getDateTime(String name) {
		return getDateTime(dataSet.getPropertyIndex(name));
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getBoolean(java.lang.String)
	 */
	@Override
	public Boolean getBoolean(String name) {
		return getBoolean(dataSet.getPropertyIndex(name));
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getNativeValue(java.lang.String)
	 */
	@Override
	public Object getNativeValue(String name) {
		return getNativeValue(dataSet.getPropertyIndex(name));
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getNativeValue(java.lang.String)
	 */
	@Override
	public Object getLogicalValue(String name, LogicalType type) {
		return getLogicalValue(dataSet.getPropertyIndex(name), type);
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getString(int)
	 */
	@Override
	public String getString(int idx) {
		return (String) getLogicalValue(idx, LogicalType.STRING);
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getInteger(int)
	 */
	@Override
	public Long getInteger(int idx) {
		return (Long) getLogicalValue(idx, LogicalType.INTEGER);
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getDecimal(int)
	 */
	@Override
	public BigDecimal getDecimal(int idx) {
		return (BigDecimal) getLogicalValue(idx, LogicalType.DECIMAL);
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getDateTime(int)
	 */
	@Override
	public Calendar getDateTime(int idx) {
		return (Calendar) getLogicalValue(idx, LogicalType.DATETIME);
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getBoolean(int)
	 */
	@Override
	public Boolean getBoolean(int idx) {
		return (Boolean) getLogicalValue(idx, LogicalType.BOOLEAN);
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getNativeValue(int)
	 */
	@Override
	public Object getNativeValue(int idx) {
		IDataSetProperty p = dataSet.getProperty(idx);
		
		return p != null ? p.toNativeValue(get(idx)) : null;
	}

	/**
	 * @see net.jsa.crib.ds.api.IDataSetItem#getNativeValue(int)
	 */
	@Override
	public Object getLogicalValue(int idx, LogicalType logicalType) {
		IDataSetProperty p = dataSet.getProperty(idx);
		
		return p != null ? p.toLogicalValue(get(idx), logicalType) : null;
	}
}
