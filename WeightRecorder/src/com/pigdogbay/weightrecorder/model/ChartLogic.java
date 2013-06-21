package com.pigdogbay.weightrecorder.model;

import java.util.Calendar;
import java.util.Date;

import org.achartengine.model.TimeSeries;

public class ChartLogic {

	UserSettings _UserSettings;
	public ChartLogic(UserSettings userSettings) {
		_UserSettings = userSettings;
	}

	public ChartAxesRanges calculateAxesRanges(Query query, long period) {
		ChartAxesRanges chartAxesRanges = new ChartAxesRanges();

		Date endTime = new Date();
		Date startTime = new Date(endTime.getTime() - period * 1000L * 60L
				* 60L * 24L);
		if (period == 0) {
			// use first reading
			startTime = query.getFirstReading().getDate();
		}
		Query matches = query.getReadingsBetweenDates(startTime, endTime);

		double min = matches.getMinWeight().getWeight();
		min = _UserSettings.WeightConverter.convert(min);
		double max = matches.getMaxWeight().getWeight();
		max = _UserSettings.WeightConverter.convert(max);
		double extra = (max - min) / 10;
		if (extra < 0.5d) {
			extra = 0.5d;
		}
		chartAxesRanges.YAxisMin = min - extra;
		chartAxesRanges.YAxisMax = max + extra;
		// add 1 day to current date
		chartAxesRanges.XAxisMax = endTime.getTime() + 1000L * 60L * 60L * 24L;
		chartAxesRanges.XAxisMin = startTime.getTime();
		return chartAxesRanges;
	}
	
	public TimeSeries createReadingsSeries(Query query)
	{
		TimeSeries series = new TimeSeries("weights");
		for (Reading reading : query.getReadings())
		{
			double weight = reading.getWeight();
			weight = _UserSettings.WeightConverter.convert(weight);
			series.add(reading.getDate(), weight);
		}
		return series;
	}
	public TimeSeries createTargetSeries()
	{
		TimeSeries series = new TimeSeries("target");
		//Display line one year back and forward
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.YEAR, false);
		double targetWeight = _UserSettings.WeightConverter.convert(_UserSettings.TargetWeight);
		for (int i=0;i<25; i++)
		{
			series.add(cal.getTime(), targetWeight);
			cal.add(Calendar.MONTH,1);
		}
		return series;
	}
	public TimeSeries createTrendSeries(Query query, long period)
	{
		//ensure readings are sorted by date before proceding
		query.sortByDate();
		TimeSeries series = new TimeSeries("trend");
		long startTime = new Date().getTime() - period * 1000L * 60L * 60L* 24L;
		if (period==0)
		{
			//use first reading
			startTime = query.getFirstReading().getDate().getTime();
		}
		else
		{
			query = query.getReadingsBetweenDates(new Date(startTime),new Date());
		}
		
		if (query.getReadings().size()<3)
		{
			return series;
		}
		TrendAnalysis trendAnalysis = new TrendAnalysis(query.getReadings());

		//Add start weight
		double weight = trendAnalysis.getEstimatedWeight(startTime);
		weight = _UserSettings.WeightConverter.convert(weight);
		series.add(new Date(startTime), weight);
		//Project forward 1 year
		Calendar cal = Calendar.getInstance();
		for (int i=0;i<13; i++)
		{
			weight = trendAnalysis.getEstimatedWeight(cal.getTimeInMillis());
			weight = _UserSettings.WeightConverter.convert(weight);
			series.add(cal.getTime(),weight);
			cal.add(Calendar.MONTH,1);
		}		
		return series;
	}
	
	
}
