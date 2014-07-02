package com.learn.devicediver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SensorsActivity extends ListActivity {

	SensorManager sManager;
	HashMap<String, Sensor> sMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		GetSensors getSensors = new GetSensors();
		getSensors.execute();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		Intent mIntent = new Intent(this, SensorMonitorActivity.class);
		mIntent.putExtra("Sensor_Type", sMap.get(l.getItemAtPosition(position))
				.getType());
		mIntent.putExtra("Sensor_Name", (String) l.getItemAtPosition(position));
		startActivity(mIntent);

	}

	private class GetSensors extends
			AsyncTask<Void, Void, HashMap<String, Sensor>> {

		private List<String> sensorsNames = new ArrayList<String>();

		@Override
		protected HashMap<String, Sensor> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HashMap<String, Sensor> sMap = new HashMap<String, Sensor>();
			for (Sensor sensor : sManager.getSensorList(Sensor.TYPE_ALL)) {
				String name = sensor.getName();
				sensorsNames.add(name);
				sMap.put(name, sensor);
			}

			return sMap;
		}

		@Override
		protected void onPostExecute(HashMap<String, Sensor> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			setListAdapter(new SensorArrayAdapter(SensorsActivity.this,
					android.R.layout.simple_list_item_1, sensorsNames, result));

			sMap = result;
		}

		private class SensorArrayAdapter extends ArrayAdapter<String> {

			public SensorArrayAdapter(Context context, int resource,
					String[] objects, HashMap<String, Sensor> sensorsMap) {
				super(context, resource, objects);
				// TODO Auto-generated constructor stub
			}

			public SensorArrayAdapter(Context context, int resource,
					List<String> objects, HashMap<String, Sensor> sensorsMap) {
				super(context, resource, objects);
				// TODO Auto-generated constructor stub
			}

		}

	}

}
