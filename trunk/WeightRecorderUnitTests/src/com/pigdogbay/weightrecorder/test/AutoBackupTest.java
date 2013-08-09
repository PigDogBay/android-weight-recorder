/**
 * 
 */
package com.pigdogbay.weightrecorder.test;

import java.util.Calendar;
import java.util.Date;

import android.test.AndroidTestCase;

import com.pigdogbay.androidutils.utils.PreferencesHelper;
import com.pigdogbay.weightrecorder.model.AutoBackup;

/**
 * @author mark.bailey
 *
 */
public class AutoBackupTest extends AndroidTestCase {

	/**
	 * Last date is 0
	 * Test method for {@link com.pigdogbay.weightrecorder.model.AutoBackup#isBackupDue(long)}.
	 */
	public void testIsBackupDue1() {
		PreferencesHelper preferencesHelper = new PreferencesHelper(mContext);
		AutoBackup target = new AutoBackup(preferencesHelper);
		target.setBackupDate(new Date(0L));
		assertTrue(target.isBackupDue(1));
		
	}
	/**
	 * Last date is now
	 * Test method for {@link com.pigdogbay.weightrecorder.model.AutoBackup#isBackupDue(long)}.
	 */
	public void testIsBackupDue2() {
		PreferencesHelper preferencesHelper = new PreferencesHelper(mContext);
		AutoBackup target = new AutoBackup(preferencesHelper);
		target.setBackupDate(new Date());
		assertFalse(target.isBackupDue(1));
	}
	/**
	 * Typical use
	 * Test method for {@link com.pigdogbay.weightrecorder.model.AutoBackup#isBackupDue(long)}.
	 */
	public void testIsBackupDue3() {
		PreferencesHelper preferencesHelper = new PreferencesHelper(mContext);
		AutoBackup target = new AutoBackup(preferencesHelper);
		//few days ago
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-6);
		target.setBackupDate(cal.getTime());
		assertFalse(target.isBackupDue(7));
	}
	/**
	 * Typical use
	 * Test method for {@link com.pigdogbay.weightrecorder.model.AutoBackup#isBackupDue(long)}.
	 */
	public void testIsBackupDue4() {
		PreferencesHelper preferencesHelper = new PreferencesHelper(mContext);
		AutoBackup target = new AutoBackup(preferencesHelper);
		//few days ago
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-8);
		target.setBackupDate(cal.getTime());
		assertTrue(target.isBackupDue(7));
	}

}
