package com.adatronics.bledeveloptool.classical;

import com.adatronics.bledeveloptool.R;
import com.adatronics.bledeveloptool.R.id;
import com.adatronics.bledeveloptool.R.layout;
import com.adatronics.bledeveloptool.ble.BleGlobal;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

/**
 * @author BojunPan@adatronics
 *
 * 2014-4-4
 */
public class BlueToothConfig extends Activity{
	private static final String TAG = "BleConfig";
	public static final String SELECTED_SENSOR = "com.adatronics.selected_sensor";
	private String msensor;
	private String mDeviceName;
	private String mDeviceAddress;
	TextView device_address;
	BluetoothDevice device;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bleservice);
		device_address = (TextView) findViewById(R.id.ble_uuid_device);
		msensor = getIntent().getStringExtra(SELECTED_SENSOR);
		device = BleGlobal.mDevices.get(msensor);
		mDeviceAddress = device.getAddress();
		mDeviceName = device.getName();
		//mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		device_address.setText("Device Address: " + mDeviceAddress);
	}
}
