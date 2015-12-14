package com.jsa.crib.ds.api;

import ind.jsa.crib.ds.internal.type.utils.DateTimePattern;
import ind.jsa.crib.ds.internal.type.utils.std.ToBgdUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToBgiUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToBoolUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToByteUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToCalUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToChrUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToDblUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToDtUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToFltUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToIntUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToLngUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToShrtUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToStrUtils;
import ind.jsa.crib.ds.internal.type.utils.std.ToTsUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.jsa.crib.ds.api.ILogicalTypeRegistry;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-test.xml"})
public class TestTypeRegistry {
	
    /**
     * Universal date and time format.
     */
    public static final String TEST_DATE_TIME_PATTERN = DateTimePattern.UNIVERSAL_DATETIME_PATTERN.toString();
    
    /**
     * Universal date format.
     */
    public static final String TEST_DATE_PATTERN = DateTimePattern.UNIVERSAL_DATE_PATTERN.toString();
    
    /**
     * Universal timestamp format.
     */
    public static final String TEST_TIMESTAMP_PATTERN = DateTimePattern.UNIVERSAL_TIMESTAMP_PATTERN.toString();
    
	// TEST_BYTE  TEST_CHR_BGD
	private static final String TEST_INT_STR = "127";
	private static final Byte TEST_BYTE = Byte.valueOf(TEST_INT_STR);
	private static final Character TEST_STR_CHR = TEST_INT_STR.charAt(0);
	private static final String TEST_CHR_STR = Character.valueOf(TEST_INT_STR.charAt(0)).toString();
	private static final Short TEST_STR_SHRT = Short.valueOf(TEST_INT_STR);
	private static final Integer TEST_STR_INT = Integer.valueOf(TEST_INT_STR);
	private static final Long TEST_STR_LNG = Long.valueOf(TEST_INT_STR);
	private static final BigInteger TEST_BGI = new BigInteger(TEST_INT_STR);
	private static final String TEST_DEC_STR = "12.345";
	private static final Float TEST_STR_FLT = Float.valueOf(TEST_DEC_STR);
	private static final Double TEST_STR_DBL = Double.valueOf(TEST_DEC_STR);
	private static final BigDecimal TEST_STR_BGD = new BigDecimal(TEST_DEC_STR);
	private static final String TEST_DTM_STR = "2013-04-14 14:50:00";
	private static final String TEST_DT_STR = "2013-04-14";
	private static final String TEST_TS_STR = "2013-04-14 14:50:01.321";
	private static final Date TEST_STR_DT = createTestDt();
	private static final Calendar TEST_STR_CAL = createTestCal();
	private static final Timestamp TEST_STR_TS = Timestamp.valueOf(TEST_TS_STR);
	private static final Calendar TEST_TS_CAL = createTestTsCal();
	private static final String TEST_BOOL_STR = "true";
	private static final Long TEST_BOOL_LNG = 1L;
	private static final Boolean TEST_STR_BOOL = Boolean.valueOf(TEST_BOOL_STR);
	private static final Long TEST_BYTE_LNG = Long.valueOf(TEST_CHR_STR);
	private static final Byte TEST_LNG_BYTE = Byte.valueOf(TEST_CHR_STR);
	private static final Long TEST_CHR_LNG = Long.valueOf(TEST_CHR_STR);
	private static final Short TEST_CHR_SHRT = Short.valueOf(TEST_CHR_STR);
	private static final Integer TEST_CHR_INT = Integer.valueOf(TEST_CHR_STR);
	private static final BigInteger TEST_CHR_BGI = BigInteger.valueOf(TEST_CHR_LNG);
	private static final Float TEST_CHR_FLT = Float.valueOf(TEST_CHR_STR);
	private static final Double TEST_CHR_DBL = Double.valueOf(TEST_CHR_STR);
	private static final BigDecimal TEST_CHR_BGD = BigDecimal.valueOf(TEST_CHR_LNG);
	private static final Long TEST_DT_LNG = Long.valueOf(TEST_STR_DT.getTime());
	private static final Long TEST_TS_LNG = Long.valueOf(TEST_STR_TS.getTime());
	private static final Long TEST_CAL_LNG = Long.valueOf(TEST_STR_CAL.getTimeInMillis());
	private static final BigDecimal TEST_DT_BGD = BigDecimal.valueOf(TEST_STR_DT.getTime());
	private static final BigDecimal TEST_TS_BGD = BigDecimal.valueOf(TEST_STR_TS.getTime());
	private static final BigDecimal TEST_CAL_BGD = BigDecimal.valueOf(TEST_STR_CAL.getTimeInMillis());
	private static final BigDecimal TEST_BOOL_BGD = BigDecimal.valueOf(TEST_BOOL_LNG);
	private static final BigDecimal TEST_BYTE_BGD = BigDecimal.valueOf(TEST_STR_LNG);
	private static final Byte TEST_BOOL_BYTE = Byte.valueOf((byte) 1);
	private static final Character TEST_BOOL_CHR = Character.valueOf('1');
	private static final Short TEST_BOOL_SHRT = Short.valueOf((short) 1);
	private static final Integer TEST_BOOL_INT = Integer.valueOf(1);
	private static final BigInteger TEST_BOOL_BGI = BigInteger.valueOf(1);
	private static final Float TEST_BOOL_FLT = Float.valueOf(1);
	private static final Double TEST_BOOL_DBL = Double.valueOf(1);
	
