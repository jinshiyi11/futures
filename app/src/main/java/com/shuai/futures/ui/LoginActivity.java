package com.shuai.futures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.Config;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.Stat;
import com.shuai.futures.logic.UserManager;
import com.shuai.futures.protocol.LoginByAccountTask;
import com.shuai.futures.protocol.LoginResult;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.protocol.RegisterByWeixinTask;
import com.shuai.futures.protocol.RegisterResult;
import com.shuai.futures.protocol.TokenInfo;
import com.shuai.futures.ui.base.BaseActivity;
import com.shuai.futures.utils.UiUtils;
import com.shuai.futures.utils.Utils;
import com.shuai.futures.view.EditTextWithClear;


/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private Context mContext;
	//登录成功后要执行的intent
	private Intent mLoginTargetIntent;
	
	
    private TextView mTvRegister;
    private TextView mTvFindPassword;
    private LinearLayout mLlName;
    private EditTextWithClear mEtAccount;
    private LinearLayout mLlPassword;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private LinearLayout mLlWeixinLogin;
    
    private RequestQueue mRequestQueue;
    private RegisterByWeixinTask mLoginByWeixinTask;
    private UserManager.LoginResultListener mLoginResultListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	mContext=this;
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        
        mRequestQueue= MyApplication.getRequestQueue();
        mLoginTargetIntent=getIntent().getParcelableExtra(Constants.EXTRA_LOGIN_TARGET_INTENT);
        
        mTvRegister=(TextView) findViewById(R.id.tv_register);
        mTvFindPassword=(TextView) findViewById(R.id.tv_find_password);
        mTvRegister.setOnClickListener(this);
        mTvFindPassword.setOnClickListener(this);
        
        mLlName=(LinearLayout) findViewById(R.id.ll_name);
        mEtAccount=(EditTextWithClear) findViewById(R.id.et_account);
        mLlPassword=(LinearLayout) findViewById(R.id.ll_password);
        mEtPassword=(EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mLlWeixinLogin=(LinearLayout) findViewById(R.id.ll_weixin_login);
        mBtnLogin.setOnClickListener(this);
        mLlWeixinLogin.setOnClickListener(this);
        
        String lastLogoutAccount = Config.getInstance().getLastPhone();
        if(!TextUtils.isEmpty(lastLogoutAccount)){
        	mEtAccount.setText(lastLogoutAccount);
        	
        	UiUtils.showSoftInput(mContext, mEtPassword);
        }else{
        	UiUtils.showSoftInput(mContext, mEtAccount);    
        }
        
        mEtPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
        	  
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
                      
                    loginByAccount();  
                      
                    return true;    
                }  
                return false;  
            }  
              
        });  
        
        mLoginResultListener=new UserManager.LoginResultListener() {

			@Override
			public void onLoginResult(LoginResult result) {
				if(result.isLoginSuccess()){
					if(mLoginTargetIntent!=null){
						startActivity(mLoginTargetIntent);
					}

					setResult(RESULT_OK);
					finish();
				}

			}
		};

		UserManager.getInstance().addLoginResultListener(mLoginResultListener);
    }

    @Override
	protected void onDestroy() { 
    	if(mLoginResultListener!=null){
    		UserManager.getInstance().removeLoginResultListener(mLoginResultListener);
    	}
    	mRequestQueue.cancelAll(this);
    	if(mLoginByWeixinTask!=null) {
            mLoginByWeixinTask.cancel();
        }
    	
		super.onDestroy();
	}

	@Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id){
        case R.id.tv_register:
        {
            Intent intent=new Intent(this,RegisterByPhoneActivity.class);
            intent.putExtra(Constants.EXTRA_IS_FIND_APSSWORD, false);
            startActivity(intent);
        }
            break;
        case R.id.tv_find_password:
        {
            Intent intent=new Intent(this,RegisterByPhoneActivity.class);
            intent.putExtra(Constants.EXTRA_IS_FIND_APSSWORD, true);
            startActivity(intent);
        }
            break;
        case R.id.btn_login:
        	loginByAccount();
        	break;
        }
    }
    
    /**
     * 通过账户密码方式登录
     */
    private void loginByAccount(){
    	//Stat.onEvent(mContext, Stat.EVENT_LOGIN_CLICK);
    	final String account=mEtAccount.getText().toString();
    	final String password=mEtPassword.getText().toString();
    	
    	if (!Utils.checkPhoneNumber(account)) {
			Utils.showShortToast(this, "请输入正确的手机号");
			Utils.shakeView(mLlName);
			return;
		}
    	
    	if (TextUtils.isEmpty(password)) {
			Utils.showShortToast(this, "请输入密码");
			Utils.shakeView(mLlPassword);
			return;
		}
    	
    	final String md5Password=Utils.md5(password);
    	LoginByAccountTask request=new LoginByAccountTask(this,account,md5Password,new Listener<TokenInfo>() {

			@Override
			public void onResponse(TokenInfo tokenInfo) {
				UserManager.getInstance().onLoginByPhoneSuccess(tokenInfo.getUid(),account,md5Password, tokenInfo.getToken());
			}
		},new ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
			}

		});

		request.setTag(this);
        mRequestQueue.add(request);
    }
}
