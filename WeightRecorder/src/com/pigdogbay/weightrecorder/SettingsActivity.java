package com.pigdogbay.weightrecorder;

import com.pigdogbay.weightrecorder.model.MainModel;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

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
    
	/* (non-Javadoc)
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 * 
	 * Allow the user to see the new colour background to whet their appetite, put up a notice for them to purchase the colour pack
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(getString(R.string.code_pref_background_colour))){
			MainModel mainModel = new MainModel(this);
			int bgId = mainModel.getBackgroundId();
			boolean hasUnlockColorPackPurchase = mainModel.getUnlockColorPack();
			mainModel.close();		
			ActivitiesHelper.setBackground(this, R.id.rootLayout, bgId);
			if  (!hasUnlockColorPackPurchase)
			{
				ActivitiesHelper.showInfoDialog(this,R.string.settings_purchase_color_pack_title , R.string.settings_purchase_color_pack_message);
			}
		}
	}	
    
}