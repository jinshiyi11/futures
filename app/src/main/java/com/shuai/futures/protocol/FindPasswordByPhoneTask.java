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
import com.shuai.futures.data.Constants;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * 使用手机号+验证码找回密码
 */
public class FindPasswordByPhoneTask extends BaseTask<RegisterResult> {
	private final static String TAG=FindPasswordByPhoneTask.class.getSimpleName();

	public FindPasswordByPhoneTask(Context context, String phone, String verifyCode, String password,
                                   Listener<RegisterResult> listener, ErrorListener errorListener) {
		super(Method.POST, UrlHelper.getUrl(context,"api/register"),getBody(context,phone,verifyCode,password), listener, errorListener);
	}
	
	private static String getBody(Context context, String phone, String verifyCode, String password){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("client", "phone"));
        params.add(new BasicNameValuePair("username", phone));
        params.add(new BasicNameValuePair("verify_code", verifyCode));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("find_password", "1"));
        
        UrlHelper.addCommonParameters(context, params);
        String body= URLEncodedUtils.format(params, "UTF-8");
		if(Constants.DEBUG){
            Log.d(TAG, body);
        }
		return body;
	}

	@Override
	protected Response<RegisterResult> parseNetworkResponse(NetworkResponse response) {
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
            
            Gson gson=new Gson();
            RegisterResult result=gson.fromJson(root.get(ProtocolUtils.DATA), RegisterResult.class);
            
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
	}

}
