package net.jsa.crib.ds.impl;

import ind.jsa.crib.ds.api.ITypeManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractTypeManager implements ITypeManager {

	private Map<String, Long> typeNatures = new HashMap<String, Long>();
	private Map<String, Map<String, Function<Object, Object>>> typeConverters = 
			new HashMap<String, Map<String, Function<Object, Object>>>();
	
	/*
	 * (non-Javadoc)
	 * @see net.jsa.crib.ds.api.ITypeManager#registerType(java.lang.String, long)
	 */
	@Override
	public void registerType(Class<?> type, long nature) {
		typeNatures.put(type.getName(), nature);
		
		// register default no-op pass-through converter
		registerConverter(type, type, (Object val) -> val);
	}

	/*
	 * (non-Javadoc)
	 * @see net.jsa.crib.ds.api.ITypeManager#getTypeNature(java.lang.Class)
	 */
	@Override
	public Long getTypeNature(Class<?> type) {
		return typeNatures.get(type.getName());
	}

	/*
	 * (non-Javadoc)
	 * @see net.jsa.crib.ds.api.ITypeManager#registerConverter(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.function.BiFunction)
	 */
	@Override
	public void registerConverter(
		Class<?> fromType, String fromVariant, Class<?> toType, String toVariant, Function<Object, Object> converter) {
		
		String fromKey = makeVariantKey(fromType, fromVariant);
		String toKey = makeVariantKey(toType, toVariant);
		
		Map<String, Function<Object, Object>> map = typeConverters.get(fromKey);
		if (map == null) {
			map = new HashMap<String, Function<Object, Object>>();
			typeConverters.put(fromKey, map);
		}
		
		map.put(toKey, converter);
	}

	/*
	 * (non-Javadoc)
	 * @see net.jsa.crib.ds.api.ITypeManager#registerTypeConverter(java.lang.Class, java.lang.Class, java.util.function.Function)
	 */
	@Override
	public void registerConverter(
		Class<?> fromType, Class<?> toType, Function<Object, Object> converter) {
		registerConverter(fromType, null, toType, null, converter);
	}

		/*
	 * (non-Javadoc)
	 * @see net.jsa.crib.ds.api.ITypeManager#convert(java.lang.Object, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Object convert(Object val, String fromVariant, Class<?> toType, String toVariant) {
		String fromKey = makeVariantKey(val.getClass(), fromVariant);
		String toKey = makeVariantKey(toType, toVariant);

		Map<String, Function<Object, Object>> map = typeConverters.get(fromKey);
		if (map == null) {
			return null;
		}
		
		Function<Object, Object> converter = map.get(toKey);
		
		return converter != null ? converter.apply(val) : null;
	}
	
	public Object convert(Object val, Class<?> toType) {
		return convert(val, null, toType, null);
	}

	private String makeVariantKey(Class<?> type, String variant) {
		return (variant != null ? type.getName() + '.' + variant : type.getName());
	}
}
