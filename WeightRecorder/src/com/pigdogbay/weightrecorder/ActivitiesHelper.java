package com.pigdogbay.weightrecorder;

import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.weightrecorder.model.DatabaseHelper;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.ReadingsSerializer;

public class ActivitiesHelper {
	public static void startExportActivity(Activity activity) {
		try {
			List<Reading> readings = MainModel.getInstance().getReverseOrderedReadings();
			if (readings.size() == 0) {
				Toast.makeText(
						activity,
						activity.getString(R.string.readings_no_readings_export),
						Toast.LENGTH_SHORT).show();

			}
			String text = ReadingsSerializer.format(readings);
			ActivityUtils.SendEmail(activity, null, "Readings", text);

		} catch (Exception e) {
			Toast.makeText(activity,
					activity.getString(R.string.readings_export_error),
					Toast.LENGTH_SHORT).show();
		}

	}

	public static void startImportActivity(Activity activity) {
		Intent intent = new Intent(activity, ImportActivity.class);
		activity.startActivity(intent);
	}
	public static void startImportActivity(Activity activity, int requestCode) {
		Intent intent = new Intent(activity, ImportActivity.class);
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static void initializeMainModel(Application application)
	{
		MainModel model = MainModel.getInstance();
		if (model.getDatabase() == null)
		{
			model.setDatabase( new DatabaseHelper(application.getApplicationContext()));
		}		
	}
}
