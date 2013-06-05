package com.pigdogbay.weightrecorder.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pigdogbay.weightrecorder.Query;
import com.pigdogbay.weightrecorder.model.Reading;

import junit.framework.TestCase;

public class QueryTest extends TestCase
{
	private List<Reading> getReadings()
	{
		
		List<Reading> readings = new ArrayList<Reading>();
		readings.add(new Reading(94.4f,new Date(112,Calendar.DECEMBER,11,8,5),""));
		readings.add(new Reading(96.4f,new Date(112,Calendar.DECEMBER,10,8,5),""));
		readings.add(new Reading(95.1f,new Date(112,Calendar.DECEMBER,12,8,5),""));
		readings.add(new Reading(95.3f,new Date(112,Calendar.DECEMBER,16,8,5),""));
		readings.add(new Reading(95.5f,new Date(112,Calendar.DECEMBER,2,8,5),""));
		readings.add(new Reading(95.8f,new Date(112,Calendar.DECEMBER,3,8,5),""));
		readings.add(new Reading(95.3f,new Date(112,Calendar.DECEMBER,5,8,5),""));
		readings.add(new Reading(94.8f,new Date(112,Calendar.DECEMBER,6,8,5),""));
		readings.add(new Reading(95.1f,new Date(112,Calendar.DECEMBER,9,8,5),""));
		readings.add(new Reading(94.6f,new Date(112,Calendar.DECEMBER,22,11,0),""));
		readings.add(new Reading(93.7f,new Date(112,Calendar.DECEMBER,23,11,0),"pigged out on hot dogs after the pub"));
		readings.add(new Reading(94.7f,new Date(112,Calendar.DECEMBER,24,11,0),""));
		readings.add(new Reading(95.0f,new Date(112,Calendar.DECEMBER,17,8,5),""));
		readings.add(new Reading(94.9f,new Date(112,Calendar.DECEMBER,18,8,5),""));
		readings.add(new Reading(94.0f,new Date(112,Calendar.DECEMBER,19,8,5),""));
		readings.add(new Reading(94.5f,new Date(112,Calendar.DECEMBER,21,11,0),""));
		return readings;
	}

	public void testSortByDate1()
	{
		Query target = new Query(getReadings());
		target.sortByDate();
		assertTrue(target.getReadings().get(0).getDate().compareTo(new Date(112,Calendar.DECEMBER,2,8,5))==0);
		int count = target.getReadings().size();
		assertTrue(target.getReadings().get(count-1).getDate().compareTo(new Date(112,Calendar.DECEMBER,24,11,0))==0);
		
	}
	/*
	 * No readings
	 * 
	 */
	public void testSortByDate2()
	{
		Query target = new Query(new ArrayList<Reading>());
		target.sortByDate();
		
	}

	public void testGetMinWeight1()
	{
		Query target = new Query(getReadings());
		Reading actual = target.getMinWeight();
		assertTrue(actual.getWeight()==93.7f);
	}
	/*
	 * No readings
	 * 
	 */
	public void testGetMinWeight2()
	{
		Query target = new Query(new ArrayList<Reading>());
		Reading actual = target.getMinWeight();
		assertTrue(actual.getWeight()==Reading.NullReading.getWeight());
	}
	public void testGetMaxWeight1()
	{
		Query target = new Query(getReadings());
		Reading actual = target.getMaxWeight();
		assertTrue(actual.getWeight()==96.4f);
	}
	/*
	 * No readings
	 * 
	 */
	public void testGetMaxWeight2()
	{
		Query target = new Query(new ArrayList<Reading>());
		Reading actual = target.getMaxWeight();
		assertTrue(actual.getWeight()==Reading.NullReading.getWeight());
	}

	public void testGetReadingsBetweenDates1()
	{
		Query target = new Query(getReadings());
		Query actual = target.getReadingsBetweenDates(new Date(112,Calendar.DECEMBER,17,8,5), new Date(112,Calendar.DECEMBER,19,8,5));
		assertTrue(actual.getReadings().size()==3);
		assertTrue(actual.getReadings().get(0).getDate().compareTo(new Date(112,Calendar.DECEMBER,17,8,5))==0);
	}
	/*
	 * No readings
	 * 
	 */
	public void testGetReadingsBetweenDates2()
	{
		Query target = new Query(new ArrayList<Reading>());
		Query actual = target.getReadingsBetweenDates(new Date(112,Calendar.DECEMBER,17,8,5), new Date(112,Calendar.DECEMBER,19,8,5));
		assertTrue(actual.getReadings().size()==0);
	}
	public void testGetLatestReading1()
	{
		Query target = new Query(getReadings());
		Reading actual = target.getLatestReading();
		assertTrue(actual.getDate().compareTo(new Date(112,Calendar.DECEMBER,24,11,0))==0);
	}	
	/*
	 * No readings
	 * 
	 */
	public void testGetLatestReading2()
	{
		Query target = new Query(new ArrayList<Reading>());
		Reading actual = target.getLatestReading();
		assertTrue(actual==Reading.NullReading);
	}	


}
