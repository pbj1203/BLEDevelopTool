package com.adatronics.bledeveloptool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.R.integer;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.adatronics.bledeveloptool.RefreshableView.PullToRefreshListener;

/**
 * @author BojunPan@adatronics
 * 
 *         2014-4-4
 */
public class BleScan extends ListActivity {
	private BluetoothAdapter mBluetoothAdapter;
	RefreshableView refreshableView;
	private boolean mScanning;
	private Handler mHandler;
	private ArrayAdapter<String> adapter;
	private static final int REQUEST_ENABLE_BT = 3;
	TextView scan_resutls;
	private String DEVICE_NAME = "NAME";
	private String DEVICE_ADDRESS = "ADDRESS";
	private String DEVICE_RSSI = "RSSI";
	private String EXTRA_NAME = "EXTRA_NAME";
	private String MAJOR = "MAJOR";
	private String MINOR = "MINOR";

	// private AsyncTask<String, Integer, Boolean> scanTask;
	// private boolean isScanning = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.blescan);
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "The device doesn't support BLE",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		scan_resutls = (TextView) findViewById(R.id.ble_scan_results);

		BluetoothManager blueManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
		mBluetoothAdapter = blueManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "BLE Not Support.", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, BleGlobal.deviceName);
		displayTextView();
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						BleGlobal.deviceName.clear();
						adapter.notifyDataSetChanged();
						// scan_resutls.setText("Scanning for devices...");
					}
				});
				BleGlobal.mDevices.clear();
				scan();
				refreshableView.finishRefreshing();
			}
		}, 0);

	}

	@Override
	protected void onListItemClick(ListView listView, View v, int position,
			long id) {
		String key = BleGlobal.deviceAddress.get(position);
		Intent intent = new Intent(BleScan.this, BleService.class);
		intent.putExtra(BleService.SELECTED_SENSOR, key);
		startActivity(intent);
		return;
	}

	@Override
	public void onResume() {
		super.onResume();
		// scan_resutls.setText("Scanning for devices...");
		BleGlobal.deviceName.clear();
		BleGlobal.mDevices.clear();
		adapter.notifyDataSetChanged();
		scan();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			if (!BleGlobal.mDevices.containsKey(device.getAddress())) {

				
				final int mrssi = rssi;
				String uuid = IntToHex2(scanRecord[9] & 0xff)
						+ IntToHex2(scanRecord[10] & 0xff)
						+ IntToHex2(scanRecord[11] & 0xff)
						+ IntToHex2(scanRecord[12] & 0xff) + "-"
						+ IntToHex2(scanRecord[13] & 0xff)
						+ IntToHex2(scanRecord[14] & 0xff) + "-"
						+ IntToHex2(scanRecord[15] & 0xff)
						+ IntToHex2(scanRecord[16] & 0xff) + "-"
						+ IntToHex2(scanRecord[17] & 0xff)
						+ IntToHex2(scanRecord[18] & 0xff) + "-"
						+ IntToHex2(scanRecord[19] & 0xff)
						+ IntToHex2(scanRecord[20] & 0xff)
						+ IntToHex2(scanRecord[21] & 0xff)
						+ IntToHex2(scanRecord[22] & 0xff)
						+ IntToHex2(scanRecord[23] & 0xff)
						+ IntToHex2(scanRecord[24] & 0xff);

				String major = IntToHex2(scanRecord[25] & 0xff)
						+ IntToHex2(scanRecord[26] & 0xff);
				String minor = IntToHex2(scanRecord[27] & 0xff)
						+ IntToHex2(scanRecord[28] & 0xff);
				// String msg = "payload = ";
				// for (byte b : scanRecord)
				// msg += String.format("%02x-", b);
				BleGlobal.mScanRecord.put(device.getAddress(), uuid);
				BleGlobal.mMajor.put(device.getAddress(), major);
				BleGlobal.mMinor.put(device.getAddress(), minor);
				BleGlobal.mDevices.put(device.getAddress(), device);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						updateListAdpater(mrssi);
					}
				});
			}

		}
	};

	private void displayTextView() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("device", String.valueOf(BleGlobal.mDevices.isEmpty()));
				if (BleGlobal.mDevices.isEmpty()) {
					scan_resutls.setText("No devices was found");
				} else {
					scan_resutls.setText("Found " + BleGlobal.mDevices.size()
							+ " devices");
				}
			}
		});
	}

	private void updateListAdpater(int rssi) {
		List<Map<String, String>> gattDevicelist = new ArrayList<Map<String, String>>();
		Set<String> keySet = BleGlobal.mDevices.keySet();
		Iterator it = keySet.iterator();
		BleGlobal.deviceName.clear();
		BleGlobal.deviceAddress.clear();
		while (it.hasNext()) {
			String key = (String) it.next();
			Map<String, String> currentData = new HashMap<String, String>();
			BluetoothDevice device = BleGlobal.mDevices.get(key);
			BleGlobal.deviceAddress.add(key);
			currentData.put(DEVICE_ADDRESS, "MAC Address: " + key);
			currentData.put(DEVICE_RSSI, "RSSI: " + String.valueOf(rssi));
			currentData.put(EXTRA_NAME,
					"UUID: " + BleGlobal.mScanRecord.get(key));
			currentData.put(MAJOR, "major: " + BleGlobal.mMajor.get(key));
			currentData.put(MINOR, "minor: " + BleGlobal.mMinor.get(key));
			gattDevicelist.add(currentData);
			if (device.getName() != null) {
				BleGlobal.deviceName.add(device.getName());
				currentData.put(DEVICE_NAME, device.getName());
			} else {
				BleGlobal.deviceName.add("Unknown Devices");
				currentData.put(DEVICE_NAME, "Unknown Devices");
			}
		}
		setListAdapter(new SimpleAdapter(this, gattDevicelist,
				R.layout.list_style,
				new String[] { DEVICE_NAME, DEVICE_ADDRESS, DEVICE_RSSI,
						EXTRA_NAME, MAJOR, MINOR }, new int[] { R.id.text_001,
						R.id.text_002, R.id.text_003, R.id.text_004,
						R.id.text_005, R.id.text_006 }));
	}

	private void scan() {
		try {
			mBluetoothAdapter.startLeScan(mLeScanCallback);
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		displayTextView();
	}

	// private void scan() {
	// if (isScanning)
	// return;
	// refreshableView.disablePull();
	// scanTask = new AsyncTask<String, Integer, Boolean>() {
	// protected Boolean doInBackground(String... something) {
	// try {
	// mBluetoothAdapter.startLeScan(mLeScanCallback);
	// Thread.sleep(5000);
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// }
	// mBluetoothAdapter.stopLeScan(mLeScanCallback);
	// return true;
	// }
	//
	// protected void onPostExecute(Boolean result) {
	// isScanning = false;
	// displayTextView();
	// refreshableView.enablePull();
	// }
	// };
	// scanTask.execute();
	// }
	private String IntToHex2(int i) {
		char hex_2[] = { Character.forDigit((i >> 4) & 0x0f, 16),
				Character.forDigit(i & 0x0f, 16) };
		String hex_2_str = new String(hex_2);
		return hex_2_str.toUpperCase();
	}
}
