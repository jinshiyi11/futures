package com.shuai.futures.net;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.greenrobot.event.EventBus;

/**
 * 网络连接或断开监听类
 */
public class ConnectionChangeMonitor extends BroadcastReceiver {
	
	/**
	 * 网络连接或断开通知事件
	 */
	public static class EventConnectionChange{
		private boolean mConnected;
		
		EventConnectionChange(boolean connected){
			mConnected=connected;
		}
		
		public boolean isConnected(){
			return mConnected;
		}
	}
	
	private Context mContext;
	
	private boolean mConnectedSent=false;//CONNECTIVITY_ACTION有时会多次触发，使用该标记过滤多余的通知
	private boolean mDisconnectedSent=false;//CONNECTIVITY_ACTION有时会多次触发，使用该标记过滤多余的通知
	
	public ConnectionChangeMonitor(Context appContext) {
		if(!(appContext instanceof Application)){
			throw new IllegalArgumentException();
		}
		
		mContext=appContext;
	}

	public void startMonitor() {
		IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		mContext.registerReceiver(this, filter);
	}

	public void stopMonitor() {
		mContext.unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(isInitialStickyBroadcast())
			return;
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.isConnected()) {
			//CONNECTIVITY_ACTION有时会多次触发，过滤多余的通知
			if(!mConnectedSent){
				mConnectedSent=true;
				mDisconnectedSent=false;
				
				EventBus.getDefault().post(new EventConnectionChange(true));
			}
			
		}else{
			if(!mDisconnectedSent){
				mDisconnectedSent=true;
				mConnectedSent=false;
				
				EventBus.getDefault().post(new EventConnectionChange(false));
			}
			
		}

	}
}
