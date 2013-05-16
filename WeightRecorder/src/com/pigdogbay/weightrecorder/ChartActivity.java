package com.pigdogbay.weightrecorder;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

public class ChartActivity extends Activity {
	private static final int MINIMUM_READINGS = 3;
	ReadingsChart _Chart;
	static int _Period = 30;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);

		checkIfEnoughReadings();
		_Chart = new ReadingsChart();
		MainModel.getInstance().initialize(getApplication());
		IUnitConverter converter = MainModel.getInstance().getWeightConverter();
		_Chart.setYAxisTitle(String.format("Weight (%s)", converter.getUnits()));
		try {
			loadPreferences();
		}
		catch (Exception e) {
		}
		ViewGroup layout = (ViewGroup) findViewById(R.id.ChartLayout);
		layout.addView(_Chart.CreateView(getReadings(), this, _Period));

	}

	private void checkIfEnoughReadings() {
		if (MainModel.getDatabase().getReadingsCount() < MINIMUM_READINGS) {
			String title = getResources().getString(
					R.string.chart_notenoughdata_title);
			String message = getResources().getString(
					R.string.chart_notenoughdata_message);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ViewGroup layout = (ViewGroup) findViewById(R.id.ChartLayout);
		switch (item.getItemId())
		{
		case (R.id.menu_chart_30_days):
			_Period = 30;
			layout.removeAllViews();
			layout.addView(_Chart.CreateView(getReadings(), this, _Period));
			break;
		case (R.id.menu_chart_60_days):
			_Period = 60;
			layout.removeAllViews();
			layout.addView(_Chart.CreateView(getReadings(), this, _Period));
			break;
		case (R.id.menu_chart_90_days):
			_Period = 90;
			layout.removeAllViews();
			layout.addView(_Chart.CreateView(getReadings(), this, _Period));
			break;
		case (R.id.menu_chart_one_year):
			_Period = 365;
			layout.removeAllViews();
			layout.addView(_Chart.CreateView(getReadings(), this, _Period));
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_chart, menu);
		return true;
	}

	private List<Reading> getReadings() {
		List<Reading> readings = MainModel.getDatabase().getAllReadings();
		if (readings.size() < MINIMUM_READINGS) {
			readings = DummyData.createRandomData(120);
		}
		IUnitConverter converter = MainModel.getInstance().getWeightConverter();
		for (Reading r : readings) {
			r.setWeight(converter.convert(r.getWeight()));
		}
		return readings;
	}

	private void loadPreferences() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String key = getString(R.string.code_pref_show_trendline_key);
		boolean show = sharedPrefs.getBoolean(key, false);
		_Chart.setShowTrendLine(show);
		key = getString(R.string.code_pref_show_targetline_key);
		show = sharedPrefs.getBoolean(key, false);
		_Chart.setShowTargetLine(show);
		double targetWeight = Double.parseDouble(sharedPrefs.getString(
				getString(R.string.code_pref_target_weight_key), "75.0"));
		targetWeight = MainModel.getInstance().getWeightConverter()
				.convert(targetWeight);
		_Chart.setTargetWeight(targetWeight);
	}

}
