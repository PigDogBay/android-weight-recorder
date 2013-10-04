package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.mvp.BackgroundColorPresenter;
import com.pigdogbay.androidutils.mvp.IBackgroundColorView;
import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.weightrecorder.HelpActivity.HelpFragment;
import com.pigdogbay.weightrecorder.HelpActivity.QuickHelpFragment;
import com.pigdogbay.weightrecorder.model.MainModel;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends FragmentActivity implements OnSharedPreferenceChangeListener, IBackgroundColorView{

	BackgroundColorPresenter _BackgroundColorPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        // Note that none of the preferences are actually defined here.
        // They're all in the XML file res/xml/preferences.xml.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SettingsPagerAdapter adapter = new SettingsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager)findViewById(R.id.rootLayout);
        viewPager.setAdapter(adapter);
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
			_BackgroundColorPresenter.updateBackground();
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

    public class SettingsPagerAdapter extends FragmentPagerAdapter {

        public SettingsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WeightSettingsFragment();
                case 1:
                    return new HeightSettingsFragment();
                case 2:
                    return new HelpActivity.QuickHelpFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "WEIGHT";
                case 1:
                    return "HEIGHT";
                case 2:
                    return "OTHER";
            }
            return null;
        }
    }

   
}