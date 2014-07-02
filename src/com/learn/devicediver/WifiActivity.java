package com.learn.devicediver;

import java.util.ArrayList;
import java.util.List;
import com.learn.wifilistview.R;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WifiActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private Button scan;
	private ListView resultsListView;
	GetWifiResults getResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_scanner);

		scan = (Button) findViewById(R.id.scan_wifi);
		scan.setOnClickListener(this);

		resultsListView = (ListView) findViewById(R.id.wifi_resultslist);
		resultsListView.setEmptyView(findViewById(R.id.no_result));
		resultsListView.setOnItemClickListener(WifiActivity.this);

		resultsListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.wifi_result, new ArrayList<String>()));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		getResults = new GetWifiResults();
		getResults.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent mIntent = new Intent(WifiActivity.this, WifiDetails.class);
		ScanResult result = (ScanResult) parent.getItemAtPosition(position);

		mIntent.putExtra("SSID", result.SSID);
		mIntent.putExtra("BSSID", result.BSSID);
		mIntent.putExtra("LEVEL", result.level);
		mIntent.putExtra("CAP", result.capabilities);

		startActivity(mIntent);
	}

	private class GetWifiResults extends
			AsyncTask<Void, ArrayList<ScanResult>, List<ScanResult>> {

		private boolean enabled = false;

		@SuppressLint("NewApi")
		@Override
		protected List<ScanResult> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			WifiManager wManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
				if (wManager.isScanAlwaysAvailable()) {
					enabled = true;
					if (wManager.startScan())
						return wManager.getScanResults();
				}

			if (wManager.isWifiEnabled()) {
				enabled = true;
				if (wManager.startScan()) {
					return wManager.getScanResults();
				} else
					this.cancel(true);
			} else
				this.cancel(true);

			return null;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			if (enabled)
				Toast.makeText(WifiActivity.this, "Error occured..",
						Toast.LENGTH_LONG).show();
			else {
				Toast.makeText(WifiActivity.this, "Enable Wifi First..",
						Toast.LENGTH_LONG).show();
				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
		}

		@Override
		protected void onPostExecute(List<ScanResult> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			resultsListView.setOnItemClickListener(WifiActivity.this);
			resultsListView.setAdapter(new ScanResultAdapter(WifiActivity.this,
					R.layout.wifi_result, result));
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		public class ScanResultAdapter extends ArrayAdapter<ScanResult> {

			public ScanResultAdapter(Context context, int textViewResourceId,
					List<ScanResult> objects) {
				super(context, textViewResourceId, objects);
				// TODO Auto-generated constructor stub
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ScanResult result = this.getItem(position);

				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View row = inflater
						.inflate(R.layout.wifi_result, parent, false);
				TextView tv = (TextView) row.findViewById(R.id.result_item);
				tv.setText(result.SSID);

								
				int str = WifiManager.calculateSignalLevel(result.level, 5);;

				if (str == 4)
					tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.p4,
							0, 0, 0);
				else if (str == 3)
					tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.p3,
							0, 0, 0);
				else if (str == 2)
					tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.p2,
							0, 0, 0);
				else if (str == 1)
					tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.p1,
							0, 0, 0);
				else
					tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.p0,
							0, 0, 0);

				return row;
			}

		}
	}

}
