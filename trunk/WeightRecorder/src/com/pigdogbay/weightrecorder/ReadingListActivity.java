package com.pigdogbay.weightrecorder;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.weightrecorder.model.IDataChangedListener;
import com.pigdogbay.weightrecorder.model.MainModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

public class ReadingListActivity extends ListActivity implements IDataChangedListener
{
	ReadingsArrayAdapter _ReadingsArrayAdapter;
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        _ReadingsArrayAdapter = new ReadingsArrayAdapter(this, MainModel.getInstance().getReverseOrderedReadings());
        setListAdapter(_ReadingsArrayAdapter);
        setBackground();
		MainModel.getInstance().initialize(getApplication());
        MainModel.getInstance().registerDataChangedListener(this);
    }
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
        MainModel.getInstance().unregisterDataChangedListener(this);
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
			ActivitiesHelper.startExportActivity(this);
			break;
		case (R.id.menu_readings_list_import):
			ActivitiesHelper.startImportActivity(this);
			break;
		default:
			return false;
		}
		return true;
	}
    

	public void onDataChanged()
	{
		_ReadingsArrayAdapter.setReadings(MainModel.getInstance().getReverseOrderedReadings());
		_ReadingsArrayAdapter.notifyDataSetChanged();
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
		MainModel.getDatabase().deleteAllReadings();
		onDataChanged();
	}
	
}
