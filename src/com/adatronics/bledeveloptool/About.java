package com.adatronics.bledeveloptool;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * @author BojunPan@adatronics
 *
 * 2014-4-4
 */
public class About extends Activity {
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       requestWindowFeature(Window.FEATURE_NO_TITLE);
	       setContentView(R.layout.about_layout);
	   }
}
