package com.shuai.futures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.Constants;
import com.shuai.futures.ui.base.BaseActivity;
import com.shuai.futures.utils.UiUtils;
import com.shuai.futures.utils.Utils;
import com.shuai.futures.view.GetVerifySmsButton;


/**
 * 注册界面,修改密码界面，找回密码界面共用该activity
 */
public class RegisterByPhoneActivity extends BaseActivity implements
        OnClickListener {
	private final String TAG = RegisterByPhoneActivity.class.getSimpleName();

	private LinearLayout mLlName;
	private GetVerifySmsButton mBtnGetVerifyCode;
	private Button mBtnRegister;
	private EditText mEtPhone;
	private LinearLayout mLlVerifyCode;
	private EditText mEtVerifyCode;
	private LinearLayout mLlPassword;
	private EditText mEtPassword;
	//private EditText mEtConfirmPassword;

	private Handler mHandler = new Handler();
	private RequestQueue mRequestQueue;
	private boolean mIsFindPassword;
	private boolean mIsModifyPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_by_phone);
		
		((TextView)findViewById(R.id.tv_title)).setText("一键注册");
		Intent intent=getIntent();
		mIsFindPassword=intent.getBooleanExtra(Constants.EXTRA_IS_FIND_APSSWORD, false);
		mIsModifyPassword=intent.getBooleanExtra(Constants.EXTRA_IS_MODIFY_APSSWORD, false);
		mRequestQueue= MyApplication.getRequestQueue();

		mLlName=(LinearLayout) findViewById(R.id.ll_name);
		mBtnGetVerifyCode = (GetVerifySmsButton) findViewById(R.id.btn_get_verify_sms);
		mBtnRegister = (Button) findViewById(R.id.btn_register);
		mEtPhone = (EditText) findViewById(R.id.et_phone);
		mLlVerifyCode=(LinearLayout) findViewById(R.id.ll_verify_sms);
		mEtVerifyCode = (EditText) findViewById(R.id.et_sms);
		mLlPassword=(LinearLayout) findViewById(R.id.ll_password);
		mEtPassword = (EditText) findViewById(R.id.et_password);
		//mEtConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);

		mBtnGetVerifyCode.setOnClickListener(this);
		mBtnRegister.setOnClickListener(this);
		
		UiUtils.showSoftInput(mContext, mEtPhone);
		
		if(mIsFindPassword){
			((TextView)findViewById(R.id.tv_title)).setText("找回密码");
			mEtPassword.setHint("请输入新密码");
			mBtnRegister.setText("找回密码");
		}else if(mIsModifyPassword){
			((TextView)findViewById(R.id.tv_title)).setText("修改密码");
			mEtPassword.setHint("请输入新密码");
			mBtnRegister.setText("确认修改");
		}
		
		mEtPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
      	  
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
                      
                    onBtnRegisterClicked();
                      
                    return true;    
                }  
                return false;  
            }  
              
        });  
	}

	@Override
	protected void onDestroy() {
		mRequestQueue.cancelAll(this);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_get_verify_sms:
			onBtnGetVerifySmsClicked();
			break;
		case R.id.btn_register:
			onBtnRegisterClicked();
			break;
		default:
			break;
		}
	}

	/**
	 * 用户点击了获取验证码
	 */
	private void onBtnGetVerifySmsClicked() {
		String phone = mEtPhone.getText().toString();
		if (!Utils.checkPhoneNumber(phone)) {
			Utils.showShortToast(this, "请输入正确的手机号");
			Utils.shakeView(mLlName);
			return;
		}
		
		mBtnGetVerifyCode.requestVerifyCode(phone);
		mBtnGetVerifyCode.setVerifyCodeListener(new GetVerifySmsButton.VerifyCodeListener() {
			
			@Override
			public void onVerifyCode(String verfiyCode) {
				mEtVerifyCode.setText(verfiyCode);
			}
		});
		
	}

	/**
	 * 用户点击了注册按钮
	 */
	private void onBtnRegisterClicked() {
//		if(mIsFindPassword)
//			Stat.onEvent(mContext, Stat.EVENT_FIND_PASSWORD_CLICK);
//
//		if(mIsModifyPassword)
//			Stat.onEvent(mContext, Stat.EVENT_MIDIFY_PASSWORD_CLICK);
		
		final String phone = mEtPhone.getText().toString();
		if (!Utils.checkPhoneNumber(phone)) {
			Utils.showShortToast(this, "请输入正确的手机号");
			Utils.shakeView(mLlName);
			return;
		}

		String verifyCode = mEtVerifyCode.getText().toString();
		if (TextUtils.isEmpty(verifyCode)) {
			Utils.showShortToast(this, "请输入短信验证码");
			Utils.shakeView(mLlVerifyCode);
			return;
		}

		String password = mEtPassword.getText().toString();
		if (TextUtils.isEmpty(password)) {
			Utils.showShortToast(this, "请输入密码");
			Utils.shakeView(mLlPassword);
			return;
		}
		
		if(password.length()<6){
			Utils.showShortToast(this, "密码长度至少为6个字符");
			Utils.shakeView(mLlPassword);
			return;
		}

//		String confirmPassword = mEtConfirmPassword.getText().toString();
//		if (TextUtils.isEmpty(confirmPassword)) {
//			Utils.showShortToast(this, "两次输入的密码不匹配");
//			return;
//		}

//		final String md5Password=Utils.md5(password);
//		RegisterByPhoneTask request=new RegisterByPhoneTask(this,phone,verifyCode,md5Password,null,new Listener<TokenInfo>() {
//
//			@Override
//			public void onResponse(TokenInfo result) {
//				if(mIsFindPassword){
//					Utils.showLongToast(mContext,"成功找回密码");
//				}else if(mIsModifyPassword){
//					Utils.showLongToast(mContext,"成功修改密码");
//				}
//
//				UserManager.getInstance().onRegisterByPhoneSuccess(result.getUid(),result.getToken(),phone, md5Password);
//				finish();
//			}
//		},new ErrorListener(){
//
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				Utils.showLongToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
//			}
//
//		});
//		request.setTag(this);
//        mRequestQueue.add(request);
	}

}
