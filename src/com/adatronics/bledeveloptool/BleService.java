package com.adatronics.bledeveloptool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author BojunPan@adatronics
 * 
 *         2014-4-4
 */
public class BleService extends ListActivity {
	private static final String TAG = "BleService";
	public static final String SELECTED_SENSOR = "com.adatronics.selected_sensor";
	private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	private String msensor;
	private String mDeviceName;
	private String mDeviceAddress;
	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;
	private int mConnectionState = STATE_DISCONNECTED;
	private BluetoothGatt mBluetoothGatt;
	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";
	TextView uuid_device;
	BluetoothDevice device;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bleservice);
		uuid_device = (TextView) findViewById(R.id.ble_uuid_device);
		msensor = getIntent().getStringExtra(SELECTED_SENSOR);
		device = BleGlobal.mDevices.get(msensor);
		mDeviceAddress = device.getAddress();
		mDeviceName = device.getName();
		//mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		uuid_device.setText("Device Address: " + mDeviceAddress);
	}

	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		/* State Machine Tracking */
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			// TODO Auto-generated method stub
			Log.i(TAG, "Current state " + connectionState(newState));

			if (status == BluetoothGatt.GATT_SUCCESS
					&& newState == BluetoothProfile.STATE_CONNECTED) {
				gatt.discoverServices();

			} else if (status == BluetoothGatt.GATT_SUCCESS
					&& newState == BluetoothProfile.STATE_DISCONNECTED) {
				Log.i(TAG, "Lose connection!!!");
				cleanUI();
			} else if (status != BluetoothGatt.GATT_SUCCESS) {
				gatt.disconnect();
				finish();
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			// TODO Auto-generated method stub
			Log.d(TAG, "Services Discovered: " + status + "   "
					+ BluetoothGatt.GATT_SUCCESS);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						displayGattServices(mBluetoothGatt.getServices());
					}
				});
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onCharacteristicRead");
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onCharacteristicWrite");
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onDescriptorWrite");
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onReadRemoteRssi");
		}

		private String connectionState(int status) {
			switch (status) {
			case BluetoothProfile.STATE_CONNECTED:
				return "Connected";
			case BluetoothProfile.STATE_DISCONNECTED:
				return "Disconnected";
			case BluetoothProfile.STATE_CONNECTING:
				return "Connecting";
			case BluetoothProfile.STATE_DISCONNECTING:
				return "Disconnecting";
			default:
				return String.valueOf(status);
			}
		}

	};

	private void displayGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices == null)
			return;
		String uuid = null;
		String unknownServiceString = "Unknown Service";
		List<Map<String, String>> gattServiceData = new ArrayList<Map<String, String>>();
		for (BluetoothGattService gattService : gattServices) {
			Map<String, String> currentServiceData = new HashMap<String, String>();
			uuid = gattService.getUuid().toString();
			currentServiceData.put(LIST_NAME,
					BleGlobal.lookup(uuid, unknownServiceString));
			currentServiceData.put(LIST_UUID, uuid);
			gattServiceData.add(currentServiceData);
		}
		setListAdapter(new SimpleAdapter(this, gattServiceData,
				android.R.layout.simple_list_item_2, new String[] { LIST_NAME,
						LIST_UUID }, new int[] { android.R.id.text1,
						android.R.id.text2 }));
	}

	void cleanUI() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setListAdapter(null);
			}
		});

	}

	@Override
	protected void onListItemClick(ListView listView, View v, int position,
			long id) {
		Map map = (Map) listView.getItemAtPosition(position);
		String listname = map.get(LIST_NAME).toString();
		String uuid = map.get(LIST_UUID).toString();
		Intent intent = new Intent(BleService.this, BleCharacteristic.class);
		intent.putExtra(BleCharacteristic.SELECTED_SERVICE_NAME, listname);
		intent.putExtra(BleCharacteristic.SELECTED_SERVICE_UUID, uuid);
		intent.putExtra(BleCharacteristic.SELECTED_SENSOR, msensor);
		startActivity(intent);
		return;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mBluetoothGatt != null) {
			mBluetoothGatt.close();
			mBluetoothGatt = null;
		}
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
	}
}
