package com.pigdogbay.weightrecorder;

import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Reading;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AddReadingActivity extends FragmentActivity
{

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.activity_add_reading);
		((Button) findViewById(R.id.AddReadingBtnAdd))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						onEnterClick();
					}
				});
		ActivitiesHelper.initializeMainModel(getApplication());		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add_reading, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case (R.id.menu_add_reading_home):
			finish();
			break;
		default:
			return false;
		}
		return true;
	}	

	private void onEnterClick()
	{
		EditFragment fragment = (EditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.AddReadingEditFragment);
		Reading reading = fragment.getReading();
		MainModel.getInstance().getDatabase().addReading(reading);
		fragment.hideKeyboard();
		savePreferences();
		Toast.makeText(this, getString(R.string.addreading_added),
				Toast.LENGTH_SHORT).show();
		this.finish();
	}

	@Override
	protected void onStart()
	{
		// onStart, the fragment will have been created by now
		super.onStart();
		loadPreferences();
	}

	private void loadPreferences()
	{
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String key = getString(R.string.code_pref_last_weight_entered_key);
		String lastWeight = sharedPrefs.getString(key, "");
		EditFragment fragment = (EditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.AddReadingEditFragment);
		fragment.setWeight(lastWeight);

	}

	private void savePreferences()
	{
		EditFragment fragment = (EditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.AddReadingEditFragment);
		String lastWeight = fragment.getWeight();
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String key = getString(R.string.code_pref_last_weight_entered_key);
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putString(key, lastWeight);
		editor.commit();

	}

}
