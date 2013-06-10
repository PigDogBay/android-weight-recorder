package com.pigdogbay.weightrecorder.model;

public class BMICalculator {
	
	public static final double SEVERLY_UNDERWEIGHT_UPPER_LIMIT = 16.5;
	public static final double UNDERWEIGHT_UPPER_LIMIT = 18.5;
	public static final double NORMAL_UPPER_LIMIT = 25;
	public static final double OVERWEIGHT_UPPER_LIMIT = 30;
	public static final double OBESE1_UPPER_LIMIT = 35;
	public static final double OBESE2_UPPER_LIMIT = 40;
	
	private UserSettings _UserSettings;
	public BMICalculator(UserSettings userSettings)
	{
		_UserSettings = userSettings;
	}
	
	/**
	 * @param weight in kilograms
	 * @return Body Mass Index
	 */
	public double calculateBMI(double weight)
	{
		double bmi = _UserSettings.Height;
		if (bmi!=0)
		{
			bmi = weight/(bmi*bmi);
		}
		return bmi;
	}
	public double calculateBMI(Reading reading)
	{
		return calculateBMI(reading.getWeight());
	}
	
	/**
	 * @param bmi
	 * @return weight in Kilograms
	 */
	public double calculateWeightFromBMI(double bmi)
	{
		return bmi*_UserSettings.Height*_UserSettings.Height;
	}	
}
