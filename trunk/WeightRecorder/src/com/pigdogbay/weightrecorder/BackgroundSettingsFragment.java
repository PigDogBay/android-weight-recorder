package com.pigdogbay.weightrecorder;

import com.pigdogbay.weightrecorder.model.MainModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BackgroundSettingsFragment extends Fragment {
	MainModel _MainModel;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_background_setting, container, false);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	_MainModel = new MainModel(getActivity());
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
    }
    private void dataExchangeToModel()
    {
    }

}
