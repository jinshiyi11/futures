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
    public static boolean SERVER_ONLINE = false;

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

    /**
     * 因为注册和找回密码很相似，所以使用同一activity实现，通过该参数控制是显示注册页面还是找回密码页面
     */
    public static final String EXTRA_IS_FIND_APSSWORD="extra_is_find_password";

    /**
     * 注册和修改也共用同一个界面
     */
    public static final String EXTRA_IS_MODIFY_APSSWORD="extra_is_modify_password";

    /**
     * 登录成功后要执行的intent
     */
    public static final String EXTRA_LOGIN_TARGET_INTENT="extra_login_target_intent";

    /**
     *
     */
    public static final String EXTRA_TARGET_INTENT_ACTIVITY="extra_target_intent_activity";

    public static final String EXTRA_URL="extra_url";

    public static final String EXTRA_IMAGE_URL="extra_image_url";

    static {
        if (!SERVER_ONLINE) {
            SERVER_ADDRESS = "http://192.168.1.102:8081";
        } else {
            SERVER_ADDRESS = "http://hehedream.duapp.com";
        }
    }
}
