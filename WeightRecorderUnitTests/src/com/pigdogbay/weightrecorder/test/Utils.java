package com.pigdogbay.weightrecorder.test;

import junit.framework.TestCase;

public class Utils {

	public static void assertRounded(TestCase test, double expected, double actual)
	{
		test.assertEquals(Math.round(expected*10000), Math.round(actual*10000));
	}
}
