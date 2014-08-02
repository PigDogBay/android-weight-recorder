package com.pigdogbay.weightrecorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeWizardFragment extends Fragment {
	public static final String TAG = "welcome";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);	
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_welcome_wizard, container,false);
		//Need to use ChildFragmentManager as ViewPager is nested in a fragment
		//If you use getFragmentManager then the red/blue/green fragments are not released
		//when the VPFragment is destroyed
		WelcomePagerAdapter adapter = new WelcomePagerAdapter(getChildFragmentManager());
        ViewPager viewPager = (ViewPager)rootView.findViewById(R.id.welcome_wizard_viewpager);
        viewPager.setAdapter(adapter);		
		return rootView;
	}
	
    public static class WelcomePagerAdapter extends FragmentPagerAdapter {

        public WelcomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WelcomeStartFragment();
                case 1:
                    return new WeightSettingsFragment();
                case 2:
                    return new HeightSettingsFragment();
                case 3:
                    return new WelcomeEndFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "WELCOME";
                case 1:
                    return "WEIGHT";
                case 2:
                    return "HEIGHT";
                case 3:
                    return "FINISHED";
            }
            return null;
        }
    }
	
    public static class WelcomeStartFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_welcome_start, container, false);
            return rootView;
        }
    }	
    public static class WelcomeEndFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_welcome_end, container, false);
            return rootView;
        }
    }		
}
