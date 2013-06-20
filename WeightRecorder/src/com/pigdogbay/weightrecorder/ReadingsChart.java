package com.pigdogbay.weightrecorder;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.pigdogbay.weightrecorder.model.ChartAxesRanges;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class ReadingsChart
{
	XYMultipleSeriesRenderer _Renderer;
	XYMultipleSeriesDataset _Dataset;
	
	public ReadingsChart()
	{
		_Renderer = new XYMultipleSeriesRenderer();
		_Renderer.setShowLegend(false);
		_Renderer.setPanEnabled(false);
		_Renderer.setZoomEnabled(false);
		_Renderer.setBackgroundColor(Color.BLACK);
		_Renderer.setApplyBackgroundColor(true);
		_Renderer.setAxesColor(Color.LTGRAY);
		_Renderer.setLabelsColor(Color.LTGRAY);
		_Renderer.setShowGrid(true);
		_Renderer.setXLabelsAlign(Align.CENTER);
		_Renderer.setYLabelsAlign(Align.RIGHT);
		_Renderer.setZoomButtonsVisible(false);

		_Dataset = new XYMultipleSeriesDataset();
		
	}
	public void setXAxisTitle(String title)
	{
		_Renderer.setXTitle(title);
	}

	public void setYAxisTitle(String title)
	{
		_Renderer.setYTitle(title);
	}
	public void setChartAxesRanges(ChartAxesRanges chartAxesRanges)
	{
		_Renderer.setXAxisMin(chartAxesRanges.XAxisMin);
		_Renderer.setXAxisMax(chartAxesRanges.XAxisMax);
		_Renderer.setYAxisMin(chartAxesRanges.YAxisMin);
		_Renderer.setYAxisMax(chartAxesRanges.YAxisMax);
	}
	
	public void addReadings(TimeSeries series)
	{
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.GREEN);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		r.setLineWidth(2f);
		_Renderer.addSeriesRenderer(r);
		_Dataset.addSeries(series);
	}
	public void addTrend(TimeSeries series)
	{
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.TRIANGLE);
		r.setFillPoints(true);
		r.setLineWidth(1.8f);
		_Renderer.addSeriesRenderer(r);
		_Dataset.addSeries(series);
	}
	public void addTarget(TimeSeries series)
	{
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.CYAN);
		r.setPointStyle(PointStyle.SQUARE);
		r.setFillPoints(true);
		r.setLineWidth(1.8f);
		_Renderer.addSeriesRenderer(r);
		_Dataset.addSeries(series);
	}
	private void fitDisplay(Context context)
	{
		int screenSize = context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE)
		{
			// Top, left, bottom, right
			_Renderer.setMargins(new int[] { 20, 80, 40, 0 });
			_Renderer.setAxisTitleTextSize(32f);
			_Renderer.setLabelsTextSize(18f);
			_Renderer.setXLabels(10);
			_Renderer.setYLabels(10);
			_Renderer.setPointSize(5f);

		}
		else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL)
		{
			_Renderer.setMargins(new int[] { 10, 40, 20, 0 });
			_Renderer.setAxisTitleTextSize(16f);
			_Renderer.setLabelsTextSize(10f);
			_Renderer.setXLabels(8);
			_Renderer.setYLabels(8);
			_Renderer.setPointSize(3f);
		}
		else
		{
			_Renderer.setMargins(new int[] { 8, 30, 16, 0 });
			_Renderer.setAxisTitleTextSize(12f);
			_Renderer.setLabelsTextSize(8f);
			_Renderer.setXLabels(6);
			_Renderer.setYLabels(6);
			_Renderer.setPointSize(2f);
		}

	}
	public GraphicalView createView(Context context)
	{
		fitDisplay(context);
		return ChartFactory.getTimeChartView(context, _Dataset, _Renderer,"d MMM");

	}
}
