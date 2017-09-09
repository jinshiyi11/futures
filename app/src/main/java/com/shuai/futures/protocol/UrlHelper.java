package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;


import com.shuai.futures.data.Constants;
import com.shuai.futures.utils.AppUtils;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

/**
 * 协议url辅助拼接类
 */
public class UrlHelper {
    private static final String TAG = UrlHelper.class.getSimpleName();

    public static String getUrl(Context context, String relativePath) {
        return getUrl(context, relativePath, null);
    }

    public static String getUrl(Context context, String relativePath, List<BasicNameValuePair> params) {
        if (params == null) {
            params = new LinkedList<BasicNameValuePair>();
        }
        UrlHelper.addCommonParameters(context, params);

        StringBuilder builder = new StringBuilder(Constants.SERVER_ADDRESS);
        builder.append("/").append(relativePath);
        String query = params == null ? "" : URLEncodedUtils.format(params, "UTF-8");
        if (query.length() != 0)
            builder.append("?").append(query);

        String url = builder.toString();
        if (Constants.DEBUG) {
            Log.d(TAG, url);
        }

        return url;
    }

    /**
     * 增加公共参数，如版本号
     *
     * @param params
     */
    public static void addCommonParameters(Context context, List<BasicNameValuePair> params) {
        params.add(new BasicNameValuePair("version", AppUtils.getVersionName(context)));
        params.add(new BasicNameValuePair("version_code", String.valueOf(AppUtils.getVersionCode(context))));
        params.add(new BasicNameValuePair("protocol_version", String.valueOf(Constants.PROTOCOL_VERSION)));
        params.add(new BasicNameValuePair("channel", AppUtils.getChannel(context)));
        params.add(new BasicNameValuePair("device", Constants.DEVICE_INFO));
    }

}
