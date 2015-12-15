package ind.jsa.crib.ds.internal.type;

import ind.jsa.crib.ds.internal.type.plugins.TypeManagerPluginCollection;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Type manager that registers common java types and conversions.
 * 
 * @author jo26419
 *
 */
@Component(value="defaultTypeManager")
public class DefaultTypeManager extends AbstractTypeManager {
	
	// Granular natures
	public static final long ATOMIC_NATURE = 		0x01;
	public static final long SCALAR_NATURE = 		0x02;
	public static final long INTEGRAL_NATURE = 		0x04;
	public static final long FRACTIONAL_NATURE = 	0x08;
	public static final long SEQUENCED_NATURE = 	0x10;
	public static final long BINARY_NATURE = 		0x20;
	public static final long CHARACTER_NATURE = 	0x40;
	public static final long DATE_NATURE = 			0x80;
	public static final long TIME_NATURE = 		  0x0100;
	public static final long COMPOSITE_NATURE =   0x0200;
	
	// Aggregate natures
	public static final long NUMERIC_NATURE = ATOMIC_NATURE & SCALAR_NATURE;
	public static final long INTEGER_NATURE = NUMERIC_NATURE & INTEGRAL_NATURE;
	public static final long DECIMAL_NATURE = NUMERIC_NATURE & FRACTIONAL_NATURE;
	public static final long STRING_NATURE = ATOMIC_NATURE & SEQUENCED_NATURE & CHARACTER_NATURE;
	public static final long DATETIME_NATURE = INTEGER_NATURE & DATE_NATURE & TIME_NATURE;
	public static final long BOOLEAN_NATURE = ATOMIC_NATURE & BINARY_NATURE;
	
	@Resource(name="stdTypeManagerPlugin")
	private ITypeManagerPlugin stdTypeManagerPlugin;
	
	@Autowired(required=false)
	private TypeManagerPluginCollection pluginCollection;
	
	/**
	 * Manually set standard type manager plugin. Not needed if using Spring IOC.
	 * The standard plugin is already wired in above.
	 * 
	 * @param stdTypeManagerPlugin
	 */
	public void setStdTypeManagerPlugin(ITypeManagerPlugin stdTypeManagerPlugin) {
		this.stdTypeManagerPlugin = stdTypeManagerPlugin;
	}
	
	@PostConstruct
	public void initialize() {
		// Add standard plugin
		if (stdTypeManagerPlugin != null) {
			addPlugin(stdTypeManagerPlugin);
		}
		
		// Add any configured plugins
		if (pluginCollection != null) {
			addPlugins(pluginCollection.getPlugins());
		}
		
		// Initialize with plugins
		super.initialize();
	}
	
	/**
	 * Determine whether given nature is atomic.
	 * 
	 * @param nature Given type nature
	 * @return An indicator
	 */
	public static boolean isAtomicNature(long nature) {
		return (nature & ATOMIC_NATURE) != 0;
	}

	/**
	 * Determine whether given nature is numeric.
	 * 
	 * @param nature Given type nature
	 * @return An indicator
	 */
	public static boolean isNumericNature(long nature) {
		return (nature & NUMERIC_NATURE) != 0;
	}
	
	/**
	 * Determine whether given nature is integral.
	 * 
	 * @param nature Given type nature
	 * @return An indicator
	 */
	public static boolean isIntegerNature(long nature) {
		return (nature & INTEGER_NATURE) != 0;
	}
	
	/**
	 * Determine whether given nature is decimal.
	 * 
	 * @param nature Given type nature
	 * @return An indicator
	 */
	public static boolean isDecimalNature(long nature) {
		return (nature & DECIMAL_NATURE) != 0;
	}
	
	/**
	 * Determine whether given nature is String.
	 * 
	 * @param nature Given type nature
	 * @return An indicator
	 */
	public static boolean isStringNature(long nature) {
		return (nature & STRING_NATURE) != 0;
	}
	
	/**
	 * Determine whether given nature is date/time.
	 * 
	 * @param nature Given type nature
	 * @return An indicator
	 */
	public static boolean isDateTimeNature(long nature) {
		return (nature & DATETIME_NATURE) != 0;
	}
	
	/**
	 * Determine whether given nature is boolean.
	 * 
	 * @param nature Given type nature
	 * @return An indicator
	 */
	public static boolean isBooleanNature(long nature) {
		return (nature & BOOLEAN_NATURE) != 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.ITypeManager#compareValues(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compareValues(Object val1, Object val2) {
		return compareValues(val1, null, val2, null);
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.ITypeManager#compareValues(java.lang.Object, java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public int compareValues(Object val1, String variant1, Object val2,	String variant2) {
		
		// Default to equality
        int cmp = 0;

        // If one of the values is null, perform simplified null comparison logic
        if (val2 == null || val2 == null) {
        	cmp = cmpNull(val1, val2); // punt
        } else {
        	long nature1 = getTypeNature(val1.getClass());
        	long nature2 = getTypeNature(val2.getClass());
        	
        	// Attempt to treat as scalar comparison
	        if (isNumericNature(nature1) && isNumericNature(nature2)) {
	        	long sc1 = (Long) convert(val1, variant1, Long.class, null);
	        	long sc2 = (Long) convert(val2, variant2, Long.class, null);
	        	
	        	return sc1 < sc2 ? -1 : (sc1 > sc2 ? 1 : 0);
	        } else { // Treat as string comparison
	        	String str1 = (String) convert(val1, variant1, String.class, null);
	        	String str2 = (String) convert(val2, variant2, String.class, null);
	        	
	        	cmp = str1.compareTo(str2);
	        }
        }
        
        return cmp;
	}
	
	/*
	 * Support method fo comparing two arbitrarily null object values.
	 *  
	 * @param o1 First value
	 * @param o2 Second value
	 * @return Null comparison indicator
	 */
    private int cmpNull(Object o1, Object o2) {
    	int cmp = 0;
    	
        if (o1 == null && o2 == null) {
            cmp = 0;
        } else if (o1 == null && o2 != null) {
            cmp = -1;
        } else if (o1 != null && o2 == null) {
            cmp = 1;
        }
        
        return cmp;
    }
}
