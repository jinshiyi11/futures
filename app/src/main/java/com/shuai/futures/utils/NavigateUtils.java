package com.shuai.futures.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.shuai.futures.data.Constants;
import com.shuai.futures.ui.LoginActivity;
import com.shuai.futures.ui.SearchActivity;

/**
 *
 */

public class NavigateUtils {
    public static final int TAB_FOLLOWED = 0;
    public static final int TAB_MARKET = 1;
    public static final int TAB_NEWS = 2;
    public static final int TAB_USER = 3;

    /**
     * 显示登录界面
     *
     * @param context
     */
    public static void showLoginActivity(Context context, Intent targetIntent) {
        showLoginActivity(context, targetIntent, -1);
    }

    public static void showLoginActivity(Context context, Intent targetIntent, int requestCode) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(Constants.EXTRA_LOGIN_TARGET_INTENT, targetIntent);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            context.startActivity(intent);
        }
    }

    public static void showSearchActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }
}
