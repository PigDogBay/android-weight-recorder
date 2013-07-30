package com.pigdogbay.weightrecorder;

import java.util.ArrayList;
import java.util.List;

import com.pigdogbay.androidutils.iab.IabHelper;
import com.pigdogbay.androidutils.iab.IabResult;
import com.pigdogbay.androidutils.iab.Inventory;
import com.pigdogbay.androidutils.iab.Purchase;
import com.pigdogbay.androidutils.iab.SkuDetails;
import com.pigdogbay.weightrecorder.model.AppPurchases;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
    
    private void addPurchasedItem(String title, String description)
    {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int margin =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, metrics);

		LinearLayout layout = new LinearLayout(this);
		LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(layoutParams);
		
		TextView titleView = new TextView(this);
		titleView.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,1f));
		titleView.setText(title);
		titleView.setTextAppearance(this, android.R.style.TextAppearance_Large);
		titleView.setTypeface(null,Typeface.BOLD);

		TextView purchasedView = new TextView(this);
		purchasedView.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,1f));
		purchasedView.setText("Purchased");
		purchasedView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
		purchasedView.setGravity(Gravity.RIGHT);

		TextView descriptionView = new TextView(this);
		layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, 0, 0, margin);
		descriptionView.setLayoutParams(layoutParams);
		descriptionView.setText(description);
		descriptionView.setTextAppearance(this, android.R.style.TextAppearance_Small);
				
		layout.addView(titleView);
		layout.addView(purchasedView);
		ViewGroup parent = (ViewGroup)this.findViewById(R.id.shopSaleItemsLayout);
		parent.addView(layout);
		parent.addView(descriptionView);
    	
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
    void complain(String message) {
        Log.e(TAG, message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }
    void addSaleItems(Inventory inventory)
    {
    	List<String> skus = inventory.getSKUs();
    	for (String sku : skus)
    	{
    		SkuDetails details = inventory.getSkuDetails(sku);
    		if (!inventory.hasPurchase(sku)){
    			addSaleItem(details.getTitle(), details.getPrice(), details.getDescription(), sku);
    		}
    		else
    		{
//    			_Helper.consumeAsync(inventory.getPurchase(sku), new IabHelper.OnConsumeFinishedListener() {
//					
//					@Override
//					public void onConsumeFinished(Purchase purchase, IabResult result) {
//						// TODO Auto-generated method stub
//						Log.v(TAG, "Consumed Purchase");
//						Log.v(TAG, result.getMessage());
//						
//					}
//				});
    			addPurchasedItem(details.getTitle(), details.getDescription());
    		}
    	}
    }
    
    private void refreshInventory()
    {
		ViewGroup parent = (ViewGroup)this.findViewById(R.id.shopSaleItemsLayout);
		parent.removeAllViews();
		List<String> moreSkus = new ArrayList<String>();
		moreSkus.add(SKU_DISABLE_ADS);
		_Helper.queryInventoryAsync(true,moreSkus,_GotInventoryListener);

    }

	private void setUpIAB() {
		// compute your public key and store it in base64EncodedPublicKey
		_Helper = new IabHelper(this, AppPurchases.getPublicKey());
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
				refreshInventory();
			}
		});
	}
	  // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener _GotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }
            addSaleItems(inventory);
            Log.d(TAG, "Query inventory was successful.");
            
        }};
            
	private void buy(String sku) {
		// Ignore payload for now, need a server for it to be effective
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
			Log.d(TAG, "Purchase successful.");
			refreshInventory();
			if (purchase.getSku().equals(SKU_TEST)) {
				Toast.makeText(ShopActivity.this, "Thankyou!",
						Toast.LENGTH_LONG).show();
			}
		}
	};

}
