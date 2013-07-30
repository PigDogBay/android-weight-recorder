package com.pigdogbay.weightrecorder.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pigdogbay.androidutils.iab.IabHelper;
import com.pigdogbay.androidutils.iab.IabResult;
import com.pigdogbay.androidutils.iab.Inventory;
import com.pigdogbay.weightrecorder.R;
import com.pigdogbay.weightrecorder.ShopActivity;

public class AppPurchases {
	public static final String SKU_DISABLE_ADS = "disable_ads";

	private static final String _base64EncodedPublicKey1 = "MIIBIjANBgkqhkiG";
	private static final String _base64EncodedPublicKey2 = "9w0BAQEFAAOCAQ8A";
	private static final String _base64EncodedPublicKey3 = "MIIBCgKCAQEAtj5n";
	private static final String _base64EncodedPublicKey4 = "ISgUocwBriIuzxAB";
	private static final String _base64EncodedPublicKey5 = "KLkNJmy1nKi5rj18";
	private static final String _base64EncodedPublicKey6 = "gb/Cm0S9i6bNi4uo";
	private static final String _base64EncodedPublicKey7 = "4+fu4XJKkBRYSgXS";
	private static final String _base64EncodedPublicKey8 = "cCIDVO+Tb9GCKupi";
	private static final String _base64EncodedPublicKey9 = "Uk7+o/xzxoOYYUiW";
	private static final String _base64EncodedPublicKey10 = "22wbLU3fbTG5Wwct";
	private static final String _base64EncodedPublicKey11 = "q2QyMnAgBDmDrn53";
	private static final String _base64EncodedPublicKey12 = "k81vR0xBwWaqsIOm";
	private static final String _base64EncodedPublicKey13 = "5GvKq46yNS4RWG+M";
	private static final String _base64EncodedPublicKey14 = "7/vcw22f7MqpFpzF";
	private static final String _base64EncodedPublicKey15 = "gWPxLPC42uU3u+C5";
	private static final String _base64EncodedPublicKey16 = "+AInt5MXKHsaeoHD";
	private static final String _base64EncodedPublicKey17 = "grax1m2eyurnbI6s";
	private static final String _base64EncodedPublicKey18 = "dJ3YseMQvjfJNHxb";
	private static final String _base64EncodedPublicKey19 = "JjeXMBNR82Hzg17R";
	private static final String _base64EncodedPublicKey20 = "IQIs+PayBjByu5Ko";
	private static final String _base64EncodedPublicKey21 = "c4depb9hwqf/c4PO";
	private static final String _base64EncodedPublicKey22 = "bumcemSpmHEjFJ6g";
	private static final String _base64EncodedPublicKey23 = "bPyIMzPpKZGGqfG4";
	private static final String _base64EncodedPublicKey24 = "RYZ8yE50+APLmt4k";
	private static final String _base64EncodedPublicKey25 = "swIDAQAB";

	public static String getPublicKey() {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(_base64EncodedPublicKey1);
		sbuffer.append(_base64EncodedPublicKey2);
		sbuffer.append(_base64EncodedPublicKey3);
		sbuffer.append(_base64EncodedPublicKey4);
		sbuffer.append(_base64EncodedPublicKey5);
		sbuffer.append(_base64EncodedPublicKey6);
		sbuffer.append(_base64EncodedPublicKey7);
		sbuffer.append(_base64EncodedPublicKey8);
		sbuffer.append(_base64EncodedPublicKey9);
		sbuffer.append(_base64EncodedPublicKey10);
		sbuffer.append(_base64EncodedPublicKey11);
		sbuffer.append(_base64EncodedPublicKey12);
		sbuffer.append(_base64EncodedPublicKey13);
		sbuffer.append(_base64EncodedPublicKey14);
		sbuffer.append(_base64EncodedPublicKey15);
		sbuffer.append(_base64EncodedPublicKey16);
		sbuffer.append(_base64EncodedPublicKey17);
		sbuffer.append(_base64EncodedPublicKey18);
		sbuffer.append(_base64EncodedPublicKey19);
		sbuffer.append(_base64EncodedPublicKey20);
		sbuffer.append(_base64EncodedPublicKey21);
		sbuffer.append(_base64EncodedPublicKey22);
		sbuffer.append(_base64EncodedPublicKey23);
		sbuffer.append(_base64EncodedPublicKey24);
		sbuffer.append(_base64EncodedPublicKey25);
		return sbuffer.toString();
	}

	Context _Context;
	IabHelper _Helper;

	public AppPurchases(Context context) {
		_Context = context;
	}

	public void SetPreferences(Inventory inventory) {

		PreferencesHelper prefHelper = new PreferencesHelper(_Context);
		if (inventory.hasDetails(SKU_DISABLE_ADS)) {
			prefHelper.setBoolean(R.string.code_pref_disable_ads_key,
					inventory.hasPurchase(SKU_DISABLE_ADS));
		}
	}

	public void QueryAsync() {
		_Helper = new IabHelper(_Context, getPublicKey());
		_Helper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (result.isSuccess()) {
					queryInventory();
				}
			}
		});
	}

	private void queryInventory() {
		List<String> moreSkus = new ArrayList<String>();
		moreSkus.add(SKU_DISABLE_ADS);
		_Helper.queryInventoryAsync(true, moreSkus,
				new IabHelper.QueryInventoryFinishedListener() {
					@Override
					public void onQueryInventoryFinished(IabResult result,
							Inventory inv) {
						try {
							if (result.isSuccess()) {
								SetPreferences(inv);
							}
						}
						finally {
							_Helper.dispose();
							_Helper = null;
						}
					}
				});

	}

}
