package com.shuai.futures.protocol;

import android.util.Log;

import com.shuai.futures.data.Constants;
import com.shuai.futures.data.KlineItem;
import com.shuai.futures.view.chart.KlineType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 *
 */
public class XueqiuKlineConverter implements Converter<ResponseBody, List<KlineItem>> {
    private static final String TAG = XueqiuKlineConverter.class.getSimpleName();
    private SimpleDateFormat mFormat;

    @Override
    public List<KlineItem> convert(ResponseBody value) throws IOException {
        String jsonString = value.string();
        if (Constants.DEBUG) {
            Log.d(TAG, jsonString);
        }

        List<KlineItem> result = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONArray root = obj.getJSONArray("chartlist");

            //日线的数据从旧到新
            if (mFormat == null) {
                mFormat = new SimpleDateFormat("yy-MM-dd");
            }
            for (int i = 0; i < root.length(); ++i) {
                JSONObject itemJson = root.getJSONObject(i);
                KlineItem item = new KlineItem();
                item.mDate = new Date(itemJson.getLong("timestamp"));
                item.mDateString = mFormat.format(item.mDate);
                item.mOpen = itemJson.getDouble("open");
                item.mHigh = itemJson.getDouble("high");
                item.mLow = itemJson.getDouble("low");
                item.mClose = itemJson.getDouble("close");
                item.mVolume = itemJson.getInt("volume");
                result.add(item);
            }
        } catch (Exception e) {
            //todo:
        }
        return result;
    }

    public static class Factory extends retrofit2.Converter.Factory {
        private XueqiuKlineConverter mConverter = new XueqiuKlineConverter();

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof XueqiuKlineItem) {
                    return mConverter;
                }
            }
            return null;
        }
    }
}
