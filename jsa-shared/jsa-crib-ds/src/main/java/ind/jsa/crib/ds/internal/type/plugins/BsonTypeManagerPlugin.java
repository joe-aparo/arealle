package ind.jsa.crib.ds.internal.type.plugins;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.bson.BsonBinary;
import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNumber;
import org.bson.BsonString;

import ind.jsa.crib.ds.api.ITypeManager;
import ind.jsa.crib.ds.api.ITypeManager.ITypeManagerPlugin;
import ind.jsa.crib.ds.internal.type.convert.bson.ToBsonDocUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreBgdUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreBgiUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreBoolUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreByteUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreCalUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreChrUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreDblUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreDtUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreFltUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreIntUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreLngUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreShrtUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreStrUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreMapUtils;
import ind.jsa.crib.ds.internal.type.convert.bson.ToCoreTsUtils;

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
		registerBsonDocConversions(typeManager);
		registerMapConversion(typeManager);
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
			(Object val) -> ToCoreChrUtils.bsonStr2Chr((BsonString) val));
		typeManager.registerConverter(BsonString.class, Byte.class, 
			(Object val) -> ToCoreByteUtils.bsonStr2Byte((BsonString) val));
		typeManager.registerConverter(BsonString.class, Short.class, 
			(Object val) -> ToCoreShrtUtils.bsonStr2Shrt((BsonString) val));
		typeManager.registerConverter(BsonString.class, Integer.class, 
			(Object val) -> ToCoreIntUtils.bsonStr2Int((BsonString) val));
		typeManager.registerConverter(BsonString.class, Long.class, 
			(Object val) -> ToCoreLngUtils.bsonStr2Lng((BsonString) val));
		typeManager.registerConverter(BsonString.class, BigInteger.class, 
			(Object val) -> ToCoreBgiUtils.bsonStr2Bgi((BsonString) val));
		typeManager.registerConverter(BsonString.class, Float.class, 
			(Object val) -> ToCoreFltUtils.bsonStr2Flt((BsonString) val));
		typeManager.registerConverter(BsonString.class, Double.class, 
			(Object val) -> ToCoreDblUtils.bsonStr2Dbl((BsonString) val));
		typeManager.registerConverter(BsonString.class, BigDecimal.class, 
			(Object val) -> ToCoreBgdUtils.bsonStr2Bgd((BsonString) val));
		typeManager.registerConverter(BsonString.class, GregorianCalendar.class, 
			(Object val) -> ToCoreCalUtils.bsonStr2Cal((BsonString) val));
		typeManager.registerConverter(BsonString.class, Date.class, 
			(Object val) -> ToCoreDtUtils.bsonStr2Dt((BsonString) val));
		typeManager.registerConverter(BsonString.class, Timestamp.class, 
			(Object val) -> ToCoreTsUtils.bsonStr2Ts((BsonString) val));
		typeManager.registerConverter(BsonString.class, Boolean.class, 
			(Object val) -> ToCoreBoolUtils.bsonStr2Bool((BsonString) val));
		typeManager.registerConverter(BsonBinary.class, String.class, 
			(Object val) -> ToCoreStrUtils.bsonBinary2Str((BsonBinary) val));
		typeManager.registerConverter(BsonDocument.class, String.class, 
			(Object val) -> ToCoreStrUtils.bsonDoc2Str((BsonDocument) val));
	}
	
	/*
	 * Register conversions from Integer.
	 */
	private void registerIntegerConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonInt32.class, String.class, 
			(Object val) -> ToCoreStrUtils.bsonInt2Str((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, Byte.class, 
			(Object val) -> ToCoreByteUtils.bsonInt2Byte((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, Character.class, 
			(Object val) -> ToCoreChrUtils.bsonInt2Chr((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, Short.class, 
			(Object val) -> ToCoreShrtUtils.bsonInt2Shrt((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, Integer.class, 
			(Object val) -> ToCoreIntUtils.bsonInt2Int((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, Long.class, 
			(Object val) -> ToCoreLngUtils.bsonInt2Lng((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, BigInteger.class, 
			(Object val) -> ToCoreBgiUtils.bsonInt2Bgi((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, Float.class, 
			(Object val) -> ToCoreFltUtils.bsonInt2Flt((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, Double.class, 
			(Object val) -> ToCoreDblUtils.bsonInt2Dbl((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, BigDecimal.class, 
			(Object val) -> ToCoreBgdUtils.bsonInt2Bgd((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, GregorianCalendar.class, 
			(Object val) -> ToCoreCalUtils.bsonInt2Cal((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, Date.class, 
			(Object val) -> ToCoreDtUtils.bsonInt2Dt((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, Timestamp.class, 
			(Object val) -> ToCoreTsUtils.bsonInt2Ts((BsonInt32) val));
		typeManager.registerConverter(BsonInt32.class, Boolean.class, 
			(Object val) -> ToCoreBoolUtils.bsonInt2Bool((BsonInt32) val));
	}
	
	/*
	 * Register conversions from Long.
	 */
	private void registerLongConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonInt64.class, String.class, 
			(Object val) -> ToCoreStrUtils.bsonLng2Str((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, Byte.class, 
			(Object val) -> ToCoreByteUtils.bsonLng2Byte((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, Character.class, 
			(Object val) -> ToCoreChrUtils.bsonLng2Chr((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, Short.class, 
			(Object val) -> ToCoreShrtUtils.bsonLng2Shrt((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, Integer.class, 
			(Object val) -> ToCoreIntUtils.bsonLng2Int((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, Long.class, 
			(Object val) -> ToCoreLngUtils.bsonLng2Lng((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, BigInteger.class, 
			(Object val) -> ToCoreBgiUtils.bsonLng2Bgi((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, Float.class, 
			(Object val) -> ToCoreFltUtils.bsonLng2Flt((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, Double.class, 
			(Object val) -> ToCoreDblUtils.bsonLng2Dbl((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, BigDecimal.class, 
			(Object val) -> ToCoreBgdUtils.bsonLng2Bgd((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, GregorianCalendar.class, 
			(Object val) -> ToCoreCalUtils.bsonLng2Cal((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, Date.class, 
			(Object val) -> ToCoreDtUtils.bsonLng2Dt((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, Timestamp.class, 
			(Object val) -> ToCoreTsUtils.bsonLng2Ts((BsonInt64) val));
		typeManager.registerConverter(BsonInt64.class, Boolean.class, 
			(Object val) -> ToCoreBoolUtils.bsonLng2Bool((BsonInt64) val));
	}
	
	/*
	 * Register conversions from Double.
	 */
	private void registerDoubleConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonDouble.class, String.class, 
			(Object val) -> ToCoreStrUtils.bsonDbl2Str((BsonDouble) val));
		typeManager.registerConverter(BsonDouble.class, Character.class, 
			(Object val) -> ToCoreChrUtils.bsonDbl2Chr((BsonDouble) val));
		typeManager.registerConverter(BsonDouble.class, Byte.class, 
			(Object val) -> ToCoreByteUtils.bsonDbl2Byte((BsonDouble) val));
		typeManager.registerConverter(BsonDouble.class, Short.class, 
			(Object val) -> ToCoreShrtUtils.bsonDbl2Shrt((BsonDouble) val));
		typeManager.registerConverter(BsonDouble.class, Integer.class, 
			(Object val) -> ToCoreIntUtils.bsonDbl2Int((BsonDouble) val));
		typeManager.registerConverter(BsonDouble.class, Long.class,
			(Object val) -> ToCoreLngUtils.bsonDbl2Lng((BsonDouble) val));
		typeManager.registerConverter(BsonDouble.class, BigInteger.class, 
			(Object val) -> ToCoreBgiUtils.bsonDbl2Bgi((BsonDouble) val));
		typeManager.registerConverter(BsonDouble.class, Float.class, 
			(Object val) -> ToCoreFltUtils.bsonDbl2Flt((BsonDouble) val));
		typeManager.registerConverter(BsonDouble.class, BigDecimal.class, 
			(Object val) -> ToCoreBgdUtils.bsonDbl2Bgd((BsonDouble) val));
		typeManager.registerConverter(BsonDouble.class, Boolean.class, 
			(Object val) -> ToCoreBoolUtils.bsonDbl2Bool((BsonDouble) val));
	}
		
	/*
	 * Register conversions from Double.
	 */
	private void registeNumberConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonNumber.class, String.class, 
			(Object val) -> ToCoreStrUtils.bsonNum2Str((BsonNumber) val));
		typeManager.registerConverter(BsonNumber.class, Character.class, 
			(Object val) -> ToCoreChrUtils.bsonNum2Chr((BsonNumber) val));
		typeManager.registerConverter(BsonNumber.class, Byte.class, 
			(Object val) -> ToCoreByteUtils.bsonNum2Byte((BsonNumber) val));
		typeManager.registerConverter(BsonNumber.class, Short.class, 
			(Object val) -> ToCoreShrtUtils.bsonNum2Shrt((BsonNumber) val));
		typeManager.registerConverter(BsonNumber.class, Integer.class, 
			(Object val) -> ToCoreIntUtils.bsonNum2Int((BsonNumber) val));
		typeManager.registerConverter(BsonNumber.class, Long.class, 
			(Object val) -> ToCoreLngUtils.bsonNum2Lng((BsonNumber) val));
		typeManager.registerConverter(BsonNumber.class, BigInteger.class, 
			(Object val) -> ToCoreBgiUtils.bsonNum2Bgi((BsonNumber) val));
		typeManager.registerConverter(BsonNumber.class, Float.class, 
			(Object val) -> ToCoreFltUtils.bsonNum2Flt((BsonNumber) val));
		typeManager.registerConverter(BsonNumber.class, BigDecimal.class, 
			(Object val) -> ToCoreBgdUtils.bsonNum2Bgd((BsonNumber) val));
		typeManager.registerConverter(BsonNumber.class, Boolean.class, 
			(Object val) -> ToCoreBoolUtils.bsonNum2Bool((BsonNumber) val));
	}
		
	/*
	 * Register conversions from Date.
	 */
	private void registerDateConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonDateTime.class, String.class, 
			(Object val) -> ToCoreStrUtils.bsonDtToStr((BsonDateTime) val));
		typeManager.registerConverter(BsonDateTime.class, Long.class, 
			(Object val) -> ToCoreLngUtils.bsonDtToLng((BsonDateTime) val));
		typeManager.registerConverter(BsonDateTime.class, BigInteger.class, 
			(Object val) -> ToCoreBgiUtils.bsonDt2Bgi((BsonDateTime) val));
		typeManager.registerConverter(BsonDateTime.class, BigDecimal.class, 
			(Object val) -> ToCoreBgdUtils.bsonDt2Bgd((BsonDateTime) val));
		typeManager.registerConverter(BsonDateTime.class, Calendar.class, 
			(Object val) -> ToCoreCalUtils.bsonDt2Cal((BsonDateTime) val));
		typeManager.registerConverter(BsonDateTime.class, Timestamp.class, 
			(Object val) -> ToCoreTsUtils.bsonDt2Ts((BsonDateTime) val));
	}
	
	/*
	 * Register conversions from Boolean.
	 */
	private void registerBooleanConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonBoolean.class, String.class, 
			(Object val) -> ToCoreStrUtils.bsonBool2Str((BsonBoolean) val));
		typeManager.registerConverter(BsonBoolean.class, Byte.class, 
			(Object val) -> ToCoreByteUtils.bsonBool2Byte((BsonBoolean) val));
		typeManager.registerConverter(BsonBoolean.class, Character.class, 
			(Object val) -> ToCoreChrUtils.bsonBool2Chr((BsonBoolean) val));
		typeManager.registerConverter(BsonBoolean.class, Short.class, 
			(Object val) -> ToCoreShrtUtils.bsonBool2Shrt((BsonBoolean) val));
		typeManager.registerConverter(BsonBoolean.class, Integer.class, 
			(Object val) -> ToCoreIntUtils.bsonBool2Int((BsonBoolean) val));
		typeManager.registerConverter(BsonBoolean.class, Long.class, 
			(Object val) -> ToCoreLngUtils.bsonBool2Lng((BsonBoolean) val));
		typeManager.registerConverter(BsonBoolean.class, BigInteger.class, 
			(Object val) -> ToCoreBgiUtils.bsonBool2Bgi((BsonBoolean) val));
		typeManager.registerConverter(BsonBoolean.class, Float.class, 
			(Object val) -> ToCoreFltUtils.bsonBool2Flt((BsonBoolean) val));
		typeManager.registerConverter(BsonBoolean.class, Double.class, 
			(Object val) -> ToCoreDblUtils.bsonBool2Dbl((BsonBoolean) val));
		typeManager.registerConverter(BsonBoolean.class, BigDecimal.class, 
			(Object val) -> ToCoreBgdUtils.bsonBool2Bgd((BsonBoolean) val));
	}

	private void registerBsonDocConversions(ITypeManager typeManager) {
		typeManager.registerConverter(BsonDocument.class, Map.class, 
			(Object val) -> ToCoreMapUtils.bsonDocToMap((BsonDocument) val));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void registerMapConversion(ITypeManager typeManager) {
		typeManager.registerConverter(Map.class, BsonDocument.class,
			(Object val) -> ToBsonDocUtils.mapToBsonDoc((Map) val));		
		typeManager.registerConverter(LinkedHashMap.class, BsonDocument.class,
			(Object val) -> ToBsonDocUtils.mapToBsonDoc((LinkedHashMap) val));		
		typeManager.registerConverter(HashMap.class, BsonDocument.class,
			(Object val) -> ToBsonDocUtils.mapToBsonDoc((HashMap) val));		
		typeManager.registerConverter(TreeMap.class, BsonDocument.class,
			(Object val) -> ToBsonDocUtils.mapToBsonDoc((TreeMap) val));		
	}
}
