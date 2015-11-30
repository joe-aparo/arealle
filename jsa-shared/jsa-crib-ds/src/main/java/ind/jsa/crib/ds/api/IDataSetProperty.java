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
}
