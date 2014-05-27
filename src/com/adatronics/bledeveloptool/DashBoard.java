package com.adatronics.bledeveloptool;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author BojunPan@adatronics
 *
 * 2014-4-4
 */
public class DashBoard extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dash_board);
		View btn_bleButton = (Button) findViewById(R.id.btn_ble);
		btn_bleButton.setOnClickListener(this);
		View btn_bluetoothButton = (Button) findViewById(R.id.btn_bluetooth);
		btn_bluetoothButton.setOnClickListener(this);
		View btn_testButton = (Button) findViewById(R.id.btn_test);
		btn_testButton.setOnClickListener(this);
		View btn_aboutButton = (Button) findViewById(R.id.btn_about);
		btn_aboutButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.btn_ble:
			Log.d("choose button","btn_ble");
			Intent btn_ble = new Intent(this, BleScan.class);
			startActivity(btn_ble);
			break;
		case R.id.btn_bluetooth:
			Intent btn_bluetooth = new Intent(this, BlueToothTools.class);
			startActivity(btn_bluetooth);
			break;
		case R.id.btn_test:
			Intent btn_test = new Intent(this, TestTools.class);
			startActivity(btn_test);
			break;
		case R.id.btn_about:
			Intent btn_about = new Intent(this, About.class);
			startActivity(btn_about);
			break;
		}
	}

}
