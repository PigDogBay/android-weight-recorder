package com.pigdogbay.weightrecorder;

import java.lang.annotation.Target;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pigdogbay.androidutils.math.BestLineFit;
import com.pigdogbay.androidutils.utils.ActivityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReportActivity extends Activity {
	private static final int MINIMUM_READINGS = 1;
	public static final long DAY_IN_MILLIS = 24L * 60L * 60L * 1000L;
	private double _TargetWeight = 75.0;
	private static int MAX_GOAL_ESTIMATION_DAYS = 500;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		TextView textView = (TextView) findViewById(R.id.ReportText);
		MainModel.getInstance().initialize(getApplication());
		List<Reading> readings = MainModel.getDatabase().getAllReadings();
		if (readings.size() < MINIMUM_READINGS) {
			readings = DummyData.createRandomData(120);
		}
		try {
			loadPreferences();
		}
		catch (Exception e) {
		}
		Query query = new Query(readings);
		textView.setText(Html.fromHtml(createReport(query)));
		textView.setMovementMethod(new ScrollingMovementMethod());
		checkIfEnoughReadings();
	}

	private String getTabbing() {
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
			return " ";
		}
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			return "\t\t";
		}
		return "\t\t\t\t";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_report, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case (R.id.menu_report_home):
			finish();
			break;
		case (R.id.menu_report_export):
			emailReport();
			break;
		default:
			return false;
		}
		return true;
	}

	private void checkIfEnoughReadings() {
		if (MainModel.getDatabase().getReadingsCount() < MINIMUM_READINGS) {
			String title = getResources().getString(
					R.string.report_notenoughdata_title);
			String message = getResources().getString(
					R.string.report_notenoughdata_message);
			new AlertDialog.Builder(this)
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

	private String createReport(Query query) {
		Date now = new Date();
		Date lastWeek = new Date(now.getTime() - 7L * DAY_IN_MILLIS);
		Date last30Days = new Date(now.getTime() - 30L * DAY_IN_MILLIS);
		StringBuilder builder = new StringBuilder();
		builder.append("<b>Body Mass Index</b><br/><br/>");
		builder.append(createBMI(query));
		builder.append("<br/><br/><b>Last 7 Days</b><br/><br/>");
		builder.append(createStats(query.getReadingsBetweenDates(lastWeek, now)));
		builder.append("<br/><br/><b>Last 30 Days</b><br/><br/>");
		builder.append(createStats(query.getReadingsBetweenDates(last30Days,
				now)));
		builder.append("<br/><br/><b>All Time</b><br/><br/>");
		builder.append(createStats(query));
		return builder.toString();

	}

	private String createBMI(Query query) {
		StringBuilder builder = new StringBuilder();
		Reading latest = query.getLatestReading();
		double bmi = MainModel.getInstance().calculateBMI(latest);
		builder.append("BMI\t");
		builder.append(getTabbing());
		builder.append(String.format("%.1f", bmi));
		builder.append("<br/>Class");
		builder.append(getTabbing());
		builder.append(getBMIClass(bmi));
		builder.append("<br>Goal\t");
		builder.append(getTabbing());
		bmi = MainModel.getInstance().calculateBMI(_TargetWeight);
		builder.append(String.format("%.1f %s (BMI %.1f)", _TargetWeight,
				getWeightUnits(), bmi));
		builder.append("<br/><br/>Ideal Weight Range<br/>");
		double startRange = MainModel.getInstance().calculateWeightFromBMI(
				18.5D);
		double endRange = MainModel.getInstance().calculateWeightFromBMI(25D);
		builder.append(weightToString(startRange) + "  -  "
				+ weightToString(endRange));
		return builder.toString();
	}

	private String createStats(Query query) {
		if (query.getReadings().size() == 0) {
			return "No readings available";
		}
		Reading reading = query.getMinWeight();
		StringBuilder builder = new StringBuilder();
		builder.append("Min weight");
		builder.append(getTabbing());
		builder.append(weightToString(reading.getWeight()));
		reading = query.getMaxWeight();
		builder.append("<br/>Max weight");
		builder.append(getTabbing());
		builder.append(weightToString(reading.getWeight()));
		builder.append("<br/>Average\t\t");
		builder.append(getTabbing());
		double avg = query.getAverageWeight();
		builder.append(weightToString(avg));
		builder.append("<br/>Average BMI");
		builder.append(getTabbing());
		builder.append(String.format("%.1f", calculateBMI(avg)));
		builder.append("<br/>Trend\t\t\t");
		builder.append(getTabbing());
		BestLineFit bestLineFit = getBestLineFit(query.getReadings());
		double trend = getTrendInDays(bestLineFit) * 7D;
		String prefix = "";
		if (trend != 0D) {
			prefix = trend > 0 ? getString(R.string.report_positive_trend)
					: getString(R.string.report_negative_trend);
			prefix = prefix + " ";
		}
		trend = Math.abs(trend);
		builder.append(prefix);
		builder.append(weightToString(trend) + " "
				+ getString(R.string.report_per_week));
		try {
			double targetWeightInKg = MainModel.getInstance()
					.getWeightConverter().inverse(_TargetWeight);
			long estimatedGoalDateMillis = getDateInMillis(bestLineFit,
					targetWeightInKg);
			long timeNowMillis = Calendar.getInstance().getTimeInMillis();
			if (estimatedGoalDateMillis > timeNowMillis) {
				builder.append("<br/>Goal Date\t");
				builder.append(getTabbing());
				String dateText = DateUtils.formatDateTime(this,
						estimatedGoalDateMillis, DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_SHOW_YEAR);
				builder.append(dateText);
			}
		}
		catch (Exception e) {
		}
		return builder.toString();

	}

	private BestLineFit getBestLineFit(List<Reading> readings) {
		BestLineFit blf = new BestLineFit();
		for (Reading r : readings) {
			blf.Add((double) r.getDate().getTime(), r.getWeight());
		}
		return blf;
	}

	private double getTrendInDays(BestLineFit blf) {
		double slope = blf.getSlope();
		// convert slope for per ms to per day
		slope = slope * (1000D * 24D * 60D * 60D);
		if (Double.isNaN(slope) || Double.isInfinite(slope)) {
			slope = 0;
		}
		return slope;
	}

	private long getDateInMillis(BestLineFit blf, double weight) {
		double millis = blf.calculateX(weight);
		if (millis < 0 || Double.isNaN(millis) || Double.isInfinite(millis)) {
			millis = 0;
		}
		return (long) millis;
	}

	private double calculateBMI(double weight) {
		double bmi = MainModel.getInstance().getHeight();
		if (bmi != 0.0d) {
			bmi = weight / (bmi * bmi);
		}
		return bmi;
	}

	private String weightToString(double weight) {
		IUnitConverter converter = MainModel.getInstance().getWeightConverter();
		weight = converter.convert(weight);
		return converter.getDisplayString(weight);
	}

	private String getWeightUnits() {
		return MainModel.getInstance().getWeightConverter().getUnits();
	}

	private void emailReport() {
		Date now = new Date();
		String dateText = DateUtils.formatDateTime(this, now.getTime(),
				DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
		String subject = getString(R.string.report_email_subject) + " "
				+ dateText;
		TextView textView = (TextView) findViewById(R.id.ReportText);
		ActivityUtils.SendEmail(this, null, subject, textView.getText()
				.toString());
	}

	private String getBMIClass(double bmi) {
		if (bmi < 16.5) {
			return getString(R.string.bmi_class_severely_underweight);
		}
		else if (bmi < 18.5) {
			return getString(R.string.bmi_class_underweight);
		}
		else if (bmi < 25) {
			return getString(R.string.bmi_class_normal);
		}
		else if (bmi < 30) {
			return getString(R.string.bmi_class_overweight);
		}
		else if (bmi < 35) {
			return getString(R.string.bmi_class_obese1);
		}
		else if (bmi < 40) {
			return getString(R.string.bmi_class_obese2);
		}
		return getString(R.string.bmi_class_obese3);
	}

	private void loadPreferences() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String s = sharedPrefs.getString(
				getString(R.string.code_pref_target_weight_key), "75.0");
		_TargetWeight = Double.parseDouble(sharedPrefs.getString(
				getString(R.string.code_pref_target_weight_key), "75.0"));
	}

}
