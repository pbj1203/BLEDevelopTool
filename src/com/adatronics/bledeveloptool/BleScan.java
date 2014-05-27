package com.adatronics.bledeveloptool;

import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.adatronics.bledeveloptool.RefreshableView.PullToRefreshListener;

/**
 * @author BojunPan@adatronics
 * 
 *         2014-4-4
 */
public class BleScan extends Activity {
	private BluetoothAdapter mBluetoothAdapter;
	RefreshableView refreshableView;
	private boolean mScanning;
	private Handler mHandler;
	private ListView deviceList;
	private ArrayAdapter<String> adapter;
	private static final int REQUEST_ENABLE_BT = 3;
	TextView scan_resutls;

//	private AsyncTask<String, Integer, Boolean> scanTask;
//	private boolean isScanning = false;

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
			Toast.makeText(this, "No Bluetooth device.", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		}
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		deviceList = (ListView) findViewById(R.id.list_devices);
		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, BleGlobal.deviceName);
		deviceList.setAdapter(adapter);
		displayTextView();
		deviceList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String key = BleGlobal.deviceAddress.get(position);
				Intent intent = new Intent(BleScan.this, BleService.class);
				intent.putExtra(BleService.SELECTED_SENSOR, key);
				startActivity(intent);
				return;
			}
		});
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						BleGlobal.deviceName.clear();
						adapter.notifyDataSetChanged();
						//scan_resutls.setText("Scanning for devices...");
					}
				});
				BleGlobal.mDevices.clear();
				scan();
				refreshableView.finishRefreshing();
			}
		}, 0);

	}

	@Override
	public void onResume() {
		super.onResume();
		//scan_resutls.setText("Scanning for devices...");
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
				BleGlobal.mDevices.put(device.getAddress(), device);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						BleGlobal.deviceName.clear();
						adapter.notifyDataSetChanged();
						updateListAdpater();
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

	private void updateListAdpater() {
		Set<String> keySet = BleGlobal.mDevices.keySet();
		Iterator it = keySet.iterator();
		BleGlobal.deviceName.clear();
		BleGlobal.deviceAddress.clear();
		while (it.hasNext()) {
			String key = (String) it.next();
			BluetoothDevice device = BleGlobal.mDevices.get(key);
			if (device.getName() != null) {
				BleGlobal.deviceName.add(device.getName());
				BleGlobal.deviceAddress.add(key);
			} else {
				BleGlobal.deviceName.add("unknown devices");
				BleGlobal.deviceAddress.add(key);
			}
		}
		adapter.notifyDataSetChanged();
		deviceList.invalidateViews();
		deviceList.setAdapter(adapter);
	}

	private void scan() {
		try {
			mBluetoothAdapter.startLeScan(mLeScanCallback);
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		displayTextView();
	}
	
//	private void scan() {
//		if (isScanning)
//			return;
//		refreshableView.disablePull();
//		scanTask = new AsyncTask<String, Integer, Boolean>() {
//			protected Boolean doInBackground(String... something) {
//				try {
//					mBluetoothAdapter.startLeScan(mLeScanCallback);
//					Thread.sleep(5000);
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//				mBluetoothAdapter.stopLeScan(mLeScanCallback);
//				return true;
//			}
//
//			protected void onPostExecute(Boolean result) {
//				isScanning = false;
//				displayTextView();
//				refreshableView.enablePull();
//			}
//		};
//		scanTask.execute();
//	}
}