	private static Date createTestDt() {
		Date dt = null;
	
		try {
			dt = new SimpleDateFormat(TEST_DATE_PATTERN).parse(TEST_DT_STR);
		} catch (ParseException e) {
			// Should never happen
		}
			
		return dt;
	}
	
	private static Calendar createTestCal() {
		Calendar cal = null;
		
		try {
			cal = Calendar.getInstance();
			cal.setTime(new SimpleDateFormat(TEST_DATE_TIME_PATTERN).parse(TEST_DTM_STR));
		} catch (ParseException e) {
			// Should never happen
		}
			
		return cal;
	}
	
	private static Calendar createTestTsCal() {
		Calendar cal = null;
		
		try {
			cal = Calendar.getInstance();
			cal.setTime(new SimpleDateFormat(TEST_TIMESTAMP_PATTERN).parse(TEST_TS_STR));
		} catch (ParseException e) {
			// Should never happen
		}
			
		return cal;
	}

	@Autowired
	ILogicalTypeRegistry typeRegistry;
	
	@Test
	public void testLogicalMappings() {
		Assert.assertTrue(typeRegistry.getMappedLogicalTypes().size() > 0);
	}
	
	@Test
	public void testClassMappings() {
		Assert.assertTrue(typeRegistry.getMappedNativeTypes().size() > 0);
	}
	
	@Test
	public void test_str2Byte() {
		Assert.assertTrue(ToByteUtils.str2Byte(TEST_INT_STR).equals(TEST_BYTE));
	}
	
	@Test
	public void test_byte2Str() {
		Assert.assertTrue(ToStrUtils.byte2Str(TEST_BYTE).equals(TEST_INT_STR));
	}

	@Test
	public void test_str2Chr() {
		Assert.assertTrue(ToChrUtils.str2Chr(TEST_INT_STR).equals(TEST_STR_CHR));
	}
	
	@Test
	public void test_chr2Str() {
		Assert.assertTrue(ToStrUtils.chr2Str(TEST_STR_CHR).equals(String.valueOf(TEST_INT_STR.charAt(0))));
	}

	@Test
	public void test_str2Shrt() {
		Assert.assertTrue(ToShrtUtils.str2Shrt(TEST_INT_STR).equals(TEST_STR_SHRT));
	}

	@Test
	public void test_shrt2Str() {
		Assert.assertTrue(ToStrUtils.shrt2Str(TEST_STR_SHRT).equals(TEST_INT_STR));
	}

	@Test
	public void test_str2Int() {
		Assert.assertTrue(ToIntUtils.str2Int(TEST_INT_STR).equals(TEST_STR_INT));
	}

	@Test
	public void test_int2Str() {
		Assert.assertTrue(ToStrUtils.int2Str(TEST_STR_INT).equals(TEST_INT_STR));
	}

	@Test
	public void test_str2Lng() {
		Assert.assertTrue(ToLngUtils.str2Lng(TEST_INT_STR).equals(TEST_STR_LNG));
	}

	@Test
	public void test_lng2Str() {
		Assert.assertTrue(ToStrUtils.lng2Str(TEST_STR_LNG).equals(TEST_INT_STR));
	}

	@Test
	public void test_str2Bgi() {
		Assert.assertTrue(ToBgiUtils.str2Bgi(TEST_INT_STR).equals(TEST_BGI));
	}

	@Test
	public void test_bgi2Str() {
		Assert.assertTrue(ToStrUtils.bgi2Str(TEST_BGI).equals(TEST_INT_STR));
	}

	@Test
	public void test_str2Flt() {
		Assert.assertTrue(ToFltUtils.str2Flt(TEST_DEC_STR).equals(TEST_STR_FLT));
	}

