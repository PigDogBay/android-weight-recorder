package com.pigdogbay.weightrecorder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.pigdogbay.androidutils.math.BestLineFit;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;

public class ReadingsChart
{
	String _YAxisTitle = "Weight";
	String _XAxisTitle = "Date";
	boolean _ShowTrendLine = false;
	boolean _ShowTargetLine = false;
	double _TargetWeight = 75.0D;
	

	public void setShowTrendLine(boolean show)
	{
		_ShowTrendLine = show;
	}
	public void setShowTargetLine(boolean show)
	{
		_ShowTargetLine = show;
	}
	public void setTargetWeight(double weight)
	{
		_TargetWeight = weight;
	}

	public void setXAxisTitle(String title)
	{
		_XAxisTitle = title;
	}

	public void setYAxisTitle(String title)
	{
		_YAxisTitle = title;
	}

	public GraphicalView CreateView(List<Reading> readings, Context context,
			int period)
	{
		Query query = new Query(readings);
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setShowLegend(false);
		renderer.setPanEnabled(false);
		renderer.setZoomEnabled(false);
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setApplyBackgroundColor(true);

		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.GREEN);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		r.setLineWidth(2f);
		renderer.addSeriesRenderer(r);

		renderer.setXTitle(_XAxisTitle);
		renderer.setYTitle(_YAxisTitle);
		renderer.setAxesColor(Color.LTGRAY);
		renderer.setLabelsColor(Color.LTGRAY);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setZoomButtonsVisible(false);

		query.sortByDate();
		setAxes(query, renderer, period);

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("weights");
		for (Reading reading : query.getReadings())
		{
			series.add(reading.getDate(), reading.getWeight());
		}
		dataset.addSeries(series);
		if (_ShowTrendLine)
		{
			addTrendLine(renderer, dataset, query, period);
		}
		if (_ShowTargetLine)
		{
			addTargetLine(renderer, dataset, query, period);
		}

		fitDisplay(context, renderer);
		return ChartFactory.getTimeChartView(context, dataset, renderer,
				"d MMM");
	}

	private void setAxes(Query query, XYMultipleSeriesRenderer renderer,
			long period)
	{
		int count = query.getReadings().size();
		if (count > 2)
		{
			Date endTime = new Date();
			Date startTime = new Date(endTime.getTime() - period * 1000L * 60L
					* 60L * 24L);
			Query matches = query.getReadingsBetweenDates(startTime, endTime);

			double min = matches.getMinWeight().getWeight();
			double max = matches.getMaxWeight().getWeight();
			double extra = (max - min) / 10;
			if (extra<0.5d){extra=0.5d;}
			renderer.setYAxisMin(min - extra);
			renderer.setYAxisMax(max + extra);
			// add 1 day to current date
			renderer.setXAxisMax(endTime.getTime() + 1000L * 60L * 60L * 24L);
			renderer.setXAxisMin(startTime.getTime());
		}

	}

	private void fitDisplay(Context context, XYMultipleSeriesRenderer renderer)
	{
		int screenSize = context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE)
		{
			// Top, left, bottom, right
			renderer.setMargins(new int[] { 20, 80, 40, 0 });
			renderer.setAxisTitleTextSize(32f);
			renderer.setLabelsTextSize(18f);
			renderer.setXLabels(10);
			renderer.setYLabels(10);
			renderer.setPointSize(5f);

		}
		else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL)
		{
			renderer.setMargins(new int[] { 10, 40, 20, 0 });
			renderer.setAxisTitleTextSize(16f);
			renderer.setLabelsTextSize(10f);
			renderer.setXLabels(8);
			renderer.setYLabels(8);
			renderer.setPointSize(3f);
		}
		else
		{
			renderer.setMargins(new int[] { 8, 30, 16, 0 });
			renderer.setAxisTitleTextSize(12f);
			renderer.setLabelsTextSize(8f);
			renderer.setXLabels(6);
			renderer.setYLabels(6);
			renderer.setPointSize(2f);
		}

	}

	private void addTrendLine(XYMultipleSeriesRenderer renderer,
			XYMultipleSeriesDataset dataset, Query query, long period)
	{

		long startTime = new Date().getTime() - period * 1000L * 60L * 60L
				* 24L;
		BestLineFit blf = new BestLineFit();
		Query queryPeriod = query.getReadingsBetweenDates(new Date(startTime),
				new Date());
		if (queryPeriod.getReadings().size() < 3)
		{
			return;
		}
		for (Reading reading : queryPeriod.getReadings())
		{
			blf.Add((double) reading.getDate().getTime(), reading.getWeight());
		}
		double slope = blf.getSlope();
		double intercept = blf.getIntercept();
		double startWeight = ((double) startTime) * slope + intercept;

		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.TRIANGLE);
		r.setFillPoints(true);
		r.setLineWidth(1.8f);
		renderer.addSeriesRenderer(r);

		TimeSeries trendSeries = new TimeSeries("trend");
		trendSeries.add(new Date(startTime), startWeight);
		//Project forward 1 year
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		for (int i=0;i<12; i++)
		{
			Date date = cal.getTime();
			double weight = ((double)date.getTime())*slope +intercept;
			trendSeries.add(date,weight);
			cal.add(Calendar.MONTH,1);
		}		
		dataset.addSeries(trendSeries);

	}
	private void addTargetLine(XYMultipleSeriesRenderer renderer,
			XYMultipleSeriesDataset dataset, Query query, long period)
	{
		Log.v("WeightRecorder","adding target line");
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.CYAN);
		r.setPointStyle(PointStyle.SQUARE);
		r.setFillPoints(true);
		r.setLineWidth(1.8f);
		renderer.addSeriesRenderer(r);

		TimeSeries trendSeries = new TimeSeries("target");
		//Display line one year back and forward
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.YEAR, false);
		for (int i=0;i<24; i++)
		{
			trendSeries.add(cal.getTime(), _TargetWeight);
			cal.add(Calendar.MONTH,1);
		}
		
		dataset.addSeries(trendSeries);		
	}
	
}
