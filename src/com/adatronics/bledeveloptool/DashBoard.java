package com.adatronics.bledeveloptool;

import com.adatronics.bledeveloptool.ble.BleScan;
import com.adatronics.bledeveloptool.classical.BlueToothTools;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author BojunPan@adatronics
 *
 * 2014-4-4
 */
public class DashBoard extends Activity implements OnClickListener{

	
	private static final int REQUEST_DISCOVERABLE = 0;	
	private static final int DISCOVERABLE_DURATION = 300;
	View btn_bleButton;
	View btn_bluetoothButton;
	Button btn_testButton;
	View btn_aboutButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dash_board);
		btn_bleButton = (Button) findViewById(R.id.btn_ble);
		btn_bleButton.setOnClickListener(this);
		btn_bluetoothButton = (Button) findViewById(R.id.btn_bluetooth);
		btn_bluetoothButton.setOnClickListener(this);
		btn_testButton = (Button) findViewById(R.id.btn_test);
		btn_testButton.setOnClickListener(this);
		btn_aboutButton = (Button) findViewById(R.id.btn_about);
		btn_aboutButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.btn_ble:
			if (!getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_BLUETOOTH_LE)) {
				Toast.makeText(this, "The device doesn't support BLE",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent btn_ble = new Intent(this, BleScan.class);
			startActivity(btn_ble);
			break;
		case R.id.btn_bluetooth:
			Intent btn_bluetooth = new Intent(this, BlueToothTools.class);
			startActivity(btn_bluetooth);
			break;
		case R.id.btn_test:
			Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
			startActivityForResult(i, REQUEST_DISCOVERABLE);
			break;
		case R.id.btn_about:
			Intent btn_about = new Intent(this, About.class);
			startActivity(btn_about);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (requestCode == REQUEST_DISCOVERABLE) {
			if (resultCode == DISCOVERABLE_DURATION) {
				btn_testButton.setEnabled(false);
				btn_testButton.setText("... Advertising ...");
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						btn_testButton.setEnabled(true);
						btn_testButton.setText("Discoverable");
					}
				}, DISCOVERABLE_DURATION*1000);
			}
		} 
		

	}

}