	@Test
	public void test_flt2Str() {
		Assert.assertTrue(ToStrUtils.flt2Str(TEST_STR_FLT).equals(TEST_DEC_STR));
	}

	@Test
	public void test_str2Dbl() {
		Assert.assertTrue(ToDblUtils.str2Dbl(TEST_DEC_STR).equals(TEST_STR_DBL));
	}

	@Test
	public void test_dbl2Str() {
		Assert.assertTrue(ToStrUtils.dbl2Str(TEST_STR_DBL).equals(TEST_DEC_STR));
	}

	@Test
	public void test_str2Bool() {
		Assert.assertTrue(ToBoolUtils.str2Bool(TEST_BOOL_STR).equals(TEST_STR_BOOL));
	}

	@Test
	public void test_bool2Str() {
		Assert.assertTrue(ToStrUtils.bool2Str(TEST_STR_BOOL).equals(TEST_BOOL_STR));
	}

	@Test
	public void test_lng2Byte() {
		Assert.assertTrue(ToByteUtils.lng2Byte(TEST_BYTE_LNG).equals(TEST_LNG_BYTE));
	}

	@Test
	public void test_byte2Lng() {
		Assert.assertTrue(ToLngUtils.byte2Lng(TEST_LNG_BYTE).equals(TEST_BYTE_LNG));
	}

	@Test
	public void test_lng2Chr() {
		Assert.assertTrue(ToChrUtils.lng2Chr(TEST_CHR_LNG).equals(TEST_STR_CHR));
	}

	@Test
	public void test_chr2Lng() {
		Assert.assertTrue(ToLngUtils.chr2Lng(TEST_STR_CHR).equals(TEST_CHR_LNG));
	}

	@Test
	public void test_lng2Shrt() {
		Assert.assertTrue(ToShrtUtils.lng2Shrt(TEST_CHR_LNG).equals(TEST_CHR_SHRT));
	}

	@Test
	public void test_shrt2Lng() {
		Assert.assertTrue(ToLngUtils.shrt2Lng(TEST_CHR_SHRT).equals(TEST_CHR_LNG));
	}
	
	@Test
	public void test_lng2Int() {
		Assert.assertTrue(ToIntUtils.lng2Int(TEST_CHR_LNG).equals(TEST_CHR_INT));
	}

	@Test
	public void test_int2Lng() {
		Assert.assertTrue(ToLngUtils.int2Lng(TEST_CHR_INT).equals(TEST_CHR_LNG));
	}
	
	@Test
	public void test_bgi2Lng() {
		Assert.assertTrue(ToLngUtils.bgi2Lng(TEST_CHR_BGI).equals(TEST_CHR_LNG));
	}

	@Test
	public void test_lng2Bgi() {
		Assert.assertTrue(ToBgiUtils.lng2Bgi(TEST_CHR_LNG).equals(TEST_CHR_BGI));
	}

	@Test
	public void test_lng2Flt() {
		Assert.assertTrue(ToFltUtils.lng2Flt(TEST_CHR_LNG).equals(TEST_CHR_FLT));
	}
	
	@Test
	public void test_flt2Lng() {
		Assert.assertTrue(ToLngUtils.flt2Lng(TEST_CHR_FLT).equals(TEST_CHR_LNG));
	}

	@Test
	public void test_lng2Dbl() {
		Assert.assertTrue(ToDblUtils.lng2Dbl(TEST_CHR_LNG).equals(TEST_CHR_DBL));
	}

	@Test
	public void test_dbl2Lng() {
		Assert.assertTrue(ToLngUtils.dbl2Lng(TEST_CHR_DBL).equals(TEST_CHR_LNG));
	}

	@Test
	public void test_lng2Bgd() {
		Assert.assertTrue(ToBgdUtils.lng2Bgd(TEST_CHR_LNG).equals(TEST_CHR_BGD));
	}

	@Test
	public void test_bgd2Lng() {
		Assert.assertTrue(ToLngUtils.bgd2Lng(TEST_CHR_BGD).equals(TEST_CHR_LNG));
	}

	@Test
	public void test_lng2Cal() {
		Assert.assertTrue(ToCalUtils.lng2Cal(TEST_CAL_LNG).equals(TEST_STR_CAL));
	}

	@Test
	public void test_cal2Lng() {
		Assert.assertTrue(ToLngUtils.cal2Lng(TEST_STR_CAL).equals(TEST_CAL_LNG));
	}

	@Test
	public void test_lng2Dt() {
		Assert.assertTrue(ToDtUtils.lng2Dt(TEST_DT_LNG).equals(TEST_STR_DT));
	}

