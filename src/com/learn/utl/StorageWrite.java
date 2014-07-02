package com.learn.utl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class StorageWrite extends AsyncTask<IOWriteInterface, Boolean, Void> {

	/**
	 * 
	 */
	private Activity activity;

	/**
	 * @param calling
	 *            activity
	 */
	public StorageWrite(Activity activity) {
		this.activity = activity;
	}

	private boolean mExternalStorageAvailable = false;
	private boolean mExternalStorageWriteable = false;
	private boolean isEmpty = false;

	private boolean checkStorage() {

		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
			this.cancel(true);
		} else {
			// Something else is wrong. It may be one of many other states,
			// but all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
			this.cancel(true);
		}

		return mExternalStorageWriteable;
	}

	@Override
	protected void onProgressUpdate(Boolean... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);

	}

	@Override
	protected Void doInBackground(IOWriteInterface... params) {

		if (checkStorage()) {

			for (IOWriteInterface param : params) {
				OutputStream os;
				try {
					InputStream is = param.fileToWrite();
					if (is.available() < 1) {
						isEmpty = true;
						this.cancel(true);
						return null;
					}

					File rootPath = Environment.getExternalStorageDirectory();
					File savePath = new File(rootPath.toString()
							+ File.separator + param.savePath());

					savePath.mkdirs();

					File mFile = new File(savePath.toString(),
							param.qualifiedName());
					os = new FileOutputStream(mFile);
					byte[] data = new byte[is.available()];
					is.read(data);
					os.write(data);
					is.close();
					os.close();

					MediaScannerConnection
							.scanFile(
									this.activity,
									new String[] { mFile.toString() },
									null,
									new MediaScannerConnection.OnScanCompletedListener() {
										public void onScanCompleted(
												String path, Uri uri) {
											Log.i("ExternalStorage", "Scanned "
													+ path + ":");
											Log.i("ExternalStorage", "-> uri="
													+ uri);
										}
									});
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					this.cancel(true);
					return null;
				} catch (IOException e) {
					e.printStackTrace();
					this.cancel(true);
					return null;
				}

			}

		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();

		if (isEmpty)
			Toast.makeText(this.activity, "No readings to save",
					Toast.LENGTH_LONG).show();
		else if (mExternalStorageAvailable)
			Toast.makeText(this.activity, "Can't Access Storage",
					Toast.LENGTH_LONG).show();
		else if (!mExternalStorageWriteable)
			Toast.makeText(this.activity, "Invalid Storage", Toast.LENGTH_LONG)
					.show();
		else
			Toast.makeText(this.activity, "Error writting file",
					Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Toast.makeText(this.activity, "File saved successfully...",
				Toast.LENGTH_LONG).show();
	}

}