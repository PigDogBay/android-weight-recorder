package com.pigdogbay.weightrecorder.model;


public class UnitConverterFactory
{
	// Numbers must match arrays.xml
	public final static int KILOGRAMS_TO_KILOGRAMS = 1;
	public final static int KILOGRAMS_TO_POUNDS = 2;
	public final static int KILOGRAMS_TO_STONES = 3;

	public final static int METRES_TO_METRES = 1;
	public final static int METRES_TO_CENTIMETRES = 2;
	public final static int METRES_TO_INCHES = 3;

	public static IUnitConverter create(int id)
	{
		switch (id)
		{
		case KILOGRAMS_TO_KILOGRAMS:
			return new FactorConverter("kg", 1.0d);
		case KILOGRAMS_TO_POUNDS:
			return new FactorConverter("lb", 2.2046213d,0.5d);
		case KILOGRAMS_TO_STONES:
			return new StonesConverter();
		default:
			throw new IllegalArgumentException();
		}
	}

	public static IUnitConverter createLengthConverter(int id)
	{
		switch (id)
		{
		case METRES_TO_METRES:
			return new FactorConverter("m", 1.0d);
		case METRES_TO_CENTIMETRES:
			return new FactorConverter("cm", 100.0d);
		case METRES_TO_INCHES:
			return new FactorConverter("in", 39.37d);
		default:
			throw new IllegalArgumentException();
		}
	}
}

class FactorConverter implements IUnitConverter
{
	String units;
	double factor;
	double step=0.1d;
	
	public FactorConverter(String units, double factor)
	{
		this.units = units;
		this.factor = factor;
	}
	public FactorConverter(String units, double factor, double step)
	{
		this.units = units;
		this.factor = factor;
		this.step = step;
	}

	public String getUnits()
	{
		return units;
	}

	public double convert(double value)
	{
		return value * factor;
	}

	public double inverse(double value)
	{
		return value / factor;
	}

	public String getDisplayString(double weight)
	{
		return String.format("%.1f %s", weight, units);
	}

	public double getStepIncrement()
	{
		return step;
	}
}

class StonesConverter implements IUnitConverter
{
	private static final String STONE_UNITS = "st";
	private static final String POUND_UNITS = "lb";
	
	private static final double STONES_TO_POUNDS = 14d;
	private static final double KILOGRAMS_TO_POUNDS = 2.2046213d;
	private static final double KILOGRAMS_TO_STONES = KILOGRAMS_TO_POUNDS/STONES_TO_POUNDS;
	public String getUnits()
	{
		return STONE_UNITS;
	}

	public double convert(double value)
	{
		return value*KILOGRAMS_TO_STONES;
	}

	public double inverse(double value)
	{
		return value/KILOGRAMS_TO_STONES;
	}

	public String getDisplayString(double weight)
	{
		weight = weight*STONES_TO_POUNDS;
		int intWeight = (int)Math.round(weight);
		int stones= intWeight/14;
		int pounds=intWeight - stones*14;
		return stones==0 ? 
				String.format("%d %s", pounds, POUND_UNITS):
				String.format("%d%s %d%s", stones, STONE_UNITS, pounds, POUND_UNITS);
	}

	public double getStepIncrement()
	{
		return 1.0d/14.0d;
	}
	
}
