package net.jsa.crib.ds.impl;

import net.jsa.common.logging.LogUtils;
import net.jsa.crib.ds.api.IProperty;

/**
 * Defines metadata for an individual data element.
 */
public class Property implements IProperty {

    private String name;
    private int size;
    private int scale;
    private String className;
    private String variant;
    
	private Class<?> classType;

    /**
     * @see com.jsa.crib.ds.api.IAttribute#getName()
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
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Set the property size.
     * 
     * @param size A logical data byte size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @see com.jsa.crib.ds.api.IAttribute#getPrecision()
     */
    @Override
    public int getScale() {
        return scale;
    }

    /**
     * Set the property numeric precision.
     * 
     * @param scale A precision for numeric types
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * @see com.jsa.crib.ds.api.IAttribute#getClassName()
     */
    @Override
    public String getClassName() {
        return className;
    }

    /**
     * Set the class name of the property.
     * 
     * @param clsName A java class name. Supported types are those that may be easily mapped to the logical
     *            types defined for a DataSet.
     */
    public void setClassName(String clsName) {
        this.className = clsName;

        // Internal class type gets set as a side-effect of setting class name
        try {
            classType = Class.forName(this.className);
        } catch (Exception ex) {
        	LogUtils.getLogger().warn("Unable to resolve class: " + className);
            classType = null;
        }
    }

    /**
     * @see net.jsa.crib.ds.api.IProperty#getClassType()
     */
    @Override
    public Class<?> getClassType() {
    	return classType;
    }

    /**
     * @see net.jsa.crib.ds.api.IProperty#getVariant()
     */
    @Override
    public String getVariant() {
		return variant;
	}

    /**
     * Sets the type variant name of the property.
     * 
     * @param variant A type variant name
     */
	public void setVariant(String variant) {
		this.variant = variant;
	}

}
