package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.utils.ActivityUtils;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AboutFragment extends Fragment {
	public static final String TAG = "about";
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels/2;
        Button btn = (Button) rootView.findViewById(R.id.aboutBtnRate);
        btn.setMinimumWidth(width);
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showWebPage(R.string.market_weightrecorder);
			}
		});
        btn = (Button) rootView.findViewById(R.id.aboutBtnFacebook);
        btn.setMinimumWidth(width);
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showWebPage(R.string.market_pigdogbay_apps);
			}
		});
        btn = (Button) rootView.findViewById(R.id.aboutBtnLegal);
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showLegalNotices();
			}
		});
        rootView.findViewById(R.id.aboutFacebookLink).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showWebPage(R.string.facebookPage);
			}
		});
        rootView.findViewById(R.id.aboutTwitterLink).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showWebPage(R.string.twitter);
			}
		});
        rootView.findViewById(R.id.aboutWebsiteLink).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showWebPage(R.string.website);
			}
		});
        

        return rootView;
    }
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);	
		setHasOptionsMenu(true);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_about, menu);
	}	
	
	private void showWebPage(int urlId)
	{
		try
		{
			ActivityUtils.ShowWebPage(getActivity(), getString(urlId));
		}
		catch (ActivityNotFoundException e)
		{ 
				Toast.makeText(getActivity(), getString(R.string.about_no_market_app), Toast.LENGTH_LONG)
					.show();
		}
		
	}
	private void showLegalNotices()
	{
		ActivityUtils.showInfoDialog(getActivity(), R.string.copyright_title, R.string.copyright);
	}	
}
