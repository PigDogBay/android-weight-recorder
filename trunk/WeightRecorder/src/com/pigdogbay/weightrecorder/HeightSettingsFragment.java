package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.usercontrols.NumberPicker;
import com.pigdogbay.weightrecorder.model.IUnitConverter;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.UnitConverterAdapter;
import com.pigdogbay.weightrecorder.model.UnitConverterFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class HeightSettingsFragment extends Fragment implements OnClickListener{
	
	MainModel _MainModel;
	NumberPicker _NumberPicker; 
	UnitConverterAdapter _UnitConverterAdapter;
	int _LengthUnitsId;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_height_settings, container, false);
        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	_MainModel = new MainModel(getActivity());
    	_LengthUnitsId = _MainModel.getLengthUnitsId();
    	setupRadioButtons();
    	((RadioButton)getActivity().findViewById(R.id.rbHeightSettingCentimetres)).setOnClickListener(this);
    	((RadioButton)getActivity().findViewById(R.id.rbHeightSettingInches)).setOnClickListener(this);
    	((RadioButton)getActivity().findViewById(R.id.rbHeightSettingFeet)).setOnClickListener(this);
    	((RadioButton)getActivity().findViewById(R.id.rbHeightSettingMetres)).setOnClickListener(this);
    	_NumberPicker = (NumberPicker)getActivity().findViewById(R.id.HeightSettingsPicker);
    	setupNumberPicker(_MainModel.getHeightInMetres());
    }
    /* 
     * Persist any changes here
     */
    @Override
    public void onPause() {
    	super.onPause();
    	if (_LengthUnitsId!=_MainModel.getLengthUnitsId())
    	{
    		_MainModel.setLengthUnitsId(_LengthUnitsId);
    	}
    	if (_UnitConverterAdapter.getValue()!=_MainModel.getHeight())
    	{
    		_MainModel.setHeight(_UnitConverterAdapter.getValue());
    	}
    }
    void setupRadioButtons()
    {
    	int radioButtonId = R.id.rbHeightSettingInches; 
    	if (UnitConverterFactory.METRES_TO_METRES==_LengthUnitsId)
    	{
    		radioButtonId = R.id.rbHeightSettingMetres;
    	}
    	else if (UnitConverterFactory.METRES_TO_CENTIMETRES==_LengthUnitsId)
    	{
    		radioButtonId = R.id.rbHeightSettingCentimetres;
    		
    	}
    	else if (UnitConverterFactory.METRES_TO_FEET==_LengthUnitsId)
    	{
    		radioButtonId = R.id.rbHeightSettingFeet;
    		
    	}
    	
    	RadioButton radioButton = (RadioButton) getActivity().findViewById(radioButtonId);
    	radioButton.setChecked(true);
    }
    void setupNumberPicker(double heightInMetres)
    {
    	IUnitConverter converter = UnitConverterFactory.createLengthConverter(_LengthUnitsId);
    	_UnitConverterAdapter = new UnitConverterAdapter(converter);
    	_UnitConverterAdapter.setValue(converter.convert(heightInMetres));
    	_NumberPicker.setNumberPickerValue(_UnitConverterAdapter);
    }

	@Override
	public void onClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked){
        	return;
        }
        switch(view.getId()) {
        case R.id.rbHeightSettingMetres:
        	_LengthUnitsId = UnitConverterFactory.METRES_TO_METRES;
            break;
        case R.id.rbHeightSettingCentimetres:
        	_LengthUnitsId = UnitConverterFactory.METRES_TO_CENTIMETRES;
            break;
        case R.id.rbHeightSettingInches:
        	_LengthUnitsId = UnitConverterFactory.METRES_TO_INCHES;
            break;
        case R.id.rbHeightSettingFeet:
        	_LengthUnitsId = UnitConverterFactory.METRES_TO_FEET;
            break;
        default:
        	return;
        }
        setupNumberPicker(_UnitConverterAdapter.getValueInPrimaryUnits());
	}
}
