package com.pigdogbay.weightrecorder;

import java.util.Date;

import com.pigdogbay.weightrecorder.model.Reading;

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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,false);
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//Disable the the android home button
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);	
		setHasOptionsMenu(true);
		wireUpButtons();
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
	private void wireUpButtons() {
		final MainActivity activity = (MainActivity) getActivity();
		((Button) activity.findViewById(R.id.HomeBtnEntry))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showNew();
			}
		});
		((Button) activity.findViewById(R.id.HomeBtnEditor))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showList();
			}
		});
		((Button) activity.findViewById(R.id.HomeBtnSettings))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showSettings();
			}
		});
		((Button) activity.findViewById(R.id.HomeBtnChart))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showChart();
			}
		});
		((Button) activity.findViewById(R.id.HomeBtnReport))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showReport();
			}
		});
		((Button) activity.findViewById(R.id.HomeBtnHelp))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				activity.showHelp();
			}
		});
		
	}
}
