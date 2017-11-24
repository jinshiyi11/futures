package com.shuai.futures.ui;

import android.os.Bundle;
import android.view.Window;
import com.shuai.futures.R;
import com.shuai.futures.ui.base.BaseFragmentActivity;

/**
 * 设置界面
 */
public class SettingsActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_settings);
	}
	
	

}
