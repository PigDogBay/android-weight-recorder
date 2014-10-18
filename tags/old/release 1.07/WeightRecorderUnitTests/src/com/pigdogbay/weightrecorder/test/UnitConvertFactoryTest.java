/**
 * 
 */
package com.pigdogbay.weightrecorder.test;

import com.pigdogbay.weightrecorder.model.IUnitConverter;
import com.pigdogbay.weightrecorder.model.UnitConverterFactory;

import junit.framework.TestCase;

/**
 * @author mark.bailey
 *
 */
public class UnitConvertFactoryTest extends TestCase {

	/*http://en.wikipedia.org/wiki/Pound_(mass)
	 * 
	 * 1lb (Avoirdupois) = 0.45359237 Kg
	 * 
	 */
	public static final double POUND_KILOGRAM = 0.45359237D;
	public static final double STONE_POUND = 14D;
	public static final double STONE_KILOGRAM = STONE_POUND*POUND_KILOGRAM;
	
	/*
	 * http://en.wikipedia.org/wiki/Inch#Modern_standardisation
	 * The current internationally accepted value for the imperial and US customary inch is exactly 25.4 millimetres.
	 * 
	 */
	public static final double INCH_METRE = 0.0254D;
	public static final double ACCURACY = 0.000000001D;
	/**
	 * Avoirdupois Test
	 * Test method for {@link com.pigdogbay.weightrecorder.model.UnitConverterFactory#create(int)}.
	 */
	public void testCreatePoundConvert1() {
		IUnitConverter converter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_POUNDS);
		double expected = 1D;
		double actual = converter.convert(POUND_KILOGRAM);
		assertEquals(expected, actual, ACCURACY);
	}
	/**
	 * Avoirdupois Test
	 * Test method for {@link com.pigdogbay.weightrecorder.model.UnitConverterFactory#create(int)}.
	 */
	public void testCreatePoundInverse1() {
		IUnitConverter converter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_POUNDS);
		double expected = POUND_KILOGRAM;
		double actual = converter.inverse(1D);
		assertEquals(expected, actual, ACCURACY);
	}
	/**
	 * Avoirdupois Test
	 * Test method for {@link com.pigdogbay.weightrecorder.model.UnitConverterFactory#create(int)}.
	 */
	public void testCreateStoneConvert1() {
		IUnitConverter converter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_STONES);
		double expected = 1D;
		double actual = converter.convert(STONE_KILOGRAM);
		assertEquals(expected, actual, ACCURACY);
	}
	/**
	 * Avoirdupois Test
	 * Test method for {@link com.pigdogbay.weightrecorder.model.UnitConverterFactory#create(int)}.
	 */
	public void testCreateStoneInverse1() {
		IUnitConverter converter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_STONES);
		double expected = STONE_KILOGRAM;
		double actual = converter.inverse(1D);
		assertEquals(expected, actual, ACCURACY);
	}
	/**
	 * Avoirdupois Test
	 * Test method for {@link com.pigdogbay.weightrecorder.model.UnitConverterFactory#create(int)}.
	 */
	public void testCreateStoneWeightString1() {
		IUnitConverter converter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_STONES);
		String expected = "10st 11lb";
		String actual = converter.getDisplayString(10.78D);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.UnitConverterFactory#createLengthConverter(int)}.
	 */
	public void testCreateInchesConvert1() {
		IUnitConverter converter = UnitConverterFactory.createLengthConverter(UnitConverterFactory.METRES_TO_INCHES);
		double expected = 1D;
		double actual = converter.convert(INCH_METRE);
		assertEquals(expected, actual, ACCURACY);
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.UnitConverterFactory#createLengthConverter(int)}.
	 */
	public void testCreateInchesInverse1() {
		IUnitConverter converter = UnitConverterFactory.createLengthConverter(UnitConverterFactory.METRES_TO_INCHES);
		double expected = INCH_METRE;
		double actual = converter.inverse(1D);
		assertEquals(expected, actual, ACCURACY);
	}

}
