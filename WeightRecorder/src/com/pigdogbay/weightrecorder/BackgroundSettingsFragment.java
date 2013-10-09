package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.mvp.BackgroundColorModel;
import com.pigdogbay.androidutils.utils.PreferencesHelper;
import com.pigdogbay.weightrecorder.model.MainModel;
import com.pigdogbay.weightrecorder.model.UnitConverterFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class BackgroundSettingsFragment extends Fragment implements OnClickListener{
	BackgroundColorModel _BackgroundColorModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_background_setting, container, false);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	_BackgroundColorModel = new BackgroundColorModel(new PreferencesHelper(getActivity()));
    	dataExchangeToControls();
    	((RadioButton)getActivity().findViewById(R.id.BackgroundSettingSkyBlue)).setOnClickListener(this);
    	((RadioButton)getActivity().findViewById(R.id.BackgroundSettingDarkPink)).setOnClickListener(this);
    	((RadioButton)getActivity().findViewById(R.id.BackgroundSettingLightPink)).setOnClickListener(this);
    	((RadioButton)getActivity().findViewById(R.id.BackgroundSettingTurquoise)).setOnClickListener(this);
    	((RadioButton)getActivity().findViewById(R.id.BackgroundSettingYellow)).setOnClickListener(this);
    	((RadioButton)getActivity().findViewById(R.id.BackgroundSettingGrey)).setOnClickListener(this);
    	((RadioButton)getActivity().findViewById(R.id.BackgroundSettingWhite)).setOnClickListener(this);
   }
	@Override
	public void onClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked){
        	return;
        }
        switch(view.getId()) {
        case R.id.BackgroundSettingSkyBlue:
        	_BackgroundColorModel.setBackgroundIndex(BackgroundColorModel.SKY_BLUE_INDEX);
            break;
        case R.id.BackgroundSettingDarkPink:
        	_BackgroundColorModel.setBackgroundIndex(BackgroundColorModel.DARK_PINK_INDEX);
            break;
        case R.id.BackgroundSettingLightPink:
        	_BackgroundColorModel.setBackgroundIndex(BackgroundColorModel.LIGHT_PINK_INDEX);
            break;
        case R.id.BackgroundSettingTurquoise:
        	_BackgroundColorModel.setBackgroundIndex(BackgroundColorModel.TURQUOISE_INDEX);
            break;
        case R.id.BackgroundSettingYellow:
        	_BackgroundColorModel.setBackgroundIndex(BackgroundColorModel.YELLOW_INDEX);
            break;
        case R.id.BackgroundSettingGrey:
        	_BackgroundColorModel.setBackgroundIndex(BackgroundColorModel.GREY_INDEX);
            break;
        case R.id.BackgroundSettingWhite:
        	_BackgroundColorModel.setBackgroundIndex(BackgroundColorModel.WHITE_INDEX);
            break;
        }
		
	}
    private void dataExchangeToControls()
    {
    	int color = _BackgroundColorModel.getBackgroundIndex();
    	int radioButtonId = R.id.BackgroundSettingSkyBlue;
    	switch (color)
    	{
		case BackgroundColorModel.DARK_PINK_INDEX:
			radioButtonId = R.id.BackgroundSettingDarkPink;
			break;
		case BackgroundColorModel.LIGHT_PINK_INDEX:
			radioButtonId = R.id.BackgroundSettingLightPink;
			break;
		case BackgroundColorModel.TURQUOISE_INDEX:
			radioButtonId = R.id.BackgroundSettingTurquoise;
			break;
		case BackgroundColorModel.YELLOW_INDEX:
			radioButtonId = R.id.BackgroundSettingYellow;
			break;
		case BackgroundColorModel.GREY_INDEX:
			radioButtonId = R.id.BackgroundSettingGrey;
			break;
		case BackgroundColorModel.WHITE_INDEX:
			radioButtonId = R.id.BackgroundSettingWhite;
			break;
    	}
    	RadioButton radioButton = (RadioButton) getActivity().findViewById(radioButtonId);
    	radioButton.setChecked(true);
    }
}