	@Test
	public void test_dt2Lng() {
		Assert.assertTrue(ToLngUtils.dt2Lng(TEST_STR_DT).equals(TEST_DT_LNG));
	}

	@Test
	public void test_lng2Ts() {
		Assert.assertTrue(ToTsUtils.lng2Ts(TEST_TS_LNG).equals(TEST_STR_TS));
	}

	@Test
	public void test_ts2Lng() {
		Assert.assertTrue(ToLngUtils.ts2Lng(TEST_STR_TS).equals(TEST_TS_LNG));
	}

	@Test
	public void test_lng2Bool() {
		Assert.assertTrue(ToBoolUtils.lng2Bool(TEST_BOOL_LNG).equals(TEST_STR_BOOL));
	}

	@Test
	public void test_bool2Lng() {
		Assert.assertTrue(ToLngUtils.bool2Lng(TEST_STR_BOOL).equals(TEST_BOOL_LNG));
	}

	@Test
	public void test_bgd2Str() {
		Assert.assertTrue(ToStrUtils.bgd2Str(TEST_STR_BGD).equals(TEST_DEC_STR));
	}

	@Test
	public void test_str2Bgd() {
		Assert.assertTrue(ToBgdUtils.str2Bgd(TEST_DEC_STR).equals(TEST_STR_BGD));
	}

	@Test
	public void test_bgd2Byte() {
		Assert.assertTrue(ToByteUtils.bgd2Byte(TEST_BYTE_BGD).equals(TEST_BYTE));
	}

	@Test
	public void test_byte2Bgd() {
		Assert.assertTrue(ToBgdUtils.byte2Bgd(TEST_BYTE).equals(TEST_BYTE_BGD));
	}

	@Test
	public void test_bgd2Chr() {
		Assert.assertTrue(ToChrUtils.bgd2Chr(TEST_CHR_BGD).equals(TEST_STR_CHR));
	}

	@Test
	public void test_chr2Bgd() {
		Assert.assertTrue(ToBgdUtils.chr2Bgd(TEST_STR_CHR).equals(TEST_CHR_BGD));
	}

	@Test
	public void test_bgd2Shrt() {
		Assert.assertTrue(ToShrtUtils.bgd2Shrt(TEST_CHR_BGD).equals(TEST_CHR_SHRT));
	}

	@Test
	public void test_shrt2Bgd() {
		Assert.assertTrue(ToBgdUtils.shrt2Bgd(TEST_CHR_SHRT).equals(TEST_CHR_BGD));
	}
	
	@Test
	public void test_bgd2Int() {
		Assert.assertTrue(ToIntUtils.bgd2Int(TEST_CHR_BGD).equals(TEST_CHR_INT));
	}

	@Test
	public void test_int2Bgd() {
		Assert.assertTrue(ToBgdUtils.int2Bgd(TEST_CHR_INT).equals(TEST_CHR_BGD));
	}
	
	@Test
	public void test_bgd2Bgi() {
		Assert.assertTrue(ToBgiUtils.bgd2Bgi(TEST_CHR_BGD).equals(TEST_CHR_BGI));
	}

	@Test
	public void test_bgi2Bgd() {
		Assert.assertTrue(ToBgdUtils.bgi2Bgd(TEST_CHR_BGI).equals(TEST_CHR_BGD));
	}
	
	@Test
	public void test_bgd2Flt() {
		Assert.assertTrue(ToFltUtils.bgd2Flt(TEST_STR_BGD).equals(TEST_STR_FLT));
	}

	@Test
	public void test_flt2Bgd() {
		Assert.assertTrue(ToBgdUtils.flt2Bgd(TEST_STR_FLT).equals(TEST_STR_BGD));
	}
	
	@Test
	public void test_bgd2Dbl() {
		Assert.assertTrue(ToDblUtils.bgd2Dbl(TEST_STR_BGD).equals(TEST_STR_DBL));
	}

	@Test
	public void test_dbl2Bgd() {
		Assert.assertTrue(ToBgdUtils.dbl2Bgd(TEST_STR_DBL).equals(TEST_STR_BGD));
	}
	
	@Test
	public void test_bgd2Cal() {
		Assert.assertTrue(ToCalUtils.bgd2Cal(TEST_CAL_BGD).equals(TEST_STR_CAL));
	}

	@Test
	public void test_cal2Bgd() {
		Assert.assertTrue(ToBgdUtils.cal2Bgd(TEST_STR_CAL).equals(TEST_CAL_BGD));
	}
	
	@Test
	public void test_bgd2Dt() {
		Assert.assertTrue(ToDtUtils.bgd2Dt(TEST_DT_BGD).equals(TEST_STR_DT));
	}

