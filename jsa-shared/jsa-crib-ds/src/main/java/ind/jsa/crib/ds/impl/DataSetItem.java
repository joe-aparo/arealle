package ind.jsa.crib.ds.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetMetaData;
import ind.jsa.crib.ds.api.IDataSetProperty;

/**
 * An instance of an item within a DataSet. An item maintains values for the
 * item via an array of object references. This array of values is managed and
 * exposed in a manner consistent with the names and order of the properties of
 * the associated DataSet.
 */
public class DataSetItem implements IDataSetItem {
	private IDataSetMetaData dataSetMetaData;
	private Object[] values;

	/**
	 * An item is connected to its containing DataSet in order to manage and
	 * expose an internal array of values for the item.
	 * 
	 * This constructor initializes a blank item.
	 * 
	 * @param dataSet
	 *            The owning DataSet
	 */
	public DataSetItem(IDataSet dataSet) {
		this(dataSet, null);
	}

	/**
	 * An item is connected to its containing DataSet in order to manage and
	 * expose an internal array of values for the item.
	 * 
	 * This constructor initializes a new item with a give set of values.
	 * 
	 * @param dataSet
	 *            The owning DataSet
	 * @param vals
	 *            A collection of initialization values
	 */
	public DataSetItem(IDataSet dataSet, Map<String, Object> vals) {
		this.values = new Object[dataSet.getMetaData().getProperties().size()];
		this.dataSetMetaData = dataSet.getMetaData();

		if (vals != null) {
			putAll(vals);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getMetaData()
	 */
	@Override
	public IDataSetMetaData getMetaData() {
		return dataSetMetaData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#clear()
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
	 * @param name
	 *            The name of the property
	 * @return The value, or null
	 * 
	 */
	public Object get(String name) {
		return get(dataSetMetaData.getPropertyIndex(name));
	}

	/**
	 * Get the value for a specified property index.
	 * 
	 * @param idx
	 *            The ordinal index of the property
	 * @return The value, or null
	 * 
	 */
	public Object get(int idx) {
		return idx >= 0 && idx < values.length ? values[idx] : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Map<String, Object> put(String name, Object val) {
		put(dataSetMetaData.getPropertyIndex(name), val);

		return this;
	}

	/**
	 * Set the value for a specified property index.
	 * 
	 * @param idx
	 *            The property index
	 * @param val
	 *            The value to set
	 * 
	 * @return Reference to this item
	 */
	public Map<String, Object> put(int idx, Object val) {
		if (idx >= 0 && idx < values.length) {
			IDataSetProperty p = dataSetMetaData.getProperty(idx);
			if (p != null) {
				values[idx] = val;
			}
		}

		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return values.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		int idx = dataSetMetaData.getPropertyIndex(key.toString());
		return idx != -1 && values[idx] != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsValue(java.lang.Object)
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

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				map.put(dataSetMetaData.getPropertyName(i), values[i]);
			}
		}

		return map.entrySet();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public Object get(Object key) {
		return get(key.toString());
	}

	/**
	 * Returns an indicator as to whether no values have been assigned to the
	 * item.
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
	 * Get the set of keys for the properties associated with the this item that
	 * have values assigned. Properties that do not have a value assigned are
	 * skipped.
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
				keys.add(dataSetMetaData.getPropertyName(i));
			}
		}

		return keys;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		for (Map.Entry<? extends String, ? extends Object> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
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
	 * @param key
	 *            The key of the item to remove
	 * @return The removed object
	 */
	@Override
	public Object remove(String key) {
		if (key == null) {
			return null;
		}

		return remove(dataSetMetaData.getPropertyIndex(key));
	}

	/**
	 * Remove an item from the set.
	 * 
	 * @param idx
	 *            The ordinal index of the item to remove
	 * @return The removed object
	 */
	@Override
	public Object remove(int idx) {
		Object tmp = null;

		if (idx >= 0 && idx < values.length) {
			tmp = values[idx];
			values[idx] = null;
		}

		return tmp;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#values()
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
		return getString(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public Byte getByte(String name) {
		return getByte(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public Character getCharacter(String name) {
		return getCharacter(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public Short getShort(String name) {
		return getShort(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public Integer getInteger(String name) {
		return getInteger(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public Long getLong(String name) {
		return getLong(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public BigInteger getBigInteger(String name) {
		return getBigInteger(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public Float getFloat(String name) {
		return getFloat(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public Double getDouble(String name) {
		return getDouble(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public BigDecimal getBigDecimal(String name) {
		return getBigDecimal(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public Calendar getCalendar(String name) {
		return getCalendar(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public Date getDate(String name) {
		return getDate(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public Timestamp getTimestamp(String name) {
		return getTimestamp(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public Boolean getBoolean(String name) {
		return getBoolean(dataSetMetaData.getPropertyIndex(name));
	}

	@Override
	public String getString(int idx) {
		return (String) getValueAsType(idx, String.class);
	}

	@Override
	public Byte getByte(int idx) {
		return (Byte) getValueAsType(idx, Byte.class);
	}

	@Override
	public Character getCharacter(int idx) {
		return (Character) getValueAsType(idx, Character.class);
	}

	@Override
	public Short getShort(int idx) {
		return (Short) getValueAsType(idx, Short.class);
	}

	@Override
	public Integer getInteger(int idx) {
		return (Integer) getValueAsType(idx, Integer.class);
	}

	@Override
	public Long getLong(int idx) {
		return (Long) getValueAsType(idx, Long.class);
	}

	@Override
	public BigInteger getBigInteger(int idx) {
		return (BigInteger) getValueAsType(idx, BigInteger.class);
	}

	@Override
	public Float getFloat(int idx) {
		return (Float) getValueAsType(idx, Float.class);
	}

	@Override
	public Double getDouble(int idx) {
		return (Double) getValueAsType(idx, Double.class);
	}

	@Override
	public BigDecimal getBigDecimal(int idx) {
		return (BigDecimal) getValueAsType(idx, BigDecimal.class);
	}

	@Override
	public Date getDate(int idx) {
		return (Date) getValueAsType(idx, Date.class);
	}

	@Override
	public Calendar getCalendar(int idx) {
		return (Calendar) getValueAsType(idx, Calendar.class);
	}

	@Override
	public Timestamp getTimestamp(int idx) {
		return (Timestamp) getValueAsType(idx, Timestamp.class);
	}

	@Override
	public Boolean getBoolean(int idx) {
		return (Boolean) getValueAsType(idx, Boolean.class);
	}
	
	private Object getValueAsType(int idx, Class<?> type) {
		IDataSetProperty p = dataSetMetaData.getProperty(idx);
		if (p == null) {
			return p;
		}
		
		return dataSetMetaData.getTypeManager().convert(get(idx), null, p.getClass(), p.getVariant());
	}
}
