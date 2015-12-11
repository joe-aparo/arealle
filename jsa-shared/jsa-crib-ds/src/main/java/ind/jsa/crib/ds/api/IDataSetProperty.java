package ind.jsa.crib.ds.api;

/**
 * Represents meta-data about a data element. 
 */
public interface IDataSetProperty {

    /**
     * Get the attribute name.
     * @return A string
     */
    String getName();

    /**
     * Get the java type class of the property.
     * @return A class
     */
    Class<?> getType();
    
    /**
     * Get the type variant identifier for the property. 
     */
    String getVariant();
    
    /**
     * Get a custom attribute value for the property.
     * 
     * @param attrName The name of the attribute
     * @return An object, or null if not found
     */
    Object getAttr(String attrName);
    
    /**
     * Indicates whether the property represents an identity value.
     * 
     * @return An indicator (false by default).
     */
    boolean isId();
    
    /**
     * Indicates whether the property is writable.
     * 
     * @return An indicator (true by default).
     */
    boolean isWritable();
    
    /**
     * Indicates whether the property is filterable.
     * 
     * @return An indicator (true by default).
     */
    boolean isFilterable();
    
    /**
     * Indicates whether the property represents a reference id value.
     * 
     * @return An indicator (false by default).
     */
    boolean isIdRef();
    
    /**
     * Indicates whether the property may be sorted on.
     * 
     * @return An indicator (true by default).
     */
    boolean isSortable();
}
