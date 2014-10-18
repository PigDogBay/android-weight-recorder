package com.pigdogbay.weightrecorder.test;

import junit.framework.TestCase;

public class Utils {
	public static final long DAY_IN_MILLIS = 24L * 60L * 60L * 1000L;
	public static final long YEAR_IN_MILLIS = DAY_IN_MILLIS*365L;

	public static void assertRounded(double expected, double actual)
	{
		TestCase.assertEquals(Math.round(expected*10000), Math.round(actual*10000));
	}
}
