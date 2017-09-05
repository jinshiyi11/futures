package com.shuai.futures.logic;

import android.content.Context;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserManager {
	private final static String TAG = UserManager.class.getSimpleName();

	private static UserManager mSelf;
	private Context mContext;
    private AtomicBoolean mIsLogining=new AtomicBoolean(false);
    private boolean mIsLogined;

	public static UserManager getInstance() {
		if (mSelf != null)
			return mSelf;

		mSelf = new UserManager();
		return mSelf;
	}

	public void init(Context context) {
		mContext = context;
	}

    public boolean isLogined() {
        return mIsLogined;
    }

    public void autoLogin() {

    }

}
