package com.shuai.futures.protocol;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.FuturesInfo;
import com.shuai.futures.data.FuturesPrice;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取一组期货的价格信息
 */
public class GetFuturesPriceListTask extends BaseTask<List<FuturesPrice>> {
    private static final String TAG = GetFuturesPriceListTask.class.getSimpleName();

    public GetFuturesPriceListTask(Context context, List<FuturesInfo> idList, Listener<List<FuturesPrice>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, getUrl(idList), null, listener, errorListener);
        if (Constants.DEBUG) {
            Log.d(TAG, getUrl(idList));
        }
    }

    /**
     * https://hq.sinajs.cn/list=id1,id2
     *
     * @param idList
     * @return
     */
    private static String getUrl(List<FuturesInfo> idList) {
        StringBuilder url = new StringBuilder("https://hq.sinajs.cn/list=");
        for (int i = 0; i < idList.size(); i++) {
            url.append(idList.get(i).mId);
            if (i != idList.size() - 1) {
                url.append(",");
            }
        }
        return url.toString();
    }

    @Override
    protected Response<List<FuturesPrice>> parseNetworkResponse(NetworkResponse response) {
        try {
            List<FuturesPrice> result = new ArrayList<>();
            //// TODO: 2017/9/8 返回的数据是gbk格式
            String data = new String(response.data, "GBK");
            if (Constants.DEBUG) {
                Log.d(TAG, data);
            }

            String[] lines = data.split("\n");
            String id;
            String[] values;
            //var hq_str_M1801="豆粕1801,145959,2741,2750,2730,2737,2742,2743,2742,2740,2739,316,86,1822500,943724,连,豆粕,2017-09-07,1,2750.000,2690.000,2754.000,2690.000,2855.000,2690.000,2987.000,2658.000,31.911";
            for (String line : lines) {
                if(line.isEmpty()){
                    continue;
                }
                id = line.substring(line.indexOf("hq_str_") + 7, line.indexOf('='));
                values = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"')).split(",");
                FuturesPrice item = new FuturesPrice();
                item.mId = id;
                item.mName = values[0];
                item.mCurrentPrice = Double.parseDouble(values[8]);
                item.mLastdayPrice = Double.parseDouble(values[10]);

                item.mHigh = Double.parseDouble(values[3]);
                item.mLow = Double.parseDouble(values[4]);
                item.mOpen = Double.parseDouble(values[2]);
                item.mVolume = Integer.parseInt(values[13]);
                result.add(item);
            }

            if (result.size() == 0) {
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
