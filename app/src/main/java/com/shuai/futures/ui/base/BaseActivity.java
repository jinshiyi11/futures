package com.shuai.futures.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.shuai.futures.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 增加友盟统计，所有需要使用Activity的地方都应该使用此类做基类
 */
public abstract class BaseActivity extends Activity {
	protected final String TAG = getClass().getSimpleName();

	protected Context mContext;

	//返回按钮
	private View btnBack;
	
	private ProgressDialog mProgressDialog;
	//private SystemBarTintManager mTintManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		//mTintManager = new SystemBarTintManager(this);
		//mTintManager.setStatusBarTintEnabled(true);
		//mTintManager.setNavigationBarTintEnabled(false);
		//mTintManager.setTintColor(Color.parseColor("#ffff0000"));

		btnBack = findViewById(R.id.iv_back);
		if (btnBack != null) {
			btnBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		MobclickAgent.onPageEnd(this.getClass().getSimpleName());
		MobclickAgent.onPause(this);
	}
	
	public void showProgressDialog(){
		if(mProgressDialog==null){
			mProgressDialog=new ProgressDialog(mContext);
		}
		
		mProgressDialog.show();
	}
	
	public void hideProgressDialog(){
		if(mProgressDialog!=null){
			mProgressDialog.dismiss();
		}
	}
}
