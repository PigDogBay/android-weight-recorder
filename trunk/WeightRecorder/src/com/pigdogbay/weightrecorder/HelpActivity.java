package com.pigdogbay.weightrecorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpActivity extends FragmentActivity
{
	static int _TextStyleID = android.R.style.TextAppearance_Medium;

	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    	HelpPagerAdapter helpPagerAdapter = new HelpPagerAdapter(getSupportFragmentManager());
    	ViewPager viewPager = (ViewPager)findViewById(R.id.helpPager);
    	viewPager.setAdapter(helpPagerAdapter);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_help, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		TextView textView = (TextView) findViewById(R.id.HelpTextBox);
		switch (item.getItemId())
		{
		case (R.id.menu_help_home):
			finish();
			break;
		case (R.id.menu_help_text_small):
			_TextStyleID = android.R.style.TextAppearance_Small;
			break;
		case (R.id.menu_help_text_medium):
			_TextStyleID = android.R.style.TextAppearance_Medium;
			break;
		case (R.id.menu_help_text_large):
			_TextStyleID = android.R.style.TextAppearance_Large;
			break;
		default:
			return false;
		}
		if(textView!=null)
		{
			textView.setTextAppearance(this, _TextStyleID);
		}
		return true;
	}	
    public class HelpPagerAdapter extends FragmentPagerAdapter {

        public HelpPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new QuickHelpFragment();
                case 1:
                    return new HelpFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "QUICK HELP";
                case 1:
                    return "HELP";
                case 2:
            }
            return null;
        }
    }
	
    public static class QuickHelpFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quickhelp, container, false);
            return rootView;
        }
    }	
    public static class HelpFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_help, container, false);
            return rootView;
        }
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
        	super.onActivityCreated(savedInstanceState);
    		TextView textView = (TextView) getActivity().findViewById(R.id.HelpTextBox);
    		textView.setText(Html.fromHtml(getString(R.string.help_html)));
    		textView.setMovementMethod(new ScrollingMovementMethod());
        }
    }	
}
