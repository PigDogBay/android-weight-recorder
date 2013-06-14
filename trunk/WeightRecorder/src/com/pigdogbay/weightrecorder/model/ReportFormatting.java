package com.pigdogbay.weightrecorder.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.pigdogbay.weightrecorder.R;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;

public class ReportFormatting {
	private static final String InvalidDateString = "---";
	UserSettings _UserSettings;
	Context _Context;
	
	public ReportFormatting(Context context, UserSettings userSettings){
		_UserSettings = userSettings;
		_Context = context;
	}
	/**
	 * @param weight in kilograms
	 * @return string representation with user units
	 */
	public String getWeightString(double weight) {
		if (Double.isNaN(weight) || Double.isInfinite(weight))
		{
			//prevent user seeing INF or NaN 
			weight = 0;
		}
		weight = _UserSettings.WeightConverter.convert(weight);
		return _UserSettings.WeightConverter.getDisplayString(weight);
	}
	public String getBMIString(double bmi) {
		if (Double.isNaN(bmi) || Double.isInfinite(bmi) || bmi<0)
		{
			//prevent user seeing INF or NaN 
			bmi = 0;
		}
		if (bmi>BMICalculator.MAX_BMI)
		{
			bmi = BMICalculator.MAX_BMI;
		}
		return String.format(Locale.US,"%.1f (%s)", bmi, BMICalculator.getString(_Context, bmi));
	}
	public String getIdealWeightRange(double startWeight, double endWeight) {
		return getWeightString(startWeight) +" - "+getWeightString(endWeight);
	}
	public String getValidDateString(long timeInMillis)
	{
		return TrendAnalysis.isGoalDateValid(timeInMillis) ? getDateString(timeInMillis) : InvalidDateString;
	}
	public String getDateString(long timeInMillis)
	{
		//DateUtils.formatDateTime suffers from the Year 2038 problem
		//One gripe with SDF is that UK locale formats like June 14, 2013 which is more American IMHO
		return SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG).format(new Date(timeInMillis));
	}
	public String getWeightTrendDirection(double trend)
	{
		int id =trend>0 ? R.string.report_gaining : R.string.report_losing;
		return  _Context.getString(id);
	}
	public String getWeightTrend(double trend)
	{
		return  String.format("%s %s",getWeightString(trend),_Context.getString(R.string.report_per_week));
	}
	
}
