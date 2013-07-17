package com.pigdogbay.weightrecorder;

import java.util.List;

import com.pigdogbay.weightrecorder.model.BMICalculator;
import com.pigdogbay.weightrecorder.model.IUnitConverter;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.ReportFormatting;
import com.pigdogbay.weightrecorder.model.UserSettings;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*
 * Good example here
 * http://techdroid.kbeanie.com/2009/07/custom-listview-for-android.html
 * 
 */
public class ReadingsArrayAdapter extends ArrayAdapter<Reading> implements OnClickListener
{
	private List<Reading> _Readings;
	private final ReadingListActivity _Activity;
	private ReportFormatting _ReportFormatting;
	private BMICalculator _BMICalculator;
	
	public ReadingsArrayAdapter(ReadingListActivity activity, List<Reading> readings, UserSettings userSettings)
	{
		super(activity, R.layout.readings_item, readings);
		_Readings = readings;
		_Activity = activity;
		_ReportFormatting = new ReportFormatting(activity, userSettings);
		_BMICalculator = new BMICalculator(userSettings);
	}
	
	public void setReadings(List<Reading> readings)
	{
		_Readings = readings;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		Reading reading = _Readings.get(position);
		if (convertView == null)
		{
			LayoutInflater inflater = _Activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.readings_item, null);
		}
		TextView textView = (TextView) convertView
				.findViewById(R.id.ResultsItemWeight);
		textView.setText(_ReportFormatting.getWeightString(reading.getWeight()));

		textView = (TextView) convertView.findViewById(R.id.ResultsItemDate);
		String dateText = _ReportFormatting.getDateTimeString(reading.getDate()); 
		textView.setText(dateText);
		textView = (TextView) convertView.findViewById(R.id.ResultsItemComment);
		textView.setText(getComment(reading));

		convertView.setOnClickListener(this);
		convertView.setTag(reading);
		return convertView;
	}
	
	/*
 	 * http://stackoverflow.com/questions/13568223/arrayadapter-indexoutofboundsexception
	 * Need to override to prevent IndexOutOfBounds Exception when a reading is deleted 
	 * 
	 * (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getCount()
	 */
	@Override
	public int getCount()
	{
		return _Readings.size();
	}
	
	public void onClick(View v)
	{
		Reading reading = (Reading)v.getTag();
		_Activity.readingSelected(reading);
	}
	
	private String getComment(Reading reading)
	{
		String comment = reading.getComment();
		if ("".equals(comment))
		{
			double bmi = _BMICalculator.calculateBMI(reading);
			comment = _ReportFormatting.getBMIString(bmi);
		}
		return comment;
	}
}
