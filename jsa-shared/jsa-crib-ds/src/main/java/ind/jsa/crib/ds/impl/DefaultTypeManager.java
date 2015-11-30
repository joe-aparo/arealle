package ind.jsa.crib.ds.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Component;

import net.jsa.crib.ds.impl.AbstractTypeManager;
import net.jsa.crib.ds.utils.type.ToBgdUtils;
import net.jsa.crib.ds.utils.type.ToBgiUtils;
import net.jsa.crib.ds.utils.type.ToBoolUtils;
import net.jsa.crib.ds.utils.type.ToByteUtils;
import net.jsa.crib.ds.utils.type.ToCalUtils;
import net.jsa.crib.ds.utils.type.ToChrUtils;
import net.jsa.crib.ds.utils.type.ToDblUtils;
import net.jsa.crib.ds.utils.type.ToDtUtils;
import net.jsa.crib.ds.utils.type.ToFltUtils;
import net.jsa.crib.ds.utils.type.ToIntUtils;
import net.jsa.crib.ds.utils.type.ToLngUtils;
import net.jsa.crib.ds.utils.type.ToShrtUtils;
import net.jsa.crib.ds.utils.type.ToStrUtils;
import net.jsa.crib.ds.utils.type.ToTsUtils;

/**
 * Standard type manager based on common java types and conversions.
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
	
	public DefaultTypeManager() {

		// Set up default type natures
		registerDefaultTypes();
		
		// Set up default conversions
		registerDefaultStringConversions();
		registerDefaultCharacterConversions();		
		registerDefaultByteConversions();
		registerDefaultShortConversions();
		registerDefaultIntegerConversions();		
		registerDefaultLongConversions();		
		registerDefaultBigIntegerConversions();		
		registerDefaultFloatConversions();		
		registerDefaultDoubleConversions();		
		registerDefaultBigDecimalConversions();		
		registerDefaultCalendarConversions();		
		registerDefaultDateConversions();		
		registerDefaultTimeStampConversions();
		registerDefaultBooleanConversions();
 	}
	
	private void registerDefaultTypes() {
		registerType(String.class, STRING_NATURE);
		registerType(Character.class, STRING_NATURE);
		registerType(Byte.class, INTEGER_NATURE);
		registerType(Short.class, INTEGER_NATURE);
		registerType(Integer.class, INTEGER_NATURE);
		registerType(Long.class, INTEGER_NATURE);
		registerType(BigInteger.class, INTEGER_NATURE);
		registerType(Float.class, DECIMAL_NATURE);
		registerType(Double.class, DECIMAL_NATURE);
		registerType(BigDecimal.class, DECIMAL_NATURE);
		registerType(GregorianCalendar.class, DATETIME_NATURE);
		registerType(Date.class, DATETIME_NATURE);
		registerType(Timestamp.class, DATETIME_NATURE);
		registerType(Boolean.class, BOOLEAN_NATURE);
	}
	
	private void registerDefaultStringConversions() {
		registerConverter(String.class, Character.class, (Object val) -> ToChrUtils.str2Chr((String) val));
		registerConverter(String.class, Byte.class, (Object val) -> ToByteUtils.str2Byte((String) val));
		registerConverter(String.class, Short.class, (Object val) -> ToShrtUtils.str2Shrt((String) val));
		registerConverter(String.class, Integer.class, (Object val) -> ToIntUtils.str2Int((String) val));
		registerConverter(String.class, Long.class, (Object val) -> ToLngUtils.str2Lng((String) val));
		registerConverter(String.class, BigInteger.class, (Object val) -> ToBgiUtils.str2Bgi((String) val));
		registerConverter(String.class, Float.class, (Object val) -> ToFltUtils.str2Flt((String) val));
		registerConverter(String.class, Double.class, (Object val) -> ToDblUtils.str2Dbl((String) val));
		registerConverter(String.class, BigDecimal.class, (Object val) -> ToBgdUtils.str2Bgd((String) val));
		registerConverter(String.class, GregorianCalendar.class, (Object val) -> ToCalUtils.str2Cal((String) val));
		registerConverter(String.class, Date.class, (Object val) -> ToDtUtils.str2Dt((String) val));
		registerConverter(String.class, Timestamp.class, (Object val) -> ToTsUtils.str2Ts((String) val));
		registerConverter(String.class, Boolean.class, (Object val) -> ToBoolUtils.str2Bool((String) val));
	}
	
	private void registerDefaultCharacterConversions() {
		registerConverter(Character.class, String.class, (Object val) -> ToStrUtils.chr2Str((Character) val));
		registerConverter(Character.class, Byte.class, (Object val) -> ToByteUtils.chr2Byte((Character) val));
		registerConverter(Character.class, Short.class, (Object val) -> ToShrtUtils.chr2Shrt((Character) val));
		registerConverter(Character.class, Integer.class, (Object val) -> ToIntUtils.chr2Int((Character) val));
		registerConverter(Character.class, Long.class, (Object val) -> ToLngUtils.chr2Lng((Character) val));
		registerConverter(Character.class, BigInteger.class, (Object val) -> ToBgiUtils.chr2Bgi((Character) val));
		registerConverter(Character.class, Float.class, (Object val) -> ToFltUtils.chr2Flt((Character) val));
		registerConverter(Character.class, Double.class, (Object val) -> ToDblUtils.chr2Dbl((Character) val));
		registerConverter(Character.class, BigDecimal.class, (Object val) -> ToBgdUtils.chr2Bgd((Character) val));
		registerConverter(Character.class, Boolean.class, (Object val) -> ToBoolUtils.chr2Bool((Character) val));
	}
	
	private void registerDefaultByteConversions() {
		registerConverter(Byte.class, String.class, (Object val) -> ToStrUtils.byte2Str((Byte) val));
		registerConverter(Byte.class, Character.class, (Object val) -> ToChrUtils.byte2Chr((Byte) val));
		registerConverter(Byte.class, Short.class, (Object val) -> ToShrtUtils.byte2Shrt((Byte) val));
		registerConverter(Byte.class, Integer.class, (Object val) -> ToIntUtils.byte2Int((Byte) val));
		registerConverter(Byte.class, Long.class, (Object val) -> ToLngUtils.byte2Lng((Byte) val));
		registerConverter(Byte.class, BigInteger.class, (Object val) -> ToBgiUtils.byte2Bgi((Byte) val));
		registerConverter(Byte.class, Float.class, (Object val) -> ToFltUtils.byte2Flt((Byte) val));
		registerConverter(Byte.class, Double.class, (Object val) -> ToDblUtils.byte2Dbl((Byte) val));
		registerConverter(Byte.class, BigDecimal.class, (Object val) -> ToBgdUtils.byte2Bgd((Byte) val));
		registerConverter(Byte.class, Boolean.class, (Object val) -> ToBoolUtils.byte2Bool((Byte) val));
	}
	
	private void registerDefaultShortConversions() {
		registerConverter(Short.class, String.class, (Object val) -> ToStrUtils.shrt2Str((Short) val));
		registerConverter(Short.class, Byte.class, (Object val) -> ToByteUtils.shrt2Byte((Short) val));
		registerConverter(Short.class, Character.class, (Object val) -> ToChrUtils.shrt2Chr((Short) val));
		registerConverter(Short.class, Integer.class, (Object val) -> ToIntUtils.shrt2Int((Short) val));
		registerConverter(Short.class, Long.class, (Object val) -> ToLngUtils.shrt2Lng((Short) val));
		registerConverter(Short.class, BigInteger.class, (Object val) -> ToBgiUtils.shrt2Bgi((Short) val));
		registerConverter(Short.class, Float.class, (Object val) -> ToFltUtils.shrt2Flt((Short) val));
		registerConverter(Short.class, Double.class, (Object val) -> ToDblUtils.shrt2Dbl((Short) val));
		registerConverter(Short.class, BigDecimal.class, (Object val) -> ToBgdUtils.shrt2Bgd((Short) val));
		registerConverter(Short.class, Boolean.class, (Object val) -> ToBoolUtils.shrt2Bool((Short) val));
	}
	
	private void registerDefaultIntegerConversions() {
		registerConverter(Integer.class, String.class, (Object val) -> ToStrUtils.int2Str((Integer) val));
		registerConverter(Integer.class, Byte.class, (Object val) -> ToByteUtils.int2Byte((Integer) val));
		registerConverter(Integer.class, Character.class, (Object val) -> ToChrUtils.int2Chr((Integer) val));
		registerConverter(Integer.class, Short.class, (Object val) -> ToShrtUtils.int2Shrt((Integer) val));
		registerConverter(Integer.class, Long.class, (Object val) -> ToLngUtils.int2Lng((Integer) val));
		registerConverter(Integer.class, BigInteger.class, (Object val) -> ToBgiUtils.int2Bgi((Integer) val));
		registerConverter(Integer.class, Float.class, (Object val) -> ToFltUtils.int2Flt((Integer) val));
		registerConverter(Integer.class, Double.class, (Object val) -> ToDblUtils.int2Dbl((Integer) val));
		registerConverter(Integer.class, BigDecimal.class, (Object val) -> ToBgdUtils.int2Bgd((Integer) val));
		registerConverter(Integer.class, Boolean.class, (Object val) -> ToBoolUtils.int2Bool((Integer) val));
	}
	
	private void registerDefaultLongConversions() {
		registerConverter(Long.class, String.class, (Object val) -> ToStrUtils.lng2Str((Long) val));
		registerConverter(Long.class, Character.class, (Object val) -> ToChrUtils.lng2Chr((Long) val));
		registerConverter(Long.class, Byte.class, (Object val) -> ToByteUtils.lng2Byte((Long) val));
		registerConverter(Long.class, Short.class, (Object val) -> ToShrtUtils.lng2Shrt((Long) val));
		registerConverter(Long.class, Integer.class, (Object val) -> ToIntUtils.lng2Int((Long) val));
		registerConverter(Long.class, BigInteger.class, (Object val) -> ToBgiUtils.lng2Bgi((Long) val));
		registerConverter(Long.class, Float.class, (Object val) -> ToFltUtils.lng2Flt((Long) val));
		registerConverter(Long.class, Double.class, (Object val) -> ToDblUtils.lng2Dbl((Long) val));
		registerConverter(Long.class, BigDecimal.class, (Object val) -> ToBgdUtils.lng2Bgd((Long) val));
		registerConverter(Long.class, GregorianCalendar.class, (Object val) -> ToCalUtils.lng2Cal((Long) val));
		registerConverter(Long.class, Date.class, (Object val) -> ToDtUtils.lng2Dt((Long) val));
		registerConverter(Long.class, Timestamp.class, (Object val) -> ToTsUtils.lng2Ts((Long) val));
		registerConverter(Long.class, Boolean.class, (Object val) -> ToBoolUtils.lng2Bool((Long) val));
	}
	
	private void registerDefaultBigIntegerConversions() {
		registerConverter(BigInteger.class, String.class, (Object val) -> ToStrUtils.bgi2Str((BigInteger) val));
		registerConverter(BigInteger.class, Character.class, (Object val) -> ToChrUtils.bgi2Chr((BigInteger) val));
		registerConverter(BigInteger.class, Byte.class, (Object val) -> ToByteUtils.bgi2Byte((BigInteger) val));
		registerConverter(BigInteger.class, Short.class, (Object val) -> ToShrtUtils.bgi2Shrt((BigInteger) val));
		registerConverter(BigInteger.class, Integer.class, (Object val) -> ToIntUtils.bgi2Int((BigInteger) val));
		registerConverter(BigInteger.class, Long.class, (Object val) -> ToLngUtils.bgi2Lng((BigInteger) val));
		registerConverter(BigInteger.class, Float.class, (Object val) -> ToFltUtils.bgi2Flt((BigInteger) val));
		registerConverter(BigInteger.class, Double.class, (Object val) -> ToDblUtils.bgi2Dbl((BigInteger) val));
		registerConverter(BigInteger.class, BigDecimal.class, (Object val) -> ToBgdUtils.bgi2Bgd((BigInteger) val));
		registerConverter(BigInteger.class, GregorianCalendar.class, (Object val) -> ToCalUtils.bgi2Cal((BigInteger) val));
		registerConverter(BigInteger.class, Date.class, (Object val) -> ToDtUtils.bgi2Dt((BigInteger) val));
		registerConverter(BigInteger.class, Timestamp.class, (Object val) -> ToTsUtils.bgi2Ts((BigInteger) val));
		registerConverter(BigInteger.class, Boolean.class, (Object val) -> ToBoolUtils.bgi2Bool((BigInteger) val));
	}
	
	private void registerDefaultFloatConversions() {
		registerConverter(Float.class, String.class, (Object val) -> ToStrUtils.flt2Str((Float) val));
		registerConverter(Float.class, Character.class, (Object val) -> ToChrUtils.flt2Chr((Float) val));
		registerConverter(Float.class, Byte.class, (Object val) -> ToByteUtils.flt2Byte((Float) val));
		registerConverter(Float.class, Short.class, (Object val) -> ToShrtUtils.flt2Shrt((Float) val));
		registerConverter(Float.class, Integer.class, (Object val) -> ToIntUtils.flt2Int((Float) val));
		registerConverter(Float.class, Long.class, (Object val) -> ToLngUtils.flt2Lng((Float) val));
		registerConverter(Float.class, BigInteger.class, (Object val) -> ToBgiUtils.flt2Bgi((Float) val));
		registerConverter(Float.class, Double.class, (Object val) -> ToDblUtils.flt2Dbl((Float) val));
		registerConverter(Float.class, BigDecimal.class, (Object val) -> ToBgdUtils.flt2Bgd((Float) val));
		registerConverter(Float.class, Boolean.class, (Object val) -> ToBoolUtils.flt2Bool((Float) val));
	}
	
	private void registerDefaultDoubleConversions() {
		registerConverter(Double.class, String.class, (Object val) -> ToStrUtils.dbl2Str((Double) val));
		registerConverter(Double.class, Character.class, (Object val) -> ToChrUtils.dbl2Chr((Double) val));
		registerConverter(Double.class, Byte.class, (Object val) -> ToByteUtils.dbl2Byte((Double) val));
		registerConverter(Double.class, Short.class, (Object val) -> ToShrtUtils.dbl2Shrt((Double) val));
		registerConverter(Double.class, Integer.class, (Object val) -> ToIntUtils.dbl2Int((Double) val));
		registerConverter(Double.class, Long.class, (Object val) -> ToLngUtils.dbl2Lng((Double) val));
		registerConverter(Double.class, BigInteger.class, (Object val) -> ToBgiUtils.dbl2Bgi((Double) val));
		registerConverter(Double.class, Float.class, (Object val) -> ToFltUtils.dbl2Flt((Double) val));
		registerConverter(Double.class, BigDecimal.class, (Object val) -> ToBgdUtils.dbl2Bgd((Double) val));
		registerConverter(Double.class, Boolean.class, (Object val) -> ToBoolUtils.dbl2Bool((Double) val));
	}
	
	private void registerDefaultBigDecimalConversions() {
		registerConverter(BigDecimal.class, String.class, (Object val) -> ToStrUtils.bgd2Str((BigDecimal) val));
		registerConverter(BigDecimal.class, Character.class, (Object val) -> ToChrUtils.bgd2Chr((BigDecimal) val));
		registerConverter(BigDecimal.class, Byte.class, (Object val) -> ToByteUtils.bgd2Byte((BigDecimal) val));
		registerConverter(BigDecimal.class, Short.class, (Object val) -> ToShrtUtils.bgd2Shrt((BigDecimal) val));
		registerConverter(BigDecimal.class, Integer.class, (Object val) -> ToIntUtils.bgd2Int((BigDecimal) val));
		registerConverter(BigDecimal.class, Long.class, (Object val) -> ToLngUtils.bgd2Lng((BigDecimal) val));
		registerConverter(BigDecimal.class, BigInteger.class, (Object val) -> ToBgiUtils.bgd2Bgi((BigDecimal) val));
		registerConverter(BigDecimal.class, Double.class, (Object val) -> ToDblUtils.bgd2Dbl((BigDecimal) val));
		registerConverter(BigDecimal.class, Float.class, (Object val) -> ToFltUtils.bgd2Flt((BigDecimal) val));
		registerConverter(BigDecimal.class, Boolean.class, (Object val) -> ToBoolUtils.bgd2Bool((BigDecimal) val));
	}
	
	private void registerDefaultCalendarConversions() {
		registerConverter(Calendar.class, String.class, (Object val) -> ToStrUtils.cal2Str((Calendar) val));
		registerConverter(Calendar.class, Long.class, (Object val) -> ToLngUtils.cal2Lng((Calendar) val));
		registerConverter(Calendar.class, BigInteger.class, (Object val) -> ToBgiUtils.cal2Bgi((Calendar) val));
		registerConverter(Calendar.class, BigDecimal.class, (Object val) -> ToBgdUtils.cal2Bgd((Calendar) val));
		registerConverter(Calendar.class, Date.class, (Object val) -> ToDtUtils.cal2Dt((Calendar) val));
		registerConverter(Calendar.class, Timestamp.class, (Object val) -> ToTsUtils.cal2Ts((Calendar) val));
	}
	
	private void registerDefaultDateConversions() {
		registerConverter(Date.class, String.class, (Object val) -> ToStrUtils.dt2Str((Date) val));
		registerConverter(Date.class, Long.class, (Object val) -> ToLngUtils.dt2Lng((Date) val));
		registerConverter(Date.class, BigInteger.class, (Object val) -> ToBgiUtils.dt2Bgi((Date) val));
		registerConverter(Date.class, BigDecimal.class, (Object val) -> ToBgdUtils.dt2Bgd((Date) val));
		registerConverter(Date.class, Calendar.class, (Object val) -> ToCalUtils.dt2Cal((Date) val));
		registerConverter(Date.class, Timestamp.class, (Object val) -> ToTsUtils.dt2Ts((Date) val));
	}
	
	private void registerDefaultTimeStampConversions() {
		registerConverter(Timestamp.class, String.class, (Object val) -> ToStrUtils.ts2Str((Timestamp) val));
		registerConverter(Timestamp.class, Long.class, (Object val) -> ToLngUtils.ts2Lng((Timestamp) val));
		registerConverter(Timestamp.class, BigInteger.class, (Object val) -> ToBgiUtils.ts2Bgi((Timestamp) val));
		registerConverter(Timestamp.class, BigDecimal.class, (Object val) -> ToBgdUtils.ts2Bgd((Timestamp) val));
		registerConverter(Timestamp.class, Calendar.class, (Object val) -> ToCalUtils.ts2Cal((Timestamp) val));
		registerConverter(Timestamp.class, Date.class, (Object val) -> ToDtUtils.ts2Dt((Timestamp) val));
	}

	private void registerDefaultBooleanConversions() {
		registerConverter(Boolean.class, String.class, (Object val) -> ToStrUtils.bool2Str((Boolean) val));
		registerConverter(Boolean.class, Byte.class, (Object val) -> ToByteUtils.bool2Byte((Boolean) val));		
		registerConverter(Boolean.class, Character.class, (Object val) -> ToChrUtils.bool2Chr((Boolean) val));
		registerConverter(Boolean.class, Short.class, (Object val) -> ToShrtUtils.bool2Shrt((Boolean) val));
		registerConverter(Boolean.class, Integer.class, (Object val) -> ToIntUtils.bool2Int((Boolean) val));
		registerConverter(Boolean.class, Long.class, (Object val) -> ToLngUtils.bool2Lng((Boolean) val));
		registerConverter(Boolean.class, BigInteger.class, (Object val) -> ToBgiUtils.bool2Bgi((Boolean) val));
		registerConverter(Boolean.class, Float.class, (Object val) -> ToFltUtils.bool2Flt((Boolean) val));
		registerConverter(Boolean.class, Double.class, (Object val) -> ToDblUtils.bool2Dbl((Boolean) val));
		registerConverter(Boolean.class, BigDecimal.class, (Object val) -> ToBgdUtils.bool2Bgd((Boolean) val));
	}
}