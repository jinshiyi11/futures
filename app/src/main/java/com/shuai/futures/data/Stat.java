package com.shuai.futures.data;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * 统计的事件
 */
public class Stat {
	/**
	 * token超时次数
	 */
	public static final String EVENT_TOKEN_TIMEOUT = "38";

	/**
	 * token超时然后重新登录次数
	 */
	public static final String EVENT_TOKEN_TIMEOUT_AND_RELOGIN = "39";
    
    public static void onEvent(Context context, String event){
    	MobclickAgent.onEvent(context,event);
    }
    
    public static void onEvent(Context context, String event, String key, String value){
    	HashMap<String,String> map = new HashMap<String,String>();
		map.put(key,value);
		MobclickAgent.onEvent(context, event,map);
    }
    
    public static void onEvent(Context context, String event, String key, String value, String key2, String value2){
    	HashMap<String,String> map = new HashMap<String,String>();
		map.put(key,value);
		map.put(key2,value2);
		MobclickAgent.onEvent(context, event,map);
    }

}
