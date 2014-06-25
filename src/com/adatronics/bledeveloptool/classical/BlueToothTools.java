package com.adatronics.bledeveloptool.classical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.adatronics.bledeveloptool.R;
import com.adatronics.bledeveloptool.RefreshableView;
import com.adatronics.bledeveloptool.R.id;
import com.adatronics.bledeveloptool.R.layout;
import com.adatronics.bledeveloptool.RefreshableView.PullToRefreshListener;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothClass.Device;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author BojunPan@adatronics
 * 
 *         2014-4-4
 */
public class BlueToothTools extends ListActivity {
	private BluetoothAdapter mBluetoothAdapter;
	RefreshableView refreshableView;
	private static final int REQUEST_ENABLE_BT = 0;
	BroadcastReceiver discoverDevicesReceiver;
	BroadcastReceiver discoveryFinishedReceiver;
	ArrayList<BluetoothDevice> pairedDevices;
	ArrayList<BluetoothDevice> discoveredDevices;
	ArrayList<String> discoveredDevicesNames;
	TextView scan_resutls;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bluetoothscan);
		discoveredDevices = new ArrayList<BluetoothDevice>();
		discoveredDevicesNames = new ArrayList<String>();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (IsBluetoothAvailable()) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(i, REQUEST_ENABLE_BT);
			}
		}
		scan_resutls = (TextView) findViewById(R.id.bluetooth_scan_results);
		refreshableView = (RefreshableView) findViewById(R.id.bluetooth_refreshable_view);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				runOnUiThread(new Runnable() {
					public void run() {
						DiscoveringDevices();
					}
				});
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, 0);

	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		BluetoothDevice deviceSelected = discoveredDevices.get(position);
		Toast.makeText(
				this,
				"You have connected to "
						+ discoveredDevices.get(position).getName(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResume() {
		super.onResume();
		DiscoveringDevices();
	}

	private void DiscoveringDevices() {
		if (discoverDevicesReceiver == null) {
			discoverDevicesReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if (BluetoothDevice.ACTION_FOUND.equals(action)) {
						BluetoothDevice device = intent
								.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						if (!discoveredDevices.contains(device)) {
							discoveredDevices.add(device);
							discoveredDevicesNames.add(device.getName());
							setListAdapter(new ArrayAdapter<String>(
									getBaseContext(),
									android.R.layout.simple_list_item_1,
									discoveredDevicesNames));
						}
					}
				}
			};
		}

		if (discoveryFinishedReceiver == null) {
			discoveryFinishedReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					getListView().setEnabled(true);
					Toast.makeText(getBaseContext(), "Discovery completed.",
							Toast.LENGTH_LONG).show();
					if (discoveredDevicesNames.isEmpty()) {
						scan_resutls.setText("No device was found");
					} else {
						scan_resutls.setText("Find "
								+ discoveredDevicesNames.size() + " devices");
					}
					unregisterReceiver(discoveryFinishedReceiver);
				}
			};
		}

		IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		IntentFilter filter2 = new IntentFilter(
				BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		registerReceiver(discoverDevicesReceiver, filter1);
		registerReceiver(discoveryFinishedReceiver, filter2);

		getListView().setEnabled(false);
		Toast.makeText(getBaseContext(),
				"Discovery in progress...please wait...", Toast.LENGTH_LONG)
				.show();
		scan_resutls.setText("Scanning Devices...");
		mBluetoothAdapter.startDiscovery();
	}

	private boolean IsBluetoothAvailable() {
		if (mBluetoothAdapter == null)
			return false;
		else
			return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		mBluetoothAdapter.cancelDiscovery();
		if (discoverDevicesReceiver != null) {
			try {
				unregisterReceiver(discoverDevicesReceiver);
			} catch (Exception e) {

			}
		}
	}
}
