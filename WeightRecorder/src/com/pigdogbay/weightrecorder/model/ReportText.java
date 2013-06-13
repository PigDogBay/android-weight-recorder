package com.pigdogbay.weightrecorder.model;

import java.util.Locale;

import com.pigdogbay.weightrecorder.R;

import android.content.Context;
import android.text.Html;
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
	public String getWeightString(double weight) {
		weight = _UserSettings.WeightConverter.convert(weight);
		return _UserSettings.WeightConverter.getDisplayString(weight);
	}
	public String getBMIString(double bmi) {
		return String.format(Locale.US,"%.1f (%s)", bmi, BMICalculator.getString(_Context, bmi));
	}
	public String getIdealWeightRange(double startWeight, double endWeight) {
		return getWeightString(startWeight) +" - "+getWeightString(endWeight);
	}
	public String getDateString(long timeInMillis)
	{
		if (!TrendAnalysis.isGoalDateValid(timeInMillis))
		{
			return "---";
		}
		return DateUtils.formatDateTime(_Context,
				timeInMillis, DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_SHOW_YEAR);
		
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
	
	public String createReport(ReportAnalysis analysis)
	{
		String template = Html.fromHtml(_Context.getString(R.string.report_template)).toString();
		template = template.replace("$latestBMI", getBMIString(analysis.getLatestBMI()));
		template = template.replace("$goalBMI", getBMIString(analysis.getTargetBMI()));
		template = template.replace("$idealWeight", getIdealWeightRange(analysis.getBottomOfIdealWeightRange(),analysis.getTopOfIdealWeightRange()));
		
		return template;
	}
	
}
