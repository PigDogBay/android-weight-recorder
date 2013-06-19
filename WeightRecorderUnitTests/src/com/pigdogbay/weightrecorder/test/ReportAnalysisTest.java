package com.pigdogbay.weightrecorder.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.pigdogbay.weightrecorder.model.BMICalculator;
import com.pigdogbay.weightrecorder.model.Query;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.ReportAnalysis;
import com.pigdogbay.weightrecorder.model.UnitConverterFactory;
import com.pigdogbay.weightrecorder.model.UserSettings;

import android.test.AndroidTestCase;

public class ReportAnalysisTest extends AndroidTestCase {
	
	private static final double HEIGHT = 1.72D;

	private UserSettings createMetricSettings()
	{
		UserSettings userSettings = new UserSettings();
		userSettings.Height=HEIGHT;
		userSettings.TargetWeight = 85;
		userSettings.WeightConverter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_KILOGRAMS);
		userSettings.LengthConverter = UnitConverterFactory.create(UnitConverterFactory.METRES_TO_METRES);
		return userSettings;
	}
	private List<Reading> createReadings(int number, double weightStep)
	{
		List<Reading> readings = new ArrayList<Reading>();
		double weight = 100;
		Calendar cal = Calendar.getInstance();
		for (int i=0;i<number;i++){
			Reading r = new Reading(weight,cal.getTime(),"");
			readings.add(r);
			weight = weight +weightStep;
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		return readings;
	}
	private ReportAnalysis createReportAnalysis()
	{
		Query query = new Query(createReadings(1000,0.01D));
		return new ReportAnalysis(createMetricSettings(), query);
	}
	
	public void testGetLatestBMI() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getLatestBMI();
		double expected = 100/(HEIGHT*HEIGHT);
		assertEquals(Math.round(expected*1000), Math.round(actual*1000));
	}

	public void testGetTargetBMI() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getTargetBMI();
		double expected = 85/(HEIGHT*HEIGHT);
		assertEquals(Math.round(expected*1000), Math.round(actual*1000));
	}

	public void testGetBottomOfIdealWeightRange() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getBottomOfIdealWeightRange();
		double expected = HEIGHT*HEIGHT*BMICalculator.UNDERWEIGHT_UPPER_LIMIT;
		assertEquals(Math.round(expected*1000), Math.round(actual*1000));
	}

	public void testGetTopOfIdealWeightRange() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getTopOfIdealWeightRange();
		double expected = HEIGHT*HEIGHT*BMICalculator.NORMAL_UPPER_LIMIT;
		assertEquals(Math.round(expected*1000), Math.round(actual*1000));
	}

	public void testGetAverageBMI() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getAverageBMI();
		double expected = 105D/(HEIGHT*HEIGHT);
		assertEquals(Math.round(expected*100), Math.round(actual*100));
	}

	public void testGetWeeklyTrendOverLastWeek() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getWeeklyTrendOverLastWeek();
		double expected = -0.07;
		assertEquals(Math.round(expected*1000), Math.round(actual*1000));
	}

	public void testGetWeeklyTrendOverLastMonth() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getWeeklyTrendOverLastMonth();
		double expected = -0.07;
		assertEquals(Math.round(expected*1000), Math.round(actual*1000));
	}

	public void testGetWeeklyTrendAllTime() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getWeeklyTrendAllTime();
		double expected = -0.07;
		assertEquals(Math.round(expected*1000), Math.round(actual*1000));
	}

	public void testGetEstimatedDateUsingLastWeek() {
		ReportAnalysis target = createReportAnalysis();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,1500);
		long expected = cal.getTimeInMillis();
		long actual = target.getEstimatedDateUsingLastWeek();
		long diff = Math.abs(expected-actual);
		assertTrue(diff<10000);
	}

	public void testGetEstimatedDateUsingLastMonth() {
		ReportAnalysis target = createReportAnalysis();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,1500);
		long expected = cal.getTimeInMillis();
		long actual = target.getEstimatedDateUsingLastMonth();
		long diff = Math.abs(expected-actual);
		assertTrue(diff<10000);
	}

	public void testGetEstimatedDateUsingAllTime() {
		ReportAnalysis target = createReportAnalysis();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,1500);
		long expected = cal.getTimeInMillis();
		long actual = target.getEstimatedDateUsingAllTime();
		long diff = Math.abs(expected-actual);
		assertTrue(diff<10000);
	}

}
