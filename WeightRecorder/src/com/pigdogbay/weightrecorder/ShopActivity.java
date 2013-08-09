package com.pigdogbay.weightrecorder;

import java.util.List;

import com.pigdogbay.androidutils.iab.IabHelper;
import com.pigdogbay.androidutils.iab.IabResult;
import com.pigdogbay.androidutils.iab.Inventory;
import com.pigdogbay.androidutils.iab.Purchase;
import com.pigdogbay.androidutils.iab.SkuDetails;
import com.pigdogbay.androidutils.utils.PreferencesHelper;
import com.pigdogbay.weightrecorder.model.AppPurchases;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

public class ShopActivity extends Activity implements
		IabHelper.OnIabPurchaseFinishedListener, IabHelper.QueryInventoryFinishedListener, IabHelper.OnIabSetupFinishedListener {

	IabHelper _Helper;
	Inventory _Inventory = null;
	// (arbitrary) request code for the purchase flow
	static final int PURCHASE_REQUEST = 10001;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		ActivitiesHelper.setBackground(this);
		setWaitScreen(true);
		_Helper = new IabHelper(this, AppPurchases.getPublicKey());
		_Helper.startSetup(this);
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
		findViewById(R.id.shopSaleItemsLayout).setVisibility(
				set ? View.GONE : View.VISIBLE);
		findViewById(R.id.shopWait).setVisibility(
				set ? View.VISIBLE : View.GONE);
	}

	private void addPurchasedItem(String title, String description) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 16f, metrics);

		LinearLayout layout = new LinearLayout(this);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(layoutParams);

		TextView titleView = new TextView(this);
		titleView.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1f));
		titleView.setText(title);
		titleView.setTextAppearance(this, android.R.style.TextAppearance_Large);
		titleView.setTypeface(null, Typeface.BOLD);

		TextView purchasedView = new TextView(this);
		purchasedView.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1f));
		purchasedView.setText("Purchased");
		purchasedView.setTextAppearance(this,
				android.R.style.TextAppearance_Medium);
		purchasedView.setGravity(Gravity.RIGHT);

		TextView descriptionView = new TextView(this);
		layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, 0, 0, margin);
		descriptionView.setLayoutParams(layoutParams);
		descriptionView.setText(description);
		descriptionView.setTextAppearance(this,
				android.R.style.TextAppearance_Small);

		layout.addView(titleView);
		layout.addView(purchasedView);
		ViewGroup parent = (ViewGroup) this
				.findViewById(R.id.shopSaleItemsLayout);
		parent.addView(layout);
		parent.addView(descriptionView);

	}

	private void addSaleItem(String title, String price, String description,
			String sku) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 16f, metrics);

		LinearLayout layout = new LinearLayout(this);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(layoutParams);

		TextView titleView = new TextView(this);
		titleView.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 3f));
		titleView.setText(title);
		titleView.setTextAppearance(this, android.R.style.TextAppearance_Large);
		titleView.setTypeface(null, Typeface.BOLD);

		TextView priceView = new TextView(this);
		priceView.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1f));
		priceView.setText(price);
		priceView
				.setTextAppearance(this, android.R.style.TextAppearance_Medium);

		Button buyButton = new Button(this);
		buyButton.setLayoutParams(new LinearLayout.LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1f));
		buyButton.setText("Buy");
		buyButton.setTag(sku);
		buyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String sku = (String) v.getTag();
				buy(sku);

			}
		});

		TextView descriptionView = new TextView(this);
		layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, 0, 0, margin);
		descriptionView.setLayoutParams(layoutParams);
		descriptionView.setText(description);
		descriptionView.setTextAppearance(this,
				android.R.style.TextAppearance_Small);

		layout.addView(titleView);
		layout.addView(priceView);
		layout.addView(buyButton);
		ViewGroup parent = (ViewGroup) this
				.findViewById(R.id.shopSaleItemsLayout);
		parent.addView(layout);
		parent.addView(descriptionView);
	}

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton(getString(R.string.ok), null);
		bld.create().show();
	}

	void addSaleItems(Inventory inventory) {
		List<String> skus = inventory.getSKUs();
		for (String sku : skus) {
			SkuDetails details = inventory.getSkuDetails(sku);
			if (inventory.hasPurchase(sku)) {
				addPurchasedItem(details.getTitle(), details.getDescription());
			}
			else {
				addSaleItem(details.getTitle(), details.getPrice(),
						details.getDescription(), sku);
			}
		}
	}

	private void refreshInventory() {
		ViewGroup parent = (ViewGroup) this
				.findViewById(R.id.shopSaleItemsLayout);
		parent.removeAllViews();
		_Helper.queryInventoryAsync(true, AppPurchases.getAppSkus(), this);

	}

	private void buy(String sku) {
		// Ignore payload for now, need a server for it to be effective
		String payload = "";
		setWaitScreen(true);
		_Helper.launchPurchaseFlow(this, sku, PURCHASE_REQUEST, this, payload);
	}

	/**
	 * Debug code to test purchases
	 * @param sku
	 */
	private void consumeItem(String sku) {
		if (_Inventory == null || !_Inventory.hasPurchase(sku)) {
			Toast.makeText(ShopActivity.this, "No Purchase To Consume",
					Toast.LENGTH_SHORT).show();
			return;
		}
		_Helper.consumeAsync(_Inventory.getPurchase(sku),
				new IabHelper.OnConsumeFinishedListener() {
					@Override
					public void onConsumeFinished(Purchase purchase,
							IabResult result) {
						String toast = result.isSuccess() ? "Purchased Consumed"
								: "Unable To Consume";
						Toast.makeText(ShopActivity.this, toast,
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Handle any billing results
		if (!_Helper.handleActivityResult(requestCode, resultCode, data)) {
			//Result not handled so pass it on or deal with it
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	@Override
	public void onIabSetupFinished(IabResult result) {
		setWaitScreen(false);
		if (!result.isSuccess()) {
			alert(getString(R.string.shop_in_app_billing_not_supported));
			return;
		}
		refreshInventory();
	}
	@Override
	public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
		if (result.isFailure()) {
			alert(getString(R.string.shop_failed_to_contact_google_play));
			return;
		}
		_Inventory = inventory;
		AppPurchases appPurchases = new AppPurchases(this);
		appPurchases.SetPreferences(inventory);
		addSaleItems(inventory);
	}
	@Override
	public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
		setWaitScreen(false);
		if (result.isFailure()) {
			alert(getString(R.string.shop_purchase_fail));
			return;
		}
		refreshInventory();
		Toast.makeText(ShopActivity.this, getString(R.string.shop_thankyou), Toast.LENGTH_LONG)
				.show();
	}
}
