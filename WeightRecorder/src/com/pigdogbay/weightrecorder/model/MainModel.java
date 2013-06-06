package com.pigdogbay.weightrecorder.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.app.Application;

/*
 * Global static container for all the activities in the application
 * 
 */
public class MainModel
{
	public static final double DEFAULT_HEIGHT=1.72;
	private IReadingsDatabase _DatabaseHelper;
	private static MainModel _Singleton;
	private IUnitConverter _WeightConverter;
	private double _Height=DEFAULT_HEIGHT;
	
	public static MainModel getInstance()
	{
		if (_Singleton==null)
		{
			_Singleton = new MainModel();
		}
		return _Singleton;
	}
	public IReadingsDatabase getDatabase()
	{
		return _DatabaseHelper;
	}
	public void setDatabase(IReadingsDatabase database)
	{
		_DatabaseHelper = database;
	}
	
	private MainModel()
	{
		_WeightConverter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_KILOGRAMS);
	}

	public List<Reading> getReverseOrderedReadings(){
		List<Reading> readings = _DatabaseHelper.getAllReadings();
		Query query = new Query(readings);
		query.sortByDate();
		readings = query.getReadings();
		Collections.reverse(readings);
		return readings;
	}
	/*
	 * @param height the height in metres
	 */
	public void setHeight(double height)
	{
		_Height= height;
	}
	/*
	 * @return height in metres
	 */
	public double getHeight()
	{
		return _Height;
	}
	public IUnitConverter getWeightConverter()
	{
		return _WeightConverter;
	}
	public void setWeightConverter(IUnitConverter converter)
	{
		_WeightConverter = converter;
	}
	
	public double calculateBMI(double weight)
	{
		double bmi = _Height;
		weight = _WeightConverter.inverse(weight);
		if (_Height!=0)
		{
			bmi = weight/(bmi*bmi);
		}
		return bmi;
	}
	public double calculateBMI(Reading reading)
	{
		double bmi = _Height;
		if (_Height!=0)
		{
			bmi = reading.getWeight()/(bmi*bmi);
		}
		return bmi;
	}
	public double calculateWeightFromBMI(double bmi)
	{
		return bmi*_Height*_Height;
	}
	public String weightToString(double weight)
	{
		weight = _WeightConverter.convert(weight);
		return String.format("%.2f ", weight) + _WeightConverter.getUnits();
	}	
		
}
