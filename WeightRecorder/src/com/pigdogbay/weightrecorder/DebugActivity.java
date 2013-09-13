package com.pigdogbay.weightrecorder;

import java.util.Date;

import com.pigdogbay.androidutils.iab.IabHelper;
import com.pigdogbay.androidutils.iab.IabResult;
import com.pigdogbay.androidutils.iab.Inventory;
import com.pigdogbay.androidutils.iab.Purchase;
import com.pigdogbay.androidutils.mvp.IAdModel;
import com.pigdogbay.androidutils.mvp.IBackgroundColorModel;
import com.pigdogbay.androidutils.utils.PreferencesHelper;
import com.pigdogbay.weightrecorder.model.AppPurchases;
import com.pigdogbay.weightrecorder.model.MainModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class DebugActivity extends Activity implements IabHelper.OnIabPurchaseFinishedListener,IabHelper.QueryInventoryFinishedListener, IabHelper.OnIabSetupFinishedListener{

	CheckBox _ChBxLargeDictionary,_ChBxColorPack, _ChBxDisableAds;
	TextView _TextLog;
	MainModel _MainModel;
	AppPurchases _AppPurchases;
	IabHelper _Helper;
	Inventory _Inventory = null;
	IAdModel _AdModel;
	IBackgroundColorModel _BackgroundColorModel;
	// (arbitrary) request code for the purchase flow
	static final int PURCHASE_REQUEST = 10001;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);
		_MainModel = new MainModel(this);
		_AdModel = _MainModel.createAdModel();
		_BackgroundColorModel = _MainModel.createBackgroundColorModel();
		_AppPurchases = new AppPurchases(this);
		_ChBxColorPack = (CheckBox) findViewById(R.id.dbgChkBxColorPackPurchase);
		_ChBxDisableAds = (CheckBox) findViewById(R.id.dbgChkBxRemoveAdsPurchase);
		_TextLog = (TextView) findViewById(R.id.dbgTxtLog); 
		wireUpControls();
		_ChBxColorPack.setChecked(_BackgroundColorModel.getUnlockColorPack());
		_ChBxDisableAds.setChecked(_AdModel.getDisableAds());
		_TextLog.setText("");
		_Helper = new IabHelper(this, AppPurchases.getPublicKey());
		_Helper.startSetup(this);
		long purchaseTime = _MainModel.getPreferencesHelper().getLong(R.string.code_pref_purchase_date,0L);
		log("Purchase Date");
		log(DateFormat.format("dd MMM yyyy,  HH:mm",purchaseTime).toString());
	}
	
	private void wireUpControls()
	{
		((Button) findViewById(R.id.dbgBtnConsumeDisableAds)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				consumeItem(AppPurchases.SKU_DISABLE_ADS);
			}
		});
		((Button) findViewById(R.id.dbgBtnConsumeColorPack)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				consumeItem(AppPurchases.SKU_COLOR_PACK);
			}
		});
		((Button) findViewById(R.id.dbgBtnResetPurchaseDate)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_AdModel.setPurchaseDate(0L);
			}
		});
		((Button) findViewById(R.id.dbgBtnMinusTenDays)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				long time = new Date().getTime();
				time = time-10L*24L*60L*60L*1000L;
				_AdModel.setPurchaseDate(time);
			}
		});
		((Button) findViewById(R.id.dbgBtnSetPurchaseDate)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_AdModel.setPurchaseDate(new Date().getTime());
			}
		});
		((Button) findViewById(R.id.dbgBtnTestPurchaseBuy)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				buy(AppPurchases.SKU_TEST);
			}
		});
		((Button) findViewById(R.id.dbgBtnTestPurchaseConsume)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				consumeItem(AppPurchases.SKU_TEST);
			}
		});
		((Button) findViewById(R.id.dbgBtnBroadcastColor)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_AppPurchases.broadcastPurchase(AppPurchases.SKU_COLOR_PACK);
			}
		});
		_ChBxColorPack.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_BackgroundColorModel.setUnlockColorPack(isChecked);
			}
		});
		_ChBxDisableAds.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_AdModel.setDisableAds(isChecked);
			}
		});
		
	}
	void log(String msg)
	{
		_TextLog.append("\n");
		_TextLog.append(msg);
	}
	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton(getString(R.string.ok), null);
		bld.create().show();
	}
	@Override
	public void onIabSetupFinished(IabResult result) {
		if (!result.isSuccess()) {
			alert(getString(R.string.shop_in_app_billing_not_supported));
			return;
		}
		log("Loading Inventory...");
		_Helper.queryInventoryAsync(true, AppPurchases.getAppSkus(), this);
	}
	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
		if (result.isFailure()) {
			log("Unable to load inventory");
			return;
		}
		_Inventory = inventory;
		log("...Inventory done.");
		log("Purchased Items:");
		for (String sku : _Inventory.getSKUs())
		{
			if (_Inventory.hasPurchase(sku))
			{
				log(sku);
			}
		}
	}
	
	
	private void consumeItem(String sku)
	{
		if (_Inventory == null || !_Inventory.hasPurchase(sku)) {
			alert("No Purchase To Consume");
			return;
		}
		_Helper.consumeAsync(_Inventory.getPurchase(sku),
				new IabHelper.OnConsumeFinishedListener() {
					@Override
					public void onConsumeFinished(Purchase purchase,
							IabResult result) {
						String msg = result.isSuccess() ? "Purchased Consumed"
								: "Unable To Consume";
						alert(msg);
					}
				});
		
	}
	private void buy(String sku) {
		// Ignore payload for now, need a server for it to be effective
		String payload = "";
		_Helper.launchPurchaseFlow(this, sku, PURCHASE_REQUEST, this, payload);
	}	
	@Override
	public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
		if (result.isFailure()) {
			alert(getString(R.string.shop_purchase_fail));
			return;
		}
		_Helper.queryInventoryAsync(true, AppPurchases.getAppSkus(), this);
		Toast.makeText(DebugActivity.this, getString(R.string.shop_thankyou), Toast.LENGTH_LONG)
				.show();
	}
	
	
}
