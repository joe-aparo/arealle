package net.jsa.crib.ds.api;

/**
 * Interface for converting between "logical" and "native" values.
 *
 * Logical values are those whose types always match one of the designated
 * logical types.  Native values are those of any type.  Conversions between
 * the, represented by instances of this interface, are maintained in the
 * logical type registry.
 * 
 * @author jsaparo
 *
 */
public interface ILogicalTypeConverter {
	/**
	 * Convert a logical value to its native value.
	 * 
	 * @param logicalValue Logical value to convert
	 *
	 * @return A native value
	 */
	Object toNativeValue(Object logicalValue);
	
	/**
	 * Convert a native value to its logical value.
	 * 
	 * @param nativeValue Native value to convert
	 *
	 * @return A logical value
	 */
	Object toLogicalValue(Object nativeValue);
}
