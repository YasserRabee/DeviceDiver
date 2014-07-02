package com.learn.devicediver;

import com.learn.wifilistview.R;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class WifiDetails extends Activity {

	Bundle mBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_details);
		mBundle = getIntent().getExtras();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		((TextView) findViewById(R.id.wifi_ssid)).setText(mBundle
				.getString("SSID"));
		((TextView) findViewById(R.id.wifi_bssid)).setText(mBundle
				.getString("BSSID"));
		((TextView) findViewById(R.id.wifi_level)).setText(mBundle
				.getInt("LEVEL") + " dBm");
		((TextView) findViewById(R.id.wifi_cap)).setText(mBundle
				.getString("CAP"));

	}

}
