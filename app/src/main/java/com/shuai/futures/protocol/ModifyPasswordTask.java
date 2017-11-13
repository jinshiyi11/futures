package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.UserInfo;
import com.shuai.futures.logic.UserManager;


import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * 修改密码
 */
public class ModifyPasswordTask extends BaseAutoReloginTask<Void> {
	public ModifyPasswordTask(Context context, String oldMd5Password, String newMd5Password, Listener<Void> listener,
                              ErrorListener errorListener) {
		super(Request.Method.POST, UrlHelper.getUrl(context,"app/UserApi/get_order_list"),
				getBody(context, oldMd5Password,newMd5Password), listener, errorListener);
	}
	
	private static List<BasicNameValuePair> getBody(Context context, String oldMd5Password, String newMd5Password) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		
		UserInfo accountInfo = UserManager.getInstance().getUserInfo();
		
		params.add(new BasicNameValuePair("uid", String.valueOf(accountInfo.getUid())));
		params.add(new BasicNameValuePair("access_token", accountInfo.getToken()));
		params.add(new BasicNameValuePair("old_password", oldMd5Password));
		params.add(new BasicNameValuePair("new_password", newMd5Password));

		UrlHelper.addCommonParameters(context, params);
		return params;
	}

	@Override
	protected Response<Void> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			if (Constants.DEBUG) {
				Log.d(TAG, jsonString);
			}
			
			JsonParser parser=new JsonParser();
			JsonObject root = parser.parse(jsonString).getAsJsonObject();
			ErrorInfo error = ProtocolUtils.getProtocolInfo(root);
			if (error.getErrorCode() != 0) {
				return Response.error(error);
			}
		

			return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

}
