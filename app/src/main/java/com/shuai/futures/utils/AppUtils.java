package com.shuai.futures.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


public class AppUtils {
	/**
     * 获取versionCode
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        int versionCode = 0;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
	
	public static String getVersionName(Context context) {
	    String versionName="";
		try {
			String packageName = context.getPackageName();
			versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	/**
	 * 获取渠道号
	 * @param context
	 * @return channel 渠道号
	 */
	public static String getChannel(Context context) {
		return getMetadata(context, "UMENG_CHANNEL");
	}
	
	public static String getMarketId(Context context) {
		return getMetadata(context, "MARKET_ID");
	}
	
	public static String getMetadata(Context context, String key) {
		String result=null;
		try {
			String packageName = context.getPackageName();
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
					packageName, PackageManager.GET_META_DATA);
			Object channel = appInfo.metaData.get(key);
			if(channel!=null)
				result=channel.toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取应用信息
	 * @param context
	 * @return packageInfo
	 */
	public static PackageInfo getApplicationInfo(Context context) {
		PackageInfo packageInfo = null;
		try {
			String packageName = context.getPackageName();
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageInfo;
	}


}
