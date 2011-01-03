package com.brosch.wiiscale.test;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Simple test application to call an Intent for the WiiScale application and
 * show the result returned.
 * 
 * @author brandon broschinsky
 * 
 */
public class WiiScaleTest extends Activity {
	public static final int WEIGHT_REQUEST = 1;
	private TextView tv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tv = (TextView) findViewById(R.id.label);
	}

	/**
	 * Request the weight from the WiiScale application on the button press, if
	 * application not available directs user to market to download app
	 * 
	 * unit can be kg or lbs
	 * 
	 * @param v
	 */
	public void onCallGetWeight(View v) {
		Uri uri =  Uri.parse("weigh://broschb.wiiscale?unit=lbs");
		if (isIntentAvailable(this, "com.broschb.action.WEIGH",uri))
			startActivityForResult(new Intent("com.broschb.action.WEIGH",uri),WEIGHT_REQUEST);
		else {
			// direct to market
			String APP_MARKET_URL = "market://details?id=com.broschb.wiiscale";
			Intent intent = new Intent( Intent.ACTION_VIEW,
			Uri.parse(APP_MARKET_URL));
			startActivity(intent);
		}
	}

	/**
	 * Here get the result from the weight intent, and update the textview to
	 * display this value
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case WEIGHT_REQUEST:
			if (resultCode == RESULT_OK) {
				String unit = data.getStringExtra("unit");
				double weight = data.getDoubleExtra("weight", 0);
				unit = "Your weight is " + " " + weight + " " + unit;
				tv.setText(unit);
			}
		}
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * 
	 * adapted from
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 * 
	 * @param context
	 *            The application's environment.
	 * @param action
	 *            The Intent action to check for availability.
	 * 
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action, Uri uri) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action,uri);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
}