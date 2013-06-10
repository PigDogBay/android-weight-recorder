package com.pigdogbay.weightrecorder.model;

import java.util.Collections;
import java.util.List;
import com.pigdogbay.weightrecorder.R;
import android.content.Context;

/*
 * Global static container for all the activities in the application
 * 
 */
public class MainModel
{
	public static final double DEFAULT_HEIGHT=1.72;
	public static final double DEFAULT_TARGET_WEIGHT=75.0;
	private IReadingsDatabase _DatabaseHelper;
	private PreferencesHelper _PreferencesHelper; 
	
	public MainModel(Context context)
	{
		_PreferencesHelper = new PreferencesHelper(context);
		_DatabaseHelper = new DatabaseHelper(context); 
	}
	
	public IReadingsDatabase getDatabase()
	{
		return _DatabaseHelper;
	}
	public PreferencesHelper getPreferencesHelper()
	{
		return _PreferencesHelper;
	}
	
	public List<Reading> getReverseOrderedReadings(){
		List<Reading> readings = _DatabaseHelper.getAllReadings();
		Query query = new Query(readings);
		query.sortByDate();
		readings = query.getReadings();
		Collections.reverse(readings);
		return readings;
	}
	/**
	 * @return weight in currently selected units
	 */
	public double getHeight()
	{
		return _PreferencesHelper.getDouble(R.string.code_pref_height_key, DEFAULT_HEIGHT);
	}
	public double getTargetWeight()
	{
		return _PreferencesHelper.getDouble(R.string.code_pref_target_weight_key, DEFAULT_TARGET_WEIGHT);
	}
	public IUnitConverter getWeightConverter()
	{
		int converterType = _PreferencesHelper.getInt(R.string.code_pref_weight_units_key, UnitConverterFactory.KILOGRAMS_TO_KILOGRAMS);
		return UnitConverterFactory.create(converterType);
	}
	public IUnitConverter getLengthConverter()
	{
		int converterType = _PreferencesHelper.getInt(R.string.code_pref_length_units_key, UnitConverterFactory.METRES_TO_METRES);
		return UnitConverterFactory.createLengthConverter(converterType);
	}
	public double getHeightInMetres()
	{
		return getLengthConverter().inverse(getHeight());
	}
	/**
	 * @param weight in current user selected units
	 * @return Body Mass Index
	 */
	public double calculateBMI(double weight)
	{
		//get height in metres
		double bmi = getLengthConverter().inverse(getHeight());
		//get weight in kilograms
		weight = getWeightConverter().inverse(weight);
		if (bmi!=0)
		{
			bmi = weight/(bmi*bmi);
		}
		return bmi;
	}
	public double calculateBMI(Reading reading)
	{
		//get height in metres
		double bmi = getLengthConverter().inverse(getHeight());
		if (bmi!=0)
		{
			bmi = reading.getWeight()/(bmi*bmi);
		}
		return bmi;
	}
	public double calculateWeightFromBMI(double bmi)
	{
		double height = getLengthConverter().inverse(getHeight());
		return bmi*height*height;
	}
	/**
	 * @param weight in kilograms
	 * @return String representation, ie 212.16 lbs
	 */
	public String weightToString(double weight)
	{
		IUnitConverter weightConverter = getWeightConverter();
		weight = weightConverter.convert(weight);
		return String.format("%.2f ", weight) + weightConverter.getUnits();
	}	
		
}
