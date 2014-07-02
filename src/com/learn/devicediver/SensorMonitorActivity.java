package com.learn.devicediver;

import java.io.ByteArrayInputStream;

import com.learn.utl.IOWrite;
import com.learn.utl.StorageWrite;
import com.learn.wifilistview.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SensorMonitorActivity extends Activity implements OnClickListener,
		SensorEventListener {

	TextView output;
	ToggleButton monitor;
	Button clear;
	Button save;
	SensorManager sManager;
	String sName;
	ScrollView mScroll;
	int sType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_monitor);

		sName = getIntent().getStringExtra("Sensor_Name");
		sType = getIntent().getIntExtra("Sensor_Type", -100);

		setTitle(sName + "Monitor");

		sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		output = (TextView) findViewById(R.id.sensor_output);
		monitor = (ToggleButton) findViewById(R.id.sensor_monitor);
		clear = (Button) findViewById(R.id.sensor_clear);
		save = (Button) findViewById(R.id.save_sensor_file);
		mScroll = (ScrollView) findViewById(R.id.scrollView1);

		monitor.setOnClickListener(this);
		clear.setOnClickListener(this);
		save.setOnClickListener(this);

	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sManager.unregisterListener(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		boolean chk = monitor.isChecked();
		outState.putCharSequence("out", output.getText());
		outState.putBoolean("monitor", chk);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.sensor_clear) {
			output.setText("");
		} else if (v.getId() == R.id.sensor_monitor) {
			if (((ToggleButton) v).isChecked()) {
				sManager.registerListener(SensorMonitorActivity.this,
						sManager.getDefaultSensor(sType),
						SensorManager.SENSOR_DELAY_NORMAL);
			} else
				sManager.unregisterListener(SensorMonitorActivity.this);
		} else if (v.getId() == R.id.save_sensor_file) {
			IOWrite ioWrite = new IOWrite(sName, ".txt", new ByteArrayInputStream(
					output.getText().toString().getBytes()), "Device Diver", true);
			StorageWrite sw = new StorageWrite(this);
			sw.execute(ioWrite);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor s, int acc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		String data = "";
		for (float val : event.values) {
			data += val + ", ";
		}

		output.append(data + "$\n");
		mScroll.fullScroll(ScrollView.FOCUS_DOWN);

	}

}
