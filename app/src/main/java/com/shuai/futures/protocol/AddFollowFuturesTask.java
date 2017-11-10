package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.FuturesInfo;
import com.shuai.futures.data.UserInfo;
import com.shuai.futures.logic.UserManager;

import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 添加关注
 */
public class AddFollowFuturesTask extends BaseAutoReloginTask<ErrorInfo> {
    private static final String TAG = AddFollowFuturesTask.class.getSimpleName();

    public AddFollowFuturesTask(Context context, String futuresId, Listener<ErrorInfo> listener, Response.ErrorListener errorListener) {
        super(Method.POST, UrlHelper.getUrl(context, "api/addFollowFutures"),
                getBody(context, futuresId), listener, errorListener);
    }

    private static List<BasicNameValuePair> getBody(Context context, String futuresId) {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("futuresId", futuresId));
        UserInfo accountInfo = UserManager.getInstance().getUserInfo();
        params.add(new BasicNameValuePair("uid", String.valueOf(accountInfo.getUid())));
        params.add(new BasicNameValuePair("token", accountInfo.getToken()));

        UrlHelper.addCommonParameters(context, params);
        return params;
    }

    @Override
    protected Response<ErrorInfo> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, "UTF-8");
            if (Constants.DEBUG) {
                Log.d(TAG, jsonString);
            }

            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(jsonString).getAsJsonObject();
            ErrorInfo result = ProtocolUtils.getProtocolInfo(root);
            if (result.getErrorCode() != 0) {
                return Response.error(result);
            }

            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
