package ind.jsa.crib.ds.internal.type.utils.std;

import net.jsa.crib.ds.api.ILogicalTypeConverter;

/**
 * Simple converter implementation that does nothing
 * but pass given values through.
 * 
 * @author jsaparo
 *
 */
public class PassThru implements ILogicalTypeConverter {

	static PassThru cvt = new PassThru();
	
	private PassThru() {
	}
	
	public static PassThru instance() {
		return cvt;
	}
	
	@Override
	public Object toNativeValue(Object logicalValue) {
		return logicalValue;
	}

	@Override
	public Object toLogicalValue(Object nativeValue) {
		return nativeValue;
	}

}
