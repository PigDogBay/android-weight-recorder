package com.pigdogbay.weightrecorder.test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.pigdogbay.weightrecorder.model.ReportFormatting;
import com.pigdogbay.weightrecorder.model.UnitConverterFactory;
import com.pigdogbay.weightrecorder.model.UserSettings;

import android.test.AndroidTestCase;
import android.util.Log;

public class ReportFormattingTest extends AndroidTestCase {

	private UserSettings createMetricSettings()
	{
		UserSettings userSettings = new UserSettings();
		userSettings.Height=1.72;
		userSettings.TargetWeight = 85;
		userSettings.WeightConverter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_KILOGRAMS);
		userSettings.LengthConverter = UnitConverterFactory.create(UnitConverterFactory.METRES_TO_METRES);
		return userSettings;
	}
	private UserSettings createBritishSettings()
	{
		UserSettings userSettings = new UserSettings();
		userSettings.Height=1.72;
		userSettings.TargetWeight = 85;
		userSettings.WeightConverter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_STONES);
		userSettings.LengthConverter = UnitConverterFactory.create(UnitConverterFactory.METRES_TO_CENTIMETRES);
		return userSettings;
	}
	private UserSettings createUSSettings()
	{
		UserSettings userSettings = new UserSettings();
		userSettings.Height=1.72;
		userSettings.TargetWeight = 85;
		userSettings.WeightConverter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_POUNDS);
		userSettings.LengthConverter = UnitConverterFactory.create(UnitConverterFactory.METRES_TO_INCHES);
		return userSettings;
	}
	
	public void testGetWeightString1() {
		ReportFormatting target = new ReportFormatting(this.mContext,createMetricSettings());
		assertEquals("0.0 kg", target.getWeightString(0) );
		assertEquals("42.8 kg", target.getWeightString(42.84) );
		assertEquals("42.9 kg", target.getWeightString(42.86) );
		assertEquals("-1.0 kg", target.getWeightString(-1) );
	}
	public void testGetWeightString2() {
		ReportFormatting target = new ReportFormatting(this.mContext,createBritishSettings());
		assertEquals("0 lb", target.getWeightString(0) );
		assertEquals("11st 11lb", target.getWeightString(75) );
		
	}
	public void testGetWeightString3() {
		ReportFormatting target = new ReportFormatting(this.mContext,createUSSettings());
		assertEquals("0.0 lb", target.getWeightString(0) );
		assertEquals("165.3 lb", target.getWeightString(75) );
		
	}
	public void testGetWeightString4() {
		ReportFormatting target = new ReportFormatting(this.mContext,createMetricSettings());
		assertEquals("0.0 kg", target.getWeightString(Double.NaN) );
		assertEquals("0.0 kg", target.getWeightString(Double.POSITIVE_INFINITY) );
		assertEquals("0.0 kg", target.getWeightString(Double.NEGATIVE_INFINITY) );
		assertEquals("0.0 kg", target.getWeightString(Double.MIN_NORMAL) );
	}

	public void testGetBMIString1() {
		ReportFormatting target = new ReportFormatting(this.mContext,createMetricSettings());
		assertEquals("25.0 (Overweight)",target.getBMIString(25));
		assertEquals("20.1 (Normal)",target.getBMIString(20.1));
		assertEquals("16.5 (Underweight)",target.getBMIString(16.5));
	}
	public void testGetBMIString2() {
		String bmiError =mContext.getString(com.pigdogbay.weightrecorder.R.string.bmi_error); 
		ReportFormatting target = new ReportFormatting(this.mContext,createMetricSettings());
		assertEquals("0.0 (Severely Underweight)",target.getBMIString(0));
		assertEquals(bmiError,target.getBMIString(-1));
		assertEquals(bmiError,target.getBMIString(Double.NaN));
		assertEquals(bmiError,target.getBMIString(Double.POSITIVE_INFINITY));
		assertEquals(bmiError,target.getBMIString(Double.NEGATIVE_INFINITY));
		assertEquals(bmiError,target.getBMIString(999999));
	}
	/**
	 * Locale Test
	 */
	public void testGetBMIString3() {
		Locale.setDefault(Locale.FRENCH);
		ReportFormatting target = new ReportFormatting(this.mContext,createMetricSettings());
		assertEquals("25,1 (Overweight)",target.getBMIString(25.1D));
		Locale.setDefault(Locale.UK);
	}

	public void testGetDateString1() {
		Locale.setDefault(Locale.UK);
		ReportFormatting target = new ReportFormatting(this.mContext,createMetricSettings());
		Calendar cal = new GregorianCalendar(2013,Calendar.JUNE,14,16,01);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
		{
			assertEquals("June 14, 2013",target.getDateString(cal.getTimeInMillis()));
		}
		else{
			assertEquals("14 June 2013",target.getDateString(cal.getTimeInMillis()));
		}
		
		//DateUtils suffers from Year 2038 problem
		cal = new GregorianCalendar(3000,Calendar.JUNE,14,16,01);
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
		{
			assertEquals("June 14, 3000",target.getDateString(cal.getTimeInMillis()));
		}
		else{
			assertEquals("14 June 3000",target.getDateString(cal.getTimeInMillis()));
		}
	}
	public void testGetDateString2() {
		Locale.setDefault(Locale.UK);
		ReportFormatting target = new ReportFormatting(this.mContext,createMetricSettings());
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
		{
			assertEquals("January 1, 1970",target.getDateString(0));
		}
		else{
			assertEquals("1 January 1970",target.getDateString(0));
		}
	}


}
