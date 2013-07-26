package com.pigdogbay.weightrecorder;

import com.pigdogbay.weightrecorder.model.MainModel;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HelpActivity extends Activity
{
	static int _TextStyleID = android.R.style.TextAppearance_Medium;

	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
		TextView textView = (TextView) findViewById(R.id.HelpTextBox);
		textView.setText(Html.fromHtml(getString(R.string.help_html)));
		textView.setMovementMethod(new ScrollingMovementMethod());
		MainModel mainModel = new MainModel(this);
		if (mainModel.getRemoveAds()){
			ActivitiesHelper.removeAds(this);
		}
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
}
