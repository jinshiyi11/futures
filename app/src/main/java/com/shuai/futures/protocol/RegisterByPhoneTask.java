package com.shuai.futures.protocol;

import android.content.Context;
import android.text.TextUtils;
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
import com.shuai.futures.data.Constants;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * 使用手机号+验证码注册
 */
public class RegisterByPhoneTask extends BaseTask<TokenInfo> {
	private final static String TAG=RegisterByPhoneTask.class.getSimpleName();
	
	/**
	 * 
	 * @param context
	 * @param phone 手机号
	 * @param verifyCode 验证码
	 * @param password MD5密码
	 * @param inviteCode 邀请码
	 * @param listener
	 * @param errorListener
	 */
	public RegisterByPhoneTask(Context context, String phone, String verifyCode, String password, String inviteCode,
                               Listener<TokenInfo> listener, ErrorListener errorListener) {
		super(Method.POST, UrlHelper.getUrl(context,"api/registerByPhone"),getBody(context,phone,verifyCode,password,inviteCode), listener, errorListener);
	}
	
	
	
	private static String getBody(Context context, String phone, String verificationCode, String password, String inviteCode){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("client", "phone"));
        params.add(new BasicNameValuePair("username", phone));
        params.add(new BasicNameValuePair("verificationCode", verificationCode));
        //password为空，表示只使用手机号和验证码注册，与服务端动态生成密码
        if(password!=null)
        	params.add(new BasicNameValuePair("password", password));
        
        if(!TextUtils.isEmpty(inviteCode))
        	params.add(new BasicNameValuePair("market_id", inviteCode));
        
        UrlHelper.addCommonParameters(context, params);
		String body= URLEncodedUtils.format(params, "UTF-8");
		if(Constants.DEBUG){
            Log.d(TAG, body);
        }
		return body;
	}

	@Override
	protected Response<TokenInfo> parseNetworkResponse(NetworkResponse response) {
		try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if(Constants.DEBUG){
                Log.d(TAG, jsonString);
            }

			JsonParser parser=new JsonParser();
			JsonObject root = parser.parse(jsonString).getAsJsonObject();
			ErrorInfo error = ProtocolUtils.getProtocolInfo(root);
			if (error.getErrorCode() != 0) {
				return Response.error(error);
			}
            
			Gson gson = new Gson();
			TokenInfo result = gson.fromJson(root.get(ProtocolUtils.DATA), TokenInfo.class);
            
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
	}

}
