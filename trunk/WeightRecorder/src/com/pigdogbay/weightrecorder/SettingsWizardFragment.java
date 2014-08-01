package com.pigdogbay.weightrecorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsWizardFragment extends Fragment{
	public static final String TAG = "settings";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_settings_wizard, container,false);
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);	
		setHasOptionsMenu(true);
		//Need to use ChildFragmentManager as ViewPager is nested in a fragment
		//If you use getFragmentManager then the red/blue/green fragments are not released
		//when the VPFragment is destroyed
        SettingsPagerAdapter adapter = new SettingsPagerAdapter(getChildFragmentManager());
        ViewPager viewPager = (ViewPager)getView().findViewById(R.id.settings_wizard_viewpager);
        viewPager.setAdapter(adapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_settings, menu);
	}
    public static class SettingsPagerAdapter extends FragmentPagerAdapter {

        public SettingsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WeightSettingsFragment();
                case 1:
                    return new HeightSettingsFragment();
                case 2:
                    return new BackgroundSettingsFragment();
                case 3:
                    return new OtherSettingsFragment();
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
                    return "WEIGHT";
                case 1:
                    return "HEIGHT";
                case 2:
                    return "BACKGROUND";
                case 3:
                    return "OTHER";
            }
            return null;
        }
    }

}
