package com.pigdogbay.weightrecorder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.weightrecorder.model.DummyData;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Query;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.ReportAnalysis;
import com.pigdogbay.weightrecorder.model.ReportFormatting;
import com.pigdogbay.weightrecorder.model.ReportText;
import com.pigdogbay.weightrecorder.model.UserSettings;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ReportActivity extends Activity {
	private static final String TEXT_FIELD_PREFIX = "ReportTextView";
	private static final int MINIMUM_READINGS = 2;
	private ReportText _ReportText;
	private ReportFormatting _ReportFormatting;
	private boolean isLoaded = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		if (!isLoaded) {
			createReport();
			isLoaded = true;
		}
		try
		{
			populateTextViews();
		}
		catch(Exception e){
			Log.v(MainActivity.TAG, "Unable to populate views: "+e.getMessage());
		}
	}
	private void createReport()
	{
		MainModel mainModel = new MainModel(this);
		UserSettings userSettings = mainModel.getUserSettings();
		List<Reading> readings = mainModel.getDatabase().getAllReadings();
		if (readings.size() < MINIMUM_READINGS) {
			readings = DummyData.createRandomData(120);
			ActivitiesHelper.showInfoDialog(this,
					R.string.report_notenoughdata_title,
					R.string.report_notenoughdata_message);
		}
		Query query = new Query(readings);
		ReportAnalysis analysis = new ReportAnalysis(userSettings, query);
		_ReportFormatting = new ReportFormatting(this, userSettings);
		_ReportText = new ReportText(analysis, _ReportFormatting);
		mainModel.close();
		
	}

	private void populateTextViews() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		for (Map.Entry<String, String> entry : _ReportText.getEntrySet())
		{
			String fieldName = entry.getKey().replace("$", TEXT_FIELD_PREFIX);
			int id = R.id.class.getField(fieldName).getInt(null);
			TextView textView = (TextView)findViewById(id);
			textView.setText(entry.getValue());
		}	
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
		String template = Html.fromHtml(this.getString(R.string.report_template)).toString();
		Date now = new Date();
		String dateText = _ReportFormatting.getDateString(now.getTime());
		String subject = getString(R.string.report_email_subject) + " "
				+ dateText;
		ActivityUtils.SendEmail(this, null, subject, _ReportText.createReport(template));
	}
}