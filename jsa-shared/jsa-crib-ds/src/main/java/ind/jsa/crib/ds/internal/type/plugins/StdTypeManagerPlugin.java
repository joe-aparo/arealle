package ind.jsa.crib.ds.internal.type.plugins;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Component;

import ind.jsa.crib.ds.api.ITypeManager;
import ind.jsa.crib.ds.api.ITypeManager.ITypeManagerPlugin;
import ind.jsa.crib.ds.internal.type.convert.std.ToBgdUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToBgiUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToBoolUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToByteUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToCalUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToChrUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToDblUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToDtUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToFltUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToIntUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToLngUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToShrtUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToStrUtils;
import ind.jsa.crib.ds.internal.type.convert.std.ToTsUtils;

/**
 * Configures standard types and converters in the type manager.
 * 
 * @author jo26419
 *
 */
@Component(value="stdTypeManagerPlugin")
public class StdTypeManagerPlugin implements ITypeManagerPlugin {
	// Granular natures
	public static final long ATOMIC_NATURE = 		0x01;
	public static final long SCALAR_NATURE = 		0x02;
	public static final long INTEGRAL_NATURE = 		0x04;
	public static final long FRACTIONAL_NATURE = 	0x08;
	public static final long SEQUENCED_NATURE = 	0x10;
	public static final long BIT_NATURE = 			0x20;
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
	public static final long BOOLEAN_NATURE = ATOMIC_NATURE & BIT_NATURE;
	public static final long BINARY_NATURE = ATOMIC_NATURE & BIT_NATURE & SEQUENCED_NATURE;
	
	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.ITypeManager.ITypeManagerPlugin#register(ind.jsa.crib.ds.api.ITypeManager)
	 */
	@Override
	public void register(ITypeManager typeManager) {
		// Set up default type natures
		registerTypes(typeManager);
		
		// Set up default conversions
		registerStringConversions(typeManager);
		registerCharacterConversions(typeManager);		
		registerByteConversions(typeManager);
		registerShortConversions(typeManager);
		registerIntegerConversions(typeManager);		
		registerLongConversions(typeManager);		
		registerBigIntegerConversions(typeManager);		
		registerFloatConversions(typeManager);		
		registerDoubleConversions(typeManager);		
		registerBigDecimalConversions(typeManager);		
		registerCalendarConversions(typeManager);		
		registerDateConversions(typeManager);		
		registerTimeStampConversions(typeManager);
		registerBooleanConversions(typeManager);
	}
	
	/*
	 * Register default types and natures.
	 */
	private void registerTypes(ITypeManager typeManager) {
		typeManager.registerType(String.class, STRING_NATURE);
		typeManager.registerType(Character.class, STRING_NATURE);
		typeManager.registerType(Byte.class, INTEGER_NATURE);
		typeManager.registerType(Short.class, INTEGER_NATURE);
		typeManager.registerType(Integer.class, INTEGER_NATURE);
		typeManager.registerType(Long.class, INTEGER_NATURE);
		typeManager.registerType(BigInteger.class, INTEGER_NATURE);
		typeManager.registerType(Float.class, DECIMAL_NATURE);
		typeManager.registerType(Double.class, DECIMAL_NATURE);
		typeManager.registerType(BigDecimal.class, DECIMAL_NATURE);
		typeManager.registerType(GregorianCalendar.class, DATETIME_NATURE);
		typeManager.registerType(Date.class, DATETIME_NATURE);
		typeManager.registerType(Timestamp.class, DATETIME_NATURE);
		typeManager.registerType(Boolean.class, BOOLEAN_NATURE);
	}
	
	/*
	 * Register conversions from String.
	 */
	private void registerStringConversions(ITypeManager typeManager) {
		typeManager.registerConverter(String.class, Character.class, (Object val) -> ToChrUtils.str2Chr((String) val));
		typeManager.registerConverter(String.class, Byte.class, (Object val) -> ToByteUtils.str2Byte((String) val));
		typeManager.registerConverter(String.class, Short.class, (Object val) -> ToShrtUtils.str2Shrt((String) val));
		typeManager.registerConverter(String.class, Integer.class, (Object val) -> ToIntUtils.str2Int((String) val));
		typeManager.registerConverter(String.class, Long.class, (Object val) -> ToLngUtils.str2Lng((String) val));
		typeManager.registerConverter(String.class, BigInteger.class, (Object val) -> ToBgiUtils.str2Bgi((String) val));
		typeManager.registerConverter(String.class, Float.class, (Object val) -> ToFltUtils.str2Flt((String) val));
		typeManager.registerConverter(String.class, Double.class, (Object val) -> ToDblUtils.str2Dbl((String) val));
		typeManager.registerConverter(String.class, BigDecimal.class, (Object val) -> ToBgdUtils.str2Bgd((String) val));
		typeManager.registerConverter(String.class, GregorianCalendar.class, (Object val) -> ToCalUtils.str2Cal((String) val));
		typeManager.registerConverter(String.class, Date.class, (Object val) -> ToDtUtils.str2Dt((String) val));
		typeManager.registerConverter(String.class, Timestamp.class, (Object val) -> ToTsUtils.str2Ts((String) val));
		typeManager.registerConverter(String.class, Boolean.class, (Object val) -> ToBoolUtils.str2Bool((String) val));
	}
	
