package com.pigdogbay.weightrecorder.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.TrendAnalysis;

import junit.framework.TestCase;

public class TrendAnalysisTest extends TestCase {

	private List<Reading> createReadings()
	{
		List<Reading> readings = new ArrayList<Reading>();
		readings.add(new Reading(110f,new GregorianCalendar(2013, Calendar.JUNE, 5, 8, 0).getTime(),""));
		readings.add(new Reading(105f,new GregorianCalendar(2013, Calendar.JUNE, 6, 8, 0).getTime(),""));
		readings.add(new Reading(100f,new GregorianCalendar(2013, Calendar.JUNE, 7, 8, 0).getTime(),""));
		return readings;
	}
	public void testOverFlow1()
	{
		List<Reading> readings = Mocks.createReadings(2000, Mocks.DAILY_WEIGHT_TREND);
		TrendAnalysis target = new TrendAnalysis(readings);
		double actual;
		double expected=Mocks.DAILY_WEIGHT_TREND;
		actual = target.getTrendInDays();
		Utils.assertRounded(this, expected, actual);
	}
	/*
	 * The long to double conversions can lose precision for times in milliseconds
	 */
	public void testOverFlow2()
	{
		int daysToTarget = (int)((Mocks.TARGET_WEIGHT-Mocks.START_WEIGHT)/Mocks.DAILY_WEIGHT_TREND);
		List<Reading> readings = Mocks.createReadings(2000, Mocks.DAILY_WEIGHT_TREND);
		TrendAnalysis target = new TrendAnalysis(readings);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,daysToTarget);
		long expected = cal.getTimeInMillis();
		long actual = target.getEstimatedDate(Mocks.TARGET_WEIGHT);
		long diff = Math.abs(expected-actual);
		//check within one hour
		assertTrue(diff<60L*60L*1000L);
	}
	
	public void testGetEstimatedDate1()
	{
		long actual;
		long expected=new GregorianCalendar(2013, Calendar.JUNE, 9, 8, 0).getTimeInMillis();
		TrendAnalysis target = new TrendAnalysis(createReadings());
		actual = target.getEstimatedDate(90f);
		assertEquals(expected, actual);
		
	}
	public void testIsGoalDateValid1()
	{
		boolean expected = true;
		boolean actual = TrendAnalysis.isGoalDateValid(Calendar.getInstance().getTimeInMillis()+10000);
		assertEquals(expected, actual);
	}
	public void testIsGoalDateValid2()
	{
		boolean expected = false;
		boolean actual = TrendAnalysis.isGoalDateValid(Calendar.getInstance().getTimeInMillis()-10000);
		assertEquals(expected, actual);
	}
	
	/**
	 * Check dates 5 years or more in future are invalid
	 */
	public void testIsGoalDateValid3()
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 5);
		boolean expected = false;
		boolean actual = TrendAnalysis.isGoalDateValid(cal.getTimeInMillis());
		assertEquals(expected, actual);
	}
	/**
	 * Check dates 4 years or more in future are valid
	 */
	public void testIsGoalDateValid4()
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 4);
		boolean expected = true;
		boolean actual = TrendAnalysis.isGoalDateValid(cal.getTimeInMillis());
		assertEquals(expected, actual);
	}
	public void test1GetTrendInDays()
	{
		double actual;
		double expected=-5;
		TrendAnalysis target = new TrendAnalysis(createReadings());
		actual =target.getTrendInDays();
		actual = Math.floor(actual*100)/100;
		assertEquals(expected, actual);		
	}
	public void testGetEstimatedWeight1()
	{
		double actual;
		double expected=95;
		TrendAnalysis target = new TrendAnalysis(createReadings());
		actual = target.getEstimatedWeight(new GregorianCalendar(2013, Calendar.JUNE, 8, 8, 0).getTimeInMillis());
		actual = Math.floor(actual*100)/100;
		assertEquals(expected, actual);
	}
}
