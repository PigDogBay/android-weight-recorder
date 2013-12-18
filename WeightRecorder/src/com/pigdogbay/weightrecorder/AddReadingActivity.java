package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.mvp.BackgroundColorPresenter;
import com.pigdogbay.androidutils.mvp.IBackgroundColorView;
import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.weightrecorder.model.IUnitConverter;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Reading;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AddReadingActivity extends FragmentActivity implements IBackgroundColorView
{
	private MainModel _MainModel;
	BackgroundColorPresenter _BackgroundColorPresenter;
	
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
		_MainModel = new MainModel(this);
		_BackgroundColorPresenter = new BackgroundColorPresenter(this,_MainModel.createBackgroundColorModel());
		_BackgroundColorPresenter.updateBackground();
		
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
		_MainModel.getDatabase().addReading(reading);
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		_MainModel.close();
	}

	private void loadPreferences()
	{
		IUnitConverter weightConverter = _MainModel.getWeightConverter();
		double lastWeight = _MainModel.getPreferencesHelper().getDouble(R.string.code_pref_last_weight_key, MainModel.DEFAULT_TARGET_WEIGHT);
		lastWeight = weightConverter.convert(lastWeight);
		EditFragment fragment = (EditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.AddReadingEditFragment);
		fragment.setWeightConvert(weightConverter);
		fragment.setWeight(lastWeight);
	}

	private void savePreferences()
	{
		EditFragment fragment = (EditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.AddReadingEditFragment);
		 double lastWeight = fragment.getWeight();
		 IUnitConverter weightConverter = _MainModel.getWeightConverter();
		 lastWeight = weightConverter.inverse(lastWeight);	
		 _MainModel.getPreferencesHelper().setDouble(R.string.code_pref_last_weight_key, lastWeight);
	}
	@Override
	public void setBackgroundColor(int id) {
		ActivityUtils.setBackground(this, R.id.rootLayout, id);
	}
}