	/*
	 * Register conversions from Character.
	 */
	private void registerCharacterConversions(ITypeManager typeManager) {
		typeManager.registerConverter(Character.class, String.class, (Object val) -> ToStrUtils.chr2Str((Character) val));
		typeManager.registerConverter(Character.class, Byte.class, (Object val) -> ToByteUtils.chr2Byte((Character) val));
		typeManager.registerConverter(Character.class, Short.class, (Object val) -> ToShrtUtils.chr2Shrt((Character) val));
		typeManager.registerConverter(Character.class, Integer.class, (Object val) -> ToIntUtils.chr2Int((Character) val));
		typeManager.registerConverter(Character.class, Long.class, (Object val) -> ToLngUtils.chr2Lng((Character) val));
		typeManager.registerConverter(Character.class, BigInteger.class, (Object val) -> ToBgiUtils.chr2Bgi((Character) val));
		typeManager.registerConverter(Character.class, Float.class, (Object val) -> ToFltUtils.chr2Flt((Character) val));
		typeManager.registerConverter(Character.class, Double.class, (Object val) -> ToDblUtils.chr2Dbl((Character) val));
		typeManager.registerConverter(Character.class, BigDecimal.class, (Object val) -> ToBgdUtils.chr2Bgd((Character) val));
		typeManager.registerConverter(Character.class, Boolean.class, (Object val) -> ToBoolUtils.chr2Bool((Character) val));
	}
	
	/*
	 * Register conversions from Byte.
	 */
	private void registerByteConversions(ITypeManager typeManager) {
		typeManager.registerConverter(Byte.class, String.class, (Object val) -> ToStrUtils.byte2Str((Byte) val));
		typeManager.registerConverter(Byte.class, Character.class, (Object val) -> ToChrUtils.byte2Chr((Byte) val));
		typeManager.registerConverter(Byte.class, Short.class, (Object val) -> ToShrtUtils.byte2Shrt((Byte) val));
		typeManager.registerConverter(Byte.class, Integer.class, (Object val) -> ToIntUtils.byte2Int((Byte) val));
		typeManager.registerConverter(Byte.class, Long.class, (Object val) -> ToLngUtils.byte2Lng((Byte) val));
		typeManager.registerConverter(Byte.class, BigInteger.class, (Object val) -> ToBgiUtils.byte2Bgi((Byte) val));
		typeManager.registerConverter(Byte.class, Float.class, (Object val) -> ToFltUtils.byte2Flt((Byte) val));
		typeManager.registerConverter(Byte.class, Double.class, (Object val) -> ToDblUtils.byte2Dbl((Byte) val));
		typeManager.registerConverter(Byte.class, BigDecimal.class, (Object val) -> ToBgdUtils.byte2Bgd((Byte) val));
		typeManager.registerConverter(Byte.class, Boolean.class, (Object val) -> ToBoolUtils.byte2Bool((Byte) val));
	}
	
	/*
	 * Register conversions from Short.
	 */
	private void registerShortConversions(ITypeManager typeManager) {
		typeManager.registerConverter(Short.class, String.class, (Object val) -> ToStrUtils.shrt2Str((Short) val));
		typeManager.registerConverter(Short.class, Byte.class, (Object val) -> ToByteUtils.shrt2Byte((Short) val));
		typeManager.registerConverter(Short.class, Character.class, (Object val) -> ToChrUtils.shrt2Chr((Short) val));
		typeManager.registerConverter(Short.class, Integer.class, (Object val) -> ToIntUtils.shrt2Int((Short) val));
		typeManager.registerConverter(Short.class, Long.class, (Object val) -> ToLngUtils.shrt2Lng((Short) val));
		typeManager.registerConverter(Short.class, BigInteger.class, (Object val) -> ToBgiUtils.shrt2Bgi((Short) val));
		typeManager.registerConverter(Short.class, Float.class, (Object val) -> ToFltUtils.shrt2Flt((Short) val));
		typeManager.registerConverter(Short.class, Double.class, (Object val) -> ToDblUtils.shrt2Dbl((Short) val));
		typeManager.registerConverter(Short.class, BigDecimal.class, (Object val) -> ToBgdUtils.shrt2Bgd((Short) val));
		typeManager.registerConverter(Short.class, Boolean.class, (Object val) -> ToBoolUtils.shrt2Bool((Short) val));
	}
	
