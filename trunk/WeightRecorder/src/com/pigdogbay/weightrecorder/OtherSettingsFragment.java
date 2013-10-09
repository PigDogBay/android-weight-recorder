package com.pigdogbay.weightrecorder;

import com.pigdogbay.weightrecorder.model.MainModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class OtherSettingsFragment extends Fragment {
	MainModel _MainModel;
	CheckBox _ChkBxShowTrendLine,_ChkBxShowTargetLine,_ChkBxAutoBackup;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other_settings, container, false);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	_MainModel = new MainModel(getActivity());
    	_ChkBxAutoBackup = (CheckBox)getActivity().findViewById(R.id.OtherSettingsBackupChkBx);
    	_ChkBxShowTargetLine = (CheckBox)getActivity().findViewById(R.id.OtherSettingsTargetLineChkBx);
    	_ChkBxShowTrendLine = (CheckBox)getActivity().findViewById(R.id.OtherSettingsTrendLineChkBx);
    	dataExchangeToControls();
    }
    
    /* 
     * Persist any changes here
     */
    @Override
    public void onPause() {
    	super.onPause();
    	dataExchangeToModel();
    }    
    private void dataExchangeToControls()
    {
    	_ChkBxShowTargetLine.setChecked(_MainModel.getShowTargetLine());
    	_ChkBxShowTrendLine.setChecked(_MainModel.getShowTrendLine());
    	_ChkBxAutoBackup.setChecked(_MainModel.getIsAutoBackupEnabled());
    }
    private void dataExchangeToModel()
    {
    	if (_MainModel.getShowTrendLine()!=_ChkBxShowTrendLine.isChecked())	{
    		_MainModel.setShowTrendLine(_ChkBxShowTrendLine.isChecked());
    	}
    	if (_MainModel.getShowTargetLine()!=_ChkBxShowTargetLine.isChecked()){
    		_MainModel.setShowTargetLine(_ChkBxShowTargetLine.isChecked());
    	}
    	if(_MainModel.getIsAutoBackupEnabled()!=_ChkBxAutoBackup.isChecked()){
    		_MainModel.setIsAutoBackupEnabled(_ChkBxAutoBackup.isChecked());
    	}
    }
}
