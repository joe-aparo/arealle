package ind.jsa.crib.ds.internal.type.plugins;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bson.BsonBinary;
import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;
import org.bson.Document;

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

public class BsonTypeManagerPlugin implements ITypeManagerPlugin {

	@Override
	public void register(ITypeManager typeManager) {
		registerTypes(typeManager);
		
		// Set up default conversions
		registerStringConversions(typeManager);
		registerIntegerConversions(typeManager);		
		registerLongConversions(typeManager);			
		registerDoubleConversions(typeManager);
		registeNumberConversions(typeManager);
		registerDateConversions(typeManager);		
		registerBooleanConversions(typeManager);
		registerBinaryConversions(typeManager);
		registerBsonDocConversions(typeManager);
		registerDocConversions(typeManager);
	}

	/*
	 * Register default types and natures.
	 */
	private void registerTypes(ITypeManager typeManager) {
		typeManager.registerType(BsonString.class, StdTypeManagerPlugin.STRING_NATURE);
		typeManager.registerType(BsonInt32.class, StdTypeManagerPlugin.INTEGER_NATURE);
		typeManager.registerType(BsonInt64.class, StdTypeManagerPlugin.INTEGER_NATURE);
		typeManager.registerType(BsonDouble.class, StdTypeManagerPlugin.DECIMAL_NATURE);
		typeManager.registerType(BsonNumber.class, StdTypeManagerPlugin.DECIMAL_NATURE);
		typeManager.registerType(BsonDateTime.class, StdTypeManagerPlugin.DATETIME_NATURE);
		typeManager.registerType(BsonBoolean.class, StdTypeManagerPlugin.BOOLEAN_NATURE);
		
		typeManager.registerType(BsonBinary.class, StdTypeManagerPlugin.BINARY_NATURE);
		typeManager.registerType(Document.class, StdTypeManagerPlugin.COMPOSITE_NATURE);
		typeManager.registerType(BsonDocument.class, StdTypeManagerPlugin.COMPOSITE_NATURE);
	}
	
