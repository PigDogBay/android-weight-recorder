package com.pigdogbay.weightrecorder;

import java.util.Locale;

import com.pigdogbay.androidutils.apprate.AppRate;
import com.pigdogbay.androidutils.mvp.BackgroundColorPresenter;
import com.pigdogbay.androidutils.mvp.IBackgroundColorView;
import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.androidutils.utils.PreferencesHelper;
import com.pigdogbay.weightrecorder.model.AutoBackup;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.SettingsUtils;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnSharedPreferenceChangeListener,IBackgroundColorView{
	public static final String TAG = "WeightTracker";

	BackgroundColorPresenter _BackgroundColorPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MainModel mainModel = new MainModel(this);
		_BackgroundColorPresenter = new BackgroundColorPresenter(this,mainModel.createBackgroundColorModel());
		_BackgroundColorPresenter.updateBackground();
		
		//if app has been rotated, then skip this part as the existing fragment will have already been recreated
		if (getSupportFragmentManager().findFragmentById(R.id.main_fragment_container)==null)
		{
			try {
				checkFirstTime(mainModel);
				checkIfBackupDue(mainModel.getPreferencesHelper());
				checkRate();
			}
			catch (Exception e) {
			}
		}
		mainModel.close();
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

	private void checkRate() {
		try {
			new AppRate(this).setCustomDialog(createRateDialog())
					.setMinDaysUntilPrompt(7).setMinLaunchesUntilPrompt(5)
					// .setMinDaysUntilPrompt(0)
					// .setMinLaunchesUntilPrompt(0)
					.init();
		}
		catch (Exception e) {
		}
	}
	private void checkFirstTime(MainModel mainModel) {
		if (!mainModel.getIsFirstTime()) {
			SettingsUtils.setDefaultSettings(Locale.getDefault(), new MainModel(this));
			mainModel.setIsFirstTime(true);
			showWelcome();
		}
		else
		{
			showHome();
		}
	}
	
	private void checkIfBackupDue(PreferencesHelper prefHelper) {
		AutoBackup autoBackup = new AutoBackup(prefHelper);
		if (autoBackup.isAutoBackupEnabled()
				&& autoBackup.isBackupDue(AutoBackup.WEEKLY_BACKUP_PERIOD_IN_DAYS)) {
			autoBackup.setBackupDateToNow();
			ActivitiesHelper.backupReadings(this);
		}
	}
	
	
	private AlertDialog.Builder createRateDialog() {
		return new AlertDialog.Builder(this)
				.setTitle(R.string.rate_dialog_title)
				.setMessage(R.string.rate_dialog_message)
				.setPositiveButton(R.string.rate_dialog_positive, null)
				.setNegativeButton(R.string.rate_dialog_negative, null)
				.setNeutralButton(R.string.rate_dialog_neutral, null);
	}

	@Override
	public void onBackPressed() {
		Fragment f = (Fragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
		if (f!=null)
		{
			super.onBackPressed();
		}
		else
		{
			showHome();
		}
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.menu_home:
			case android.R.id.home:
				showHome();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	private void replaceFragment(Fragment fragment, String tag) {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.main_fragment_container, fragment, tag)
				.commit();
	}
	
	
	public void showHome(){
		replaceFragment(new HomeFragment(), HomeFragment.TAG);
	}
	public void showAbout(){
		replaceFragment(new AboutFragment(), AboutFragment.TAG);
	}
	public void showWelcome(){
		replaceFragment(new WelcomeWizardFragment(), WelcomeWizardFragment.TAG);
	}
	public void showNew(){
		Log.v(TAG,"Show New");
	}
	public void showEdit(){
		Log.v(TAG,"Show Edit");
	}
	public void showSettings(){
		replaceFragment(new SettingsWizardFragment(), SettingsWizardFragment.TAG);
	}
	public void showChart(){
		Log.v(TAG,"Show Chart");
	}
	public void showReport(){
		replaceFragment(new ReportFragment(), ReportFragment.TAG);
	}
	public void showHelp(){
		replaceFragment(new HelpWizardFragment(), HelpWizardFragment.TAG);
	}
	
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		if (key.equals(getString(R.string.code_pref_background_colour))){
			_BackgroundColorPresenter.updateBackground();
		}
	}	
	@Override
	public void setBackgroundColor(int id) {
		ActivityUtils.setBackground(this, R.id.root_layout, id);
	}	
}
