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
	private static final MainModel _Singleton = new MainModel();
	private IUnitConverter _WeightConverter;
	private List<IDataChangedListener> _DataChangedListeners;
	private double _Height=DEFAULT_HEIGHT;
	
	public static MainModel getInstance()
	{
		return _Singleton;
	}
	public static IReadingsDatabase getDatabase()
	{
		return _Singleton._DatabaseHelper;
	}

	private MainModel()
	{
		_WeightConverter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_KILOGRAMS);
		_DataChangedListeners = new ArrayList<IDataChangedListener>();
	}

	public void initialize(Application application)
	{
		if (_DatabaseHelper == null)
		{
			_DatabaseHelper = new DatabaseHelper(
					application.getApplicationContext());
		}
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
	
	public void registerDataChangedListener(IDataChangedListener listener)
	{
		_DataChangedListeners.add(listener);
	}
	public void unregisterDataChangedListener(IDataChangedListener listener)
	{
		_DataChangedListeners.remove(listener);
	}
	public void notifyDataChanged()
	{
		for (IDataChangedListener listener : _DataChangedListeners)
		{
			listener.onDataChanged();
		}
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
