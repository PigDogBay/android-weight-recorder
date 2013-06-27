package com.pigdogbay.weightrecorder.test;

import java.util.Locale;

import com.pigdogbay.weightrecorder.model.PreferencesHelper;

import android.preference.PreferenceManager;
import android.test.AndroidTestCase;

public class PreferencesHelperTest extends AndroidTestCase {

	/*
	 * Basic case
	 */
	public void testGetInt1()
	{
		int expected = 42;
		int actual;
		int id = com.pigdogbay.weightrecorder.R.string.code_pref_weight_units_key;
		PreferencesHelper target = new PreferencesHelper(this.mContext);
		target.setInt(id,expected);
		actual = target.getInt(id,0);
		assertEquals(expected, actual);
	}
	/*
	 * Spanish locale
	 */
	public void testGetInt2()
	{
		Locale.setDefault(new Locale("es"));
		testGetInt1();
		Locale.setDefault(Locale.US);
	}
	/*
	 * Badly formatted
	 */
	public void testGetInt3()
	{
		int expected = 88;
		int actual;
		int id = com.pigdogbay.weightrecorder.R.string.code_pref_weight_units_key;
		PreferencesHelper target = new PreferencesHelper(this.mContext);
		target.setDouble(id,1.23);
		actual = target.getInt(id,expected);
		assertEquals(expected, actual);
	}
	/*
	 * Basic case
	 */
	public void testGetDouble1()
	{
		double expected = 1.23;
		double actual;
		int id = com.pigdogbay.weightrecorder.R.string.code_pref_height_key;
		PreferencesHelper target = new PreferencesHelper(this.mContext);
		target.setDouble(id,expected);
		actual = target.getDouble(id,0);
		assertEquals(expected, actual);
	}
	/*
	 * Spanish locale
	 */
	public void testGetDouble2()
	{
		Locale.setDefault(new Locale("es"));
		testGetDouble1();
		Locale.setDefault(Locale.UK);
	}
	/*
	 * Test API for locale behaviour
	 * Expect locale to have no effect
	 */
	public void testDoubleCheck()
	{
		Locale.setDefault(new Locale("es"));
		assertEquals(1.23D,Double.parseDouble("1.23"));
		assertEquals("4.56",Double.toString(4.56D));
		Locale.setDefault(Locale.UK);
	}
	
	/*
	 * Basic case
	 */
	public void testGetBoolean1()
	{
		int id = com.pigdogbay.weightrecorder.R.string.code_pref_welcome_shown_key;
		PreferencesHelper target = new PreferencesHelper(this.mContext);
		target.setBoolean(id,true);
		assertTrue(target.getBoolean(id,false));
		target.setBoolean(id,false);
		assertFalse(target.getBoolean(id,false));
	}
	/*
	 * Spanish locale
	 */
	public void testGetBoolean2()
	{
		Locale.setDefault(new Locale("es"));
		testGetBoolean1();
		Locale.setDefault(Locale.US);
	}
}
