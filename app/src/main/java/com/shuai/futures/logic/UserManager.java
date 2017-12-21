package com.shuai.futures.logic;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shuai.futures.MyApplication;
import com.shuai.futures.data.Config;
import com.shuai.futures.data.LoginStateChanged;
import com.shuai.futures.data.UserInfo;
import com.shuai.futures.protocol.BaseAutoReloginTask;
import com.shuai.futures.protocol.ErrorInfo;
import com.shuai.futures.protocol.LoginByAccountTask;
import com.shuai.futures.protocol.LoginResult;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.protocol.TokenInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class UserManager {
	private final static String TAG = UserManager.class.getSimpleName();

	public final static int NOT_LOGIN = 0;

	private static UserManager mSelf;
	private Context mContext;
	private Config mConfig;
    private AtomicBoolean mIsLogining=new AtomicBoolean(false);
    private boolean mIsLogined;
	private UserInfo mAccountInfo;
	//使用CopyOnWriteArrayList允许在接收通知的时候移除listener
	private List<LoginResultListener> mLoginResultListeners = new CopyOnWriteArrayList<LoginResultListener>();
	private List<LogoutListener> mLogoutListeners = new CopyOnWriteArrayList<LogoutListener>();

	public interface LoginResultListener {
		void onLoginResult(LoginResult result);
	}

	public interface LogoutListener {
		void onLogout();
	}

	public static UserManager getInstance() {
		if (mSelf != null)
			return mSelf;

		mSelf = new UserManager();
		return mSelf;
	}

	private UserManager() {
		mConfig=Config.getInstance();
	}

	public void addLoginResultListener(LoginResultListener listener) {
		mLoginResultListeners.add(listener);
	}

	public void removeLoginResultListener(LoginResultListener listener) {
		mLoginResultListeners.remove(listener);
	}

	public void addLogoutResultListener(LogoutListener listener) {
		mLogoutListeners.add(listener);
	}

	public void removeLogoutResultListener(LogoutListener listener) {
		mLogoutListeners.remove(listener);
	}

	public void init(Context context) {
		mContext = context;
		String phone = mConfig.getPhone();
		if(!TextUtils.isEmpty(phone)){
			mAccountInfo=new UserInfo();
			mAccountInfo.setPhoneNumber(phone);
			mAccountInfo.setPassword(mConfig.getPassword());
		}

	}

    public boolean isLogined() {
        return mIsLogined;
    }

    public UserInfo getUserInfo() {
        return mAccountInfo;
    }

	private void notifyLoginResult(LoginResult result) {
		boolean loginStateChanged=false;
		if(result.isLoginSuccess()){
			loginStateChanged=mIsLogined==false;
			mIsLogined=true;
		}else{
			//登录失败,用户名或密码错误
			if(result.getErrorCode()== ErrorInfo.ERROR_PASSWORD_NOT_CORRECT || result.getErrorCode()==ErrorInfo.ERROR_USERNAME_NOT_EXIST){
				logout(false);
			}
		}

		for (LoginResultListener listener : mLoginResultListeners) {
			listener.onLoginResult(result);
		}

		if(loginStateChanged)
			EventBus.getDefault().post(LoginStateChanged.Logined);
	}

	public void logout(boolean byUser) {
		if (mAccountInfo != null){
			Config.getInstance().setLastPhone(mAccountInfo.getPhoneNumber());
		}

		if(byUser) {
			//如果是用户主动logout，则清除用户相关信息
			mAccountInfo = null;
		}

		if(!mIsLogined)
			return;

		boolean loginStateChanged=mIsLogined==true;
		mIsLogined=false;

		//取消当前未执行并且需要用户登录信息的请求，因为此时用户已经logout了
		MyApplication.getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {

			@Override
			public boolean apply(Request<?> request) {
				//TODO:add log
				if((request instanceof BaseAutoReloginTask<?>) || (request instanceof LoginByAccountTask))
					return true;
				else
					return false;
			}
		});

		for (LogoutListener listener : mLogoutListeners) {
			listener.onLogout();
		}

		if(loginStateChanged)
			EventBus.getDefault().post(LoginStateChanged.Logout);

	}

	/**
	 * 自动登录，发现token过期之后，重新获取token
	 */
	public void autoLogin() {
		if(mAccountInfo==null)
			return;

		int loginType = mAccountInfo.getLoginType();
		String account = mAccountInfo.getPhoneNumber();
		if(TextUtils.isEmpty(account))
			return;

		if (mIsLogining.get()) {
			return;
		}

		mIsLogining.set(true);
		Request<?> request =  new LoginByAccountTask(mContext, account,
				mAccountInfo.getPassword(), new Response.Listener<TokenInfo>() {

			@Override
			public void onResponse(TokenInfo tokenInfo) {
				mIsLogining.set(false);
				mAccountInfo.setUid(tokenInfo.getUid());
				mAccountInfo.setToken(tokenInfo.getToken());
				notifyLoginResult(new LoginResult());
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mIsLogining.set(false);

				ErrorInfo info = ProtocolUtils.getErrorInfo(error);
				LoginResult result = new LoginResult(
						info.getErrorCode(), info.getErrorMessage());
				notifyLoginResult(result);
			}
		});

		RequestQueue requestQueue = MyApplication.getRequestQueue();
		request.setTag(this);
		requestQueue.add(request);
	}

    public void onRegisterByPhoneSuccess(long uid,String token,String phone,String md5Password){
        mAccountInfo = new UserInfo();
        mAccountInfo.setUid(uid);
        mAccountInfo.setToken(token);
        mAccountInfo.setPassword(md5Password);
        mAccountInfo.setPhoneNumber(phone);

        mConfig.setPhoneAndPassword(phone,md5Password);
        notifyLoginResult(new LoginResult());

        EventBus.getDefault().post(mAccountInfo);
    }

    public void onLoginByPhoneSuccess(long uid,String phone,String md5Password, String token) {
        if(mAccountInfo==null){
            mAccountInfo = new UserInfo();
        }

        mAccountInfo.setUid(uid);
        mAccountInfo.setPhoneNumber(phone);
        mAccountInfo.setPassword(md5Password);
        mAccountInfo.setToken(token);

		mConfig.setPhoneAndPassword(phone,md5Password);
        notifyLoginResult(new LoginResult());

        EventBus.getDefault().post(mAccountInfo);
    }

}
