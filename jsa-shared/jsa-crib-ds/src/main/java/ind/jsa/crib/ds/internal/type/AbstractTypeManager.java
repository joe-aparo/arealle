package ind.jsa.crib.ds.internal.type;

import ind.jsa.crib.ds.api.ITypeManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.util.CollectionUtils;

/**
 * Base class for ITypeManager implementations. Supports the idea of 'plugins'
 * that user used to initialize the type manager in a flexible manner.
 * 
 * @author jo26419
 *
 */
public abstract class AbstractTypeManager implements ITypeManager {

	private Map<String, Long> typeNatures = new HashMap<String, Long>();
	private Map<String, Map<String, Function<Object, Object>>> typeConverters = 
			new HashMap<String, Map<String, Function<Object, Object>>>();

	public List<ITypeManagerPlugin> plugins = new ArrayList<ITypeManagerPlugin>();
	
	/**
	 * Sets the plugins to be used for initialization. Must be called before the
	 * call to initialize.
	 * 
	 * @param plugins
	 */
	public void addPlugins(List<ITypeManagerPlugin> plugins) {
		this.plugins.addAll(plugins);
	}
	
	/**
	 * Add an individuall plugin. Must be called before the call to initialize.
	 * 
	 * @param plugin
	 */
	public void addPlugin(ITypeManagerPlugin plugin) {
		this.plugins.add(plugin);
	}
	
	/**
	 * Initialize the type manager by registering any associated plugins.
	 */
	public void initialize() {
		if (!CollectionUtils.isEmpty(plugins)) {
			for (ITypeManagerPlugin plugin : plugins) {
				plugin.register(this);
			}
		}
	}
	
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
	
	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.ITypeManager#convert(java.lang.Object, java.lang.Class)
	 */
	@Override
	public Object convert(Object val, Class<?> toType) {
		return convert(val, null, toType, null);
	}

	private String makeVariantKey(Class<?> type, String variant) {
		return (variant != null ? type.getName() + '.' + variant : type.getName());
	}
}
