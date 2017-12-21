package com.shuai.futures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.LoginStateChanged;
import com.shuai.futures.logic.UserManager;
import com.shuai.futures.ui.base.BaseTabFragment;
import com.shuai.futures.utils.NavigateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * "我的"tab页
 */
public class UserCenterFragment extends BaseTabFragment implements OnClickListener {
	protected Context mContext;

	private FragmentManager mFragmentManager;
	private RequestQueue mRequestQueue;
	private ViewGroup mRoot;
	private ImageView ivMessage;
	private ImageView ivSettings;
	private boolean mIsLogined;
	//账户余额
	private LinearLayout mLlBalance;
	private TextView mTvBalance;
	//我的微币
	private LinearLayout mLlCoin;
	private TextView mTvCoin;
	
	
	private UserManager mAccountManager;

	public UserCenterFragment() {
		super(R.layout.fragment_user);
	}

	@Override
	protected void onInit(View inflated, Bundle savedInstanceState) {
		mContext=getActivity();

		mRoot=(ViewGroup) inflated;
		mFragmentManager=((FragmentActivity)mContext).getSupportFragmentManager();
		mRequestQueue = MyApplication.getRequestQueue();
        
		ivMessage = (ImageView) inflated.findViewById(R.id.iv_message);
		ivSettings = (ImageView) inflated.findViewById(R.id.iv_settings);
		mLlBalance=(LinearLayout) inflated.findViewById(R.id.ll_balance);
		mTvBalance=(TextView) inflated.findViewById(R.id.tv_balance);
		mLlCoin=(LinearLayout) inflated.findViewById(R.id.ll_coin);
		mTvCoin=(TextView) inflated.findViewById(R.id.tv_coin);
		
		mLlBalance.setOnClickListener(this);
		mLlCoin.setOnClickListener(this);
		ivMessage.setOnClickListener(this);
		ivSettings.setOnClickListener(this);
		mAccountManager= UserManager.getInstance();
		
		onLoginStateChanged();
		EventBus.getDefault().register(this);
		
		return;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

//		if(isVisibleToUser)
//			EventBus.getDefault().post(new EventUserCenterTabSelected());
	}
	
	@Override
	public void onDestroyView() {
		EventBus.getDefault().unregister(this);
		if(mRequestQueue!=null){
			mRequestQueue.cancelAll(this);
		}
		super.onDestroyView();
	}
	
	@Subscribe
	public void onEvent(LoginStateChanged state){
		onLoginStateChanged();
	}
	
//	@Subscribe
//	public void onEvent(UserInfo info){
//		mTvBalance.setText(String.format("%1$.2f", info.getBalance()));
//		mTvCoin.setText(String.format("%1$d", (int)info.getCoin()));
//	}

	/**
	 * 登录状态变化
	 */
	private void onLoginStateChanged(){
		mIsLogined=mAccountManager.isLogined();
		if(mIsLogined){
			showUserLoginedUi();
		}else{
			showUnLoginUi();
		}
	}

	/**
	 * 显示用户未登录界面
	 */
	private void showUnLoginUi() {	
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.replace(R.id.rl_head, new UserCenterUnloginHeadFragment());
		transaction.replace(R.id.fl_orderlist, new UserCenterLoginFragment());
		transaction.commitAllowingStateLoss();
		
		getFragmentManager().executePendingTransactions();

		mTvBalance.setText("0.00");
		mTvCoin.setText("0");
		
	}

	/**
	 * 显示已登录用户界面
	 */
	private void showUserLoginedUi() {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.replace(R.id.rl_head, new UserCenterLoginedHeadFragment());
		transaction.replace(R.id.fl_orderlist, new UserCenterSettingsFragment());
		transaction.commitAllowingStateLoss();

//		GetUserExtendInfoTask request=new GetUserExtendInfoTask(mContext,null,null);
//		request.setTag(this);
//    	mRequestQueue.add(request);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.iv_message:
		{
//			Intent intent=new Intent(mContext, MessageActivity.class);
//			startActivity(intent);
		}
			break;
		case R.id.iv_settings:
		{
			Intent intent=new Intent(mContext, SettingsActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.ll_balance:
		{
//            NavigateUtils.showBalanceActivity(mContext);
		}
			break;
		case R.id.ll_coin:
		{

//			if(mIsLogined){
//				Intent intent=new Intent(mContext,CoinDetailActivity.class);
//				startActivity(intent);
//			}else{
//				Intent intent=new Intent(mContext,LoginActivity.class);
//				startActivity(intent);
//			}
		}
			break;

		default:
			break;
		}

	}

}
