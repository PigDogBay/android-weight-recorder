package com.pigdogbay.weightrecorder;

import com.pigdogbay.androidutils.iab.IabHelper;
import com.pigdogbay.androidutils.iab.IabResult;
import com.pigdogbay.androidutils.iab.Purchase;
import com.pigdogbay.androidutils.iab.SkuDetails;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopActivity extends Activity {

	IabHelper _Helper;
	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;

	static final String SKU_TEST = "android.test.purchased";
	static final String SKU_DISABLE_ADS = "disable_ads";
	static final String TAG = "WeightRecorder";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);

		setWaitScreen(true);
		setUpIAB();
		addSaleItem("Test Item", "$0.99", "Static Test Item", SKU_TEST);
		addSaleItem("Disable Ads", "$0.99", "Remove all ads from the app", SKU_DISABLE_ADS);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (_Helper != null) {
			_Helper.dispose();
		}
		_Helper = null;

	}
    void setWaitScreen(boolean set) {
        findViewById(R.id.shopSaleItemsLayout).setVisibility(set ? View.GONE : View.VISIBLE);
        findViewById(R.id.shopWait).setVisibility(set ? View.VISIBLE : View.GONE);
    }
	
	private void addSaleItem(String title, String price, String description, String sku)
	{
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int margin =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, metrics);

		LinearLayout layout = new LinearLayout(this);
		LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(layoutParams);
		
		TextView titleView = new TextView(this);
		titleView.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,3f));
		titleView.setText(title);
		titleView.setTextAppearance(this, android.R.style.TextAppearance_Large);
		titleView.setTypeface(null,Typeface.BOLD);

		TextView priceView = new TextView(this);
		priceView.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,1f));
		priceView.setText(price);
		priceView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
		
		Button buyButton  = new Button(this);
		buyButton.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,1f));
		buyButton.setText("Buy");
		buyButton.setTag(sku);
		buyButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String sku = (String)v.getTag();
				buy(sku);
				
			}
		});

		TextView descriptionView = new TextView(this);
		layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, 0, 0, margin);
		descriptionView.setLayoutParams(layoutParams);
		descriptionView.setText(description);
		descriptionView.setTextAppearance(this, android.R.style.TextAppearance_Small);
		
		
		layout.addView(titleView);
		layout.addView(priceView);
		layout.addView(buyButton);
		ViewGroup parent = (ViewGroup)this.findViewById(R.id.shopSaleItemsLayout);
		parent.addView(layout);
		parent.addView(descriptionView);
	}

	private void setUpIAB() {
		// !SECURITY!
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtj5nISgUocwBriIuzxABKLkNJmy1nKi5rj18gb/Cm0S9i6bNi4uo4+fu4XJKkBRYSgXScCIDVO+Tb9GCKupiUk7+o/xzxoOYYUiW22wbLU3fbTG5Wwctq2QyMnAgBDmDrn53k81vR0xBwWaqsIOm5GvKq46yNS4RWG+M7/vcw22f7MqpFpzFgWPxLPC42uU3u+C5+AInt5MXKHsaeoHDgrax1m2eyurnbI6sdJ3YseMQvjfJNHxbJjeXMBNR82Hzg17RIQIs+PayBjByu5Koc4depb9hwqf/c4PObumcemSpmHEjFJ6gbPyIMzPpKZGGqfG4RYZ8yE50+APLmt4kswIDAQAB";

		// compute your public key and store it in base64EncodedPublicKey
		_Helper = new IabHelper(this, base64EncodedPublicKey);
		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		_Helper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");
				setWaitScreen(false);
				if (!result.isSuccess()) {
					Toast.makeText(ShopActivity.this, "Unable to set up IAB.",
							Toast.LENGTH_LONG).show();
					return;
				}

				// Hooray, IAB is fully set up. Now, let's get an inventory of
				// stuff we own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				// mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}

	private void buy(String sku) {
		// !SECURITY!
		String payload = "";
		setWaitScreen(true);
		_Helper.launchPurchaseFlow(this, sku, RC_REQUEST,
				_PurchaseFinishedListener, payload);
	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!_Helper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }
	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct.
		 * It will be the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase
		 * and verifying it here might seem like a good approach, but this will
		 * fail in the case where the user purchases an item on one device and
		 * then uses your app on a different device, because on the other device
		 * you will not have access to the random string you originally
		 * generated.
		 * 
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different
		 * between them, so that one user's purchase can't be replayed to
		 * another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app
		 * wasn't the one who initiated the purchase flow (so that items
		 * purchased by the user on one device work on other devices owned by
		 * the user).
		 * 
		 * Using your own server to store and verify developer payloads across
		 * app installations is recommended.
		 */

		return true;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener _PurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);
			setWaitScreen(false);
			if (result.isFailure()) {
				Toast.makeText(ShopActivity.this, "Unable to purchase item.",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				Toast.makeText(ShopActivity.this, "Unable to verify item.",
						Toast.LENGTH_LONG).show();
				return;
			}

			Log.d(TAG, "Purchase successful.");

			if (purchase.getSku().equals(SKU_TEST)) {
				Toast.makeText(ShopActivity.this, "Thankyou!",
						Toast.LENGTH_LONG).show();
			}
		}
	};

}
