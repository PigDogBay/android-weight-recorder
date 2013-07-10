package com.pigdogbay.weightrecorder;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.androidutils.utils.FileUtils;
import com.pigdogbay.weightrecorder.model.MainModel;
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
			String subject = FileUtils.appendDate(
					activity.getString(R.string.app_name), ".csv");
			ActivityUtils.SendEmail(activity, null, subject, text,
					activity.getString(R.string.share_readings_chooser_title));

		}
		catch (Exception e) {
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

	public static void showInfoDialog(Context context, int titleID,
			int messageID) {
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

	public static void shareReadings(Activity activity) {
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
			String filename = FileUtils.appendDate(
					activity.getString(R.string.app_name), ".csv");
			File path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			File file = new File(path, filename);
			if (file.exists()) {
				file.delete();
			}
			FileUtils.writeTextFile(file, text);
			Toast.makeText(activity, filename, Toast.LENGTH_SHORT).show();
			SendFile(activity, file, "text/plain");

		}
		catch (Exception e) {
			Toast.makeText(activity,
					activity.getString(R.string.readings_export_error),
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void SendFile(Activity activity, File file, String type) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType(type);
		i.putExtra(Intent.EXTRA_EMAIL, "");
		i.putExtra(Intent.EXTRA_SUBJECT, file.getName());
		i.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.facebookPage));
		Uri uri = Uri.fromFile(file);
		i.putExtra(Intent.EXTRA_STREAM, uri);
		try {
			activity.startActivity(Intent.createChooser(i,
					activity.getString(R.string.share_readings_chooser_title)));
		}
		catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(activity,
					activity.getString(R.string.about_no_market_app),
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void shareText(Activity activity, String subject, String text) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL, "");
		i.putExtra(Intent.EXTRA_SUBJECT, subject);
		i.putExtra(Intent.EXTRA_TEXT, text);
		try {
			activity.startActivity(Intent.createChooser(i,
					activity.getString(R.string.share_readings_chooser_title)));
		}
		catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(activity,
					activity.getString(R.string.about_no_market_app),
					Toast.LENGTH_SHORT).show();
		}

	}

}
