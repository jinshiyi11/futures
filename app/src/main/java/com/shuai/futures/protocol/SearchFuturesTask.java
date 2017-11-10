package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
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

public class SearchFuturesTask extends BaseTask<List<FuturesInfo>> {
    private static final String TAG = SearchFuturesTask.class.getSimpleName();

    public SearchFuturesTask(Context context, String key, Listener<List<FuturesInfo>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, getUrl(context, key), null, listener, errorListener);
    }

    private static String getUrl(Context context, String key) {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("key", key));
        return UrlHelper.getUrl(context, "api/searchFutures", params);
    }

    @Override
    protected Response<List<FuturesInfo>> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, "UTF-8");
            if (Constants.DEBUG) {
                Log.d(TAG, jsonString);
            }

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<FuturesInfo>>() {
            }.getType();
            List<FuturesInfo> result = gson.fromJson(jsonString, type);
            if (result == null) {
                return Response.error(new ParseError());
            }

            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
