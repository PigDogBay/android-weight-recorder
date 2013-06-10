package com.pigdogbay.weightrecorder.model;

import java.util.Collections;
import java.util.List;
import com.pigdogbay.weightrecorder.R;
import android.content.Context;

/*
 * Interface to the user data and settings
 * 
 */
public class MainModel
{
	public static final double DEFAULT_HEIGHT=1.72;
	public static final double DEFAULT_TARGET_WEIGHT=75.0;
	private Context _Context;
	private IReadingsDatabase _DatabaseHelper;
	private PreferencesHelper _PreferencesHelper; 
	
	public MainModel(Context context)
	{
		_Context = context;
	}
	
	public IReadingsDatabase getDatabase()
	{
		if (_DatabaseHelper==null)
		{
			_DatabaseHelper = new DatabaseHelper(_Context); 
		}
		return _DatabaseHelper;
	}
	public PreferencesHelper getPreferencesHelper()
	{
		if (_PreferencesHelper==null)
		{
			_PreferencesHelper = new PreferencesHelper(_Context);
		}
		return _PreferencesHelper;
	}
	public void close()
	{
		if (_DatabaseHelper!=null)
		{
			_DatabaseHelper.close();
		}
	}
	
	public List<Reading> getReverseOrderedReadings(){
		List<Reading> readings = getDatabase().getAllReadings();
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
		return getPreferencesHelper().getDouble(R.string.code_pref_height_key, DEFAULT_HEIGHT);
	}
	public double getTargetWeight()
	{
		return getPreferencesHelper().getDouble(R.string.code_pref_target_weight_key, DEFAULT_TARGET_WEIGHT);
	}
	public IUnitConverter getWeightConverter()
	{
		int converterType = getPreferencesHelper().getInt(R.string.code_pref_weight_units_key, UnitConverterFactory.KILOGRAMS_TO_KILOGRAMS);
		return UnitConverterFactory.create(converterType);
	}
	public IUnitConverter getLengthConverter()
	{
		int converterType = getPreferencesHelper().getInt(R.string.code_pref_length_units_key, UnitConverterFactory.METRES_TO_METRES);
		return UnitConverterFactory.createLengthConverter(converterType);
	}
	public double getHeightInMetres()
	{
		return getLengthConverter().inverse(getHeight());
	}
	public double getTargetWeightInKilograms()
	{
		return getWeightConverter().inverse(getTargetWeight());
	}
	
	public UserSettings getUserSettings()
	{
		UserSettings userSettings = new UserSettings();
		userSettings.Height = getHeightInMetres();
		userSettings.TargetWeight = getTargetWeightInKilograms();
		userSettings.LengthConverter = getLengthConverter();
		userSettings.WeightConverter = getWeightConverter();
		return userSettings;
	}
		
}
