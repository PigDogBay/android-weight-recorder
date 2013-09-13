package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.mvp.BackgroundColorPresenter;
import com.pigdogbay.androidutils.mvp.IBackgroundColorView;
import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Reading;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class EditReadingActivity extends FragmentActivity implements IBackgroundColorView{
	private Reading _Reading;
	private MainModel _MainModel;
	private BackgroundColorPresenter _BackgroundColorPresenter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_edit_reading);
		((Button) findViewById(R.id.EditReadingBtnUpdate))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						onUpdateClick();
					}
				});
		((Button) findViewById(R.id.EditReadingBtnDelete))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						onDeleteClick();
					}
				});

		_MainModel = new MainModel(this);
		_BackgroundColorPresenter = new BackgroundColorPresenter(this,_MainModel.createBackgroundColorModel());
		_BackgroundColorPresenter.updateBackground();
		int id = getIntent().getIntExtra("ReadingID", -1);
		if (id == -1) {
			_Reading = new Reading();
		}
		else {
			_Reading = _MainModel.getDatabase().getReading(id);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_edit_reading, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case (R.id.menu_edit_reading_home):
			finish();
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	protected void onStart() {
		// onStart, the fragment will have been created by now
		super.onStart();
		EditFragment fragment = (EditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.EditReadingEditFragment);
		fragment.setWeightConvert(_MainModel.getWeightConverter());
		fragment.setReading(_Reading);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		_MainModel.close();
	}

	private void onUpdateClick() {
		EditFragment fragment = (EditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.EditReadingEditFragment);
		Reading reading = fragment.getReading();
		reading.setID(_Reading.getID());
		_MainModel.getDatabase().updateReading(reading);
		Toast.makeText(this, getString(R.string.editreading_updated),
				Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK);
		this.finish();
	}

	private void onDeleteClick() {
		_MainModel.getDatabase().deleteReading(_Reading);
		Toast.makeText(this, getString(R.string.editreading_deleted),
				Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK);
		this.finish();
	}
	@Override
	public void setBackgroundColor(int id) {
		ActivityUtils.setBackground(this, R.id.rootLayout, id);
	}
	@Override
	public void showPurchaseRequiredWarning() {
		//Do nothing
	}	
	

}
