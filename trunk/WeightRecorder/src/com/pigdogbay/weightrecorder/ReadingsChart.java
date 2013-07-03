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
import android.util.TypedValue;

public class ReadingsChart
{
	XYMultipleSeriesRenderer _Renderer;
	XYMultipleSeriesDataset _Dataset;
	Context _Context;
	public ReadingsChart(Context context)
	{
		_Context = context;
		_Renderer = new XYMultipleSeriesRenderer();
		_Renderer.setShowLegend(false);
		_Renderer.setPanEnabled(true);
		_Renderer.setZoomEnabled(true);
		_Renderer.setBackgroundColor(Color.BLACK);
		_Renderer.setApplyBackgroundColor(true);
		_Renderer.setAxesColor(Color.LTGRAY);
		_Renderer.setLabelsColor(Color.LTGRAY);
		_Renderer.setShowGrid(true);
		_Renderer.setXLabelsAlign(Align.CENTER);
		_Renderer.setYLabelsAlign(Align.RIGHT);
		_Renderer.setZoomButtonsVisible(true);

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
		float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, _Context.getResources().getDisplayMetrics());
		r.setLineWidth(size);
		_Renderer.addSeriesRenderer(r);
		_Dataset.addSeries(series);
	}
	public void addTrend(TimeSeries series)
	{
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.TRIANGLE);
		r.setFillPoints(true);
		float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.8f, _Context.getResources().getDisplayMetrics());
		r.setLineWidth(size);
		_Renderer.addSeriesRenderer(r);
		_Dataset.addSeries(series);
	}
	public void addTarget(TimeSeries series)
	{
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.CYAN);
		r.setPointStyle(PointStyle.SQUARE);
		r.setFillPoints(true);
		float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.8f, _Context.getResources().getDisplayMetrics());
		r.setLineWidth(size);
		_Renderer.addSeriesRenderer(r);
		_Dataset.addSeries(series);
	}
	private void fitDisplay()
	{
		int screenSize = _Context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, _Context.getResources().getDisplayMetrics());
		_Renderer.setPointSize(size);
		size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, _Context.getResources().getDisplayMetrics());
		_Renderer.setAxisTitleTextSize(size);
		size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, _Context.getResources().getDisplayMetrics());
		_Renderer.setLabelsTextSize(size);
		if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE)
		{
			// Top, left, bottom, right
			_Renderer.setMargins(new int[] { 20, 80, 40, 0 });
			_Renderer.setXLabels(10);
			_Renderer.setYLabels(10);

		}
		else if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL)
		{
			_Renderer.setMargins(new int[] { 10, 40, 20, 0 });
			_Renderer.setXLabels(8);
			_Renderer.setYLabels(8);
		}
		else
		{
			_Renderer.setMargins(new int[] { 8, 30, 16, 0 });
			_Renderer.setXLabels(6);
			_Renderer.setYLabels(6);
		}

	}
	public GraphicalView createView()
	{
		fitDisplay();
		return ChartFactory.getTimeChartView(_Context, _Dataset, _Renderer,"d MMM");

	}
}
