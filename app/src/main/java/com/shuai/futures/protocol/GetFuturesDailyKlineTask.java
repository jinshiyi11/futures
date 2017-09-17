package com.shuai.futures.protocol;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.KlineItem;
import com.shuai.futures.view.chart.KlineType;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.umeng.analytics.pro.dm.i;

/**
 * 获取日线数据
 */
public class GetFuturesDailyKlineTask extends BaseTask<List<KlineItem>> {
    private static final String TAG = GetFuturesDailyKlineTask.class.getSimpleName();
    private SimpleDateFormat mFormat;
    private KlineType mKlineType;

    public GetFuturesDailyKlineTask(Context context, String futureId, KlineType klineType, Listener<List<KlineItem>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, getUrl(futureId, klineType), null, listener, errorListener);
        mKlineType = klineType;
    }

    /**
     * http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesDailyKLine?symbol=M0
     *
     * @param futureId
     * @param klineType
     * @return
     */
    private static String getUrl(String futureId, KlineType klineType) {
        StringBuilder url = null;
        switch (klineType) {
            case K5Minutes:
                url = new StringBuilder("http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesMiniKLine5m?symbol=");
                break;
            case K15Minutes:
                url = new StringBuilder("http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesMiniKLine15m?symbol=");
                break;
            case K30Minutes:
                url = new StringBuilder("http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesMiniKLine30m?symbol=");
                break;
            case K60Minutes:
                url = new StringBuilder("http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesMiniKLine60m?symbol=");
                break;
            case KDaily:
                url = new StringBuilder("http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesDailyKLine?symbol=");
                break;
        }
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

            JSONArray root = new JSONArray(jsonString);
            List<KlineItem> result = new ArrayList<>(root.length());
            if (mKlineType == KlineType.KDaily) {
                //日线的数据从旧到新
                if (mFormat == null) {
                    mFormat = new SimpleDateFormat("yy-MM-dd");
                }
                for (int i = 0; i < root.length(); ++i) {
                    JSONArray itemJson = root.getJSONArray(i);
                    KlineItem item = new KlineItem();
                    item.mDateString = itemJson.getString(0);
                    item.mDate = mFormat.parse(item.mDateString);
                    item.mOpen = Double.parseDouble(itemJson.getString(1));
                    item.mHigh = Double.parseDouble(itemJson.getString(2));
                    item.mLow = Double.parseDouble(itemJson.getString(3));
                    item.mClose = Double.parseDouble(itemJson.getString(4));
                    item.mVolume = Integer.parseInt(itemJson.getString(5));
                    result.add(item);
                }
            } else {
                //分钟线的数据从新到旧
                if (mFormat == null) {
                    mFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                }
                for (int i = root.length() - 1; i >= 0; --i) {
                    JSONArray itemJson = root.getJSONArray(i);
                    KlineItem item = new KlineItem();
                    item.mDateString = itemJson.getString(0);
                    item.mDate = mFormat.parse(item.mDateString);
                    item.mOpen = Double.parseDouble(itemJson.getString(1));
                    item.mHigh = Double.parseDouble(itemJson.getString(2));
                    item.mLow = Double.parseDouble(itemJson.getString(3));
                    item.mClose = Double.parseDouble(itemJson.getString(4));
                    item.mVolume = Integer.parseInt(itemJson.getString(5));
                    result.add(item);
                }
            }

            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
