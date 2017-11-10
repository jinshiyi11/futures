package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shuai.futures.data.Constants;
import com.shuai.futures.logic.UserManager;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * 通过用户名密码方式登录
 */
public class LoginByAccountTask extends BaseTask<TokenInfo> {
    private final static String TAG = LoginByAccountTask.class.getSimpleName();

    public LoginByAccountTask(Context context, int loginType, String account,
                              String password, Listener<TokenInfo> listener,
                              ErrorListener errorListener) {
        super(Method.POST, UrlHelper.getUrl(context, "api/login"),
                getBody(context, loginType, account, password), listener,
                errorListener);
    }

    private static String getBody(Context context, int loginType,
                                  String account, String password) {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        if (loginType == UserManager.LOGIN_BY_PHONE) {
            params.add(new BasicNameValuePair("client", "phone"));
            params.add(new BasicNameValuePair("username", account));
            params.add(new BasicNameValuePair("password", password));
        } else {
            params.add(new BasicNameValuePair("client", "weixin"));
            params.add(new BasicNameValuePair("uid", account));
            params.add(new BasicNameValuePair("password", password));
        }

        UrlHelper.addCommonParameters(context, params);
        String body = URLEncodedUtils.format(params, "UTF-8");
        if (Constants.DEBUG) {
            Log.d(TAG, body);
        }
        return body;
    }

    @Override
    protected Response<TokenInfo> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (Constants.DEBUG) {
                Log.d(TAG, jsonString);
            }

            JsonParser parser=new JsonParser();
            JsonObject root = parser.parse(jsonString).getAsJsonObject();
            ErrorInfo error = ProtocolUtils.getProtocolInfo(root);
            if (error.getErrorCode() != 0) {
                return Response.error(error);
            }

            Gson gson = new Gson();
            TokenInfo result = gson.fromJson(root.get(ProtocolUtils.DATA), TokenInfo.class);

            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

}
