package com.shuai.futures.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetUtils {
	public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetInfo != null && activeNetInfo.isConnected();
        } catch (Exception e) {
            return true;
        }
    }
	
    
    /**
     * 用户当前的网络环境是否为2G
     * @return
     */
	public static boolean isNetWork2g(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager==null)
			return false;
		
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		boolean connected= activeNetInfo != null && activeNetInfo.isConnected();
		if(!connected)
			return false;
		
		int networkType=activeNetInfo.getType();
		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE:
		case TelephonyManager.NETWORK_TYPE_CDMA:
		case TelephonyManager.NETWORK_TYPE_1xRTT:
		case TelephonyManager.NETWORK_TYPE_IDEN:
		return true;

		default:
			return false;
		}

	}
    
    /**
     * 用户当前的网络环境是否为3G
     * @return
     */
    public static boolean isNetWork3g(Context context){
    	ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager==null)
			return false;
		
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		boolean connected= activeNetInfo != null && activeNetInfo.isConnected();
		if(!connected)
			return false;
		
		int networkType=activeNetInfo.getType();
		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
		case TelephonyManager.NETWORK_TYPE_HSUPA:
		case TelephonyManager.NETWORK_TYPE_HSPA:
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
		case TelephonyManager.NETWORK_TYPE_EHRPD:
		case TelephonyManager.NETWORK_TYPE_HSPAP:
		return true;

		default:
			return false;
		}
    }
}
