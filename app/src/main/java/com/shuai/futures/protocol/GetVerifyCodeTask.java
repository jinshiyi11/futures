package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.shuai.futures.data.Constants;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;


/**
 * 请求服务端发送验证码
 */
public class GetVerifyCodeTask extends BaseTask<Void> {
	private final static String TAG=GetVerifyCodeTask.class.getSimpleName();

	public GetVerifyCodeTask(Context context, String phone, Listener<Void> listener, ErrorListener errorListener) {
		super(Method.POST, UrlHelper.getUrl(context,"register/phone_get_verify_code"), getBody(context, phone), listener, errorListener);
	}
	
	private static String getBody(Context context, String phone){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("zxtype", "phone"));
        params.add(new BasicNameValuePair("username", phone));
        UrlHelper.addCommonParameters(context, params);
        
        String body= URLEncodedUtils.format(params, "UTF-8");
		if(Constants.DEBUG){
            Log.d(TAG, body);
        }
		return body;
	}

	@Override
	protected Response<Void> parseNetworkResponse(NetworkResponse response) {
		try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if(Constants.DEBUG){
                Log.d(TAG, jsonString);
            }
            
            JSONObject root=new JSONObject(jsonString);
//            ErrorInfo error=ProtocolUtils.getProtocolInfo(root);
//            if(error.getErrorCode()!=0){
//            	return Response.error(error);
//            }

            return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
	}

}
