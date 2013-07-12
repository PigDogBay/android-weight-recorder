package com.pigdogbay.weightrecorder;

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

public class ReadingListActivity extends ListActivity
{
	//Constants for Activity Result requests 
	protected static final int REQUEST_EDIT = 1;
	protected static final int REQUEST_IMPORT = 2;
	
	private MainModel _MainModel;
	private ReadingsArrayAdapter _ReadingsArrayAdapter;
	private BroadcastReceiver _BroadcastReceiver;
	private boolean _DataChanged = false;

	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        _MainModel = new MainModel(this);
        _ReadingsArrayAdapter = new ReadingsArrayAdapter(this, _MainModel.getReverseOrderedReadings(),_MainModel.getWeightConverter(), _MainModel.getHeightInMetres());
        setListAdapter(_ReadingsArrayAdapter);
        setBackground();
        _BroadcastReceiver = new ImportBroadcastReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(_BroadcastReceiver, new IntentFilter(ImportActivity.NEW_IMPORTED_READINGS));
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
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void setBackground()
	{
        Drawable background = getResources().getDrawable(R.drawable.background);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
		{
	        this.getListView().setBackgroundDrawable(background);
		} 
		else
		{
	        this.getListView().setBackground(background);
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
	
}
