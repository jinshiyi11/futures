package com.shuai.futures.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

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
