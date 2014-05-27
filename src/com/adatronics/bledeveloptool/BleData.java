package com.adatronics.bledeveloptool;

import java.util.UUID;

import android.R.string;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BleData extends Activity {
	private static final String TAG = "BleData";
	public static final String SELECTED_CHARACTER_NAME = "com.adatronics.selected_character_name";
	public static final String SELECTED_CHARACTER_UUID = "com.adatronics.selected_character_uuid";
	public static final String SELECTED_SERVICE_UUID = "com.adatronics.selected_service_uuid";
	public static final String SELECTED_SENSOR = "com.adatronics.selected_sensor";
	private String msensor;
	private String mcharacter_name;
	private String mcharacter_uuid;
	private String mservice_uuid;
	TextView Rssi;
	TextView CharacterName;
	TextView ConnectionState;
	TextView Uuid;
	TextView Property;
	Button writeButton;
	EditText Hex;
	EditText ASCII;
	BluetoothDevice device;
	private boolean write_flag = false;
	private boolean read_flag = false;
	private boolean notify_flag = false;
	private BluetoothGatt mBluetoothGatt;
	private BluetoothGattCharacteristic mcharacteristic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bledata);
		CharacterName = (TextView) findViewById(R.id.ble_character);
		Uuid = (TextView) findViewById(R.id.UUID);
		ConnectionState = (TextView) findViewById(R.id.connection_state);
		Property = (TextView) findViewById(R.id.properties);
		Rssi = (TextView) findViewById(R.id.rssi);
		Hex = (EditText) findViewById(R.id.Hex);
		ASCII = (EditText) findViewById(R.id.ASCII);
		writeButton = (Button) findViewById(R.id.write);
		msensor = getIntent().getStringExtra(SELECTED_SENSOR);
		mcharacter_name = getIntent().getStringExtra(SELECTED_CHARACTER_NAME);
		mcharacter_uuid = getIntent().getStringExtra(SELECTED_CHARACTER_UUID);
		mservice_uuid = getIntent().getStringExtra(SELECTED_SERVICE_UUID);
		CharacterName.setText(mcharacter_name);
		Uuid.setText("UUID: " + mcharacter_uuid.toUpperCase());
		device = BleGlobal.mDevices.get(msensor);
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		Hex.setEnabled(false);
		ASCII.setEnabled(false);
		writeButton.setVisibility(View.INVISIBLE);
	}

	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		/* State Machine Tracking */
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			// TODO Auto-generated method stub
			Log.i(TAG, "Current state " + connectionState(newState));
			
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				gatt.discoverServices();
				gatt.readRemoteRssi();
				updateConnectionState("Connetion State: Connected");
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				Log.i(TAG, "Lose connection!!!");
				updateConnectionState("Connetion State: Not Connected");
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
				mcharacteristic = gatt.getService(
						UUID.fromString(mservice_uuid)).getCharacteristic(
						UUID.fromString(mcharacter_uuid));
				String str = "";
				if (isCharacterisitcReadable(mcharacteristic)) {
					str += " Read";
					gatt.readCharacteristic(mcharacteristic);
				}
				if (isCharacteristicWriteable(mcharacteristic)) {
					if (str.isEmpty() == false) {
						str += " &";
					}
					str += " Write";
					setvisible();
				}
				if (isCharacterisiticNotifiable(mcharacteristic)) {
					if (str.isEmpty() == false) {
						str += " &";
					}
					str += " Notify";
					gatt.setCharacteristicNotification(mcharacteristic, true);
				}
				displayProperties("Properities:" + str);
				// gatt.readCharacteristic(mcharacteristic);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onCharacteristicRead");
			int offset = 0;
			String ascii = characteristic.getStringValue(offset);
			byte[] hex = characteristic.getValue();
			StringBuilder hexString = new StringBuilder(hex.length);
			if (hex != null && hex.length > 0) {
				for (byte byteChar : hex)
					hexString.append(String.format("%02X ", byteChar));
			}
			displayHex(hexString.toString());
			displayASCII(ascii);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onCharacteristicWrite");
			byte[] hex = characteristic.getValue();
			StringBuilder hexString = new StringBuilder(hex.length);
			if (hex != null && hex.length > 0) {
				for (byte byteChar : hex)
					hexString.append(String.format("%02X ", byteChar));
			}
			displayHex(hexString.toString());
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
			displayRemoteRssi(gatt, "RSSI: " + String.valueOf(rssi));
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
		// finish();
	}

	private void updateConnectionState(final String state) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ConnectionState.setText(state);
			}
		});
	}

	private void displayRemoteRssi(final BluetoothGatt gatt, final String rssi) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				gatt.readRemoteRssi();
				Rssi.setText(rssi);

			}
		});
	}

	private void displayProperties(final String properties) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Property.setText(properties);
			}
		});
	}

	private void displayHex(final String hex) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Hex.setText(hex);
			}
		});
	}

	private void displayASCII(final String ascii) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ASCII.setText(ascii);
			}
		});
	}

	private void setvisible() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				writeButton.setVisibility(View.VISIBLE);
				ASCII.setEnabled(true);
			}
		});
	}

	public void onclick_write(View v) {
		String data = "";
		data = ASCII.getText().toString();
		mcharacteristic.setValue(data.getBytes());
		mBluetoothGatt.writeCharacteristic(mcharacteristic);
		Toast.makeText(this, "String Value Updated", Toast.LENGTH_SHORT).show();
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
