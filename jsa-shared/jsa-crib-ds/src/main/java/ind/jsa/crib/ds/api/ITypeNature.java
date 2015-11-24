package ind.jsa.crib.ds.api;

public interface ITypeNature {
	public static final byte SCALAR = 0x01;
	public static final byte INTEGRAL = 0x02;
	public static final byte FRACTIONAL = 0x04;
	public static final byte SEQUENCED = 0x08;
	public static final byte ATOMIC = 0x10;
}
