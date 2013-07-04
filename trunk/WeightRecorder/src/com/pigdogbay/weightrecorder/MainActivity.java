package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.apprate.AppRate;
import com.pigdogbay.weightrecorder.model.PreferencesHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity
{

	public static final String TAG = "WeightRecorder";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		wireUpButtons();
		checkFirstTime();
	    checkRate();
	}
	private void checkRate()
	{
		try {
			new AppRate(this)
			.setCustomDialog(createRateDialog())
			.setMinDaysUntilPrompt(7)
			.setMinLaunchesUntilPrompt(5)
//			.setMinDaysUntilPrompt(0)
//			.setMinLaunchesUntilPrompt(0)
			.init();
		}
		catch (Exception e) {
		}
	}
	private AlertDialog.Builder createRateDialog()
	{
		return new AlertDialog.Builder(this)
		.setTitle(R.string.rate_dialog_title)
		.setMessage(R.string.rate_dialog_message)
		.setPositiveButton(R.string.rate_dialog_positive, null)
		.setNegativeButton(R.string.rate_dialog_negative, null)
		.setNeutralButton(R.string.rate_dialog_neutral,null);
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

	private void checkFirstTime()
	{
		PreferencesHelper prefHelper = new PreferencesHelper(this);
		boolean welcomeScreenShown = prefHelper.getBoolean(R.string.code_pref_welcome_shown_key, false);
		if (!welcomeScreenShown)
		{
			showWelcomeScreen();
			prefHelper.setBoolean(R.string.code_pref_welcome_shown_key, true);
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
