package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.utils.ActivityUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeFragment extends Fragment {
	public static final String TAG = "home";
    private static final String EVENT_LABEL = "Home Screen";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,false);
		//Disable the the android home button
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);	
		setHasOptionsMenu(true);
		wireUpButtons(rootView);
		return rootView;
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_home, menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case (R.id.menu_home_about):
			((MainActivity) getActivity()).showAbout();
			return true;
		case (R.id.menu_home_welcome):
			((MainActivity) getActivity()).showWelcome();
			return true;
		case (R.id.menu_home_go_pro):
			ActivityUtils.ShowAppOnMarketPlace(getActivity(), R.string.market_weightrecorderpro);
			WeightRecorderApplication.trackEvent(HomeFragment.this, EVENT_LABEL, "menu_go_pro");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
	private void wireUpButtons(View view) {
		final MainActivity activity = (MainActivity) getActivity();
		((Button) view.findViewById(R.id.HomeBtnEntry))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showNew();
			}
		});
		((Button) view.findViewById(R.id.HomeBtnEditor))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showList();
			}
		});
		((Button) view.findViewById(R.id.HomeBtnSettings))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showSettings();
			}
		});
		((Button) view.findViewById(R.id.HomeBtnChart))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showChart();
			}
		});
		((Button) view.findViewById(R.id.HomeBtnReport))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showReport();
			}
		});
		((Button) view.findViewById(R.id.HomeBtnHelp))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showHelp();
			}
		});
		
	}
}
