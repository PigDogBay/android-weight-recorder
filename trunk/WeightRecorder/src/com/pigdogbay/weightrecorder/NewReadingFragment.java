package com.pigdogbay.weightrecorder;

import com.pigdogbay.weightrecorder.model.IUnitConverter;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.Reading;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NewReadingFragment extends EditFragment {
	public static final String TAG = "new";
	private MainModel _MainModel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_new_reading, container,false);

		((Button) rootView.findViewById(R.id.AddReadingBtnAdd))
		.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				onEnterClick();
			}
		});
		
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);	
		setHasOptionsMenu(true);
		_MainModel = new MainModel(getActivity());
		loadPreferences();
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_new_reading, menu);
	}
	private void onEnterClick()
	{
		Reading reading = getReading();
		_MainModel.getDatabase().addReading(reading);
		hideKeyboard();
		savePreferences();
		Toast.makeText(getActivity(), getString(R.string.addreading_added),
				Toast.LENGTH_SHORT).show();
		((MainActivity)getActivity()).showHome();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		_MainModel.close();
	}
	private void loadPreferences()
	{
		IUnitConverter weightConverter = _MainModel.getWeightConverter();
		double lastWeight = _MainModel.getPreferencesHelper().getDouble(R.string.code_pref_last_weight_key, MainModel.DEFAULT_TARGET_WEIGHT);
		lastWeight = weightConverter.convert(lastWeight);
		setWeightConvert(weightConverter);
		setWeight(lastWeight);
	}

	private void savePreferences()
	{
		 double lastWeight = getWeight();
		 IUnitConverter weightConverter = _MainModel.getWeightConverter();
		 lastWeight = weightConverter.inverse(lastWeight);	
		 _MainModel.getPreferencesHelper().setDouble(R.string.code_pref_last_weight_key, lastWeight);
	}

}