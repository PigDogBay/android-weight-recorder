package com.pigdogbay.weightrecorder;

import java.util.List;

import com.pigdogbay.androidutils.apprate.AppRate;
import com.pigdogbay.weightrecorder.model.IUnitConverter;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.UnitConverterFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements
		OnSharedPreferenceChangeListener
{

	public static final String TAG = "WeightRecorder";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		LoadPreferences(prefs);
		setContentView(R.layout.activity_main);
		wireUpButtons();
		ActivitiesHelper.initializeMainModel(getApplication());		

		checkFirstTime();
		new AppRate(this).setMinDaysUntilPrompt(7)
		.setMinLaunchesUntilPrompt(5).init();
	}

	private void wireUpButtons()
	{
		((Button) findViewById(R.id.MainBtnEntry))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						Intent intent = new Intent(MainActivity.this,
								AddReadingActivity.class);
						startActivity(intent);
					}
				});
		((Button) findViewById(R.id.MainBtnEditor))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						Intent intent = new Intent(MainActivity.this,
								ReadingListActivity.class);
						startActivity(intent);
					}
				});
		((Button) findViewById(R.id.MainBtnChart))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						Intent intent = new Intent(MainActivity.this,
								ChartActivity.class);
						startActivity(intent);
					}
				});
		((Button) findViewById(R.id.MainBtnReport))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						Intent intent = new Intent(MainActivity.this,
								ReportActivity.class);
						startActivity(intent);
					}
				});
		((Button) findViewById(R.id.MainBtnHelp))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						Intent intent = new Intent(MainActivity.this,
								HelpActivity.class);
						startActivity(intent);
					}
				});
		((Button) findViewById(R.id.MainBtnSettings))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						Intent intent = new Intent(MainActivity.this,
								SettingsActivity.class);
						startActivity(intent);
					}
				});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case (R.id.menu_main_about):
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		case (R.id.menu_main_export):
			ActivitiesHelper.startExportActivity(this);
			break;
		case (R.id.menu_main_import):
			ActivitiesHelper.startImportActivity(this);
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
	{
		LoadPreferences(prefs);
	}

	private void LoadPreferences(SharedPreferences prefs)
	{
		try
		{
			int weightUnits = Integer.parseInt(prefs.getString(getString(R.string.code_pref_weight_units_key),"1"));
			IUnitConverter converter = UnitConverterFactory.create(weightUnits);
			MainModel.getInstance().setWeightConverter(converter);
			int lengthUnits = Integer.parseInt(prefs.getString(getString(R.string.code_pref_length_units_key),"1"));
			double height = Double.parseDouble(prefs.getString(getString(R.string.code_pref_height_key), "1.72"));
			IUnitConverter lengthConverter = UnitConverterFactory.createLengthConverter(lengthUnits);
			//convert to metres
			height = lengthConverter.inverse(height);
			MainModel.getInstance().setHeight(height);
		}
		catch (Exception e)
		{
			//main model will default to kilograms
			Log.v(TAG, "Unable to load preferences: "+e.getMessage());
		}

	}
	private void checkFirstTime()
	{
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String key = getString(R.string.code_pref_welcome_shown_key);
		boolean welcomeScreenShown = sharedPrefs.getBoolean(key, false);
		if (!welcomeScreenShown)
		{
			showWelcomeScreen();
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putBoolean(key, true);
			editor.commit();
		}
	}
	private void showWelcomeScreen()
	{
		String whatsNewTitle = getResources().getString(R.string.main_welcome_title);
		String whatsNewText = getResources().getString(R.string.main_welcome_msg);
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(whatsNewTitle)
				.setMessage(whatsNewText)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{
								dialog.dismiss();
							}
						}).show();
	}	
}
