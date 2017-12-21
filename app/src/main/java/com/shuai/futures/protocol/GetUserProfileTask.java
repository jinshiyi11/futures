package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.UserInfo;
import com.shuai.futures.logic.UserManager;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * 获取用户的基本信息
 */
public class GetUserProfileTask extends
		BaseAutoReloginTask<GetUserProfileTask.UserProfile> {
	private final static String TAG = GetUserProfileTask.class.getSimpleName();

	static class UserProfile {
		@SerializedName("phone")
        String mPhone;
		
		@SerializedName("nickName")
        String mNickName;
		
		@SerializedName("headImageUrl")
        String mHeadUrl;
	}

	public GetUserProfileTask(Context context, ErrorListener errorListener) {
		super(Method.GET, UrlHelper.getUrl(context,"api/user/profile"),
				getBody(context), new Listener<UserProfile>() {

					@Override
					public void onResponse(UserProfile response) {
						UserInfo userInfo = UserManager.getInstance().getUserInfo();
						if(userInfo!=null){
							userInfo.setPhoneNumber(response.mPhone);
							userInfo.setNickName(response.mNickName);
							userInfo.setHeadImageUrl(response.mHeadUrl);
							
							EventBus.getDefault().post(userInfo);
							
						}else{
							if(Constants.DEBUG)
								Log.e(TAG, "userInfo is null");
						}

					}

				}, errorListener);
	}

	private static List<BasicNameValuePair> getBody(Context context) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

		UserInfo accountInfo = UserManager.getInstance().getUserInfo();

		if(accountInfo!=null){
			params.add(new BasicNameValuePair("uid", String.valueOf(accountInfo.getUid())));
			params.add(new BasicNameValuePair("access_token", accountInfo.getToken()));
		}

		UrlHelper.addCommonParameters(context, params);
		return params;
	}

	@Override
	protected Response<UserProfile> parseNetworkResponse(
			NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			if (Constants.DEBUG) {
				Log.d(TAG, jsonString);
			}
			
			JsonParser parser=new JsonParser();
			JsonObject root = parser.parse(jsonString).getAsJsonObject();
			ErrorInfo error = ProtocolUtils.getProtocolInfo(root);
			if (error.getErrorCode() != 0) {
				return Response.error(error);
			}
			
			Gson gson = new Gson();
			UserProfile result = gson.fromJson(root.getAsJsonObject(ProtocolUtils.DATA), UserProfile.class);

			return Response.success(result,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}

	}

}
