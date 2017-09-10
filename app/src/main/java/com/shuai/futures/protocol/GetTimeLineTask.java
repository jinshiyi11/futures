package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.TimeLineItem;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取分时线数据
 */
public class GetTimeLineTask extends BaseTask<List<TimeLineItem>> {
    private static final String TAG = GetTimeLineTask.class.getSimpleName();

    public GetTimeLineTask(Context context, String futureId, Listener<List<TimeLineItem>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, getUrl(futureId), null, listener, errorListener);
    }

    private static String getUrl(String futureId) {
        StringBuilder url = new StringBuilder("http://stock2.finance.sina.com.cn/futures/api/jsonp.php//InnerFuturesNewService.getMinLine?symbol=");

        url.append(futureId);
        return url.toString();
    }

    @Override
    protected Response<List<TimeLineItem>> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, "UTF-8");
            if (Constants.DEBUG) {
                Log.d(TAG, jsonString);
            }
            jsonString = jsonString.substring(jsonString.indexOf('(') + 1, jsonString.lastIndexOf(')'));

            List<TimeLineItem> result = new ArrayList<>();
            JSONArray root = new JSONArray(jsonString);
            for (int i = 0; i < root.length(); i++) {
                JSONArray itemJson = root.getJSONArray(i);
                TimeLineItem item = new TimeLineItem();
                item.mMinuteSecond = itemJson.getString(0);
                item.mCurrentPrice = Double.parseDouble(itemJson.getString(1));
                item.mAveragePrice = Double.parseDouble(itemJson.getString(2));
                item.mTurnover = Integer.parseInt(itemJson.getString(3));
                item.mVolume = Integer.parseInt(itemJson.getString(4));
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
