package com.pigdogbay.weightrecorder.model;

import java.util.Date;

public class ReportAnalysis {
	public static final long DAY_IN_MILLIS = 24L * 60L * 60L * 1000L;

	UserSettings _UserSettings;
	BMICalculator _BMICalculator;
	TrendAnalysis _TrendAnalysis, _TrendanalysisLastWeek, _TrendanalysisLastMonth;
	public double MinWeight,MaxWeight,AverageWeight,LatestWeight; 
	public boolean IsWeekTrendAvaialble=false, IsMonthTrendAvailable=false;
	public int Count;
	
	public ReportAnalysis(UserSettings userSettings, Query query)
	{
		_UserSettings = userSettings;
		_BMICalculator = new BMICalculator(userSettings);
		MinWeight = query.getMinWeight().getWeight();
		MaxWeight = query.getMaxWeight().getWeight();
		LatestWeight = query.getLatestReading().getWeight();
		AverageWeight = query.getAverageWeight();
		Count = query._Readings.size();
		
		Date now = new Date();
		Date lastWeek = new Date(now.getTime() - 7L * DAY_IN_MILLIS);
		Date lastMonth = new Date(now.getTime() - 30L * DAY_IN_MILLIS);
		_TrendAnalysis = new TrendAnalysis(query.getReadings());
		_TrendanalysisLastMonth = _TrendAnalysis;
		_TrendanalysisLastWeek = _TrendAnalysis;
		Query querySubset = query.getReadingsBetweenDates(lastMonth, now);
		if (querySubset.getReadings().size()>1)
		{
			_TrendanalysisLastMonth = new TrendAnalysis(querySubset.getReadings());
			IsMonthTrendAvailable=true;
			querySubset = querySubset.getReadingsBetweenDates(lastWeek, now);
			if (querySubset.getReadings().size()>1)
			{
				_TrendanalysisLastWeek = new TrendAnalysis(querySubset.getReadings());
				IsWeekTrendAvaialble=true;
			}
		}
	}
	
	public double getLatestBMI()
	{
		return _BMICalculator.calculateBMI(LatestWeight);		
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
	public double getAverageBMI()
	{
		return _BMICalculator.calculateBMI(AverageWeight);
	}
	public double getWeeklyTrendOverLastWeek()
	{
		return _TrendanalysisLastWeek.getTrendInDays()*7D;
	}
	public double getWeeklyTrendOverLastMonth()
	{
		return _TrendanalysisLastMonth.getTrendInDays()*7D;
	}
	public double getWeeklyTrendAllTime()
	{
		return _TrendAnalysis.getTrendInDays()*7D;
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
