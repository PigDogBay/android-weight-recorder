package com.pigdogbay.weightrecorder;

import com.pigdogbay.weightrecorder.model.MainModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        // Note that none of the preferences are actually defined here.
        // They're all in the XML file res/xml/preferences.xml.
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setContentView(R.layout.activity_settings);
        ActivitiesHelper.setBackground(this);
		PreferenceManager
		.getDefaultSharedPreferences(this)
		.registerOnSharedPreferenceChangeListener(this);
        
        
    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
		PreferenceManager.getDefaultSharedPreferences(this)
		.unregisterOnSharedPreferenceChangeListener(this);
    }
    
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void setBackground(int backgroundID)
	{
        Drawable background = getResources().getDrawable(backgroundID);
        View view = getListView();
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
		{
	        view.setBackgroundDrawable(background);
		} 
		else
		{
	        view.setBackground(background);
		}
	}
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(getString(R.string.code_pref_background_colour))){
			ActivitiesHelper.setBackground(this);
		}
		
	}	
    
}