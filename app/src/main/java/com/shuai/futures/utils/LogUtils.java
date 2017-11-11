package com.shuai.futures.utils;

import android.util.Log;

/**
 *
 */

public class LogUtils {
    private static final boolean LOG=true;

    public static void e(String tag, String msg) {
        if(LOG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if(LOG){
            Log.e(tag,msg,tr);
        }
    }

}
