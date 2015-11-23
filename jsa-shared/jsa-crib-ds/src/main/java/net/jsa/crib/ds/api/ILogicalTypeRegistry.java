package net.jsa.crib.ds.api;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a class that maintains associations between native and logical types,
 * and the conversions that allow values of one type to be converted to the other.
 * 
 * A number of the methods accept a String argument called 'cls'. The convention
 * for this class key string is: "{java.class.name}[/{variant}]". In other words,
 * it is a java class name with an optional variant suffix.
 * 
 * Resolution of a given class key is first attempted on the full string. If that
 * fails, an attempt will be made to resolve based on the java class name only.
 * This mechanism allows for fall-back handling for the majority of standard primitive
 * types while providing a means to configure deviations if needed.
 * 
 * @author jsaparo
 *
 */
public interface ILogicalTypeRegistry {
	/**
	 * Associate a native class to a logical type.
	 * 
	 * @param cls The class key to associate
	 * @param type The logical type
	 */
	void setLogicalTypeForClass(String cls, LogicalType type);
	
    /**
     * Get the logical type associated with a given class key.
     * 
     * @param cls A class
     * @return A logical type
     */
	LogicalType getLogicalTypeForClass(String cls);
	
	/**
	 * Get all logical types that have mappings to classes.
	 * 
	 * @return A set of logical types
	 */
    Set<LogicalType> getMappedLogicalTypes();
    
	/**
	 * Get all classes that have mappings to logical types.
	 * 
	 * @return A set of native types
	 */
    Set<String> getMappedNativeTypes();
    
	/**
	 * Get all classes mapped to a logical type.
	 * 
	 * @param type The logical type
	 * @return A collection of classes
	 */
	Collection<String> getClassesForLogicalType(LogicalType type);
	
	/**
	 * Register a value converter between a class and a logical type.
	 * 
	 * @param cls The native class
	 * @param type The logical type
	 * @param converter A value converter
	 */
	void registerConverter (String cls, LogicalType type, ILogicalTypeConverter converter);
	
	/**
	 * Get the value converter for a given class and logical type.
	 * 
	 * @param cls The native class
	 * @param type The logical type
	 * @return A value converter
	 */
	ILogicalTypeConverter getConverter(String cls, LogicalType type);
	
    /**
     * Utility method to determine whether a given object is numeric in type.
     * 
     * @param val The object to check
     * @param variant A variant identifier, or null
     * 
     * @return An indicator
     */
    boolean isNumericValue(Object val, String variant);
	
    /**
     * Utility method to determine whether a given object has a scalar value.
     * 
     * @param val The object to check
     * @param variant A variant identifier, or null
     * 
     * @return An indicator
     */
    boolean hasScalarValue(Object val, String variant);

    /**
     * Utility method to determine whether a class is a scalar type.
     * 
     * @param cls The class to check
     * @return An indicator
     */
    boolean isScalarType(String cls);

    /**
     * Utility method to convert a scalar type object to a long value.
     * 
     * @param val The value to convert
     * @param variant A variant identifier, or null
     * 
     * @return A long value
     */
    long getScalarValue(Object val, String variant);	
    
    /**
     * Utility method to determine whether a given string represents a number.
     * 
     * @param str The string to check
     * @return An indicator
     */
    boolean isNumericString(String str);
    
    /**
     * Compare two values for equality.
     * 
     * @param val1 First value
     * @param val2 Second value
     * @param variant A variant identifier, or null
     * 
     * @return True if matched, false otherwise
     */
    int compareValues(Object val1, Object val2, String variant);

    /**
     * Safely construct a standard class key for a given class and variant.
     * 
     * @param type The class
     * @param variant A variant identifier, or null
     * 
     * @return A standard class key
     */
    String makeClassKey(Class<?> type, String variant);
}
