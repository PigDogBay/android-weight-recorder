package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.mvp.AdPresenter;
import com.pigdogbay.androidutils.mvp.BackgroundColorPresenter;
import com.pigdogbay.androidutils.mvp.IAdView;
import com.pigdogbay.androidutils.mvp.IBackgroundColorView;
import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Reading;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;

public class ReadingListActivity extends ListActivity implements IBackgroundColorView
{
	//Constants for Activity Result requests 
	protected static final int REQUEST_EDIT = 1;
	protected static final int REQUEST_IMPORT = 2;
	
	private MainModel _MainModel;
	private ReadingsArrayAdapter _ReadingsArrayAdapter;
	private BroadcastReceiver _BroadcastReceiver;
	private boolean _DataChanged = false;

	AdPresenter _AdPresenter;
	BackgroundColorPresenter _BackgroundColorPresenter;
	
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_readings_list);
        _MainModel = new MainModel(this);
        _ReadingsArrayAdapter = new ReadingsArrayAdapter(this, _MainModel.getReverseOrderedReadings(),_MainModel.getUserSettings());
        setListAdapter(_ReadingsArrayAdapter);
        _BroadcastReceiver = new ImportBroadcastReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(_BroadcastReceiver, new IntentFilter(ImportActivity.NEW_IMPORTED_READINGS));

		_BackgroundColorPresenter = new BackgroundColorPresenter(this,_MainModel.createBackgroundColorModel());
		_BackgroundColorPresenter.updateBackground();
		if (_ReadingsArrayAdapter.getCount()==0)
		{
			ActivityUtils.showInfoDialog(this,R.string.readings_no_readings_dialog_title,R.string.readings_no_readings_dialog_msg);
		}

    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		_MainModel.close();
        //Its recommended this should be in onResume() / onPause() 
		//as onDestroy is not guaranteed to be called
		//But this means the I can't listen for events when the import activity is showing
		LocalBroadcastManager.getInstance(this).unregisterReceiver(_BroadcastReceiver);
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (_DataChanged)
		{
			onDataChanged();
		}
	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode==REQUEST_EDIT && resultCode==RESULT_OK)
    	{
   			//Reload readings as a change has been made
   			onDataChanged();
    	}
    }	
	/*
	 * Called back from array adapter when a reading is selected
	 */
	public void readingSelected(Reading reading)
	{
		Intent intent = new Intent(this,EditReadingActivity.class);
		intent.putExtra("ReadingID", reading.getID());
		startActivityForResult(intent,REQUEST_EDIT);
	}
	private void onDataChanged()
	{
		_ReadingsArrayAdapter.setReadings(_MainModel.getReverseOrderedReadings());
		_ReadingsArrayAdapter.notifyDataSetChanged();
		_DataChanged=false;
	}	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.menu_readings_list, menu);
        return true;
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case (R.id.menu_readings_list_home):
			finish();
			break;
		case (R.id.menu_readings_list_delete_all):
			deleteAllMenuOption();
			break;
		case (R.id.menu_readings_list_export):
			ActivitiesHelper.shareReadings(this);
			break;
		case (R.id.menu_readings_list_import):
			ActivitiesHelper.startImportActivity(this);
			break;
		default:
			return false;
		}
		return true;
	}

	private void deleteAllMenuOption()
	{
		String title = getResources().getString(R.string.editreading_delete_dialog_title);
		String message = getResources().getString(R.string.editreading_delete_dialog_message);
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(title)
				.setMessage(message)
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{
								dialog.dismiss();
							}
						})	
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{
								deleteAll();
								dialog.dismiss();
							}
						}).show();		
	}
	private void deleteAll()
	{
		_MainModel.getDatabase().deleteAllReadings();
		onDataChanged();
	}
	
	private class ImportBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			ReadingListActivity.this._DataChanged=true;
		}
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