	/*
	 * Register conversions from Integer.
	 */
	private void registerIntegerConversions(ITypeManager typeManager) {
		typeManager.registerConverter(Integer.class, String.class, (Object val) -> ToStrUtils.int2Str((Integer) val));
		typeManager.registerConverter(Integer.class, Byte.class, (Object val) -> ToByteUtils.int2Byte((Integer) val));
		typeManager.registerConverter(Integer.class, Character.class, (Object val) -> ToChrUtils.int2Chr((Integer) val));
		typeManager.registerConverter(Integer.class, Short.class, (Object val) -> ToShrtUtils.int2Shrt((Integer) val));
		typeManager.registerConverter(Integer.class, Long.class, (Object val) -> ToLngUtils.int2Lng((Integer) val));
		typeManager.registerConverter(Integer.class, BigInteger.class, (Object val) -> ToBgiUtils.int2Bgi((Integer) val));
		typeManager.registerConverter(Integer.class, Float.class, (Object val) -> ToFltUtils.int2Flt((Integer) val));
		typeManager.registerConverter(Integer.class, Double.class, (Object val) -> ToDblUtils.int2Dbl((Integer) val));
		typeManager.registerConverter(Integer.class, BigDecimal.class, (Object val) -> ToBgdUtils.int2Bgd((Integer) val));
		typeManager.registerConverter(Integer.class, Boolean.class, (Object val) -> ToBoolUtils.int2Bool((Integer) val));
	}
	
	/*
	 * Register conversions from Long.
	 */
	private void registerLongConversions(ITypeManager typeManager) {
		typeManager.registerConverter(Long.class, String.class, (Object val) -> ToStrUtils.lng2Str((Long) val));
		typeManager.registerConverter(Long.class, Character.class, (Object val) -> ToChrUtils.lng2Chr((Long) val));
		typeManager.registerConverter(Long.class, Byte.class, (Object val) -> ToByteUtils.lng2Byte((Long) val));
		typeManager.registerConverter(Long.class, Short.class, (Object val) -> ToShrtUtils.lng2Shrt((Long) val));
		typeManager.registerConverter(Long.class, Integer.class, (Object val) -> ToIntUtils.lng2Int((Long) val));
		typeManager.registerConverter(Long.class, BigInteger.class, (Object val) -> ToBgiUtils.lng2Bgi((Long) val));
		typeManager.registerConverter(Long.class, Float.class, (Object val) -> ToFltUtils.lng2Flt((Long) val));
		typeManager.registerConverter(Long.class, Double.class, (Object val) -> ToDblUtils.lng2Dbl((Long) val));
		typeManager.registerConverter(Long.class, BigDecimal.class, (Object val) -> ToBgdUtils.lng2Bgd((Long) val));
		typeManager.registerConverter(Long.class, GregorianCalendar.class, (Object val) -> ToCalUtils.lng2Cal((Long) val));
		typeManager.registerConverter(Long.class, Date.class, (Object val) -> ToDtUtils.lng2Dt((Long) val));
		typeManager.registerConverter(Long.class, Timestamp.class, (Object val) -> ToTsUtils.lng2Ts((Long) val));
		typeManager.registerConverter(Long.class, Boolean.class, (Object val) -> ToBoolUtils.lng2Bool((Long) val));
	}
	
