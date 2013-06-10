package com.pigdogbay.weightrecorder;

import java.util.List;

import com.pigdogbay.weightrecorder.model.IUnitConverter;
import com.pigdogbay.weightrecorder.model.Reading;

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
	private IUnitConverter _WeightConverter;
	private double _Height;
	
	public ReadingsArrayAdapter(ReadingListActivity activity, List<Reading> readings, IUnitConverter weightConverter, double heightInMetres)
	{
		super(activity, R.layout.readings_item, readings);
		_Readings = readings;
		_Activity = activity;
		_WeightConverter = weightConverter;
		_Height = heightInMetres;
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
		textView.setText(weightToString(reading.getWeight()));

		textView = (TextView) convertView.findViewById(R.id.ResultsItemDate);
		String dateText = DateUtils.formatDateTime(this.getContext(), reading.getDate().getTime(), DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_YEAR);
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
			comment = String.format("BMI %.1f",calculateBMI(reading));
		}
		return comment;
	}
	private double calculateBMI(Reading reading)
	{
		double bmi = _Height;
		if (bmi!=0)
		{
			bmi = reading.getWeight()/(bmi*bmi);
		}
		return bmi;
	}	
	private String weightToString(double weight)
	{
		weight = _WeightConverter.convert(weight);
		return _WeightConverter.getDisplayString(weight);
	}

}
