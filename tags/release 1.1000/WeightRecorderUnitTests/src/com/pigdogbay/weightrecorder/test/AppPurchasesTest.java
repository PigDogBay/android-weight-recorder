/**
 * 
 */
package com.pigdogbay.weightrecorder.test;

import com.pigdogbay.weightrecorder.model.AppPurchases;

import junit.framework.TestCase;

/**
 * @author mark.bailey
 *
 */
public class AppPurchasesTest extends TestCase {

	String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtj5nISgUocwBriIuzxABKLkNJmy1nKi5rj18gb/Cm0S9i6bNi4uo4+fu4XJKkBRYSgXScCIDVO+Tb9GCKupiUk7+o/xzxoOYYUiW22wbLU3fbTG5Wwctq2QyMnAgBDmDrn53k81vR0xBwWaqsIOm5GvKq46yNS4RWG+M7/vcw22f7MqpFpzFgWPxLPC42uU3u+C5+AInt5MXKHsaeoHDgrax1m2eyurnbI6sdJ3YseMQvjfJNHxbJjeXMBNR82Hzg17RIQIs+PayBjByu5Koc4depb9hwqf/c4PObumcemSpmHEjFJ6gbPyIMzPpKZGGqfG4RYZ8yE50+APLmt4kswIDAQAB";

	/**
	 * Test method for {@link com.pigdogbay.weightrecorder.model.AppPurchases#getPublicKey()}.
	 */
	public void testGetPublicKey() {
		
		String actual = AppPurchases.getPublicKey();
		assertEquals(base64EncodedPublicKey, actual);
	}

}
