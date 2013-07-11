package com.pigdogbay.weightrecorder.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Formatting / parsing uses the standard US Locale
 * Double.toString() and Double.parseDouble use US Locale 
 *
 */
public class PreferencesHelper {

	SharedPreferences _SharedPreferences;
	Context _Context;

	public PreferencesHelper(Context context) {
		_SharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		_Context = context;
	}

	public boolean getBoolean(int id, boolean defaultValue)
	{
		String key = _Context.getString(id);
		return _SharedPreferences.getBoolean(key, defaultValue);	
	}
	public void setBoolean(int id, boolean flag)
	{
		String key = _Context.getString(id);
		SharedPreferences.Editor editor = _SharedPreferences.edit();
		editor.putBoolean(key, flag);
		editor.commit();		
	}
	public long getLong(int id, long defaultValue) {
		String key = _Context.getString(id);
		long value = defaultValue;
		try {
			String stringValue = _SharedPreferences.getString(key,Long.toString(defaultValue));
			value = Long.parseLong(stringValue);
		}
		catch (Exception ex) {
		}
		return value;
	}

	public void setLong(int id, long value) {
		String key = _Context.getString(id);
		SharedPreferences.Editor editor = _SharedPreferences.edit();
		editor.putString(key, Long.toString(value));
		editor.commit();
	}
	public int getInt(int id, int defaultValue) {
		String key = _Context.getString(id);
		int intValue = defaultValue;
		try {
			String stringValue = _SharedPreferences.getString(key,Integer.toString(defaultValue));
			intValue = Integer.parseInt(stringValue);
		}
		catch (Exception ex) {
		}
		return intValue;
	}

	public void setInt(int id, int value) {
		String key = _Context.getString(id);
		SharedPreferences.Editor editor = _SharedPreferences.edit();
		editor.putString(key, Integer.toString(value));
		editor.commit();
	}
	public double getDouble(int id, double defaultValue) {
		String key = _Context.getString(id);
		double value = defaultValue;
		try {
			String stringValue = _SharedPreferences.getString(key,Double.toString(defaultValue));
			value = Double.parseDouble(stringValue);
		}
		catch (Exception ex) {
		}
		return value;
	}

	public void setDouble(int id, double value) {
		String key = _Context.getString(id);
		SharedPreferences.Editor editor = _SharedPreferences.edit();
		editor.putString(key, Double.toString(value));
		editor.commit();
	}
	public String getString(int id, String defaultValue)
	{
		String key = _Context.getString(id);
		return _SharedPreferences.getString(key, defaultValue);
	}
	public void setString(int id, String value)
	{
		String key = _Context.getString(id);
		SharedPreferences.Editor editor = _SharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

}
