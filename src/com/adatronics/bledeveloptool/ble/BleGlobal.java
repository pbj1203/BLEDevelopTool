package com.adatronics.bledeveloptool.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.bluetooth.BluetoothDevice;

/**
 * @author BojunPan@adatronics
 *
 * 2014-4-4
 */
public class BleGlobal {
	private static HashMap<String, String> Servermap = new HashMap();
	public static ArrayList<String> deviceName = new ArrayList<String>();
	public static ArrayList<String> deviceAddress = new ArrayList<String>();
	public static ArrayList<String> deviceRSSI = new ArrayList<String>();
	
	public static Map<String, BluetoothDevice> mDevices = new HashMap<String, BluetoothDevice>();
	public static Map<String, String> mRSSI = new HashMap<String, String>();
	public static Map<String, String> mScanRecord = new HashMap<String, String>();
	public static Map<String, String> mMajor = new HashMap<String, String>();
	public static Map<String, String> mMinor = new HashMap<String, String>();
	static {
        // Sample Services.
		Servermap.put("00001800-0000-1000-8000-00805f9b34fb", "Generic Access Profile");
		Servermap.put("00001801-0000-1000-8000-00805f9b34fb", "Generic Attribute");
		Servermap.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
		Servermap.put("00001811-0000-1000-8000-00805f9b34fb", "Alert Notification Service");
		Servermap.put("0000180f-0000-1000-8000-00805f9b34fb", "Battery Service");
		Servermap.put("00001810-0000-1000-8000-00805f9b34fb", "Blood Pressure");
		Servermap.put("00001805-0000-1000-8000-00805f9b34fb", "Current Time Service");
		Servermap.put("00001818-0000-1000-8000-00805f9b34fb", "Cycling Power");
		Servermap.put("00001816-0000-1000-8000-00805f9b34fb", "Cycling Speed and Cadence");
		Servermap.put("00001808-0000-1000-8000-00805f9b34fb", "Glucose");
		Servermap.put("00001809-0000-1000-8000-00805f9b34fb", "Health Thermometer");
		Servermap.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate");
		Servermap.put("00001812-0000-1000-8000-00805f9b34fb", "Human Interface Device");
		Servermap.put("00001802-0000-1000-8000-00805f9b34fb", "Immediate Alert");
		Servermap.put("00001803-0000-1000-8000-00805f9b34fb", "Link Loss");
		Servermap.put("00001807-0000-1000-8000-00805f9b34fb", "Next DST Change Service");
		Servermap.put("0000180e-0000-1000-8000-00805f9b34fb", "Phone Alert Status Service");
		Servermap.put("00001806-0000-1000-8000-00805f9b34fb", "Reference Time Update Service");
		Servermap.put("00001814-0000-1000-8000-00805f9b34fb", "Running Speed and Cadence");
		Servermap.put("00001813-0000-1000-8000-00805f9b34fb", "Scan Parameters");
		Servermap.put("00001804-0000-1000-8000-00805f9b34fb", "Tx Power");
		Servermap.put("0000C004-0000-1000-8000-00805f9b34fb", "Heart Rate Measurement");
		Servermap.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
		Servermap.put("00002a24-0000-1000-8000-00805f9b34fb", "Model Number String");
		Servermap.put("00002a26-0000-1000-8000-00805f9b34fb", "Firmware Revision String");
		Servermap.put("00002a27-0000-1000-8000-00805f9b34fb", "Hardware Revision String");
		Servermap.put("00002a43-0000-1000-8000-00805f9b34fb", "Alert Category ID");
		Servermap.put("00002a42-0000-1000-8000-00805f9b34fb", "Alert Category ID Bit Mask");
		Servermap.put("00002a06-0000-1000-8000-00805f9b34fb", "Alert Level");
		Servermap.put("00002a44-0000-1000-8000-00805f9b34fb", "Alert Notification Control Point");
		Servermap.put("00002a3f-0000-1000-8000-00805f9b34fb", "Alert Status");
		Servermap.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance");
		Servermap.put("00002a19-0000-1000-8000-00805f9b34fb", "Battery Level");
		Servermap.put("00002a49-0000-1000-8000-00805f9b34fb", "Blood Pressure Feature");
		Servermap.put("00002a35-0000-1000-8000-00805f9b34fb", "Blood Pressure Measurement");
		Servermap.put("00002a38-0000-1000-8000-00805f9b34fb", "Body Sensor Location");
		Servermap.put("00002a22-0000-1000-8000-00805f9b34fb", "Boot Keyboard Input Report");
		Servermap.put("00002a32-0000-1000-8000-00805f9b34fb", "Boot Keyboard Output Report");
		Servermap.put("00002a33-0000-1000-8000-00805f9b34fb", "Boot Mouse Input Report");
		Servermap.put("00002a33-0000-1000-8000-00805f9b34fb", "Boot Mouse Input Report");
		Servermap.put("00002a5c-0000-1000-8000-00805f9b34fb", "CSC Feature");
		Servermap.put("00002a5b-0000-1000-8000-00805f9b34fb", "CSC Measurement");
		Servermap.put("00002a2b-0000-1000-8000-00805f9b34fb", "Current Time");
		Servermap.put("00002a66-0000-1000-8000-00805f9b34fb", "Cycling Power Control Point");
		Servermap.put("00002a65-0000-1000-8000-00805f9b34fb", "Cycling Power Feature");
		Servermap.put("00002a63-0000-1000-8000-00805f9b34fb", "Cycling Power Measurement");
		Servermap.put("00002a64-0000-1000-8000-00805f9b34fb", "Cycling Power Vector");
		Servermap.put("00002a08-0000-1000-8000-00805f9b34fb", "Date Time");
		Servermap.put("00002a0a-0000-1000-8000-00805f9b34fb", "Day Date Time");
		Servermap.put("00002a09-0000-1000-8000-00805f9b34fb", "Day of Week");
		Servermap.put("00002a00-0000-1000-8000-00805f9b34fb", "Device Name");
		Servermap.put("00002a0d-0000-1000-8000-00805f9b34fb", "DST Offset");
		Servermap.put("00002a0c-0000-1000-8000-00805f9b34fb", "Exact Time 256");
		Servermap.put("00002a26-0000-1000-8000-00805f9b34fb", "Firmware Revision String");
		Servermap.put("00002a51-0000-1000-8000-00805f9b34fb", "Glucose Feature");
		Servermap.put("00002a18-0000-1000-8000-00805f9b34fb", "Glucose Measurement");
		Servermap.put("00002a34-0000-1000-8000-00805f9b34fb", "Glucose Measurement Context");
		Servermap.put("00002a27-0000-1000-8000-00805f9b34fb", "Hardware Revision String");
		Servermap.put("00002a39-0000-1000-8000-00805f9b34fb", "Heart Rate Control Point");
		Servermap.put("00002a37-0000-1000-8000-00805f9b34fb", "Heart Rate Measurement");
		Servermap.put("00002a4c-0000-1000-8000-00805f9b34fb", "HID Control Point");
		Servermap.put("00002a4a-0000-1000-8000-00805f9b34fb", "HID Information");
		Servermap.put("00002a2a-0000-1000-8000-00805f9b34fb", "IEEE 11073-20601 Regulatory");
		Servermap.put("00002a36-0000-1000-8000-00805f9b34fb", "Intermediate Cuff Pressure");
		Servermap.put("00002a1e-0000-1000-8000-00805f9b34fb", "ntermediate Temperature");
		Servermap.put("00002a6b-0000-1000-8000-00805f9b34fb", "LN Control Point");
		Servermap.put("00002a6a-0000-1000-8000-00805f9b34fb", "LN Feature");
		Servermap.put("00002a0f-0000-1000-8000-00805f9b34fb", "Local Time Information");
		Servermap.put("00002a67-0000-1000-8000-00805f9b34fb", "Location and Speed");
		Servermap.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
		Servermap.put("00002a21-0000-1000-8000-00805f9b34fb", "Measurement Interval");
		Servermap.put("00002a25-0000-1000-8000-00805f9b34fb", "Model Number String");
		Servermap.put("00002a68-0000-1000-8000-00805f9b34fb", "Navigation");
		Servermap.put("00002a46-0000-1000-8000-00805f9b34fb", "New Alert");
		Servermap.put("00002a04-0000-1000-8000-00805f9b34fb", "Peripheral Connection Parameters");
		Servermap.put("00002a02-0000-1000-8000-00805f9b34fb", "Peripheral Privacy Flag");
		Servermap.put("00002a50-0000-1000-8000-00805f9b34fb", "PnP ID");
		Servermap.put("00002a69-0000-1000-8000-00805f9b34fb", "Position Quality");
		Servermap.put("00002a4e-0000-1000-8000-00805f9b34fb", "Protocol Mode");
		Servermap.put("00002a03-0000-1000-8000-00805f9b34fb", "Reconnection Address");
		Servermap.put("00002a52-0000-1000-8000-00805f9b34fb", "Record Access Control Point");
		Servermap.put("00002a14-0000-1000-8000-00805f9b34fb", "Reference Time Information");
		Servermap.put("00002a4d-0000-1000-8000-00805f9b34fb", "Report");
		Servermap.put("00002a4b-0000-1000-8000-00805f9b34fb", "Report Map");
		Servermap.put("00002a40-0000-1000-8000-00805f9b34fb", "Ringer Control Point");
		Servermap.put("00002a41-0000-1000-8000-00805f9b34fb", "Ringer Setting");
		Servermap.put("00002a54-0000-1000-8000-00805f9b34fb", "RSC Feature");
		Servermap.put("00002a53-0000-1000-8000-00805f9b34fb", "RSC Measurement");
		Servermap.put("00002a55-0000-1000-8000-00805f9b34fb", "SC Control Point");
		Servermap.put("00002a4f-0000-1000-8000-00805f9b34fb", "Scan Interval Window");
		Servermap.put("00002a31-0000-1000-8000-00805f9b34fb", "Scan Refresh");
		Servermap.put("00002a5d-0000-1000-8000-00805f9b34fb", "Sensor Location");
		Servermap.put("00002a25-0000-1000-8000-00805f9b34fb", "Serial Number String");
		Servermap.put("00002a05-0000-1000-8000-00805f9b34fb", "Service Changed");
		Servermap.put("00002a28-0000-1000-8000-00805f9b34fb", "Software Revision String");
		Servermap.put("00002a47-0000-1000-8000-00805f9b34fb", "Supported New Alert Category");
		Servermap.put("00002a48-0000-1000-8000-00805f9b34fb", "Supported Unread Alert Category");
		Servermap.put("00002a23-0000-1000-8000-00805f9b34fb", "System ID");
		Servermap.put("00002a1c-0000-1000-8000-00805f9b34fb", "Temperature Measurement");
		Servermap.put("00002a1d-0000-1000-8000-00805f9b34fb", "Temperature Type");
		Servermap.put("00002a12-0000-1000-8000-00805f9b34fb", "Time Accuracy");
		Servermap.put("00002a13-0000-1000-8000-00805f9b34fb", "Time Source");
		Servermap.put("00002a16-0000-1000-8000-00805f9b34fb", "Time Update Control Point");
		Servermap.put("00002a17-0000-1000-8000-00805f9b34fb", "Time Update State");
		Servermap.put("00002a11-0000-1000-8000-00805f9b34fb", "Time with DST");
		Servermap.put("00002a0e-0000-1000-8000-00805f9b34fb", "Time Zone");
		Servermap.put("00002a08-0000-1000-8000-00805f9b34fb", "Tx Power Level");
		Servermap.put("00002a45-0000-1000-8000-00805f9b34fb", "Unread Alert Status");
    }
	public static String lookup(String uuid, String defaultName) {
        String name = Servermap.get(uuid);
        return name == null ? defaultName : name;
    }
}
