/**
 * 
 */
package com.pigdogbay.weightrecorder.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.achartengine.model.TimeSeries;

import com.pigdogbay.weightrecorder.model.ChartAxesRanges;
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
	public void testCalculateAxesRanges1() {
		List<Reading> readings = new ArrayList<Reading>();
		Calendar cal1 = new GregorianCalendar(2013,Calendar.JANUARY,14,16,01,0);
		Calendar cal2 = new GregorianCalendar(2013,Calendar.JUNE,20,14,50,0);
		readings.add(new Reading(100,cal1.getTime(),""));
		readings.add(new Reading(92,cal2.getTime(),""));
		ChartLogic target = new ChartLogic(Mocks.createMetricSettings(Mocks.HEIGHT, Mocks.DAILY_WEIGHT_TREND));
		ChartAxesRanges ranges = target.calculateAxesRanges(new Query(readings), 0L);
		assertEquals(ranges.XAxisMin,(double)cal1.getTimeInMillis());
		assertEquals(ranges.XAxisMax,(double)(cal2.getTimeInMillis()+Utils.DAY_IN_MILLIS),1000L);
		assertEquals(ranges.YAxisMin,92-0.8);
		assertEquals(ranges.YAxisMax,100+0.8);
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#calculateAxesRanges(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * US Units
	 */
	public void testCalculateAxesRanges2() {
		List<Reading> readings = new ArrayList<Reading>();
		Calendar cal1 = new GregorianCalendar(2013,Calendar.JANUARY,14,16,01,0);
		Calendar cal2 = new GregorianCalendar(2013,Calendar.JUNE,20,14,50,0);
		readings.add(new Reading(100,cal1.getTime(),""));
		readings.add(new Reading(92,cal2.getTime(),""));
		ChartLogic target = new ChartLogic(Mocks.createUSSettings(Mocks.HEIGHT, Mocks.DAILY_WEIGHT_TREND));
		ChartAxesRanges ranges = target.calculateAxesRanges(new Query(readings), 0L);
		assertEquals(ranges.XAxisMin,(double)cal1.getTimeInMillis());
		assertEquals(ranges.XAxisMax,(double)(cal2.getTimeInMillis()+Utils.DAY_IN_MILLIS),1000L);
		assertEquals(ranges.YAxisMin,(92-0.8)/UnitConverterFactory.POUND_TO_KILOGRAM_FACTOR,0.0001);
		assertEquals(ranges.YAxisMax,(100+0.8)/UnitConverterFactory.POUND_TO_KILOGRAM_FACTOR,0.0001);
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#calculateAxesRanges(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * Period 30 days
	 */
	public void testCalculateAxesRanges3() {
		ChartLogic target = new ChartLogic(Mocks.createMetricSettings(Mocks.HEIGHT, Mocks.DAILY_WEIGHT_TREND));
		Query query = new Query(Mocks.createReadings(100, 0.4));
		ChartAxesRanges ranges = target.calculateAxesRanges(query, 30L);
		Calendar cal = Calendar.getInstance();
		assertEquals(cal.getTimeInMillis()-Utils.DAY_IN_MILLIS*30L,ranges.XAxisMin,1000L);
		assertEquals(cal.getTimeInMillis()+Utils.DAY_IN_MILLIS,ranges.XAxisMax,1000L);
		//30 days * 0.4 =  12, but its is 29 days since due to the 30th day being a few milliseconds short of the start time 
		assertEquals(88.4-1.16,ranges.YAxisMin,0.0001);
		assertEquals(100+1.16,ranges.YAxisMax,0.0001);
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#calculateAxesRanges(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * 0 Readings
	 */
	public void testCalculateAxesRanges4() {
		ChartLogic target = new ChartLogic(Mocks.createMetricSettings(Mocks.HEIGHT, Mocks.DAILY_WEIGHT_TREND));
		List<Reading> empty = new ArrayList<Reading>();
		Query query = new Query(empty); 
		ChartAxesRanges ranges = target.calculateAxesRanges(query, 0L);
		Calendar cal = Calendar.getInstance();
		assertEquals(0D,ranges.XAxisMin);
		assertEquals(0D,ranges.XAxisMax);
		assertEquals(0D,ranges.YAxisMin);
		assertEquals(0D,ranges.YAxisMax);
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#calculateAxesRanges(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * 0 Readings in 30 day period
	 */
	public void testCalculateAxesRanges5() {
		ChartLogic target = new ChartLogic(Mocks.createMetricSettings(Mocks.HEIGHT, Mocks.DAILY_WEIGHT_TREND));
		Query query = new Query(Mocks.getChristmasReadings());
		ChartAxesRanges ranges = target.calculateAxesRanges(query, 30L);
		Calendar cal = Calendar.getInstance();
		assertEquals(cal.getTimeInMillis()-Utils.DAY_IN_MILLIS*30L,ranges.XAxisMin,1000L);
		assertEquals(cal.getTimeInMillis()+Utils.DAY_IN_MILLIS,ranges.XAxisMax,1000L);
		double delta = (ChartLogic.AXIS_DEFAULT_MAX_WEIGHT - ChartLogic.AXIS_DEFAULT_MIN_WEIGHT)/10D;
		assertEquals(ChartLogic.AXIS_DEFAULT_MIN_WEIGHT-delta,ranges.YAxisMin);
		assertEquals(ChartLogic.AXIS_DEFAULT_MAX_WEIGHT+delta,ranges.YAxisMax);
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#calculateAxesRanges(com.pigdogbay.weightrecorder.model.Query, long)}.
	 * Min Padding
	 */
	public void testCalculateAxesRanges6() {
		ChartLogic target = new ChartLogic(Mocks.createMetricSettings(Mocks.HEIGHT, Mocks.DAILY_WEIGHT_TREND));
		Query query = new Query(Mocks.createReadings(100, 0.0D));
		ChartAxesRanges ranges = target.calculateAxesRanges(query, 30L);
		assertEquals(100-ChartLogic.AXIS_MIN_PADDING,ranges.YAxisMin,0.0001);
		assertEquals(100+ChartLogic.AXIS_MIN_PADDING,ranges.YAxisMax,0.0001);
	}

	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createReadingsSeries(com.pigdogbay.weightrecorder.model.Query)}.
	 */
	public void testCreateReadingsSeries1() {
		ChartLogic target = new ChartLogic(Mocks.createMetricSettings(1.72D, 87.5D));
		Query query = new Query(Mocks.getChristmasReadings());
		TimeSeries series = target.createReadingsSeries(query);
		assertEquals(query.getReadings().size(), series.getItemCount());
		assertEquals(query.getMinWeight().getWeight(),series.getMinY());
		assertEquals(query.getMaxWeight().getWeight(),series.getMaxY());
		assertEquals(query.getFirstReading().getDate().getTime(),series.getMinX(), 1000L);
		assertEquals(query.getLatestReading().getDate().getTime(),series.getMaxX(), 1000L);
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createReadingsSeries(com.pigdogbay.weightrecorder.model.Query)}.
	 * Empty data
	 */
	public void testCreateReadingsSeries2() {
		List<Reading> empty = new ArrayList<Reading>();
		Query query = new Query(empty); 
		ChartLogic target = new ChartLogic(Mocks.createMetricSettings(1.72D, 87.5D));
		TimeSeries series = target.createReadingsSeries(query);
		assertEquals(0, series.getItemCount());
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createReadingsSeries(com.pigdogbay.weightrecorder.model.Query)}.
	 * US Units
	 */
	public void testCreateReadingsSeries3() {
		ChartLogic target = new ChartLogic(Mocks.createUSSettings(1.72D, 87.5D));
		Query query = new Query(Mocks.getChristmasReadings());
		TimeSeries series = target.createReadingsSeries(query);
		assertEquals(query.getMinWeight().getWeight()/UnitConverterFactory.POUND_TO_KILOGRAM_FACTOR,series.getMinY());
		assertEquals(query.getMaxWeight().getWeight()/UnitConverterFactory.POUND_TO_KILOGRAM_FACTOR,series.getMaxY());
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
