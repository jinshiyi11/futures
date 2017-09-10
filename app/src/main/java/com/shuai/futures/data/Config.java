package com.shuai.futures.data;

import android.content.Context;
import android.os.Handler;

/**
 *
 */

public class Config {
    private Context mContext;
    private Handler mHandler;
    private static Config mSelf;

    private Config() {

    }

    public void init(Context context) {
        mContext = context;
        mHandler=new Handler();
    }

    public static Config getInstance() {
        if (mSelf != null)
            return mSelf;

        mSelf = new Config();
        return mSelf;
    }

    public void loadConfig() {

    }

    public void saveConfig() {

    }

    public String getVerifyCodeRegex() {
        return "测试 验证码：(\\d{4})";
    }

    public String getLastLogoutAccount() {
        return null;
    }
}
