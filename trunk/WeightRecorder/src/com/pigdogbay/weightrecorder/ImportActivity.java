package com.pigdogbay.weightrecorder;

import java.util.ArrayList;
import java.util.List;

import com.pigdogbay.weightrecorder.model.Reading;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ImportActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import);
		((Button) findViewById(R.id.ImportOKButton))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						importReadings();
					}
				});
		showHelp();
		MainModel.getInstance().initialize(getApplication());
	}
	
	private void showHelp()
	{
		String title = getResources().getString(R.string.import_dialog_title);
		String message = getResources().getString(R.string.import_dialog_message);
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(title)
				.setMessage(message)
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
	
	private void importReadings()
	{
		EditText editText = (EditText) findViewById(R.id.ImportEdit);
		String text = editText.getText().toString();
		if (text == null || "".equals(text))
		{
			Toast.makeText(this, getString(R.string.import_no_text),
					Toast.LENGTH_SHORT).show();
		}
		else
		{
			List<Reading> readings = ReadingsSerializer.parse(text);
			int count = readings.size();
			if (count>0)
			{
				MainModel.getDatabase().addReadings(readings);
				MainModel.getInstance().notifyDataChanged();
			}
			Toast.makeText(this, String.valueOf(count)+ getString(R.string.import_readings_added),
					Toast.LENGTH_SHORT).show();
		}
		this.finish();
	}


}
