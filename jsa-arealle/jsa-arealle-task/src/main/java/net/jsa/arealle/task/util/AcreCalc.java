package net.jsa.arealle.task.util;

/**
 * Utility method for converting between acres and square feet.
 * 
 * @author jsaparo
 *
 */
public class AcreCalc {
	public static final int SQUARE_FEET_PER_ACRE = 43560;
	public static final String ACRE_UNITS = "A";
	public static final String SQUARE_FEET_UNITS = "SF";
	
	/**
	 * Convert given value to square feet.
	 * 
	 * @param lotSize A given number to convert
	 * @param lotUnits The unit of the given number.
	 * @return
	 */
	public static Integer calcSqFeet(Double lotSize, String lotUnits) {
		if (lotUnits.equals(ACRE_UNITS)) {
			// Assume given acres
			return (int) (lotSize * SQUARE_FEET_PER_ACRE);
		} else {
			// pass through
			return lotSize.intValue();
		}
	}
	
	/**
	 * Convert given value to acres.
	 * 
	 * @param lotSize A given number to convert
	 * @param lotUnits The unit of the given number.
	 * @return
	 */
	public static Double calcAcres(Double lotSize, String lotUnits) {
		if (lotUnits.equals(ACRE_UNITS)) {
			// pass through
			return lotSize;
		} else {
			// Asssume given square feet
			return lotSize / SQUARE_FEET_PER_ACRE;
		}
	}
}
