package com.pigdogbay.weightrecorder.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.pigdogbay.weightrecorder.R;
import android.content.Context;

/*
 * Interface to the user data and settings
 * 
 */
public class MainModel
{
	public static final double DEFAULT_HEIGHT=1.72D;
	//Same as 175lb
	public static final double DEFAULT_TARGET_WEIGHT=79.3787D;
	public static final double DEFAULT_HEIGHT_INCHES=72D;
	public static final double DEFAULT_TARGET_WEIGHT_POUNDS=175D;
	public static final long AD_FREE_GRACE_PERIOD_IN_MILLIS = 7L*24L*60L*60L*1000L;
	
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
		return getPreferencesHelper().getDouble(R.string.code_pref_height_key, DEFAULT_HEIGHT_INCHES);
	}
	public double getTargetWeight()
	{
		return getPreferencesHelper().getDouble(R.string.code_pref_target_weight_key, DEFAULT_TARGET_WEIGHT_POUNDS);
	}
	public IUnitConverter getWeightConverter()
	{
		int converterType = getPreferencesHelper().getInt(R.string.code_pref_weight_units_key, UnitConverterFactory.KILOGRAMS_TO_POUNDS);
		return UnitConverterFactory.create(converterType);
	}
	public IUnitConverter getLengthConverter()
	{
		int converterType = getPreferencesHelper().getInt(R.string.code_pref_length_units_key, UnitConverterFactory.METRES_TO_INCHES);
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
	public boolean getShowTargetLine()
	{
		return getPreferencesHelper().getBoolean(R.string.code_pref_show_targetline_key, true);
	}
	public boolean getShowTrendLine()
	{
		return getPreferencesHelper().getBoolean(R.string.code_pref_show_trendline_key, true);
	}
	public boolean getRemoveAds()
	{
		long purchaseTime = getPreferencesHelper().getLong(R.string.code_pref_purchase_date,0L);
		long timeElapsedSincePurchase = new Date().getTime()-purchaseTime;
		if (timeElapsedSincePurchase<AD_FREE_GRACE_PERIOD_IN_MILLIS)
		{
			return true;
		}
		return getPreferencesHelper().getBoolean(R.string.code_pref_disable_ads_key, false);
	}
	public boolean getUnlockColorPack()
	{
		return getPreferencesHelper().getBoolean(R.string.code_pref_unlock_color_pack_key, false);
	}
	public int getBackgroundId()
	{
		int colorIndex =getPreferencesHelper().getInt(R.string.code_pref_background_colour, 0);
		switch (colorIndex){
		case 1:
			return R.drawable.bgpink;
		case 2:
			return R.drawable.bggrey;
		case 3:
			return R.drawable.white;
		case 4:
			return R.drawable.bglightpink;
		}
		return R.drawable.bgskyblue;
	}
	
	public UserSettings getUserSettings()
	{
		UserSettings userSettings = new UserSettings();
		userSettings.Height = getHeightInMetres();
		userSettings.TargetWeight = getTargetWeightInKilograms();
		userSettings.LengthConverter = getLengthConverter();
		userSettings.WeightConverter = getWeightConverter();
		userSettings.ShowTargetLine= getShowTargetLine();
		userSettings.ShowTrendLine = getShowTrendLine();
		return userSettings;
	}
		
}