	/*
	 * Register conversions from String.
	 */
	private void registerStringConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonString.class, Character.class, (Object val) -> ToChrUtils.str2Chr((String) val));
		typeManager.registerConverter(BsonString.class, Byte.class, (Object val) -> ToByteUtils.str2Byte((String) val));
		typeManager.registerConverter(BsonString.class, Short.class, (Object val) -> ToShrtUtils.str2Shrt((String) val));
		typeManager.registerConverter(BsonString.class, Integer.class, (Object val) -> ToIntUtils.str2Int((String) val));
		typeManager.registerConverter(BsonString.class, Long.class, (Object val) -> ToLngUtils.str2Lng((String) val));
		typeManager.registerConverter(BsonString.class, BigInteger.class, (Object val) -> ToBgiUtils.str2Bgi((String) val));
		typeManager.registerConverter(BsonString.class, Float.class, (Object val) -> ToFltUtils.str2Flt((String) val));
		typeManager.registerConverter(BsonString.class, Double.class, (Object val) -> ToDblUtils.str2Dbl((String) val));
		typeManager.registerConverter(BsonString.class, BigDecimal.class, (Object val) -> ToBgdUtils.str2Bgd((String) val));
		typeManager.registerConverter(BsonString.class, GregorianCalendar.class, (Object val) -> ToCalUtils.str2Cal((String) val));
		typeManager.registerConverter(BsonString.class, Date.class, (Object val) -> ToDtUtils.str2Dt((String) val));
		typeManager.registerConverter(BsonString.class, Timestamp.class, (Object val) -> ToTsUtils.str2Ts((String) val));
		typeManager.registerConverter(BsonString.class, Boolean.class, (Object val) -> ToBoolUtils.str2Bool((String) val));
	}
	
	/*
	 * Register conversions from Integer.
	 */
	private void registerIntegerConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonInt32.class, String.class, (Object val) -> ToStrUtils.int2Str((Integer) val));
		typeManager.registerConverter(BsonInt32.class, Byte.class, (Object val) -> ToByteUtils.int2Byte((Integer) val));
		typeManager.registerConverter(BsonInt32.class, Character.class, (Object val) -> ToChrUtils.int2Chr((Integer) val));
		typeManager.registerConverter(BsonInt32.class, Short.class, (Object val) -> ToShrtUtils.int2Shrt((Integer) val));
		typeManager.registerConverter(BsonInt32.class, Long.class, (Object val) -> ToLngUtils.int2Lng((Integer) val));
		typeManager.registerConverter(BsonInt32.class, BigInteger.class, (Object val) -> ToBgiUtils.int2Bgi((Integer) val));
		typeManager.registerConverter(BsonInt32.class, Float.class, (Object val) -> ToFltUtils.int2Flt((Integer) val));
		typeManager.registerConverter(BsonInt32.class, Double.class, (Object val) -> ToDblUtils.int2Dbl((Integer) val));
		typeManager.registerConverter(BsonInt32.class, BigDecimal.class, (Object val) -> ToBgdUtils.int2Bgd((Integer) val));
		typeManager.registerConverter(BsonInt32.class, Boolean.class, (Object val) -> ToBoolUtils.int2Bool((Integer) val));
	}
	
	/*
	 * Register conversions from Long.
	 */
	private void registerLongConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonInt64.class, String.class, (Object val) -> ToStrUtils.lng2Str((Long) val));
		typeManager.registerConverter(BsonInt64.class, Character.class, (Object val) -> ToChrUtils.lng2Chr((Long) val));
		typeManager.registerConverter(BsonInt64.class, Byte.class, (Object val) -> ToByteUtils.lng2Byte((Long) val));
		typeManager.registerConverter(BsonInt64.class, Short.class, (Object val) -> ToShrtUtils.lng2Shrt((Long) val));
		typeManager.registerConverter(BsonInt64.class, Integer.class, (Object val) -> ToIntUtils.lng2Int((Long) val));
		typeManager.registerConverter(BsonInt64.class, BigInteger.class, (Object val) -> ToBgiUtils.lng2Bgi((Long) val));
		typeManager.registerConverter(BsonInt64.class, Float.class, (Object val) -> ToFltUtils.lng2Flt((Long) val));
		typeManager.registerConverter(BsonInt64.class, Double.class, (Object val) -> ToDblUtils.lng2Dbl((Long) val));
		typeManager.registerConverter(BsonInt64.class, BigDecimal.class, (Object val) -> ToBgdUtils.lng2Bgd((Long) val));
		typeManager.registerConverter(BsonInt64.class, GregorianCalendar.class, (Object val) -> ToCalUtils.lng2Cal((Long) val));
		typeManager.registerConverter(BsonInt64.class, Date.class, (Object val) -> ToDtUtils.lng2Dt((Long) val));
		typeManager.registerConverter(BsonInt64.class, Timestamp.class, (Object val) -> ToTsUtils.lng2Ts((Long) val));
		typeManager.registerConverter(BsonInt64.class, Boolean.class, (Object val) -> ToBoolUtils.lng2Bool((Long) val));
	}
	
	/*
	 * Register conversions from Double.
	 */
	private void registerDoubleConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonDouble.class, String.class, (Object val) -> ToStrUtils.dbl2Str((Double) val));
		typeManager.registerConverter(BsonDouble.class, Character.class, (Object val) -> ToChrUtils.dbl2Chr((Double) val));
		typeManager.registerConverter(BsonDouble.class, Byte.class, (Object val) -> ToByteUtils.dbl2Byte((Double) val));
		typeManager.registerConverter(BsonDouble.class, Short.class, (Object val) -> ToShrtUtils.dbl2Shrt((Double) val));
		typeManager.registerConverter(BsonDouble.class, Integer.class, (Object val) -> ToIntUtils.dbl2Int((Double) val));
		typeManager.registerConverter(BsonDouble.class, Long.class, (Object val) -> ToLngUtils.dbl2Lng((Double) val));
		typeManager.registerConverter(BsonDouble.class, BigInteger.class, (Object val) -> ToBgiUtils.dbl2Bgi((Double) val));
		typeManager.registerConverter(BsonDouble.class, Float.class, (Object val) -> ToFltUtils.dbl2Flt((Double) val));
		typeManager.registerConverter(BsonDouble.class, BigDecimal.class, (Object val) -> ToBgdUtils.dbl2Bgd((Double) val));
		typeManager.registerConverter(BsonDouble.class, Boolean.class, (Object val) -> ToBoolUtils.dbl2Bool((Double) val));
	}
		
	/*
	 * Register conversions from Double.
	 */
	private void registeNumberConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonNumber.class, String.class, (Object val) -> ToStrUtils.dbl2Str((Double) val));
		typeManager.registerConverter(BsonNumber.class, Character.class, (Object val) -> ToChrUtils.dbl2Chr((Double) val));
		typeManager.registerConverter(BsonNumber.class, Byte.class, (Object val) -> ToByteUtils.dbl2Byte((Double) val));
		typeManager.registerConverter(BsonNumber.class, Short.class, (Object val) -> ToShrtUtils.dbl2Shrt((Double) val));
		typeManager.registerConverter(BsonNumber.class, Integer.class, (Object val) -> ToIntUtils.dbl2Int((Double) val));
		typeManager.registerConverter(BsonNumber.class, Long.class, (Object val) -> ToLngUtils.dbl2Lng((Double) val));
		typeManager.registerConverter(BsonNumber.class, BigInteger.class, (Object val) -> ToBgiUtils.dbl2Bgi((Double) val));
		typeManager.registerConverter(BsonNumber.class, Float.class, (Object val) -> ToFltUtils.dbl2Flt((Double) val));
		typeManager.registerConverter(BsonNumber.class, BigDecimal.class, (Object val) -> ToBgdUtils.dbl2Bgd((Double) val));
		typeManager.registerConverter(BsonNumber.class, Boolean.class, (Object val) -> ToBoolUtils.dbl2Bool((Double) val));
	}
		
	/*
	 * Register conversions from Date.
	 */
	private void registerDateConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonDateTime.class, String.class, (Object val) -> ToStrUtils.dt2Str((Date) val));
		typeManager.registerConverter(BsonDateTime.class, Long.class, (Object val) -> ToLngUtils.dt2Lng((Date) val));
		typeManager.registerConverter(BsonDateTime.class, BigInteger.class, (Object val) -> ToBgiUtils.dt2Bgi((Date) val));
		typeManager.registerConverter(BsonDateTime.class, BigDecimal.class, (Object val) -> ToBgdUtils.dt2Bgd((Date) val));
		typeManager.registerConverter(BsonDateTime.class, Calendar.class, (Object val) -> ToCalUtils.dt2Cal((Date) val));
		typeManager.registerConverter(BsonDateTime.class, Timestamp.class, (Object val) -> ToTsUtils.dt2Ts((Date) val));
	}
	
	/*
	 * Register conversions from Boolean.
	 */
	private void registerBooleanConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonBoolean.class, String.class, (Object val) -> ToStrUtils.bool2Str((Boolean) val));
		typeManager.registerConverter(BsonBoolean.class, Byte.class, (Object val) -> ToByteUtils.bool2Byte((Boolean) val));		
		typeManager.registerConverter(BsonBoolean.class, Character.class, (Object val) -> ToChrUtils.bool2Chr((Boolean) val));
		typeManager.registerConverter(BsonBoolean.class, Short.class, (Object val) -> ToShrtUtils.bool2Shrt((Boolean) val));
		typeManager.registerConverter(BsonBoolean.class, Integer.class, (Object val) -> ToIntUtils.bool2Int((Boolean) val));
		typeManager.registerConverter(BsonBoolean.class, Long.class, (Object val) -> ToLngUtils.bool2Lng((Boolean) val));
		typeManager.registerConverter(BsonBoolean.class, BigInteger.class, (Object val) -> ToBgiUtils.bool2Bgi((Boolean) val));
		typeManager.registerConverter(BsonBoolean.class, Float.class, (Object val) -> ToFltUtils.bool2Flt((Boolean) val));
		typeManager.registerConverter(BsonBoolean.class, Double.class, (Object val) -> ToDblUtils.bool2Dbl((Boolean) val));
		typeManager.registerConverter(BsonBoolean.class, BigDecimal.class, (Object val) -> ToBgdUtils.bool2Bgd((Boolean) val));
	}

	private void registerDocConversions(ITypeManager typeManager) {
		// TODO Auto-generated method stub
		
	}

	private void registerBsonDocConversions(ITypeManager typeManager) {
		// TODO Auto-generated method stub
		
	}

	private void registerBinaryConversions(ITypeManager typeManager) {
		// TODO Auto-generated method stub
		
	}
}
