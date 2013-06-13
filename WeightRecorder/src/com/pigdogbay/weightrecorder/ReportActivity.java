package com.pigdogbay.weightrecorder;

import java.util.Date;
import java.util.List;

import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.weightrecorder.model.DummyData;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Query;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.ReportAnalysis;
import com.pigdogbay.weightrecorder.model.ReportText;
import com.pigdogbay.weightrecorder.model.UserSettings;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ReportActivity extends Activity {
	private static final int MINIMUM_READINGS = 2;
	private UserSettings _UserSettings;
	private ReportText _ReportText;
	private ReportAnalysis _ReportAnalysis;
	private boolean isLoaded = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		if (!isLoaded) {
			MainModel mainModel = new MainModel(this);
			_UserSettings = mainModel.getUserSettings();
			List<Reading> readings = mainModel.getDatabase().getAllReadings();
			if (readings.size() < MINIMUM_READINGS) {
				readings = DummyData.createRandomData(120);
				ActivitiesHelper.showInfoDialog(this,
						R.string.report_notenoughdata_title,
						R.string.report_notenoughdata_message);
			}
			Query query = new Query(readings);
			_ReportAnalysis = new ReportAnalysis(_UserSettings, query);
			_ReportText = new ReportText(this, _UserSettings);
			mainModel.close();
			isLoaded = true;
		}
		populateTextViews();
	}

	private void populateTextViews() {
		TextView textView = (TextView) findViewById(R.id.ReportTextViewLatestBMI);
		textView.setText(_ReportText.getBMIString(_ReportAnalysis.getLatestBMI()));
		textView = (TextView) findViewById(R.id.ReportTextViewGoalBMI);
		textView.setText(_ReportText.getBMIString(_ReportAnalysis.getTargetBMI()));
		textView = (TextView) findViewById(R.id.ReportTextViewIdealRange);
		textView.setText(_ReportText.getIdealWeightRange(
				_ReportAnalysis.getBottomOfIdealWeightRange(),
				_ReportAnalysis.getTopOfIdealWeightRange()));

		textView = (TextView) findViewById(R.id.ReportTextViewMinWeight);
		textView.setText(_ReportText.getWeightString(_ReportAnalysis.MinWeight));
		textView = (TextView) findViewById(R.id.ReportTextViewMaxWeight);
		textView.setText(_ReportText.getWeightString(_ReportAnalysis.MaxWeight));
		textView = (TextView) findViewById(R.id.ReportTextViewAverageWeight);
		textView.setText(_ReportText
				.getWeightString(_ReportAnalysis.AverageWeight));
		textView = (TextView) findViewById(R.id.ReportTextViewAverageBMI);
		textView.setText(_ReportText.getBMIString(_ReportAnalysis
				.getAverageBMI()));
		textView = (TextView) findViewById(R.id.ReportTextViewCount);
		textView.setText(Integer.toString(_ReportAnalysis.Count));

		textView = (TextView) findViewById(R.id.ReportTextViewTrendAllGoalDate);
		textView.setText(_ReportText.getDateString(_ReportAnalysis
				.getEstimatedDateUsingAllTime()));
		textView = (TextView) findViewById(R.id.ReportTextViewTrendAllValue);
		textView.setText(_ReportText.getWeightTrend(_ReportAnalysis
				.getWeeklyTrendAllTime()));
		textView = (TextView) findViewById(R.id.ReportTextViewTrendAllTitle);
		textView.setText(_ReportText.getWeightTrendDirection(_ReportAnalysis
				.getWeeklyTrendAllTime()));

		textView = (TextView) findViewById(R.id.ReportTextViewTrendMonthGoalDate);
		textView.setText(_ReportText.getDateString(_ReportAnalysis
				.getEstimatedDateUsingLastMonth()));
		textView = (TextView) findViewById(R.id.ReportTextViewTrendMonthValue);
		textView.setText(_ReportText.getWeightTrend(_ReportAnalysis
				.getWeeklyTrendOverLastMonth()));
		textView = (TextView) findViewById(R.id.ReportTextViewTrendMonthTitle);
		textView.setText(_ReportText.getWeightTrendDirection(_ReportAnalysis
				.getWeeklyTrendOverLastMonth()));

		textView = (TextView) findViewById(R.id.ReportTextViewTrendWeekGoalDate);
		textView.setText(_ReportText.getDateString(_ReportAnalysis
				.getEstimatedDateUsingLastWeek()));
		textView = (TextView) findViewById(R.id.ReportTextViewTrendWeekValue);
		textView.setText(_ReportText.getWeightTrend(_ReportAnalysis
				.getWeeklyTrendOverLastWeek()));
		textView = (TextView) findViewById(R.id.ReportTextViewTrendWeekTitle);
		textView.setText(_ReportText.getWeightTrendDirection(_ReportAnalysis
				.getWeeklyTrendOverLastWeek()));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_report, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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

	private void emailReport() {
		Date now = new Date();
		String dateText = _ReportText.getDateString(now.getTime());
		String subject = getString(R.string.report_email_subject) + " "
				+ dateText;
		ActivityUtils.SendEmail(this, null, subject, _ReportText.createReport(_ReportAnalysis));
	}
}
