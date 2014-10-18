package com.pigdogbay.weightrecorder.test;

import java.util.List;

import com.pigdogbay.androidutils.diagnostics.Timing;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.Synchronization;

import junit.framework.TestCase;

public class SynchronizationTest extends TestCase {

	/**
	 * Timing
	 */
	public void testMerge1()
	{
		List<Reading> readings = Mocks.createReadings(1000, 0.01);
		Synchronization target = new Synchronization(readings);
		Timing t = new Timing();
		target.Merge(readings);
		long duration = t.getMilliseconds();
		t.LogDuration();
		assertTrue(duration<2000L);
	}
	
	/**
	 * All merged
	 */
	public void testMerge2()
	{
		List<Reading> original = Mocks.getChristmasReadings();
		int expected = original.size();
		List<Reading> same = Mocks.getChristmasReadings();
		Synchronization target = new Synchronization(original);
		target.Merge(same);
		assertEquals(expected, target._Readings.size());
		
	}
	/**
	 * Check for changes
	 */
	public void testMerge3()
	{
		List<Reading> original = Mocks.getChristmasReadings();
		List<Reading> updated = Mocks.getChristmasReadings();
		updated.get(5).setComment("testMerge");
		updated.get(5).setWeight(42.22D);
		Synchronization target = new Synchronization(original);
		target.Merge(updated);
		assertEquals("testMerge",original.get(5).getComment());
		assertEquals(42.22D, original.get(5).getWeight());
	}	
	/**
	 * Check that new readings are added
	 */
	public void testMerge4()
	{
		List<Reading> original = Mocks.getChristmasReadings();
		int expected = original.size()+10;
		List<Reading> newReadings = Mocks.createReadings(10, 0.01D);
		Synchronization target = new Synchronization(original);
		target.Merge(newReadings);
		assertEquals(expected, target._Readings.size());
	}
}
