package com.learn.devicediver;

import com.learn.wifilistview.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity implements OnClickListener {

	private String[] names = new String[] { "Wifi", "Sensors", "Cellular " };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, names);
		setListAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.wifi_select) {
			Intent mIntent = new Intent(this, WifiActivity.class);
			startActivity(mIntent);
		} else if (v.getId() == R.id.sensor_select) {
			Intent mIntent = new Intent(this, SensorsActivity.class);
			startActivity(mIntent);
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent mIntent;
		if (position == 0) {// i.e Wifi
			mIntent = new Intent(this, WifiActivity.class);
			startActivity(mIntent);
		} else if (position == 1) { // i.e sensors-
			mIntent = new Intent(this, SensorsActivity.class);
			startActivity(mIntent);
		} else if (position == 2) {// i.e cellular
			mIntent = new Intent(this, CellularActivity.class);
			startActivity(mIntent);
		}

	}

}
