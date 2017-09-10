package com.shuai.futures.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.shuai.futures.R;

public class ToastDialog extends Dialog {
	private TextView mTvMessage;
	private Handler mHandler=new Handler();
	private final int DURATION=1000;
	private Runnable mRunnable;

	public ToastDialog(Context context) {
		super(context, R.style.default_dialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_toast);
		
		mTvMessage=(TextView) findViewById(R.id.tv_message);
		
		mRunnable=new Runnable() {
			
			@Override
			public void run() {
				dismiss();
			}
		};
		mHandler.postDelayed(mRunnable, DURATION);
	}
	
	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
		if(mRunnable!=null)
			mHandler.removeCallbacks(mRunnable);
	}

	public void setMessage(String message){
		mTvMessage.setText(message);
	}
	
}
