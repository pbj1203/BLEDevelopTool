package com.adatronics.bledeveloptool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

public class BleCharacteristic extends ListActivity {
	private static final String TAG = "BleCharacter";
	public static final String SELECTED_SERVICE_NAME = "com.adatronics.selected_service_name";
	public static final String SELECTED_SERVICE_UUID = "com.adatronics.selected_service_uuid";
	public static final String SELECTED_SENSOR = "com.adatronics.selected_sensor";
	private String msensor;
	private String mservice_name;
	private String mservice_uuid;
	TextView ServiceName;
	TextView ServiceUUID;
	private BluetoothGatt mBluetoothGatt;
	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";
	private final String LIST_PROPERTY = "PROPERTY";
	private BluetoothDevice device;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.blecharacter);
		ServiceName = (TextView) findViewById(R.id.ble_service);
		ServiceUUID = (TextView) findViewById(R.id.ble_uuid_service);
		msensor = getIntent().getStringExtra(SELECTED_SENSOR);
		mservice_name = getIntent().getStringExtra(SELECTED_SERVICE_NAME);
		mservice_uuid = getIntent().getStringExtra(SELECTED_SERVICE_UUID);
		ServiceUUID.setText(mservice_uuid);
		ServiceName.setText(mservice_name);
		device = BleGlobal.mDevices.get(msensor);
		// mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
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
				gatt.connect();
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
						displayGattCharacter();
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

	private void displayGattCharacter() {

		String unknownCharacterString = "Unknown Characterictics";
		String uuid = null;
		UUID mService_uuid = UUID.fromString(mservice_uuid);
		BluetoothGattService gattService = mBluetoothGatt
				.getService(mService_uuid);
		if (gattService == null)
			return;
		List<BluetoothGattCharacteristic> gattCharacteristics = gattService
				.getCharacteristics();
		List<Map<String, String>> gattCharacteristicData = new ArrayList<Map<String, String>>();
		for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
			Map<String, String> currentServiceData = new HashMap<String, String>();
			uuid = gattCharacteristic.getUuid().toString();
			currentServiceData.put(LIST_NAME,
					BleGlobal.lookup(uuid, unknownCharacterString));
			currentServiceData.put(LIST_UUID, uuid);
			String property = "Properities: ";
			if (isCharacterisitcReadable(gattCharacteristic)) {
				property += " Read";
			}
			if (isCharacteristicWriteable(gattCharacteristic)) {
				if (property.isEmpty() == false) {
					property += " &";
				}
				property += " Write";
			}
			if (isCharacterisiticNotifiable(gattCharacteristic)) {
				if (property.isEmpty() == false) {
					property += " &";
				}
				property += " Notify";
			}
			currentServiceData.put(LIST_PROPERTY, property);
			gattCharacteristicData.add(currentServiceData);
		}
		setListAdapter(new SimpleAdapter(this, gattCharacteristicData,
				R.layout.list_style, new String[] { LIST_NAME, LIST_UUID,
						LIST_PROPERTY }, new int[] { R.id.text_001,
						R.id.text_002, R.id.text_003 }));
	}

	@Override
	protected void onListItemClick(ListView listView, View v, int position,
			long id) {
		Map map = (Map) listView.getItemAtPosition(position);
		String listname = map.get(LIST_NAME).toString();
		String uuid = map.get(LIST_UUID).toString();
		Intent intent = new Intent(BleCharacteristic.this, BleData.class);
		intent.putExtra(BleData.SELECTED_CHARACTER_NAME, listname);
		intent.putExtra(BleData.SELECTED_CHARACTER_UUID, uuid);
		intent.putExtra(BleData.SELECTED_SENSOR, msensor);
		intent.putExtra(BleData.SELECTED_SERVICE_UUID, mservice_uuid);
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

	public static boolean isCharacterisitcReadable(
			BluetoothGattCharacteristic pChar) {
		return ((pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
	}

	public static boolean isCharacteristicWriteable(
			BluetoothGattCharacteristic pChar) {
		return (pChar.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
	}

	public boolean isCharacterisiticNotifiable(BluetoothGattCharacteristic pChar) {
		return (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
	}
}
