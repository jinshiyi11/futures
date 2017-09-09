package com.shuai.futures.protocol;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class ProtocolUtils {
	public static final String TAG=ProtocolUtils.class.getSimpleName();
	
	public static ErrorInfo getErrorInfo(VolleyError error){
		Log.e(TAG, error.toString());
		
		if(error instanceof ErrorInfo){
			return (ErrorInfo)error;
		}else if(error instanceof NetworkError){
			return new ErrorInfo(ErrorInfo.ERROR_NERWORK_CONNECTION, "网络未连接");
		}else if(error instanceof TimeoutError){
			return new ErrorInfo(ErrorInfo.ERROR_NERWORK_TIMEOUT,"网络连接超时");
		}else if(error instanceof ServerError){
			return new ErrorInfo(ErrorInfo.ERROR_SERVER,"服务异常");
		}else if(error instanceof ParseError){
			return new ErrorInfo(ErrorInfo.ERROR_DATA,"数据异常");
		}else{
			return new ErrorInfo(ErrorInfo.ERROR_UNKONW,"未知错误");
		}
	}

}
