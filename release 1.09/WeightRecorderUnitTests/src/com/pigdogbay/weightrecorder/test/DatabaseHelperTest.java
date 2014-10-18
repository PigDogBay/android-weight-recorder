package com.pigdogbay.weightrecorder.test;

import java.util.Date;
import java.util.List;

import com.pigdogbay.weightrecorder.model.DatabaseHelper;
import com.pigdogbay.weightrecorder.model.Reading;

import android.test.AndroidTestCase;

public class DatabaseHelperTest extends AndroidTestCase {
	
	
	public void testDeleteAll()
	{
		DatabaseHelper target = new DatabaseHelper(mContext);
		target.deleteAllReadings();
		assertEquals(0, target.getReadingsCount());
	}
	public void testAddReading()
	{
		Date now = new Date();
		DatabaseHelper target = new DatabaseHelper(mContext);
		target.deleteAllReadings();
		Reading reading = new Reading(72D,now, "hello");
		target.addReading(reading);
		assertEquals(1, target.getReadingsCount());
		List<Reading> readings = target.getAllReadings();
		assertEquals(72D,readings.get(0).getWeight());
		assertEquals(now.getTime(),readings.get(0).getDate().getTime());
		assertEquals("hello",readings.get(0).getComment());
		assertTrue(readings.get(0).getID()>0);
	}
	public void testAddReadings()
	{
		List<Reading> readings =Mocks.getChristmasReadings(); 
		DatabaseHelper target = new DatabaseHelper(mContext);
		target.deleteAllReadings();
		target.addReadings(readings);
		assertEquals(readings.size(), target.getReadingsCount());
		target.deleteAllReadings();
		
	}
	public void testUpdateReadings()
	{
		Reading original = new Reading(100D,new Date(),"song pop");
		DatabaseHelper target = new DatabaseHelper(mContext);
		target.deleteAllReadings();
		target.addReading(original);
		Reading dbReading = target.getAllReadings().get(0);
		dbReading.setComment("candy crush");
		target.updateReading(dbReading);
		dbReading = target.getAllReadings().get(0);
		assertEquals("candy crush", dbReading.getComment());
		assertEquals(original.getDate(), dbReading.getDate());
		assertEquals(original.getWeight(), dbReading.getWeight());
		
	}

}
