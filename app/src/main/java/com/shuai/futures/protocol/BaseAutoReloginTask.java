/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shuai.futures.protocol;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.shuai.futures.MyApplication;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.Stat;
import com.shuai.futures.data.UserInfo;
import com.shuai.futures.logic.UserManager;


import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

/**
 * 如果token过期，自动重新登录
 * 
 * @param <T>
 */
public abstract class BaseAutoReloginTask<T> extends BaseTask<T> {

	private int mRetryLoginCount = 0;
	private final int MAX_RETRY_COUNT = 1;
	private final Response.ErrorListener mErrorListener;
	// private VolleyError mLastResponseError;

	// 记录下之前的参数方便token过期时重新生成request
	private List<BasicNameValuePair> mParams;
	private String mOriginalUrl;
	private Listener<T> mListener;

	public BaseAutoReloginTask(int method, String url,
                               List<BasicNameValuePair> params, Listener<T> listener,
                               ErrorListener errorListener) {
		super(method, getUrl(method,url, params), method== Method.GET?null:getParam(params), listener, null);

		if (Constants.DEBUG) {
			Log.d(TAG, getUrl(method,url, params));
		}
		
		mParams = params;
		mOriginalUrl=url;
		mListener = listener;
		mErrorListener = errorListener;
	}
	
	@Override
	public String getUrl() {
		if(getMethod()== Method.POST)
			return super.getUrl();
		
		String query=getNewParam();
		if(TextUtils.isEmpty(query))
			return mOriginalUrl;
		else {
			return mOriginalUrl+"?"+query;
		}
	}

	private static String getUrl(int method, String url, List<BasicNameValuePair> params){
		if(method== Method.POST)
			return url;
		
		if(params == null || params.size()==0)
			return url;
		
		return url+"?"+getParam(params);
	}

	private static String getParam(List<BasicNameValuePair> params) {
		if (params == null)
			return null;

		String body = URLEncodedUtils.format(params, "UTF-8");
		
		if (Constants.DEBUG) {
			Log.d("BaseAutoReloginTask", body);
		}
		
		return body;
	}

	private String getNewParam() {
		if (mParams == null) {
			return null;
		}

		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		for (BasicNameValuePair item : mParams) {
			if (item.getName().equals("token")) {
				UserInfo userInfo = UserManager.getInstance().getUserInfo();
				if(userInfo!=null)
					params.add(new BasicNameValuePair("access_token", userInfo.getToken()));
			} else {
				params.add(item);
			}
		}

		return getParam(params);
	}

	@Override
	public void deliverError(VolleyError error) {
		// 如果token过期，自动重新登录
		if (error != null && (error instanceof ErrorInfo)) {
			// mLastResponseError=error;
			ErrorInfo info = (ErrorInfo) error;
			if (info.getErrorCode() == ErrorInfo.ERROR_TOKEN_TIMEOUT) {
				Stat.onEvent(MyApplication.getAppContext(), Stat.EVENT_TOKEN_TIMEOUT);
				if (mRetryLoginCount < MAX_RETRY_COUNT) {
					Stat.onEvent(MyApplication.getAppContext(), Stat.EVENT_TOKEN_TIMEOUT_AND_RELOGIN);
					// TODO:使用手机号验证码注册，由于服务端会随机生成密码，客户端不知道密码，自动登录刷新token会出现密码错误，此时应该logout
					mRetryLoginCount++;

					final UserManager accountManager = UserManager.getInstance();
					accountManager.addLoginResultListener(new UserManager.LoginResultListener() {

								@Override
								public void onLoginResult(LoginResult result) {
									// 接收到通知了，移除listener
									accountManager
											.removeLoginResultListener(this);
									// 只要任务没被取消，不管是否重新登录成功，都再做一次任务
									if (!BaseAutoReloginTask.this.isCanceled()) {
										// 更新token信息
										if(getMethod()== Method.POST){
											BaseAutoReloginTask.this.setBody(getNewParam());
											Log.d(TAG, getNewParam());
										}
										
										MyApplication.getRequestQueue().add(BaseAutoReloginTask.this);
									}

								}
							});
					accountManager.autoLogin();

					return;
				} else {
					UserManager.getInstance().logout(false);
				}
			}
		}

		onError(error);
	}

	private void onError(VolleyError error) {
		if (mErrorListener != null && !isCanceled()) {
			mErrorListener.onErrorResponse(error);
		}
	}

}