	@Test
	public void test_dt2Bgd() {
		Assert.assertTrue(ToBgdUtils.dt2Bgd(TEST_STR_DT).equals(TEST_DT_BGD));
	}
	
	@Test
	public void test_bgd2Ts() {
		Assert.assertTrue(ToTsUtils.bgd2Ts(TEST_TS_BGD).equals(TEST_STR_TS));
	}

	@Test
	public void test_ts2Bgd() {
		Assert.assertTrue(ToBgdUtils.ts2Bgd(TEST_STR_TS).equals(TEST_TS_BGD));
	}
	
	@Test
	public void test_bgd2Bool() {
		Assert.assertTrue(ToBoolUtils.bgd2Bool(TEST_BOOL_BGD).equals(TEST_STR_BOOL));
	}

	@Test
	public void test_bool2Bgd() {
		Assert.assertTrue(ToBgdUtils.bool2Bgd(TEST_STR_BOOL).equals(TEST_BOOL_BGD));
	}

	@Test
	public void test_cal2Dt() {
		Assert.assertTrue(ToDtUtils.cal2Dt(TEST_STR_CAL).equals(TEST_STR_CAL.getTime()));
	}

	@Test
	public void test_dt2Cal() {
		Assert.assertTrue(ToCalUtils.dt2Cal(TEST_STR_CAL.getTime()).equals(TEST_STR_CAL));
	}

	@Test
	public void test_cal2Ts() {
		Assert.assertTrue(ToTsUtils.cal2Ts(TEST_TS_CAL).equals(TEST_STR_TS));
	}

	@Test
	public void test_ts2Cal() {
		Assert.assertTrue(ToCalUtils.ts2Cal(TEST_STR_TS).equals(TEST_TS_CAL));
	}

	@Test
	public void test_bool2Byte() {
		Assert.assertTrue(ToByteUtils.bool2Byte(TEST_STR_BOOL).equals(TEST_BOOL_BYTE));
	}

	@Test
	public void test_byte2Bool() {
		Assert.assertTrue(ToBoolUtils.byte2Bool(TEST_BOOL_BYTE).equals(TEST_STR_BOOL));
	}

	@Test
	public void test_bool2Chr() {
		Assert.assertTrue(ToChrUtils.bool2Chr(TEST_STR_BOOL).equals(TEST_BOOL_CHR));
	}

	@Test
	public void test_chr2Bool() {
		Assert.assertTrue(ToBoolUtils.chr2Bool(TEST_BOOL_CHR).equals(TEST_STR_BOOL));
	}

	@Test
	public void test_bool2Shrt() {
		Assert.assertTrue(ToShrtUtils.bool2Shrt(TEST_STR_BOOL).equals(TEST_BOOL_SHRT));
	}

	@Test
	public void test_shrt2Bool() {
		Assert.assertTrue(ToBoolUtils.shrt2Bool(TEST_BOOL_SHRT).equals(TEST_STR_BOOL));
	}

	@Test
	public void test_bool2Int() {
		Assert.assertTrue(ToIntUtils.bool2Int(TEST_STR_BOOL).equals(TEST_BOOL_INT));
	}

	@Test
	public void test_int2Bool() {
		Assert.assertTrue(ToBoolUtils.int2Bool(TEST_BOOL_INT).equals(TEST_STR_BOOL));
	}

	@Test
	public void test_bool2Bgi() {
		Assert.assertTrue(ToBgiUtils.bool2Bgi(TEST_STR_BOOL).equals(TEST_BOOL_BGI));
	}

	@Test
	public void test_bgi2Bool() {
		Assert.assertTrue(ToBoolUtils.bgi2Bool(TEST_BOOL_BGI).equals(TEST_STR_BOOL));
	}

	@Test
	public void test_bool2Flt() {
		Assert.assertTrue(ToFltUtils.bool2Flt(TEST_STR_BOOL).equals(TEST_BOOL_FLT));
	}

	@Test
	public void test_flt2Bool() {
		Assert.assertTrue(ToBoolUtils.flt2Bool(TEST_BOOL_FLT).equals(TEST_STR_BOOL));
	}

	@Test
	public void test_bool2Dbl() {
		Assert.assertTrue(ToDblUtils.bool2Dbl(TEST_STR_BOOL).equals(TEST_BOOL_DBL));
	}

	@Test
	public void test_dbl2Bool() {
		Assert.assertTrue(ToBoolUtils.dbl2Bool(TEST_BOOL_DBL).equals(TEST_STR_BOOL));
	}
}
