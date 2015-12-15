package ind.jsa.crib.ds.api;

import java.util.function.Function;

/*
 * Interface for registering type metadata and logic for converting
 * values of one type to another.
 * 
 */
public interface ITypeManager {

	void registerType(Class<?> type, long nature);
	
	Long getTypeNature(Class<?> type);
	
	void registerConverter(Class<?> fromType, String fromVariant, Class<?> toType, String toVariant, Function<Object, Object> converter);

	void registerConverter(Class<?> fromType, Class<?> toType, Function<Object, Object> converter);
	
	Object convert(Object val, String fromvariant, Class<?> toType, String toVariant);
	
	Object convert(Object val, Class<?> toType);
	
	int compareValues(Object val1, Object val2);
	
	int compareValues(Object val1, String variant1, Object val2, String variant2);
	
	public interface ITypeManagerPlugin {
		/**
		 * Register the plugin with the type manager.
		 * @param typeManager The type manager to register with
		 */
		void register (ITypeManager typeManager);
	}
}
