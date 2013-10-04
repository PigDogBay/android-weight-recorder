package com.pigdogbay.weightrecorder;

import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.UnitConverterFactory;

import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class WeightSettingsFragment extends Fragment implements OnClickListener{
	
	MainModel _MainModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weight_settings, container, false);
        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	_MainModel = new MainModel(getActivity());
    	setRadioButtons();
    	((RadioButton)getActivity().findViewById(R.id.rbWeightSettingKilograms)).setOnClickListener(this);
    	((RadioButton)getActivity().findViewById(R.id.rbWeightSettingPounds)).setOnClickListener(this);
    	((RadioButton)getActivity().findViewById(R.id.rbWeightSettingStones)).setOnClickListener(this);
    }
    
    void setRadioButtons()
    {
    	int weightUnitsId = _MainModel.getWeightUnitsId();
    	int radioButtonId = R.id.rbWeightSettingKilograms; 
    	if (UnitConverterFactory.KILOGRAMS_TO_POUNDS==weightUnitsId)
    	{
    		radioButtonId = R.id.rbWeightSettingPounds;
    	}
    	else if (UnitConverterFactory.KILOGRAMS_TO_STONES==weightUnitsId)
    	{
    		radioButtonId = R.id.rbWeightSettingStones;
    		
    	}
    	RadioButton radioButton = (RadioButton) getActivity().findViewById(radioButtonId);
    	radioButton.setChecked(true);
    }

	@Override
	public void onClick(View view) {
    	Log.v("WeightRecorder","onRadioButtonClicked");
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked){
        	return;
        }
        switch(view.getId()) {
        case R.id.rbWeightSettingKilograms:
        	_MainModel.setWeightUnitsId(UnitConverterFactory.KILOGRAMS_TO_KILOGRAMS);
            break;
        case R.id.rbWeightSettingPounds:
        	_MainModel.setWeightUnitsId(UnitConverterFactory.KILOGRAMS_TO_POUNDS);
            break;
        case R.id.rbWeightSettingStones:
        	_MainModel.setWeightUnitsId(UnitConverterFactory.KILOGRAMS_TO_STONES);
            break;
        }
		
	}}
