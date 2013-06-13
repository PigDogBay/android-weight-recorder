package com.pigdogbay.weightrecorder.model;

import java.util.Date;

public class ReportAnalysis {
	public static final long DAY_IN_MILLIS = 24L * 60L * 60L * 1000L;

	UserSettings _UserSettings;
	BMICalculator _BMICalculator;
	TrendAnalysis _TrendAnalysis, _TrendanalysisLastWeek, _TrendanalysisLastMonth;
	double _MinWeight,_MaxWeight,_AverageWeight,_LatestWeight; 
	int _Count;
	
	public ReportAnalysis(UserSettings userSettings, Query query)
	{
		_UserSettings = userSettings;
		_BMICalculator = new BMICalculator(userSettings);
		_TrendAnalysis = new TrendAnalysis(query.getReadings());
		_MinWeight = query.getMinWeight().getWeight();
		_MaxWeight = query.getMaxWeight().getWeight();
		_LatestWeight = query.getLatestReading().getWeight();
		_AverageWeight = query.getAverageWeight();
		_Count = query._Readings.size();
		Date now = new Date();
		Date lastWeek = new Date(now.getTime() - 7L * DAY_IN_MILLIS);
		Date lastMonth = new Date(now.getTime() - 30L * DAY_IN_MILLIS);
		_TrendAnalysis = new TrendAnalysis(query.getReadings());
		Query queryLastMonth = query.getReadingsBetweenDates(lastMonth, now);
		_TrendanalysisLastMonth = new TrendAnalysis(queryLastMonth.getReadings());
		_TrendanalysisLastWeek = new TrendAnalysis(queryLastMonth.getReadingsBetweenDates(lastWeek, now).getReadings());
	}
	
	public double getLatestBMI()
	{
		return _BMICalculator.calculateBMI(_LatestWeight);		
	}
	public double getTargetBMI()
	{
		return _BMICalculator.calculateBMI(_UserSettings.TargetWeight);
	}
	public double getBottomOfIdealWeightRange()
	{
		return _BMICalculator.calculateWeightFromBMI(BMICalculator.UNDERWEIGHT_UPPER_LIMIT);
	}
	public double getTopOfIdealWeightRange()
	{
		return _BMICalculator.calculateWeightFromBMI(BMICalculator.NORMAL_UPPER_LIMIT);
	}
	public double getMinWeight()
	{
		return _MinWeight;
	}
	public double getMaxWeight()
	{
		return _MaxWeight;
	}
	public double getAverageWeight()
	{
		return _AverageWeight;
	}
	public double getAverageBMI()
	{
		return _BMICalculator.calculateBMI(_AverageWeight);
	}
	public int getCount()
	{
		return _Count;
	}
	public double getWeeklyTrendOverLastWeek()
	{
		return _TrendanalysisLastWeek.getTrendInDays()*7;
	}
	public double getWeeklyTrendOverLastMonth()
	{
		return _TrendanalysisLastMonth.getTrendInDays()*7;
	}
	public double getWeeklyTrendAllTime()
	{
		return _TrendAnalysis.getTrendInDays()*7;
	}
	public long getEstimatedDateUsingLastWeek()
	{
		return _TrendanalysisLastWeek.getEstimatedDate(_UserSettings.TargetWeight);
	}
	public long getEstimatedDateUsingLastMonth()
	{
		return _TrendanalysisLastMonth.getEstimatedDate(_UserSettings.TargetWeight);
	}
	public long getEstimatedDateUsingAllTime()
	{
		return _TrendAnalysis.getEstimatedDate(_UserSettings.TargetWeight);
	}
}
