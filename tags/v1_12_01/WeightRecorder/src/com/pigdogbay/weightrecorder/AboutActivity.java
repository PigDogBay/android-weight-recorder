package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.mvp.BackgroundColorPresenter;
import com.pigdogbay.androidutils.mvp.IBackgroundColorView;
import com.pigdogbay.androidutils.utils.ActivityUtils;
import com.pigdogbay.weightrecorder.model.MainModel;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class AboutActivity extends Activity implements IBackgroundColorView
{
	BackgroundColorPresenter _BackgroundColorPresenter;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels/2;
		
        Button btn = (Button) findViewById(R.id.aboutBtnRate);
        btn.setMinimumWidth(width);
        btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
				showWebPage(R.string.market_weightrecorder);
			}
		});
        btn = (Button) findViewById(R.id.aboutBtnFacebook);
        btn.setMinimumWidth(width);
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showWebPage(R.string.facebookPage);
			}
		});
		((Button) findViewById(R.id.aboutBtnLegal)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				showLegal();
			}
		});
		
		_BackgroundColorPresenter = new BackgroundColorPresenter(this,new MainModel(this).createBackgroundColorModel());
		_BackgroundColorPresenter.updateBackground();
		
	}

	private void showWebPage(int urlId)
	{
		try
		{
			ActivityUtils.ShowWebPage(this, getString(urlId));
		}
		catch (ActivityNotFoundException e)
		{ 
				Toast.makeText(this, getString(R.string.about_no_market_app), Toast.LENGTH_LONG)
					.show();
		}
		
	}
	private void showLegal()
	{
		ActivityUtils.showInfoDialog(this, R.string.copyright_title, R.string.copyright);
	}
	@Override
	public void setBackgroundColor(int id) {
		ActivityUtils.setBackground(this, R.id.rootLayout, id);
	}
	@Override
	public void showPurchaseRequiredWarning() {
		//Do nothing
	}	
}