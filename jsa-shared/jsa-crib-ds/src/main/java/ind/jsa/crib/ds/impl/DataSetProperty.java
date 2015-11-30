package ind.jsa.crib.ds.impl;

import java.util.HashMap;
import java.util.Map;

import ind.jsa.crib.ds.api.IDataSetProperty;

/**
 * Defines metadata for an individual data element.
 */
public class DataSetProperty implements IDataSetProperty
{
	public static final String SIZE_ATTR = "size";
	public static final String SCALE_ATTR = "scale";
	
    private String name;
    private Class<?> type = String.class;
    private String variant;
    private boolean readable = true;
    private boolean writable = true;
    private boolean filterable = true;
    private boolean instanceId = false;
    private boolean referenceId = false;
	private Map<String, Object> attrs = new HashMap<String, Object>();

    public DataSetProperty(String name) {
    	this.name = name;
    }
    
    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IPropertyMetadata#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the property name.
     * 
     * @param name A name, assumed to be unique within a set
     */
    public void setName(String name) {
        this.name = name;
    }

	/**
     * @see com.jsa.crib.ds.api.IAttribute#getSize()
     */
    public int getSize() {
        Integer size = (Integer) attrs.get(SIZE_ATTR);
        return size != null ? size.intValue() : 0;
    }

    /**
     * Set the property size.
     * 
     * @param size A logical data byte size
     */
    public void setSize(int size) {
        attrs.put(SIZE_ATTR, Integer.valueOf(size));
    }

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IPropertyMetadata#getType()
     */
    @Override
    public Class<?> getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * @see ind.jsa.crib.ds.api.IPropertyMetadata#getVariant()
     */
    @Override
    public String getVariant() {
		return variant;
	}

    /**
     * Set the type variant identifier.
     * 
     * @param variant
     */
	public void setVariant(String variant) {
		this.variant = variant;
	}

	/**
	 * Set a custom property.
	 * 
	 * @param attrName
	 * @param value
	 */
	public void setAttr(String attrName, Object value) {
		attrs.put(attrName, value);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IPropertyMetadata#getAttr(java.lang.String)
	 */
	@Override
	public Object getAttr(String attrName) {
		return attrs.get(attrName);
	}

	/**
	 * Set whether property is readable.
	 * 
	 * @param readable
	 */
	public void setReadable(boolean readable) {
		this.readable = readable;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetProperty#isReadable()
	 */
	@Override
	public boolean isReadable() {
		return readable;
	}

	/**
	 * Set whether property is writable.
	 * 
	 * @param writable
	 */
	public void setWritable(boolean writable) {
		this.writable = writable;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetProperty#isWritable()
	 */
	@Override
	public boolean isWritable() {
		return writable;
	}

	/**
	 * Set whether property is filterable.
	 * 
	 * @param filterable
	 */
	public void setFilterable(boolean filterable) {
		this.filterable = filterable;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetProperty#isFilterable()
	 */
	@Override
	public boolean isFilterable() {
		return filterable;
	}
	
	/**
	 * Set whether property is an instance identifier.
	 * 
	 * @param instanceId
	 */
	public void setInstanceId(boolean instanceId) {
		this.instanceId = instanceId;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetProperty#isInstanceId()
	 */
	@Override
    public boolean isInstanceId() {
		return instanceId;
	}

	/**
	 * Set whether property is a reference identifier.
	 * 
	 * @param referenceId
	 */
	public void setReferenceId(boolean referenceId) {
		this.referenceId = referenceId;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSetProperty#isReferenceId()
	 */
	@Override
	public boolean isReferenceId() {
		return referenceId;
	}
}
