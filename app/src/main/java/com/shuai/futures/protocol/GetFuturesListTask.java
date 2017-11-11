package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;

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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GetFuturesListTask extends BaseTask<List<FuturesInfo>> {
    private static final String TAG = GetFuturesListTask.class.getSimpleName();

    public GetFuturesListTask(Context context, Listener<List<FuturesInfo>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, UrlHelper.getUrl(context, "api/getFuturesList"),
                null, listener, errorListener);
    }

    @Override
    protected Response<List<FuturesInfo>> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, "UTF-8");
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
            Type type = new TypeToken<ArrayList<FuturesInfo>>() {
            }.getType();
            List<FuturesInfo> result = gson.fromJson(root.get(ProtocolUtils.DATA), type);
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
