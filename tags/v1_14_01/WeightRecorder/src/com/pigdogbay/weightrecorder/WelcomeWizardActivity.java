package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.mvp.BackgroundColorPresenter;
import com.pigdogbay.androidutils.mvp.IBackgroundColorView;
import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.weightrecorder.model.MainModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeWizardActivity extends FragmentActivity implements IBackgroundColorView{

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_welcome_wizard);
        WelcomePagerAdapter adapter = new WelcomePagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager)findViewById(R.id.rootLayout);
        viewPager.setAdapter(adapter);		
        BackgroundColorPresenter backgroundColorPresenter = new BackgroundColorPresenter(this,new MainModel(this).createBackgroundColorModel());
		backgroundColorPresenter.updateBackground();
        
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_settings, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case (R.id.menu_settings_home):
			finish();
			break;
		default:
			return false;
		}
		return true;
	}    
	
	@Override
	public void setBackgroundColor(int id) {
		ActivityUtils.setBackground(this, R.id.rootLayout, id);
	}

    public class WelcomePagerAdapter extends FragmentPagerAdapter {

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
