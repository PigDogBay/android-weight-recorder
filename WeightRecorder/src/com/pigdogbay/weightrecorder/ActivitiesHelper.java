package com.pigdogbay.weightrecorder;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.androidutils.utils.FileUtils;
import com.pigdogbay.weightrecorder.model.DatabaseHelper;
import com.pigdogbay.weightrecorder.model.IReadingsDatabase;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Query;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.ReadingsSerializer;

public class ActivitiesHelper {
	public static void startExportActivity(Activity activity) {
		try {
			MainModel mainModel = new MainModel(activity);
			List<Reading> readings = mainModel.getReverseOrderedReadings();
			mainModel.close();
			if (readings.size() == 0) {
				Toast.makeText(
						activity,
						activity.getString(R.string.readings_no_readings_export),
						Toast.LENGTH_SHORT).show();
				return;

			}
			String text = ReadingsSerializer.format(readings);
			String subject = FileUtils.appendDate(activity.getString(R.string.app_name),"");
			ActivityUtils.SendEmail(activity, null, 
					subject,
					text,
					activity.getString(R.string.share_readings_chooser_title));

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
	
	public static void showInfoDialog(Context context, int titleID, int messageID)
	{
		String title = context.getResources().getString(titleID);
		String message = context.getResources().getString(messageID);
		new AlertDialog.Builder(context)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
		
	}
}
