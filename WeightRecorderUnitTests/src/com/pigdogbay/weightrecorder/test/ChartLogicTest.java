/**
 * 
 */
package com.pigdogbay.weightrecorder.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.achartengine.model.TimeSeries;

import android.widget.CalendarView;

import com.pigdogbay.weightrecorder.model.ChartLogic;
import com.pigdogbay.weightrecorder.model.Query;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.UnitConverterFactory;
import com.pigdogbay.weightrecorder.model.UserSettings;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author mark.bailey
 *
 */
public class ChartLogicTest extends TestCase {

	private static final int ExpectedNumberOfPointsOnTrendLine = 14;
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#calculateAxesRanges(com.pigdogbay.weightrecorder.model.Query, long)}.
	 */
	public void testCalculateAxesRanges() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createReadingsSeries(com.pigdogbay.weightrecorder.model.Query)}.
	 */
	public void testCreateReadingsSeries() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTargetSeries()}.
	 */
	public void testCreateTargetSeries1() {
		ChartLogic target = new ChartLogic(Mocks.createMetricSettings(1.72D, 87.5D));
		TimeSeries series = target.createTargetSeries();
		long now = Calendar.getInstance().getTimeInMillis();
		assertEquals(25, series.getItemCount());
		assertEquals(87.5,series.getMaxY());
		assertEquals(87.5,series.getMinY());
		assertTrue(series.getMaxX()>now);
		assertTrue(series.getMinX()<now);
		double span = series.getMaxX() - series.getMinX();
		assertTrue(span>=2D*(double)Utils.YEAR_IN_MILLIS);
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTargetSeries()}.
	 */
	public void testCreateTargetSeries2() {
		ChartLogic target = new ChartLogic(Mocks.createUSSettings(1.72D, 87.5D));
		TimeSeries series = target.createTargetSeries();
		Assert.assertEquals(192.9044794117679D, series.getMinY(), 0.000001D);
		Assert.assertEquals(192.9044794117679D, series.getMaxY(), 0.000001D);
	}

	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTrendSeries(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * Min / max time check - all data trend
	 */
	public void testCreateTrendSeriesTimeCheck1() {
		Query query = new Query(Mocks.createReadings(100, Mocks.DAILY_WEIGHT_TREND));
		UserSettings settings = Mocks.createMetricSettings(Mocks.HEIGHT, Mocks.TARGET_WEIGHT);
		ChartLogic target = new ChartLogic(settings);
		TimeSeries series = target.createTrendSeries(query, 0);
		assertEquals(ExpectedNumberOfPointsOnTrendLine, series.getItemCount());
		long expected = query.getFirstReading().getDate().getTime();
		long actual = (long)series.getMinX();
		assertEquals(expected,actual);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 1);
		expected = cal.getTimeInMillis();
		actual = (long)series.getMaxX();
		assertEquals(expected,actual, Utils.DAY_IN_MILLIS);
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTrendSeries(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * Min / max time check 90 day trend
	 */
	public void testCreateTrendSeriesTimeCheck2() {
		Query query = new Query(Mocks.createReadings(100, Mocks.DAILY_WEIGHT_TREND));
		UserSettings settings = Mocks.createMetricSettings(Mocks.HEIGHT, Mocks.TARGET_WEIGHT);
		ChartLogic target = new ChartLogic(settings);
		TimeSeries series = target.createTrendSeries(query, 90);
		assertEquals(ExpectedNumberOfPointsOnTrendLine, series.getItemCount());
		
		long expected = Calendar.getInstance().getTimeInMillis()-90*Utils.DAY_IN_MILLIS;
		long actual = (long)series.getMinX();
		assertEquals(expected,actual, 1000L*60L*60L);
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTrendSeries(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * Weight check
	 */
	public void testCreateTrendSeriesWeightCheck1() {
		Query query = new Query(Mocks.createReadings(100, Mocks.DAILY_WEIGHT_TREND));
		UserSettings settings = Mocks.createMetricSettings(Mocks.HEIGHT, Mocks.TARGET_WEIGHT);
		ChartLogic target = new ChartLogic(settings);
		TimeSeries series = target.createTrendSeries(query, 0);
		assertEquals(ExpectedNumberOfPointsOnTrendLine, series.getItemCount());
		//Base expected weight on min time
		double minX = series.getMinX();
		double timeNow = Calendar.getInstance().getTimeInMillis();
		double days = (timeNow-minX)/Utils.DAY_IN_MILLIS;
		double expected = 100-days*Mocks.DAILY_WEIGHT_TREND;
		double actual = series.getMaxY();
		assertEquals(expected, actual, 0.01D);
		
		//Min weight, in future
		expected = 100+365*Mocks.DAILY_WEIGHT_TREND;
		actual = series.getMinY();
		assertEquals(expected, actual, 0.01D);
		
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTrendSeries(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * Weight check 90 day trend
	 */
	public void testCreateTrendSeriesWeightCheck2() {
		Query query = new Query(Mocks.createReadings(100, Mocks.DAILY_WEIGHT_TREND));
		UserSettings settings = Mocks.createMetricSettings(Mocks.HEIGHT, Mocks.TARGET_WEIGHT);
		ChartLogic target = new ChartLogic(settings);
		TimeSeries series = target.createTrendSeries(query, 90);
		assertEquals(ExpectedNumberOfPointsOnTrendLine, series.getItemCount());
		
		double expected = 100-90*Mocks.DAILY_WEIGHT_TREND;
		double actual = series.getMaxY();
		assertEquals(expected, actual, 0.01D);
		
		//Min weight, in future
		expected = 100+365*Mocks.DAILY_WEIGHT_TREND;
		actual = series.getMinY();
		assertEquals(expected, actual, 0.01D);
		
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTrendSeries(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * Weight check 90 day trend using US Weights
	 */
	public void testCreateTrendSeriesWeightCheck3() {
		Query query = new Query(Mocks.createReadings(100, Mocks.DAILY_WEIGHT_TREND));
		UserSettings settings = Mocks.createUSSettings(Mocks.HEIGHT, Mocks.TARGET_WEIGHT);
		ChartLogic target = new ChartLogic(settings);
		TimeSeries series = target.createTrendSeries(query, 90);
		
		double expected = (100-90*Mocks.DAILY_WEIGHT_TREND)/UnitConverterFactory.POUND_TO_KILOGRAM_FACTOR;
		double actual = series.getMaxY();
		assertEquals(expected, actual, 0.01D);
		
		//Min weight, in future
		expected = (100+365*Mocks.DAILY_WEIGHT_TREND)/UnitConverterFactory.POUND_TO_KILOGRAM_FACTOR;
		actual = series.getMinY();
		//assertEquals(expected, actual, 0.01D);
		
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTrendSeries(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * Readings too old
	 */
	public void testCreateTrendSeries1() {
		Query query = new Query(Mocks.getChristmasReadings()); 
		ChartLogic target = new ChartLogic(Mocks.createUSSettings(Mocks.HEIGHT, Mocks.TARGET_WEIGHT));
		TimeSeries series = target.createTrendSeries(query, 90);
		assertEquals(0, series.getItemCount());
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTrendSeries(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * No readings
	 */
	public void testCreateTrendSeries2() {
		List<Reading> empty = new ArrayList<Reading>();
		Query query = new Query(empty); 
		ChartLogic target = new ChartLogic(Mocks.createUSSettings(Mocks.HEIGHT, Mocks.TARGET_WEIGHT));
		TimeSeries series = target.createTrendSeries(query, 0);
		assertEquals(0, series.getItemCount());
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTrendSeries(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * Future readings only, but try create trend based on past 30 days
	 */
	public void testCreateTrendSeries3() {

		Query query = new Query(Mocks.createFutureReadings()); 
		ChartLogic target = new ChartLogic(Mocks.createUSSettings(Mocks.HEIGHT, Mocks.TARGET_WEIGHT));
		TimeSeries series = target.createTrendSeries(query, 30);
		assertEquals(0, series.getItemCount());
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTrendSeries(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * Future readings only, but try create trend all readings including future
	 */
	public void testCreateTrendSeries4() {
		Query query = new Query(Mocks.createFutureReadings()); 
		ChartLogic target = new ChartLogic(Mocks.createUSSettings(Mocks.HEIGHT, Mocks.TARGET_WEIGHT));
		TimeSeries series = target.createTrendSeries(query, 0);
		assertEquals(ExpectedNumberOfPointsOnTrendLine, series.getItemCount());
	}

}
