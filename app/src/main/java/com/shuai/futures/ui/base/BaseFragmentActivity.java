package com.shuai.futures.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.shuai.futures.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 增加友盟统计，所有需要使用Activity的地方都应该使用此类做基类
 */
public abstract class BaseFragmentActivity extends AppCompatActivity {
	protected Context mContext;
	private View btnBack;
	
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().getDecorView().setSystemUiVisibility(
//				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
////						| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//						| View.SYSTEM_UI_FLAG_IMMERSIVE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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
	
	@Override
	public void setContentView(int layoutResID) {
		mContext=this;
		super.setContentView(layoutResID);

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
