package net.jsa.crib.ds.api;

import java.util.function.BiFunction;

/*
 * Scalar - Useful ui hint (between)
 * 
 * 
 */
public interface ITypeManager {

	void registerScalar(String typeName);
	
	void registerConverter(String fromTypeName, String toTypeName, BiFunction<Object, Object, Object> converter);
	
	boolean isScalar(String typeName);
	
	BiFunction<Object, Object, Object> getConverter(String fromTypeName, String toTypeName);
	
	Object convert(Object val, String toTypeName);
}
