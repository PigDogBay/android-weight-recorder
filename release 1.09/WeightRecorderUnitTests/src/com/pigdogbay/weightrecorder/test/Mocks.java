package com.pigdogbay.weightrecorder.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.UnitConverterFactory;
import com.pigdogbay.weightrecorder.model.UserSettings;

public class Mocks {

	public static final double DAILY_WEIGHT_TREND = -0.01D;
	public static final double HEIGHT = 1.72D;
	public static final double START_WEIGHT = 100D;
	public static final double TARGET_WEIGHT = 85D;
	
	public static UserSettings createMetricSettings(double height, double weight)
	{
		UserSettings userSettings = new UserSettings();
		userSettings.Height=height;
		userSettings.TargetWeight = weight;
		userSettings.WeightConverter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_KILOGRAMS);
		userSettings.LengthConverter = UnitConverterFactory.create(UnitConverterFactory.METRES_TO_METRES);
		return userSettings;
	}
	public static UserSettings createUSSettings(double height, double weight)
	{
		UserSettings userSettings = new UserSettings();
		userSettings.Height=height;
		userSettings.TargetWeight = weight;
		userSettings.WeightConverter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_POUNDS);
		userSettings.LengthConverter = UnitConverterFactory.create(UnitConverterFactory.METRES_TO_INCHES);
		return userSettings;
	}
	public static List<Reading> createReadings(int number, double weightStep)
	{
		return createReadings(START_WEIGHT,number, weightStep);
	}
	public static List<Reading> createReadings(double startWeight,int number, double weightStep)
	{
		List<Reading> readings = new ArrayList<Reading>();
		double weight = 100;
		Calendar cal = Calendar.getInstance();
		for (int i=0;i<number;i++){
			Reading r = new Reading(weight,cal.getTime(),"");
			readings.add(r);
			weight = weight -weightStep;
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		return readings;
	}
	
	public static List<Reading> getChristmasReadings()
	{
		ArrayList<Reading> readings = new ArrayList<Reading>();
		readings.add(new Reading(94.2D,new Date(112,11,22,13,23,10),"nearly there"));
		readings.add(new Reading(94.1D,new Date(112,11,23,13,23,10),"Last day at work"));
		readings.add(new Reading(94.7D,new Date(112,11,24,13,23,10),"Christmas eve"));
		readings.add(new Reading(94.6D,new Date(112,11,25,13,23,10),"Christmas Day"));
		readings.add(new Reading(95.23D,new Date(112,11,26,13,23,10),"Boxing Day"));
		readings.add(new Reading(96.1D,new Date(112,11,31,13,23,10),"New years eve"));
		readings.add(new Reading(94.22D,new Date(113,0,14,18,15,49),"Birthday"));
		readings.add(new Reading(93.5D,new Date(113,0,18,8,59,10),"today"));
		return readings;
	}
	public static List<Reading> createFutureReadings()
	{
		Calendar cal = Calendar.getInstance();
		List<Reading> future = new ArrayList<Reading>();
		for (int i=0;i<100;i++)
		{
			cal.add(Calendar.DATE,1);
			future.add(new Reading(100D,cal.getTime(),""));
		}
		return future;
	}
	
}
