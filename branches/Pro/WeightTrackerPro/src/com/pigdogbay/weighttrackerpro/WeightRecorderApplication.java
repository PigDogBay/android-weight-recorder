package com.pigdogbay.weighttrackerpro;

import java.util.List;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

public class WeightRecorderApplication  extends Application{
	private static final String CATEGORY = "Weight Recorder";
	private static final String PREFIX = "WR ";
	private List<String> _Dictionary;
	private Tracker _Tracker;	

	synchronized Tracker getTracker(){
		if (_Tracker==null){
			_Tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.app_tracker);
		}
		return _Tracker;
	}
	
	public static void trackEvent(Activity activity, String screenName, String label){
    	Tracker t = ((WeightRecorderApplication)activity.getApplication()).getTracker();
    	t.send(new HitBuilders.EventBuilder()
		.setCategory(CATEGORY)
		.setAction("WR "+screenName)
		.setLabel("WR "+label)
		.build());
	}	
	
	public static void trackEvent(Fragment fragment, String screenName, String label){
		trackEvent(fragment.getActivity(), screenName, label);
	}	

}
