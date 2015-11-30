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
}