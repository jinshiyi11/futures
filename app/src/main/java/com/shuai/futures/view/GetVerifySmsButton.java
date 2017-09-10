package com.shuai.futures.view;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.shuai.futures.MyApplication;
import com.shuai.futures.SMSBroadcastReceiver;
import com.shuai.futures.data.Config;
import com.shuai.futures.protocol.GetVerifyCodeTask;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.ui.ToastDialog;
import com.shuai.futures.utils.Utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取验证码按钮
 */
public class GetVerifySmsButton extends Button implements SMSBroadcastReceiver.MessageListener {

	private Handler mHandler = new Handler();
	private Runnable mCallback;
	private final static int TIME_OUT=60;
	private int mTimeOut;
	private String mOriginalText;
	private SMSBroadcastReceiver mReceiver;
	private RequestQueue mRequestQueue= MyApplication.getRequestQueue();
	
	private VerifyCodeListener mVerifyCodeListener;

	public interface VerifyCodeListener  {
        public void onVerifyCode(String verfiyCode);
    }
	
	public GetVerifySmsButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GetVerifySmsButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GetVerifySmsButton(Context context) {
		super(context);
	}
	
	public void setVerifyCodeListener(VerifyCodeListener verifyCodeListener){
		mVerifyCodeListener=verifyCodeListener;
	}

	/**
	 * 请求发送验证码
	 * @param phone
	 */
	public void requestVerifyCode(String phone) {
//		Stat.onEvent(getContext(), Stat.EVENT_GET_VERIFY_CODE_CLICK);
		setEnabled(false);
		mTimeOut=TIME_OUT;
		mOriginalText=getText().toString();
		stopReceiver();
		stopCountDown();
		
		IntentFilter filter=new IntentFilter();
		filter.addAction(SMSBroadcastReceiver.SMS_RECEIVED_ACTION);
		mReceiver=new SMSBroadcastReceiver();
		mReceiver.setOnReceivedMessageListener(this);
		getContext().registerReceiver(mReceiver, filter);
		
		GetVerifyCodeTask request=new GetVerifyCodeTask(getContext(), phone, new Listener<Void>() {

			@Override
			public void onResponse(Void arg0) {
				//Utils.showShortToast(getContext(), "验证码已发送");
				ToastDialog dlg=new ToastDialog(getContext());
				dlg.show();
				dlg.setMessage("验证码已发送");
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
//				Utils.showShortToast(getContext(), ProtocolUtils.getErrorInfo(error).getErrorMessage());
//				setEnabled(true);
//				setText(mOriginalText);
//				stopCountDown();
//				stopReceiver();

				//// TODO: 2017/9/10
				ToastDialog dlg=new ToastDialog(getContext());
				dlg.show();
				dlg.setMessage("验证码已发送");
			}
		});
		request.setTag(getContext());       
        mRequestQueue.add(request);
		
		mCallback = new Runnable() {

			@Override
			public void run() {
				mTimeOut--;
				if(mTimeOut>0){
					setText(String.format("%ds",mTimeOut));
					mHandler.postDelayed(mCallback, 1000);
				}
				else{
					setText(mOriginalText);
					setEnabled(true);
				}
			}
		};

		mHandler.postDelayed(mCallback, 1000);
	}
	
	/**
	 * 停止倒计时
	 */
	private void stopCountDown(){
		if (mCallback != null){
			mHandler.removeCallbacks(mCallback);
			mCallback=null;
		}
	}
	
	private void stopReceiver(){
		if(mReceiver!=null){
			getContext().unregisterReceiver(mReceiver);
			mReceiver=null;
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		stopCountDown();
		stopReceiver();
		super.onDetachedFromWindow();
	}

	@Override
	public void onMessage(String sender, String message, long time) {
		if(TextUtils.isEmpty(message))
			return;
		
		String verifyCodeRegex= Config.getInstance().getVerifyCodeRegex();
		if(!TextUtils.isEmpty(verifyCodeRegex)){
			Pattern pattern = Pattern.compile(verifyCodeRegex);
	        Matcher matcher = pattern.matcher(message);
	        if (matcher.find()) {
	        	setEnabled(true);
				setText(mOriginalText);
				stopCountDown();
				stopReceiver();
				
	        	String verfiyCode=matcher.group(1);
	        	if(mVerifyCodeListener!=null)
	        		mVerifyCodeListener.onVerifyCode(verfiyCode);
	        }
		}
	}

}
