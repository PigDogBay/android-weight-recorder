package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.mvp.BackgroundColorPresenter;
import com.pigdogbay.androidutils.mvp.IBackgroundColorView;
import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.weightrecorder.model.MainModel;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener, IBackgroundColorView{

	BackgroundColorPresenter _BackgroundColorPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        // Note that none of the preferences are actually defined here.
        // They're all in the XML file res/xml/preferences.xml.
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setContentView(R.layout.activity_settings);
		_BackgroundColorPresenter = new BackgroundColorPresenter(this,new MainModel(this).createBackgroundColorModel());
		_BackgroundColorPresenter.updateBackground();
    }
    @Override
    protected void onResume() {
    	super.onResume();
		PreferenceManager.getDefaultSharedPreferences(this)
		.registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    protected void onPause() {
    	super.onPause();
		PreferenceManager.getDefaultSharedPreferences(this)
		.unregisterOnSharedPreferenceChangeListener(this);
    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
		PreferenceManager.getDefaultSharedPreferences(this)
		.unregisterOnSharedPreferenceChangeListener(this);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_settings, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case (R.id.menu_settings_home):
			finish();
			break;
		default:
			return false;
		}
		return true;
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
			_BackgroundColorPresenter.trialUpdateBackground();
		}
	}	
	@Override
	public void setBackgroundColor(int id) {
		ActivityUtils.setBackground(this, R.id.rootLayout, id);
	}
	@Override
	public void showPurchaseRequiredWarning() {
		ActivityUtils.showInfoDialog(this,R.string.settings_purchase_color_pack_title , R.string.settings_purchase_color_pack_message);
	}	
   
}