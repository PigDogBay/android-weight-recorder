package com.pigdogbay.weightrecorder.model;

import android.content.Context;
import android.text.format.DateUtils;

public class ReportText {
	UserSettings _UserSettings;
	Context _Context;
	
	public ReportText(Context context, UserSettings userSettings){
		_UserSettings = userSettings;
		_Context = context;
	}
	/**
	 * @param weight in kilograms
	 * @return string representation with user units
	 */
	public String weightToString(double weight) {
		weight = _UserSettings.WeightConverter.convert(weight);
		return _UserSettings.WeightConverter.getDisplayString(weight);
	}
	public String bmiToString(double bmi) {
		return String.format("%.1f (%s)", bmi, BMICalculator.getString(_Context, bmi));
	}
	public String idealWeightRangeToString(double startWeight, double endWeight) {
		return weightToString(startWeight) +" - "+weightToString(endWeight);
	}
	public String getDateString(long timeInMillis)
	{
		return DateUtils.formatDateTime(_Context,
				timeInMillis, DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_SHOW_YEAR);
		
	}
	
}
