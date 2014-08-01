package com.pigdogbay.weightrecorder;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pigdogbay.weightrecorder.model.DummyData;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Query;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.ReportAnalysis;
import com.pigdogbay.weightrecorder.model.ReportFormatting;
import com.pigdogbay.weightrecorder.model.ReportText;
import com.pigdogbay.weightrecorder.model.UserSettings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReportFragment  extends Fragment {
	public static final String TAG = "report";

	private static final String TEXT_FIELD_PREFIX = "ReportTextView";
	private static final int MINIMUM_READINGS = 2;
	private ReportText _ReportText;
	private ReportFormatting _ReportFormatting;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_report, container,false);
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);	
		setHasOptionsMenu(true);
		//Dont set up controls again if state has been saved (eg on a rotate)
		if (savedInstanceState==null) {
			init();
		}
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_report, menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case (R.id.menu_report_export):
			emailReport();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
	private void init()
	{
		MainModel mainModel = new MainModel(getActivity());
		createReport(mainModel);
		mainModel.close();
		try {
			populateTextViews();
		}
		catch (Exception e) {
			Log.v(MainActivity.TAG,
					"Unable to populate views: " + e.getMessage());
		}
	}
	private void createReport(MainModel mainModel) {
		UserSettings userSettings = mainModel.getUserSettings();
		List<Reading> readings = mainModel.getDatabase().getAllReadings();
		if (readings.size() < MINIMUM_READINGS) {
			readings = DummyData.createRandomData(120);
			ActivitiesHelper.showInfoDialog(getActivity(),
					R.string.report_notenoughdata_title,
					R.string.report_notenoughdata_message);
		}
		Query query = new Query(readings);
		ReportAnalysis analysis = new ReportAnalysis(userSettings, query);
		_ReportFormatting = new ReportFormatting(getActivity(), userSettings);
		_ReportText = new ReportText(analysis, _ReportFormatting);
	}

	private void populateTextViews() throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException {
		for (Map.Entry<String, String> entry : _ReportText.getEntrySet()) {
			String fieldName = entry.getKey().replace("$", TEXT_FIELD_PREFIX);
			int id = R.id.class.getField(fieldName).getInt(null);
			TextView textView = (TextView) getView().findViewById(id);
			textView.setText(entry.getValue());
		}
	}	
	private void emailReport() {
		Date now = new Date();
		String dateText = _ReportFormatting.getDateString(now.getTime());
		String subject = getString(R.string.report_email_subject) + " "	+ dateText;
		String template = Html.fromHtml(getActivity().getString(R.string.report_template)).toString();
		String text = _ReportText.createReport(template);
		ActivitiesHelper.shareText(getActivity(), subject, text,R.string.report_share_chooser_title);
	}

}
