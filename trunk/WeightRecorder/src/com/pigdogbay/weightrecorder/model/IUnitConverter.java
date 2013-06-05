package com.pigdogbay.weightrecorder.model;

public interface IUnitConverter
{
	String getUnits();
	double convert(double value);
	double inverse(double value);
	double getStepIncrement();
	String getDisplayString(double weight);
}
