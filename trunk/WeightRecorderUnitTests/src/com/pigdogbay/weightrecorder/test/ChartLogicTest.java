/**
 * 
 */
package com.pigdogbay.weightrecorder.test;

import java.util.Calendar;

import org.achartengine.model.TimeSeries;

import com.pigdogbay.weightrecorder.model.ChartLogic;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author mark.bailey
 *
 */
public class ChartLogicTest extends TestCase {

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
		TimeSeries timeSeries = target.createTargetSeries();
		long now = Calendar.getInstance().getTimeInMillis();
		assertEquals(25, timeSeries.getItemCount());
		assertEquals(87.5,timeSeries.getMaxY());
		assertEquals(87.5,timeSeries.getMinY());
		assertTrue(timeSeries.getMaxX()>now);
		assertTrue(timeSeries.getMinX()<now);
		double span = timeSeries.getMaxX() - timeSeries.getMinX();
		assertTrue(span>=2D*(double)Utils.YEAR_IN_MILLIS);
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTargetSeries()}.
	 */
	public void testCreateTargetSeries2() {
		ChartLogic target = new ChartLogic(Mocks.createUSSettings(1.72D, 87.5D));
		TimeSeries timeSeries = target.createTargetSeries();
		Assert.assertEquals(192.9044794117679D, timeSeries.getMinY(), 0.000001D);
		Assert.assertEquals(192.9044794117679D, timeSeries.getMaxY(), 0.000001D);
	}

	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.ChartLogic#createTrendSeries(com.pigdogbay.weightrecorder.model.Query, long)}.
	 */
	public void testCreateTrendSeries() {
		fail("Not yet implemented"); // TODO
	}

}
