package com.shuai.futures.ui.base;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends Fragment {
	protected final String TAG = getClass().getSimpleName();
	
	private String mStatTag;
	
	/**
	 * 设置统计附加前缀，解决一个fragment在多个地方用到，友盟统计无法区分的问题(不仅是无法区分)
	 */
	public void setStatTag(String statTag){
		mStatTag=statTag;
	}
	
	public String getStatTag(){
		return mStatTag;
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TextUtils.isEmpty(mStatTag)?this.getClass().getSimpleName():mStatTag+this.getClass().getSimpleName());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TextUtils.isEmpty(mStatTag)?this.getClass().getSimpleName():mStatTag+this.getClass().getSimpleName());
	}

}
