package com.shuai.futures.protocol;

import com.android.volley.VolleyError;

@SuppressWarnings("serial")
public class ErrorInfo extends VolleyError {
    private static final String TAG = ErrorInfo.class.getSimpleName();

    private final int mErrorCode;
    private final String mErrorMessage;

    /**
     * 有些错误可能包含详细信息，用该字段存储
     */
    private String mErrorDetail;

    /**
     * token过期
     */
    public final static int ERROR_TOKEN_TIMEOUT = 1;

    /**
     * 登录的用户名不存在
     */
    public final static int ERROR_USERNAME_NOT_EXIST = 102;

    /**
     * 登录密码错误
     */
    public final static int ERROR_PASSWORD_NOT_CORRECT = 103;

    /**
     * 客户端保留错误码起始值
     */
    private final static int RESERVED_ERROR_BASE = -10000;

    /**
     * 未知错误
     */
    public final static int ERROR_UNKONW = RESERVED_ERROR_BASE;

    //微信授权错误
    public final static int ERROR_WEIXIN_OAUTH_ERROR = RESERVED_ERROR_BASE + 1;

    //微信授权被取消
    public final static int ERROR_WEIXIN_OAUTH_CANCEL = RESERVED_ERROR_BASE + 2;

    /**
     * 网络连接异常
     */
    public final static int ERROR_NERWORK_CONNECTION = RESERVED_ERROR_BASE + 3;

    /**
     * 网络连接超时
     */
    public final static int ERROR_NERWORK_TIMEOUT = RESERVED_ERROR_BASE + 4;

    /**
     * 返回的数据有问题，如缺字段或字段数据格式不对等
     */
    public final static int ERROR_DATA = RESERVED_ERROR_BASE + 5;

    /**
     * 服务器异常，若服务端返回502
     */
    public final static int ERROR_SERVER = RESERVED_ERROR_BASE + 6;

    public ErrorInfo(int errorCode, String errorMessage) {
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public String getErrorDetail() {
        return mErrorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        mErrorDetail = errorDetail;
    }

    @Override
    public String toString() {
        return "ErrorInfo [mErrorCode=" + mErrorCode + ", mErrorMessage=" + mErrorMessage + "]";
    }


}
