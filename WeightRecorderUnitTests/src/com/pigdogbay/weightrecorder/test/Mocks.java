package com.pigdogbay.weightrecorder.test;

import java.util.ArrayList;
import java.util.Calendar;
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
}
