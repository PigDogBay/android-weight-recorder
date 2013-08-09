/**
 * 
 */
package com.pigdogbay.weightrecorder.test;

import java.util.Calendar;
import java.util.Date;

import com.pigdogbay.androidutils.utils.PreferencesHelper;
import com.pigdogbay.weightrecorder.model.MainModel;

import android.test.AndroidTestCase;

/**
 * @author mark.bailey
 *
 */
public class MainModelTest extends AndroidTestCase {

	private boolean testGetRemoveAds(boolean disableBanners, int daysSincePurchase)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -daysSincePurchase);
		Date purchaseDate = cal.getTime();
		PreferencesHelper prefs = new PreferencesHelper(this.mContext);
		prefs.setBoolean(com.pigdogbay.weightrecorder.R.string.code_pref_disable_ads_key, disableBanners);
		prefs.setLong(com.pigdogbay.weightrecorder.R.string.code_pref_purchase_date, purchaseDate.getTime());
		MainModel target = new MainModel(this.mContext);
		return target.getRemoveAds();
	}
	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.MainModel#getRemoveAds()}.
	 */
	public void testGetRemoveAds() {
		assertTrue(testGetRemoveAds(false, 2));
		assertFalse(testGetRemoveAds(false, 10));
		assertTrue(testGetRemoveAds(true, 2));
		assertTrue(testGetRemoveAds(true, 10));
	}
}
