package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.mvp.AdPresenter;
import com.pigdogbay.androidutils.mvp.IAdView;
import com.pigdogbay.weightrecorder.model.MainModel;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HelpActivity extends Activity implements IAdView
{
	static int _TextStyleID = android.R.style.TextAppearance_Medium;
	AdPresenter _AdPresenter;

	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
		TextView textView = (TextView) findViewById(R.id.HelpTextBox);
		textView.setText(Html.fromHtml(getString(R.string.help_html)));
		textView.setMovementMethod(new ScrollingMovementMethod());
		MainModel mainModel = new MainModel(this);
		_AdPresenter = new AdPresenter(this, mainModel.createAdModel());
		try{_AdPresenter.adCheck();}catch(Exception e){}
		mainModel.close();
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
		textView.setTextAppearance(this, _TextStyleID);
		return true;
	}	
	@Override
	public void removeAd() {
		ActivitiesHelper.removeAds(this);
	}

	@Override
	public void showAd() {
		ActivitiesHelper.loadAd(this);
	}
	
}
