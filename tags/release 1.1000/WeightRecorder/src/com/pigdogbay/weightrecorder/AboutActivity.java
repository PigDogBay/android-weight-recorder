package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.utils.ActivityUtils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class AboutActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		RegisterIcon(R.id.facebookImg, R.string.facebookPage);
		RegisterIcon(R.id.twitterImg, R.string.twitter);
		RegisterIcon(R.id.bloggerImg, R.string.blogger);
		
		((Button) findViewById(R.id.AboutBtnRate)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				ShowWebPage(getString(R.string.market_weightrecorder));
			}
		});
		ImageView img = (ImageView) findViewById(R.id.mailImg);
		img.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				ActivityUtils.SendEmail(AboutActivity.this, new String[]{getString(R.string.email)}, AboutActivity.this.getString(R.string.about_email_subject), AboutActivity.this.getString(R.string.about_email_content));
			}
		});
		
	}
	private void RegisterIcon(int iconID, final int stringID)
	{
		ImageView img = (ImageView) findViewById(iconID);
		img.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				ShowWebPage(getString(stringID));
			}
		});
	}

	private void ShowWebPage(String url)
	{
		try
		{
			ActivityUtils.ShowWebPage(this, url);
		}
		catch (ActivityNotFoundException e)
		{ 
				Toast.makeText(this, getString(R.string.about_no_market_app), Toast.LENGTH_LONG)
					.show();
		}
		
	}
}
