package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.FuturesInfo;
import com.shuai.futures.data.KlineItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取日线数据
 */
public class GetFuturesDailyKlineTask extends BaseTask<List<KlineItem>> {
    private static final String TAG = GetFuturesDailyKlineTask.class.getSimpleName();

    public GetFuturesDailyKlineTask(Context context, String futureId, Listener<List<KlineItem>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, getUrl(futureId), null, listener, errorListener);
    }

    private static String getUrl(String futureId) {
        StringBuilder url = new StringBuilder("http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesDailyKLine?symbol=");
        url.append(futureId);
        return url.toString();
    }

    @Override
    protected Response<List<KlineItem>> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, "UTF-8");
            if (Constants.DEBUG) {
                Log.d(TAG, jsonString);
            }

            List<KlineItem> result = new ArrayList<>();
            JSONArray root = new JSONArray(jsonString);
            for (int i = 0; i < root.length(); i++) {
                JSONArray itemJson = root.getJSONArray(i);
                KlineItem item = new KlineItem();
                item.mDate = itemJson.getString(0);
                item.mOpen = Double.parseDouble(itemJson.getString(1));
                item.mHigh = Double.parseDouble(itemJson.getString(2));
                item.mLow = Double.parseDouble(itemJson.getString(3));
                item.mClose = Double.parseDouble(itemJson.getString(4));
                item.mVolume = Integer.parseInt(itemJson.getString(5));
                result.add(item);
            }

            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
