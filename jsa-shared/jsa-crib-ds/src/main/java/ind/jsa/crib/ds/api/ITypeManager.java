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
}