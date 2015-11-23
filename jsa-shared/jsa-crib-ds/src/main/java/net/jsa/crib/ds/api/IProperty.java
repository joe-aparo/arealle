package net.jsa.crib.ds.api;

/**
 * Represents meta-data about a data element. 
 */
public interface IProperty {

    /**
     * Get the attribute name.
     * @return A string
     */
    String getName();

    /**
     * Get the attribute's class name.
     * @return A string
     */
    String getClassName();

    /**
     * Get the java class of the property.
     * @return A class
     */
    Class<?> getClassType();
    
    /**
     * Get the type variant name for the property. This is used to refine
     * conversion logic when needed. 
     */
    String getVariant();
    
    /**
     * Get the attribute's physical size.
     * @return An integral size
     */
    int getSize();

    /**
     * Get the attribute's numeric scale.
     * @return An integral precision
     */
    int getScale();
}
