package com.pigdogbay.weightrecorder.test;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.ReadingsSerializer;

import junit.framework.TestCase;

public class ReadingsSerializerTest extends TestCase
{
	public static String EXAMPLE_DATA = 
			"94.60,2012-12-25 13:23:10,\"Christmas Day\"\n"
			+"95.23,2012-12-26 13:23:10,\"Boxing Day\"\n"
			+"96.10,2012-12-31 13:23:10,\"New years eve\"\n"
			+"94.22,2013-01-14 18:15:49,\"Birthday\"\n"
			+"93.50,2013-01-18 08:59:10,\"today\"\n";
	public static String BAD_DATA = 
			"94.6fewjfojewfpo\n"
			+"95.20,2012-12-26 13:23:10,\"Boxing Day\"\n"
			+"\n"
			+"94.20,2013-01-14 18:15:49,\"Birthday\"\n"
			+"93.50,2013-01-18 08:59:10,\"today\"\n";
	
	List<Reading> createReadings()
	{
		ArrayList<Reading> readings = new ArrayList<Reading>();
		readings.add(new Reading(94.6D,new Date(112,11,25,13,23,10),"Christmas Day"));
		readings.add(new Reading(95.23D,new Date(112,11,26,13,23,10),"Boxing Day"));
		readings.add(new Reading(96.1D,new Date(112,11,31,13,23,10),"New years eve"));
		readings.add(new Reading(94.22D,new Date(113,0,14,18,15,49),"Birthday"));
		readings.add(new Reading(93.5D,new Date(113,0,18,8,59,10),"today"));
		return readings;
	}

	public void testformat1()
	{
		List<Reading> readings = createReadings();
		String text = ReadingsSerializer.format(readings);
		assertTrue(EXAMPLE_DATA.equals(text));
	}
	public void testformat2()
	{
		ArrayList<Reading> readings = new ArrayList<Reading>();
		String text = ReadingsSerializer.format(readings);
		assertTrue("".equals(text));
		
	}
	public void testparse1()
	{
		List<Reading> readings = ReadingsSerializer.parse(EXAMPLE_DATA);
		assertTrue(readings.size()==5);
		assertTrue(readings.get(0).getComment().equals("Christmas Day"));
		assertTrue(readings.get(3).getDate().equals(new Date(113,0,14,18,15,49)));
		assertTrue(readings.get(1).getWeight()==95.23D);
		
	}
	public void testparse2()
	{
		List<Reading> readings = ReadingsSerializer.parse(BAD_DATA);
		assertTrue(readings.size()==3);
		assertTrue(readings.get(0).getComment().equals("Boxing Day"));
		assertTrue(readings.get(1).getDate().equals(new Date(113,0,14,18,15,49)));
		assertTrue(readings.get(2).getWeight()==93.5D);
	}
	
	public void testFormatParse()
	{
		List<Reading> readings = ReadingsSerializer.parse(EXAMPLE_DATA);
		String text = ReadingsSerializer.format(readings);
		assertTrue(EXAMPLE_DATA.equals(text));
	}
}
