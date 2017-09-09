package com.shuai.futures.data;

import com.shuai.futures.BuildConfig;

/**
 *
 */

public class Constants {
    public static boolean DEBUG = BuildConfig.DEBUG;

    /**
     * 是使用线上服务还是开发环境的服务
     */
    public static boolean SERVER_ONLINE = true;

    public static final String SERVER_ADDRESS;

    /**
     * 主界面显示哪个tab
     */
    public static final String EXTRA_TAB = "extra_tab";

    public static final String EXTRA_FUTURES_ID = "futures_id";
    public static final String EXTRA_FUTURES_NAME = "futures_name";

    /**
     * 标明请求来自android客户端
     */
    public static final String DEVICE_INFO = "android";

    public static final int PROTOCOL_VERSION = 1;

    static {
        if (!SERVER_ONLINE) {
            SERVER_ADDRESS = "http://192.168.1.102:8080";
        } else {
            SERVER_ADDRESS = "http://hehedream.duapp.com";
        }
    }
}
