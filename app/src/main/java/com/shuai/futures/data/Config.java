package com.shuai.futures.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public class Config {
    private Context mContext;
    private SharedPreferences mPreferences;
    private static Config mSelf;
    private static final String CONFIG_NAME = "config";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_LAST_PHONE = "last_phone";
    private static final String KEY_PASSWORD = "password";

    private Config() {

    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mPreferences = mContext.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
    }

    public static Config getInstance() {
        if (mSelf != null)
            return mSelf;

        mSelf = new Config();
        return mSelf;
    }

    public String getVerifyCodeRegex() {
        return "测试 验证码：(\\d{4})";
    }

    public void setPhoneAndPassword(String phone, String password) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public String getPhone() {
        return mPreferences.getString(KEY_PHONE,null);
    }

    public String getPassword() {
        return mPreferences.getString(KEY_PASSWORD,null);
    }

    /**
     * 上次登录的手机号
     * @return
     */
    public String getLastPhone() {
        return mPreferences.getString(KEY_LAST_PHONE,null);
    }

    public void setLastPhone(String lastPhone) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(KEY_LAST_PHONE,lastPhone);
        editor.apply();
    }
}
