package com.pigdogbay.weightrecorder;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class EditReadingActivity extends FragmentActivity
{
	Reading _Reading;

	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setContentView(R.layout.activity_edit_reading);
		((Button) findViewById(R.id.EditReadingBtnUpdate))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						onUpdateClick();
					}
				});
		((Button) findViewById(R.id.EditReadingBtnDelete))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						onDeleteClick();
					}
				});

		int id = getIntent().getIntExtra("ReadingID", -1);
		if (id == -1)
		{
			_Reading = new Reading();
		}
		MainModel.getInstance().initialize(getApplication());
		_Reading = MainModel.getDatabase().getReading(id);
	}

	@Override
	protected void onStart()
	{
		// onStart, the fragment will have been created by now
		super.onStart();
		EditFragment fragment = (EditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.EditReadingEditFragment);
		fragment.setReading(_Reading);
	}

	private void onUpdateClick()
	{
		EditFragment fragment = (EditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.EditReadingEditFragment);
		Reading reading = fragment.getReading();
		reading.setID(_Reading.getID());
		MainModel.getDatabase().updateReading(reading);
		Toast.makeText(this, getString(R.string.editreading_updated),
				Toast.LENGTH_SHORT).show();
		MainModel.getInstance().notifyDataChanged();
		this.finish();
	}

	private void onDeleteClick()
	{
		MainModel.getDatabase().deleteReading(_Reading);
		Toast.makeText(this, getString(R.string.editreading_deleted),
				Toast.LENGTH_SHORT).show();
		MainModel.getInstance().notifyDataChanged();
		this.finish();
	}

}
