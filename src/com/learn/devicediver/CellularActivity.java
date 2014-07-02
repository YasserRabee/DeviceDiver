package com.learn.devicediver;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.learn.utl.IOWrite;
import com.learn.utl.StorageWrite;
import com.learn.wifilistview.R;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CellularActivity extends Activity implements OnClickListener {

	TextView output;
	ToggleButton monitor;
	Button clear;
	Button save;
	GetCellular cell;
	ScrollView mScroll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cellular);

		output = (TextView) findViewById(R.id.cell_output);
		monitor = (ToggleButton) findViewById(R.id.cell_monitor);
		clear = (Button) findViewById(R.id.cell_clear);
		save = (Button) findViewById(R.id.save_cell_file);
		mScroll = (ScrollView) findViewById(R.id.scrollView2);

		monitor.setOnClickListener(this);
		clear.setOnClickListener(this);
		save.setOnClickListener(this);
	}

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

		if (v.getId() == R.id.cell_clear) {
			output.setText("");
		} else if (v.getId() == R.id.cell_monitor) {
			if (((ToggleButton) v).isChecked()) {
				cell = new GetCellular();
				cell.execute();
			} else
				cell.cancel(true);
		} else if (v.getId() == R.id.save_cell_file) {
			IOWrite ioWrite = new IOWrite("Cellular", ".txt", new ByteArrayInputStream(
					output.getText().toString().getBytes()), "Device Diver", true);
			StorageWrite sw = new StorageWrite(this);
			sw.execute(ioWrite);
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private class GetCellular extends
			AsyncTask<Void, List<CellInfo>, List<CellInfo>> {

		private boolean notSupported;

		@SuppressWarnings("unchecked")
		@Override
		protected List<CellInfo> doInBackground(Void... params) {

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
				notSupported = true;
				this.cancel(true);
				return null;
			}

			while (!isCancelled()) {
				List<CellInfo> value = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
						.getAllCellInfo();
				if (value == null) {
					notSupported = true;
					this.cancel(true);
					return null;
				}
				this.publishProgress(value);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					this.cancel(true);
				}
			}
			return ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
					.getAllCellInfo();
		}

		@Override
		protected void onProgressUpdate(List<CellInfo>... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

			String res = "##########\n";

			for (List<CellInfo> list : values) {
				res += "Cells count =" + list.size() + "\n";
				for (CellInfo cellInfo : list) {
					res += cellInfo.toString() + "\n-----------------------\n";
				}
			}

			output.append(res);
			mScroll.fullScroll(ScrollView.FOCUS_DOWN);
		}

		@Override
		protected void onPostExecute(List<CellInfo> result) {
			super.onPostExecute(result);

			String res = "";

			for (CellInfo cellInfo : result) {
				res += "timeStamp count =" + result.size() + "\n";
				res += cellInfo.toString() + "\n";
			}

			output.append(res);
			mScroll.fullScroll(ScrollView.FOCUS_DOWN);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			if (notSupported)
				Toast.makeText(CellularActivity.this,
						"Not supported by your device or android version",
						Toast.LENGTH_LONG).show();
			monitor.setChecked(false);
		}
	}
}
