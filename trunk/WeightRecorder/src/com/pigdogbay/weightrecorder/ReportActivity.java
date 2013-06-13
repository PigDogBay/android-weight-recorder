package com.pigdogbay.weightrecorder;

import java.util.Date;
import java.util.List;

import com.pigdogbay.weightrecorder.model.BMICalculator;
import com.pigdogbay.weightrecorder.model.DummyData;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Query;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.ReportAnalysis;
import com.pigdogbay.weightrecorder.model.ReportText;
import com.pigdogbay.weightrecorder.model.TrendAnalysis;
import com.pigdogbay.weightrecorder.model.UserSettings;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ReportActivity extends Activity {
	private static final int MINIMUM_READINGS = 1;
	public static final long DAY_IN_MILLIS = 24L * 60L * 60L * 1000L;
	private UserSettings _UserSettings;
	private MainModel _MainModel;
	private ReportText _ReportText;
	private ReportAnalysis _ReportAnalysis;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		_MainModel = new MainModel(this);
		_UserSettings = _MainModel.getUserSettings();
		List<Reading> readings = _MainModel.getDatabase().getAllReadings();
		if (readings.size() < MINIMUM_READINGS) {
			readings = DummyData.createRandomData(120);
			ActivitiesHelper.showInfoDialog(this, R.string.report_notenoughdata_title, R.string.report_notenoughdata_message);
		}
		_ReportAnalysis = new ReportAnalysis(_UserSettings, readings);
		_ReportText = new ReportText(this, _UserSettings);
		populateTextViews();
//		TextView textView = (TextView) findViewById(R.id.ReportText);
//		Query query = new Query(readings);
//		textView.setText(Html.fromHtml(createReport(query)));
//		textView.setMovementMethod(new ScrollingMovementMethod());
	}
	
	private void populateTextViews()
	{
		TextView textView = (TextView) findViewById(R.id.ReportTextViewLatestBMI);
		textView.setText(_ReportText.bmiToString(_ReportAnalysis.getLatestBMI()));
		textView = (TextView) findViewById(R.id.ReportTextViewGoalBMI);
		textView.setText(_ReportText.bmiToString(_ReportAnalysis.getTargetBMI()));
		textView = (TextView) findViewById(R.id.ReportTextViewIdealRange);
		textView.setText(_ReportText.idealWeightRangeToString(
				_ReportAnalysis.getBottomOfIdealWeightRange(),
				_ReportAnalysis.getTopOfIdealWeightRange()));
		textView = (TextView) findViewById(R.id.ReportTextViewMinWeight);
		textView.setText(_ReportText.weightToString(_ReportAnalysis.getMinWeight()));
		textView = (TextView) findViewById(R.id.ReportTextViewMaxWeight);
		textView.setText(_ReportText.weightToString(_ReportAnalysis.getMaxWeight()));
		textView = (TextView) findViewById(R.id.ReportTextViewAverageWeight);
		textView.setText(_ReportText.weightToString(_ReportAnalysis.getAverageWeight()));
		textView = (TextView) findViewById(R.id.ReportTextViewAverageBMI);
		textView.setText(_ReportText.bmiToString(_ReportAnalysis.getAverageBMI()));
		textView = (TextView) findViewById(R.id.ReportTextViewCount);
		textView.setText(_ReportText.weightToString(_ReportAnalysis.getCount()));
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		_MainModel.close();
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
		BMICalculator bmiCalculator = new BMICalculator(_UserSettings);
		StringBuilder builder = new StringBuilder();
		Reading latest = query.getLatestReading();
		double bmi = bmiCalculator.calculateBMI(latest);
		builder.append("BMI\t");
		builder.append(getTabbing());
		builder.append(String.format("%.1f", bmi));
		builder.append("<br/>Class");
		builder.append(getTabbing());
		builder.append(getBMIClass(bmi));
		builder.append("<br>Goal\t");
		builder.append(getTabbing());
		bmi = bmiCalculator.calculateBMI(_UserSettings.TargetWeight);
		builder.append(weightToString(_UserSettings.TargetWeight));
		builder.append(String.format(" (BMI %.1f)", bmi));
		builder.append("<br/><br/>Ideal Weight Range<br/>");
		double startRange = bmiCalculator.calculateWeightFromBMI(BMICalculator.UNDERWEIGHT_UPPER_LIMIT);
		double endRange = bmiCalculator.calculateWeightFromBMI(BMICalculator.NORMAL_UPPER_LIMIT);
		builder.append(weightToString(startRange) + "  -  "
				+ weightToString(endRange));
		return builder.toString();
	}

	private String createStats(Query query) {
		BMICalculator bmiCalculator = new BMICalculator(_UserSettings);
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
		builder.append(String.format("%.1f", bmiCalculator.calculateBMI(avg)));
		builder.append("<br/>Trend\t\t\t");
		builder.append(getTabbing());
		TrendAnalysis trendAnalysis = new TrendAnalysis(query.getReadings());
		double trend = trendAnalysis.getTrendInDays();
		String prefix = "";
//		if (trend != 0D) {
//			prefix = trend > 0 ? getString(R.string.report_positive_trend)
//					: getString(R.string.report_negative_trend);
//			prefix = prefix + " ";
//		}
		trend = Math.abs(trend);
		builder.append(prefix);
		builder.append(weightToString(trend) + " "
				+ getString(R.string.report_per_week));
		try {
			long estimatedGoalDateMillis = trendAnalysis.getEstimatedDate(_UserSettings.TargetWeight);
			if (TrendAnalysis.isGoalDateValid(estimatedGoalDateMillis)) {
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
	/**
	 * @param weight in kilograms
	 * @return string representation with user units
	 */
	private String weightToString(double weight) {
		weight = _UserSettings.WeightConverter.convert(weight);
		return _UserSettings.WeightConverter.getDisplayString(weight);
	}

	private void emailReport() {
		Date now = new Date();
		String dateText = DateUtils.formatDateTime(this, now.getTime(),
				DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
		String subject = getString(R.string.report_email_subject) + " "
				+ dateText;
//		TextView textView = (TextView) findViewById(R.id.ReportText);
//		ActivityUtils.SendEmail(this, null, subject, textView.getText()
//				.toString());
	}

	private String getBMIClass(double bmi) {
		if (bmi < BMICalculator.SEVERLY_UNDERWEIGHT_UPPER_LIMIT) {
			return getString(R.string.bmi_class_severely_underweight);
		}
		else if (bmi < BMICalculator.UNDERWEIGHT_UPPER_LIMIT) {
			return getString(R.string.bmi_class_underweight);
		}
		else if (bmi < BMICalculator.NORMAL_UPPER_LIMIT) {
			return getString(R.string.bmi_class_normal);
		}
		else if (bmi < BMICalculator.OVERWEIGHT_UPPER_LIMIT) {
			return getString(R.string.bmi_class_overweight);
		}
		else if (bmi < BMICalculator.OBESE1_UPPER_LIMIT) {
			return getString(R.string.bmi_class_obese1);
		}
		else if (bmi < BMICalculator.OBESE2_UPPER_LIMIT) {
			return getString(R.string.bmi_class_obese2);
		}
		return getString(R.string.bmi_class_obese3);
	}

}
