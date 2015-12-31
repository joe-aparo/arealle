package ind.jsa.crib.ds.internal;

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

import org.apache.commons.lang3.StringUtils;

import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetProperty;

/**
 * An instance of an item within a DataSet. An item maintains values for the
 * item via an array of object references. This array of values is managed and
 * exposed in a manner consistent with the names and order of the properties of
 * the associated DataSet.
 */
public class DataSetItem implements IDataSetItem {
	private IDataSet dataSet;
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

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#setId(java.lang.Object)
	 */
	@Override
	public void setId(Object value) {
        String idPropName = dataSet.getMetaData().getIdPropertyName();
        if (!StringUtils.isEmpty(idPropName)) {
        	put(idPropName, value);
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getId()
	 */
	@Override
	public Object getId() {
        return get(dataSet.getMetaData().getIdPropertyName());
	}
	
	/**
	 * An item is connected to its containing DataSet in order to manage and
	 * expose an internal array of values for the item.
	 * 
	 * This constructor initializes a new item with a give set of values.
	 * 
	 * @param dataSet
	 *            The dataSet defining the item's physical properties
	 * @param vals
	 *            A collection of initialization values
	 */
	public DataSetItem(IDataSet dataSet, Map<String, Object> vals) {
		this.values = new Object[dataSet.getMetaData().getProperties().size()];
		this.dataSet = dataSet;

		if (vals != null) {
			putAll(vals);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getDataSet()
	 */
	@Override
	public IDataSet getDataSet() {
		return dataSet;
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

	/*
	 * 
	 */
	public Object get(String name) {
		return get(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#get(int)
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
		put(dataSet.getMetaData().getPropertyIndex(name), val);

		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#put(int, java.lang.Object)
	 */
	public Map<String, Object> put(int idx, Object val) {
		if (idx >= 0 && idx < values.length) {
			IDataSetProperty p = dataSet.getMetaData().getProperty(idx);
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
		int idx = dataSet.getMetaData().getPropertyIndex(key.toString());
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
				map.put(dataSet.getMetaData().getPropertyName(i), values[i]);
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

	/*
	 * (non-Javadoc)
	 * @see java.util.Map#isEmpty()
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
				keys.add(dataSet.getMetaData().getPropertyName(i));
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

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#remove(java.lang.String)
	 */
	@Override
	public Object remove(String key) {
		if (key == null) {
			return null;
		}

		return remove(dataSet.getMetaData().getPropertyIndex(key));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#remove(int)
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

	/*
	 * @see net.jsa.crib.ds.api.IDataSetItem#getString(java.lang.String)
	 */
	@Override
	public String getString(String name) {
		return getString(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getByte(java.lang.String)
	 */
	@Override
	public Byte getByte(String name) {
		return getByte(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getCharacter(java.lang.String)
	 */
	@Override
	public Character getCharacter(String name) {
		return getCharacter(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getShort(java.lang.String)
	 */
	@Override
	public Short getShort(String name) {
		return getShort(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getInteger(java.lang.String)
	 */
	@Override
	public Integer getInteger(String name) {
		return getInteger(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getLong(java.lang.String)
	 */
	@Override
	public Long getLong(String name) {
		return getLong(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getBigInteger(java.lang.String)
	 */
	@Override
	public BigInteger getBigInteger(String name) {
		return getBigInteger(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getFloat(java.lang.String)
	 */
	@Override
	public Float getFloat(String name) {
		return getFloat(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getDouble(java.lang.String)
	 */
	@Override
	public Double getDouble(String name) {
		return getDouble(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getBigDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getBigDecimal(String name) {
		return getBigDecimal(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getCalendar(java.lang.String)
	 */
	@Override
	public Calendar getCalendar(String name) {
		return getCalendar(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(String name) {
		return getDate(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getTimestamp(java.lang.String)
	 */
	@Override
	public Timestamp getTimestamp(String name) {
		return getTimestamp(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getBoolean(java.lang.String)
	 */
	@Override
	public Boolean getBoolean(String name) {
		return getBoolean(dataSet.getMetaData().getPropertyIndex(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getString(int)
	 */
	@Override
	public String getString(int idx) {
		return (String) getValueAsType(idx, String.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getByte(int)
	 */
	@Override
	public Byte getByte(int idx) {
		return (Byte) getValueAsType(idx, Byte.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getCharacter(int)
	 */
	@Override
	public Character getCharacter(int idx) {
		return (Character) getValueAsType(idx, Character.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getShort(int)
	 */
	@Override
	public Short getShort(int idx) {
		return (Short) getValueAsType(idx, Short.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getInteger(int)
	 */
	@Override
	public Integer getInteger(int idx) {
		return (Integer) getValueAsType(idx, Integer.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getLong(int)
	 */
	@Override
	public Long getLong(int idx) {
		return (Long) getValueAsType(idx, Long.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getBigInteger(int)
	 */
	@Override
	public BigInteger getBigInteger(int idx) {
		return (BigInteger) getValueAsType(idx, BigInteger.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getFloat(int)
	 */
	@Override
	public Float getFloat(int idx) {
		return (Float) getValueAsType(idx, Float.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getDouble(int)
	 */
	@Override
	public Double getDouble(int idx) {
		return (Double) getValueAsType(idx, Double.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(int idx) {
		return (BigDecimal) getValueAsType(idx, BigDecimal.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getDate(int)
	 */
	@Override
	public Date getDate(int idx) {
		return (Date) getValueAsType(idx, Date.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getCalendar(int)
	 */
	@Override
	public Calendar getCalendar(int idx) {
		return (Calendar) getValueAsType(idx, Calendar.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(int idx) {
		return (Timestamp) getValueAsType(idx, Timestamp.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetItem#getBoolean(int)
	 */
	@Override
	public Boolean getBoolean(int idx) {
		return (Boolean) getValueAsType(idx, Boolean.class);
	}
	
	/*
	 * Support method top coerce a value for a property to
	 * a specified type
	 * 
	 * @param idx The index of the value to convert
	 * @param type The type to convert the value to
	 * 
	 * @return A value of the requested type
	 */
	private Object getValueAsType(int idx, Class<?> type) {
		IDataSetProperty p = dataSet.getMetaData().getProperty(idx);
		if (p == null) {
			return p;
		}
		
		// Use the type manager associated with the metadata object to do the work
		return dataSet.getTypeManager().convert(get(idx), null, type, p.getVariant());
	}
}
