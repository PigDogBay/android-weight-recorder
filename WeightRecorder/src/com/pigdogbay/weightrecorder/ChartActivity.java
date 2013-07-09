package com.pigdogbay.weightrecorder;

import java.io.File;
import java.util.List;

import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.androidutils.utils.FileUtils;
import com.pigdogbay.weightrecorder.model.ChartLogic;
import com.pigdogbay.weightrecorder.model.DummyData;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Query;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.UserSettings;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

public class ChartActivity extends Activity {
	private static final int MINIMUM_READINGS = 3;
	static int _Period = 0;
	private MainModel _MainModel;
	private boolean _UseDummyReadings;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);

		_MainModel = new MainModel(this);
		_UseDummyReadings =_MainModel.getDatabase().getReadingsCount()< MINIMUM_READINGS; 
		createChart();
		if (_UseDummyReadings)
		{
			ActivitiesHelper.showInfoDialog(this, R.string.chart_notenoughdata_title, R.string.chart_notenoughdata_message);
		}

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		_MainModel.close();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case (R.id.menu_chart_home):
			finish();
			return true;
		case (R.id.menu_chart_share):
			shareScreenShot();
			break;
		case (R.id.menu_chart_all):
			_Period = 0;
			break;
		case (R.id.menu_chart_30_days):
			_Period = 30;
			break;
		case (R.id.menu_chart_60_days):
			_Period = 60;
			break;
		case (R.id.menu_chart_90_days):
			_Period = 90;
			break;
		case (R.id.menu_chart_one_year):
			_Period = 365;
			break;
		default:
			return false;
		}
		createChart();
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_chart, menu);
		return true;
	}
	private void createChart()
	{
		ViewGroup layout = (ViewGroup) findViewById(R.id.ChartLayout);
		layout.removeAllViews();
		UserSettings userSettings = _MainModel.getUserSettings();
		Query query = new Query(createReadings());
		query.sortByDate();
		ChartLogic chartLogic = new ChartLogic(userSettings);
		ReadingsChart chart = new ReadingsChart(this);
		chart.setXAxisTitle(getString(R.string.chart_xaxis_title));
		chart.setYAxisTitle(String.format("%s (%s)", 
				getString(R.string.chart_yaxis_title),
				userSettings.WeightConverter.getUnits()));
		chart.setChartAxesRanges(chartLogic.calculateAxesRanges(query, _Period));
		chart.addReadings(chartLogic.createReadingsSeries(query));
		if (userSettings.ShowTargetLine)
		{
			chart.addTarget(chartLogic.createTargetSeries());
		}
		if (userSettings.ShowTrendLine)
		{
			chart.addTrend(chartLogic.createTrendSeries(query, _Period));
		}
		layout.addView(chart.createView());
	}
	private List<Reading> createReadings()
	{
		return _UseDummyReadings ?
				DummyData.createRandomData(120) : 
				_MainModel.getDatabase().getAllReadings();
	}
	private void shareScreenShot()
	{
		try
		{
			Bitmap screenshot = ActivityUtils.takeScreenShot(this);
			String filename = FileUtils.appendDate(this.getString(R.string.app_name),".png");
			File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			File file = new File(path,filename);
			if (file.exists()) {
				file.delete();
			}			
			FileUtils.writeImage(file, screenshot);
			ActivitiesHelper.SendFile(this, file,"image/png");
		}
		catch(Exception e){
			Toast.makeText(this,
					this.getString(R.string.readings_export_error),
					Toast.LENGTH_SHORT).show();		}
	}
}