	/*
	 * Register conversions from Integer.
	 */
	private void registerBigIntegerConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BigInteger.class, String.class, (Object val) -> ToStrUtils.bgi2Str((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, Character.class, (Object val) -> ToChrUtils.bgi2Chr((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, Byte.class, (Object val) -> ToByteUtils.bgi2Byte((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, Short.class, (Object val) -> ToShrtUtils.bgi2Shrt((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, Integer.class, (Object val) -> ToIntUtils.bgi2Int((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, Long.class, (Object val) -> ToLngUtils.bgi2Lng((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, Float.class, (Object val) -> ToFltUtils.bgi2Flt((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, Double.class, (Object val) -> ToDblUtils.bgi2Dbl((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, BigDecimal.class, (Object val) -> ToBgdUtils.bgi2Bgd((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, GregorianCalendar.class, (Object val) -> ToCalUtils.bgi2Cal((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, Date.class, (Object val) -> ToDtUtils.bgi2Dt((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, Timestamp.class, (Object val) -> ToTsUtils.bgi2Ts((BigInteger) val));
		typeManager.registerConverter(BigInteger.class, Boolean.class, (Object val) -> ToBoolUtils.bgi2Bool((BigInteger) val));
	}
	
	/*
	 * Register conversions from Float.
	 */
	private void registerFloatConversions(ITypeManager typeManager) {
		typeManager.registerConverter(Float.class, String.class, (Object val) -> ToStrUtils.flt2Str((Float) val));
		typeManager.registerConverter(Float.class, Character.class, (Object val) -> ToChrUtils.flt2Chr((Float) val));
		typeManager.registerConverter(Float.class, Byte.class, (Object val) -> ToByteUtils.flt2Byte((Float) val));
		typeManager.registerConverter(Float.class, Short.class, (Object val) -> ToShrtUtils.flt2Shrt((Float) val));
		typeManager.registerConverter(Float.class, Integer.class, (Object val) -> ToIntUtils.flt2Int((Float) val));
		typeManager.registerConverter(Float.class, Long.class, (Object val) -> ToLngUtils.flt2Lng((Float) val));
		typeManager.registerConverter(Float.class, BigInteger.class, (Object val) -> ToBgiUtils.flt2Bgi((Float) val));
		typeManager.registerConverter(Float.class, Double.class, (Object val) -> ToDblUtils.flt2Dbl((Float) val));
		typeManager.registerConverter(Float.class, BigDecimal.class, (Object val) -> ToBgdUtils.flt2Bgd((Float) val));
		typeManager.registerConverter(Float.class, Boolean.class, (Object val) -> ToBoolUtils.flt2Bool((Float) val));
	}
	
	/*
	 * Register conversions from Double.
	 */
	private void registerDoubleConversions(ITypeManager typeManager) {
		typeManager.registerConverter(Double.class, String.class, (Object val) -> ToStrUtils.dbl2Str((Double) val));
		typeManager.registerConverter(Double.class, Character.class, (Object val) -> ToChrUtils.dbl2Chr((Double) val));
		typeManager.registerConverter(Double.class, Byte.class, (Object val) -> ToByteUtils.dbl2Byte((Double) val));
		typeManager.registerConverter(Double.class, Short.class, (Object val) -> ToShrtUtils.dbl2Shrt((Double) val));
		typeManager.registerConverter(Double.class, Integer.class, (Object val) -> ToIntUtils.dbl2Int((Double) val));
		typeManager.registerConverter(Double.class, Long.class, (Object val) -> ToLngUtils.dbl2Lng((Double) val));
		typeManager.registerConverter(Double.class, BigInteger.class, (Object val) -> ToBgiUtils.dbl2Bgi((Double) val));
		typeManager.registerConverter(Double.class, Float.class, (Object val) -> ToFltUtils.dbl2Flt((Double) val));
		typeManager.registerConverter(Double.class, BigDecimal.class, (Object val) -> ToBgdUtils.dbl2Bgd((Double) val));
		typeManager.registerConverter(Double.class, Boolean.class, (Object val) -> ToBoolUtils.dbl2Bool((Double) val));
	}
	
	/*
	 * Register conversions from BigDecimal.
	 */
	private void registerBigDecimalConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BigDecimal.class, String.class, (Object val) -> ToStrUtils.bgd2Str((BigDecimal) val));
		typeManager.registerConverter(BigDecimal.class, Character.class, (Object val) -> ToChrUtils.bgd2Chr((BigDecimal) val));
		typeManager.registerConverter(BigDecimal.class, Byte.class, (Object val) -> ToByteUtils.bgd2Byte((BigDecimal) val));
		typeManager.registerConverter(BigDecimal.class, Short.class, (Object val) -> ToShrtUtils.bgd2Shrt((BigDecimal) val));
		typeManager.registerConverter(BigDecimal.class, Integer.class, (Object val) -> ToIntUtils.bgd2Int((BigDecimal) val));
		typeManager.registerConverter(BigDecimal.class, Long.class, (Object val) -> ToLngUtils.bgd2Lng((BigDecimal) val));
		typeManager.registerConverter(BigDecimal.class, BigInteger.class, (Object val) -> ToBgiUtils.bgd2Bgi((BigDecimal) val));
		typeManager.registerConverter(BigDecimal.class, Double.class, (Object val) -> ToDblUtils.bgd2Dbl((BigDecimal) val));
		typeManager.registerConverter(BigDecimal.class, Float.class, (Object val) -> ToFltUtils.bgd2Flt((BigDecimal) val));
		typeManager.registerConverter(BigDecimal.class, Boolean.class, (Object val) -> ToBoolUtils.bgd2Bool((BigDecimal) val));
	}
	
	/*
	 * Register conversions from Calendar.
	 */
	private void registerCalendarConversions(ITypeManager typeManager) {
		typeManager.registerConverter(Calendar.class, String.class, (Object val) -> ToStrUtils.cal2Str((Calendar) val));
		typeManager.registerConverter(Calendar.class, Long.class, (Object val) -> ToLngUtils.cal2Lng((Calendar) val));
		typeManager.registerConverter(Calendar.class, BigInteger.class, (Object val) -> ToBgiUtils.cal2Bgi((Calendar) val));
		typeManager.registerConverter(Calendar.class, BigDecimal.class, (Object val) -> ToBgdUtils.cal2Bgd((Calendar) val));
		typeManager.registerConverter(Calendar.class, Date.class, (Object val) -> ToDtUtils.cal2Dt((Calendar) val));
		typeManager.registerConverter(Calendar.class, Timestamp.class, (Object val) -> ToTsUtils.cal2Ts((Calendar) val));
	}
	
	/*
	 * Register conversions from Date.
	 */
	private void registerDateConversions(ITypeManager typeManager) {
		typeManager.registerConverter(Date.class, String.class, (Object val) -> ToStrUtils.dt2Str((Date) val));
		typeManager.registerConverter(Date.class, Long.class, (Object val) -> ToLngUtils.dt2Lng((Date) val));
		typeManager.registerConverter(Date.class, BigInteger.class, (Object val) -> ToBgiUtils.dt2Bgi((Date) val));
		typeManager.registerConverter(Date.class, BigDecimal.class, (Object val) -> ToBgdUtils.dt2Bgd((Date) val));
		typeManager.registerConverter(Date.class, Calendar.class, (Object val) -> ToCalUtils.dt2Cal((Date) val));
		typeManager.registerConverter(Date.class, Timestamp.class, (Object val) -> ToTsUtils.dt2Ts((Date) val));
	}
	
	/*
	 * Register conversions from Timestamp.
	 */
	private void registerTimeStampConversions(ITypeManager typeManager) {
		typeManager.registerConverter(Timestamp.class, String.class, (Object val) -> ToStrUtils.ts2Str((Timestamp) val));
		typeManager.registerConverter(Timestamp.class, Long.class, (Object val) -> ToLngUtils.ts2Lng((Timestamp) val));
		typeManager.registerConverter(Timestamp.class, BigInteger.class, (Object val) -> ToBgiUtils.ts2Bgi((Timestamp) val));
		typeManager.registerConverter(Timestamp.class, BigDecimal.class, (Object val) -> ToBgdUtils.ts2Bgd((Timestamp) val));
		typeManager.registerConverter(Timestamp.class, Calendar.class, (Object val) -> ToCalUtils.ts2Cal((Timestamp) val));
		typeManager.registerConverter(Timestamp.class, Date.class, (Object val) -> ToDtUtils.ts2Dt((Timestamp) val));
	}

	/*
	 * Register conversions from Boolean.
	 */
	private void registerBooleanConversions(ITypeManager typeManager) {
		typeManager.registerConverter(Boolean.class, String.class, (Object val) -> ToStrUtils.bool2Str((Boolean) val));
		typeManager.registerConverter(Boolean.class, Byte.class, (Object val) -> ToByteUtils.bool2Byte((Boolean) val));		
		typeManager.registerConverter(Boolean.class, Character.class, (Object val) -> ToChrUtils.bool2Chr((Boolean) val));
		typeManager.registerConverter(Boolean.class, Short.class, (Object val) -> ToShrtUtils.bool2Shrt((Boolean) val));
		typeManager.registerConverter(Boolean.class, Integer.class, (Object val) -> ToIntUtils.bool2Int((Boolean) val));
		typeManager.registerConverter(Boolean.class, Long.class, (Object val) -> ToLngUtils.bool2Lng((Boolean) val));
		typeManager.registerConverter(Boolean.class, BigInteger.class, (Object val) -> ToBgiUtils.bool2Bgi((Boolean) val));
		typeManager.registerConverter(Boolean.class, Float.class, (Object val) -> ToFltUtils.bool2Flt((Boolean) val));
		typeManager.registerConverter(Boolean.class, Double.class, (Object val) -> ToDblUtils.bool2Dbl((Boolean) val));
		typeManager.registerConverter(Boolean.class, BigDecimal.class, (Object val) -> ToBgdUtils.bool2Bgd((Boolean) val));
	}
}
