package ind.jsa.crib.ds.api;

import java.util.function.Function;

/*
 * Interface for registering type metadata and logic for converting
 * values of one type to another.
 * 
 */
public interface ITypeManager {

	/**
	 * Introduce a class and its nature to the registry.
	 * 
	 * @param type The class
	 * @param nature Its nature
	 */
	void registerType(Class<?> type, long nature);
	
	/**
	 * Get the nature of a given class
	 * 
	 * @param type The class
	 * @return Its nature
	 */
	Long getTypeNature(Class<?> type);
	
	/**
	 * Register a function that will convert a value of one type
	 * to that of another.
	 * 
	 * @param fromType The type to convert from
	 * @param fromVariant A variant of the type (special cases)
	 * @param toType The type to convert to
	 * @param toVariant A variant of the type (special cases)
	 * @param converter A function that will perform the conversion
	 */
	void registerConverter(Class<?> fromType, String fromVariant, Class<?> toType, String toVariant, Function<Object, Object> converter);

	/**
	 * Register a function that will convert a value of one type
	 * to that of another.
	 * 
	 * @param fromType The type to convert from
	 * @param toType The type to convert to
	 * @param converter A function that will perform the conversion
	 */
	void registerConverter(Class<?> fromType, Class<?> toType, Function<Object, Object> converter);
	
	/**
	 * Convert a given value to that of a specified type.
	 * 
	 * @param val The value to convert
	 * @param fromvariant A variant of the type (special cases)
	 * @param toType The type to convert to
	 * @param toVariant A variant of the type (special cases)
	 * 
	 * @return A converted value, or null if either the given value is null,
	 * or the conversion could not be performed
	 */
	Object convert(Object val, String fromvariant, Class<?> toType, String toVariant);
	
	/**
	 * Convert a given value to that of a specified type.
	 * 
	 * @param val The value to convert
	 * @param toType The type to convert to
	 * 
	 * @return A converted value, or null if either the given value is null,
	 * or the conversion could not be performed
	 */
	Object convert(Object val, Class<?> toType);
	
	/**
	 * Compare two object by value.
	 * 
	 * @param val1 First value
	 * @param val2 Second value
	 * 
	 * @return 0 if values are equal, -1 if val1 is less than val2,
	 * and 1 if val1 is greater than val2
	 */	
	int compareValues(Object val1, Object val2);
	
	/**
	 * Convert a given value to that of a specified type.
	 * 
	 * @param val The value to convert
	 * @param variant1 A variant of the type (special cases)
	 * @param toType The type to convert to
	 * @param variant2 A variant of the type (special cases)
	 * 
	 * @return A converted value, or null if either the given value is null,
	 * or the conversion could not be performed
	 */
	int compareValues(Object val1, String variant1, Object val2, String variant2);
	
	/**
	 * A class for extending the registry upon initialization.
	 * 
	 * @author jsaparo
	 *
	 */
	public interface ITypeManagerPlugin {
		/**
		 * Register the plugin with the type manager.
		 * 
		 * @param typeManager The type manager to register with
		 */
		void register (ITypeManager typeManager);
	}
}
