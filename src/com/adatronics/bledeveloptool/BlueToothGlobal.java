package com.adatronics.bledeveloptool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.bluetooth.BluetoothDevice;

/**
 * @author BojunPan@adatronics
 *
 * 2014-4-4
 */
public class BlueToothGlobal {
	public static ArrayList<String> deviceName = new ArrayList<String>();
	public static ArrayList<String> deviceAddress = new ArrayList<String>();
	public static Map<String, BluetoothDevice> mDevices = new HashMap<String, BluetoothDevice>();
}
