package com.shuai.futures.protocol;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shuai.futures.MyApplication;
import com.shuai.futures.data.Constants;
import com.umeng.analytics.social.UMSocialService;


import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 通过微信注册
 * 先登录到微信，拿到微信的token，然后把微信的token传到我们自己的服务器验证，服务端到微信的服务器验证该token并返回本系统的uid和token以及服务端为用户分配的随机密码
 */
public class RegisterByWeixinTask {
	private final static String WEIXIN_LOGIN_URL="";
	private AtomicBoolean mCanceled;

	private Context mContext;
	private Listener<RegisterResult> mListener;
	private ErrorListener mErrorListener;
	
	private RequestQueue mRequestQueue;
	private String mWeixinId;

	/**
	 * 拿到微信的token后，到app的服务端验证并登陆
	 *  
	 */
	private class LoginToServerTask extends BaseTask<RegisterResult> {
		private final String TAG=getClass().getSimpleName();

		public LoginToServerTask(Context context, String token) {
			super(Method.GET, UrlHelper.getUrl(context,"api/register"),RegisterByWeixinTask.getBody(context,token), mListener, mErrorListener);
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
	
	private static String getBody(Context context, String token){
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("client", "phone"));
        params.add(new BasicNameValuePair("token", token));
        
        UrlHelper.addCommonParameters(context, params);
		return URLEncodedUtils.format(params, "UTF-8");
	}

	public RegisterByWeixinTask(Context context, Listener<RegisterResult> listener, ErrorListener errorListener) {
		mContext=context;
		mListener=listener;
		mErrorListener=errorListener;
		
		mRequestQueue= MyApplication.getRequestQueue();
		
	}
	
	public void login(){
		loginToWeixin();
	}
	
	public void cancel(){
		mCanceled.set(true);
		if(mRequestQueue!=null)
			mRequestQueue.cancelAll(this);
	}
	
	private void loginToWeixin(){
//		UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
//		UMWXHandler wxHandler = new UMWXHandler(mContext,Constants.APP_ID_WEIXIN,Constants.APP_SECRET_WEIXIN);
//		wxHandler.addToSocialSDK();
//
//		mController.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
//		    @Override
//		    public void onStart(SHARE_MEDIA platform) {
//		        //授权开始
//		    }
//		    @Override
//		    public void onError(SocializeException e, SHARE_MEDIA platform) {
//		        //授权错误
//		    	if(mCanceled.get())
//		    		return;
//		    	mErrorListener.onErrorResponse(new ErrorInfo(ErrorInfo.ERROR_WEIXIN_OAUTH_ERROR, "微信授权错误"));
//		    }
//		    @Override
//		    public void onComplete(Bundle value, SHARE_MEDIA platform) {
//		        //授权完成
//		    	if(mCanceled.get())
//		    		return;
//		    	String token=null;
//		    	mWeixinId=value.getString("uid");
//		    	LoginToServerTask request=new LoginToServerTask(mContext,token);
//		    	request.setTag(RegisterByWeixinTask.this);
//		        mRequestQueue.add(request);
//		    }
//
//		    @Override
//		    public void onCancel(SHARE_MEDIA platform) {
//		        //授权取消
//		    	if(mCanceled.get())
//		    		return;
//		    	mErrorListener.onErrorResponse(new ErrorInfo(ErrorInfo.ERROR_WEIXIN_OAUTH_CANCEL, "微信授权被取消"));
//		    }
//		} );
	}

}
