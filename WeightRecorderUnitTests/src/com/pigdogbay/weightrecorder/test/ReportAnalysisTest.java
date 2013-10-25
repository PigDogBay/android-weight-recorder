package com.pigdogbay.weightrecorder.test;

import java.util.Calendar;
import com.pigdogbay.weightrecorder.model.BMICalculator;
import com.pigdogbay.weightrecorder.model.Query;
import com.pigdogbay.weightrecorder.model.ReportAnalysis;
import android.test.AndroidTestCase;

public class ReportAnalysisTest extends AndroidTestCase {
	

	private ReportAnalysis createReportAnalysis()
	{
		Query query = new Query(Mocks.createReadings(1000,Mocks.DAILY_WEIGHT_TREND));
		return new ReportAnalysis(Mocks.createMetricSettings(Mocks.HEIGHT,Mocks.TARGET_WEIGHT), query);
	}
	
	public void testGetLatestBMI() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getLatestBMI();
		double expected = Mocks.START_WEIGHT/(Mocks.HEIGHT*Mocks.HEIGHT);
		Utils.assertRounded(expected, actual);
	}

	public void testGetTargetBMI() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getTargetBMI();
		double expected = Mocks.TARGET_WEIGHT/(Mocks.HEIGHT*Mocks.HEIGHT);
		Utils.assertRounded(expected, actual);
	}

	public void testGetBottomOfIdealWeightRange() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getBottomOfIdealWeightRange();
		double expected = Mocks.HEIGHT*Mocks.HEIGHT*BMICalculator.UNDERWEIGHT_UPPER_LIMIT;
		Utils.assertRounded(expected, actual);
	}

	public void testGetTopOfIdealWeightRange() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getTopOfIdealWeightRange();
		double expected = Mocks.HEIGHT*Mocks.HEIGHT*BMICalculator.NORMAL_UPPER_LIMIT;
		Utils.assertRounded(expected, actual);
	}

	public void testGetAverageBMI() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getAverageBMI();
		double expected = 104.995D/(Mocks.HEIGHT*Mocks.HEIGHT);
		Utils.assertRounded(expected, actual);
	}

	public void testGetWeeklyTrendOverLastWeek() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getWeeklyTrendOverLastWeek();
		double expected = Mocks.DAILY_WEIGHT_TREND*7;
		Utils.assertRounded(expected, actual);
	}

	public void testGetWeeklyTrendOverLastMonth() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getWeeklyTrendOverLastMonth();
		double expected = Mocks.DAILY_WEIGHT_TREND*7;
		Utils.assertRounded(expected, actual);
	}

	public void testGetWeeklyTrendAllTime() {
		ReportAnalysis target = createReportAnalysis();
		double actual = target.getWeeklyTrendAllTime();
		double expected = Mocks.DAILY_WEIGHT_TREND*7;
		Utils.assertRounded(expected, actual);
	}

	public void testGetEstimatedDateUsingLastWeek() {
		ReportAnalysis target = createReportAnalysis();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,1500);
		long expected = cal.getTimeInMillis();
		long actual = target.getEstimatedDateUsingLastWeek();
		long diff = Math.abs(expected-actual);
		//check within one hour
		assertTrue(diff<2L*60L*60L*1000L);
	}

	public void testGetEstimatedDateUsingLastMonth() {
		ReportAnalysis target = createReportAnalysis();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,1500);
		long expected = cal.getTimeInMillis();
		long actual = target.getEstimatedDateUsingLastMonth();
		long diff = Math.abs(expected-actual);
		//check within one hour
		assertTrue(diff<2L*60L*60L*1000L);
	}

	public void testGetEstimatedDateUsingAllTime() {
		ReportAnalysis target = createReportAnalysis();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,1500);
		long expected = cal.getTimeInMillis();
		long actual = target.getEstimatedDateUsingAllTime();
		long diff = Math.abs(expected-actual);
		//check within one hour
		assertTrue(diff<2L*60L*60L*1000L);
	}

}
