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
import ind.jsa.crib.ds.internal.type.convert.core.ToBgdUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToBgiUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToBoolUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToByteUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToCalUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToChrUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToDblUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToDtUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToFltUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToIntUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToLngUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToShrtUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToStrUtils;
import ind.jsa.crib.ds.internal.type.convert.core.ToTsUtils;

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
		typeManager.registerType(BsonDocument.class, StdTypeManagerPlugin.COMPOSITE_NATURE);
	}
	
	/*
	 * Register conversions from String.
	 */
	private void registerStringConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonString.class, Character.class, 
			(Object val) -> val != null ? ToChrUtils.str2Chr(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, Byte.class, 
			(Object val) -> val != null ? ToByteUtils.str2Byte(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, Short.class, 
			(Object val) -> val != null ? ToShrtUtils.str2Shrt(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, Integer.class, 
			(Object val) -> val != null ? ToIntUtils.str2Int(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, Long.class, 
			(Object val) -> val != null ? ToLngUtils.str2Lng(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, BigInteger.class, 
			(Object val) -> val != null ? ToBgiUtils.str2Bgi(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, Float.class, 
			(Object val) -> val != null ? ToFltUtils.str2Flt(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, Double.class, 
			(Object val) -> val != null ? ToDblUtils.str2Dbl(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, BigDecimal.class, 
			(Object val) -> val != null ? ToBgdUtils.str2Bgd(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, GregorianCalendar.class, 
			(Object val) -> val != null ? ToCalUtils.str2Cal(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, Date.class, 
			(Object val) -> val != null ? ToDtUtils.str2Dt(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, Timestamp.class, 
			(Object val) -> val != null ? ToTsUtils.str2Ts(((BsonString) val).toString()) : null);
		typeManager.registerConverter(BsonString.class, Boolean.class, 
			(Object val) -> val != null ? ToBoolUtils.str2Bool(((BsonString) val).toString()) : null);
	}
	
	/*
	 * Register conversions from Integer.
	 */
	private void registerIntegerConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonInt32.class, String.class, 
			(Object val) -> val != null ? ToStrUtils.lng2Str(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, Byte.class, 
			(Object val) -> val != null ? ToByteUtils.lng2Byte(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, Character.class, 
			(Object val) -> val != null ? ToChrUtils.lng2Chr(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, Short.class, 
			(Object val) -> val != null ? ToShrtUtils.lng2Shrt(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, Integer.class, 
			(Object val) -> val != null ? ToIntUtils.lng2Int(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, Long.class, 
			(Object val) -> val != null ? Long.valueOf(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, BigInteger.class, 
			(Object val) -> val != null ? ToBgiUtils.lng2Bgi(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, Float.class, 
			(Object val) -> val != null ? ToFltUtils.lng2Flt(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, Double.class, 
			(Object val) -> val != null ? ToDblUtils.lng2Dbl(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, BigDecimal.class, 
			(Object val) -> val != null ? ToBgdUtils.lng2Bgd(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, GregorianCalendar.class, 
			(Object val) -> val != null ? ToCalUtils.lng2Cal(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, Date.class, 
			(Object val) -> val != null ? ToDtUtils.lng2Dt(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, Timestamp.class, 
			(Object val) -> val != null ? ToTsUtils.lng2Ts(((BsonInt32) val).longValue()) : null);
		typeManager.registerConverter(BsonInt32.class, Boolean.class, 
			(Object val) -> val != null ? ToBoolUtils.lng2Bool(((BsonInt32) val).longValue()) : null);
	}
	
	/*
	 * Register conversions from Long.
	 */
	private void registerLongConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonInt64.class, String.class, 
			(Object val) -> val != null ? ToStrUtils.lng2Str(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, Byte.class, 
			(Object val) -> val != null ? ToByteUtils.lng2Byte(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, Character.class, 
			(Object val) -> val != null ? ToChrUtils.lng2Chr(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, Short.class, 
			(Object val) -> val != null ? ToShrtUtils.lng2Shrt(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, Integer.class, 
			(Object val) -> val != null ? ToIntUtils.lng2Int(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, Long.class, 
			(Object val) -> val != null ? Long.valueOf(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, BigInteger.class, 
			(Object val) -> val != null ? ToBgiUtils.lng2Bgi(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, Float.class, 
			(Object val) -> val != null ? ToFltUtils.lng2Flt(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, Double.class, 
			(Object val) -> val != null ? ToDblUtils.lng2Dbl(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, BigDecimal.class, 
			(Object val) -> val != null ? ToBgdUtils.lng2Bgd(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, GregorianCalendar.class, 
			(Object val) -> val != null ? ToCalUtils.lng2Cal(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, Date.class, 
			(Object val) -> val != null ? ToDtUtils.lng2Dt(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, Timestamp.class, 
			(Object val) -> val != null ? ToTsUtils.lng2Ts(((BsonInt64) val).longValue()) : null);
		typeManager.registerConverter(BsonInt64.class, Boolean.class, 
			(Object val) -> val != null ? ToBoolUtils.lng2Bool(((BsonInt64) val).longValue()) : null);
	}
	
	/*
	 * Register conversions from Double.
	 */
	private void registerDoubleConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonDouble.class, String.class, 
			(Object val) -> val != null ? ToStrUtils.dbl2Str(((BsonDouble) val).doubleValue()) : null);
		typeManager.registerConverter(BsonDouble.class, Character.class, 
			(Object val) -> val != null ? ToChrUtils.dbl2Chr(((BsonDouble) val).doubleValue()) : null);
		typeManager.registerConverter(BsonDouble.class, Byte.class, 
			(Object val) -> val != null ? ToByteUtils.dbl2Byte(((BsonDouble) val).doubleValue()) : null);
		typeManager.registerConverter(BsonDouble.class, Short.class, 
			(Object val) -> val != null ? ToShrtUtils.dbl2Shrt(((BsonDouble) val).doubleValue()) : null);
		typeManager.registerConverter(BsonDouble.class, Integer.class, 
			(Object val) -> val != null ? ToIntUtils.dbl2Int(((BsonDouble) val).doubleValue()) : null);
		typeManager.registerConverter(BsonDouble.class, Long.class, 
			(Object val) -> val != null ? ToLngUtils.dbl2Lng(((BsonDouble) val).doubleValue()) : null);
		typeManager.registerConverter(BsonDouble.class, BigInteger.class, 
			(Object val) -> val != null ? ToBgiUtils.dbl2Bgi(((BsonDouble) val).doubleValue()) : null);
		typeManager.registerConverter(BsonDouble.class, Float.class, 
			(Object val) -> val != null ? ToFltUtils.dbl2Flt(((BsonDouble) val).doubleValue()) : null);
		typeManager.registerConverter(BsonDouble.class, BigDecimal.class, 
			(Object val) -> val != null ? ToBgdUtils.dbl2Bgd(((BsonDouble) val).doubleValue()) : null);
		typeManager.registerConverter(BsonDouble.class, Boolean.class, 
			(Object val) -> val != null ? ToBoolUtils.dbl2Bool(((BsonDouble) val).doubleValue()) : null);
	}
		
	/*
	 * Register conversions from Double.
	 */
	private void registeNumberConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonNumber.class, String.class, 
			(Object val) -> val != null ? ToStrUtils.dbl2Str(((BsonNumber) val).doubleValue()) : null);
		typeManager.registerConverter(BsonNumber.class, Character.class, 
			(Object val) -> val != null ? ToChrUtils.dbl2Chr(((BsonNumber) val).doubleValue()) : null);
		typeManager.registerConverter(BsonNumber.class, Byte.class, 
			(Object val) -> val != null ? ToByteUtils.dbl2Byte(((BsonNumber) val).doubleValue()) : null);
		typeManager.registerConverter(BsonNumber.class, Short.class, 
			(Object val) -> val != null ? ToShrtUtils.dbl2Shrt(((BsonNumber) val).doubleValue()) : null);
		typeManager.registerConverter(BsonNumber.class, Integer.class, 
			(Object val) -> val != null ? ToIntUtils.dbl2Int(((BsonNumber) val).doubleValue()) : null);
		typeManager.registerConverter(BsonNumber.class, Long.class, 
			(Object val) -> val != null ? ToLngUtils.dbl2Lng(((BsonNumber) val).doubleValue()) : null);
		typeManager.registerConverter(BsonNumber.class, BigInteger.class, 
			(Object val) -> val != null ? ToBgiUtils.dbl2Bgi(((BsonNumber) val).doubleValue()) : null);
		typeManager.registerConverter(BsonNumber.class, Float.class, 
			(Object val) -> val != null ? ToFltUtils.dbl2Flt(((BsonNumber) val).doubleValue()) : null);
		typeManager.registerConverter(BsonNumber.class, BigDecimal.class, 
			(Object val) -> val != null ? ToBgdUtils.dbl2Bgd(((BsonNumber) val).doubleValue()) : null);
		typeManager.registerConverter(BsonNumber.class, Boolean.class, 
			(Object val) -> val != null ? ToBoolUtils.dbl2Bool(((BsonNumber) val).doubleValue()) : null);
	}
		
	/*
	 * Register conversions from Date.
	 */
	private void registerDateConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonDateTime.class, String.class, 
			(Object val) -> val != null ? ToStrUtils.dt2Str(new Date(((BsonDateTime) val).getValue())) : null);
		typeManager.registerConverter(BsonDateTime.class, Long.class, 
			(Object val) -> val != null ? Long.valueOf(((BsonDateTime) val).getValue()) : null);
		typeManager.registerConverter(BsonDateTime.class, BigInteger.class, 
			(Object val) -> val != null ? ToBgiUtils.lng2Bgi(((BsonDateTime) val).getValue()) : null);
		typeManager.registerConverter(BsonDateTime.class, BigDecimal.class, 
			(Object val) -> val != null ? ToBgdUtils.lng2Bgd(((BsonDateTime) val).getValue()) : null);
		typeManager.registerConverter(BsonDateTime.class, Calendar.class, 
			(Object val) -> val != null ? ToCalUtils.lng2Cal(((BsonDateTime) val).getValue()) : null);
		typeManager.registerConverter(BsonDateTime.class, Timestamp.class, 
			(Object val) -> val != null ? ToTsUtils.lng2Ts(((BsonDateTime) val).getValue()) : null);
	}
	
	/*
	 * Register conversions from Boolean.
	 */
	private void registerBooleanConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonBoolean.class, String.class, 
			(Object val) -> val != null ? ToStrUtils.bool2Str(((BsonBoolean) val).getValue()) : null);
		typeManager.registerConverter(BsonBoolean.class, Byte.class, 
			(Object val) -> val != null ? ToByteUtils.bool2Byte(((BsonBoolean) val).getValue()) : null);
		typeManager.registerConverter(BsonBoolean.class, Character.class, 
			(Object val) -> val != null ? ToChrUtils.bool2Chr(((BsonBoolean) val).getValue()) : null);
		typeManager.registerConverter(BsonBoolean.class, Short.class, 
			(Object val) -> val != null ? ToShrtUtils.bool2Shrt(((BsonBoolean) val).getValue()) : null);
		typeManager.registerConverter(BsonBoolean.class, Integer.class, 
			(Object val) -> val != null ? ToIntUtils.bool2Int(((BsonBoolean) val).getValue()) : null);
		typeManager.registerConverter(BsonBoolean.class, Long.class, 
			(Object val) -> val != null ? ToLngUtils.bool2Lng(((BsonBoolean) val).getValue()) : null);
		typeManager.registerConverter(BsonBoolean.class, BigInteger.class, 
			(Object val) -> val != null ? ToBgiUtils.bool2Bgi(((BsonBoolean) val).getValue()) : null);
		typeManager.registerConverter(BsonBoolean.class, Float.class, 
			(Object val) -> val != null ? ToFltUtils.bool2Flt(((BsonBoolean) val).getValue()) : null);
		typeManager.registerConverter(BsonBoolean.class, Double.class, 
			(Object val) -> val != null ? ToDblUtils.bool2Dbl(((BsonBoolean) val).getValue()) : null);
		typeManager.registerConverter(BsonBoolean.class, BigDecimal.class, 
			(Object val) -> val != null ? ToBgdUtils.bool2Bgd(((BsonBoolean) val).getValue()) : null);
	}

	private void registerBsonDocConversions(ITypeManager typeManager) {
		// TODO Auto-generated method stub
		
	}

	private void registerBinaryConversions(ITypeManager typeManager) {
		// TODO Auto-generated method stub
		
	}
}
