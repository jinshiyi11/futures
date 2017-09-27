package com.shuai.futures.protocol;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shuai.futures.data.TimeLineItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class XDataProcesser {
    private static final String TAG = XDataProcesser.class.getSimpleName();
    private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

    private static class FixInfo {
        public XLabelInfo.HourMinute start;
        public int count;
    }

    private XDataProcesser() {

    }

    private Map<String, XLabelInfo> mXLabelInfoMap;
    private static XDataProcesser mSelf;

    public static synchronized XDataProcesser getInstance() {
        if (mSelf == null) {
            mSelf = new XDataProcesser();
        }
        return mSelf;
    }

    public synchronized void setXLabelInfoMap(String data) {
        try {
            Map<String, XLabelInfo> map = new HashMap<>();
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(data).getAsJsonArray();
            for (int i = 0; i < array.size(); ++i) {
                JsonObject element = (JsonObject) array.get(i);
                String[] keys = element.get("key").getAsString().toLowerCase().split(",");
                JsonElement dataElement = element.get("data");
                XLabelInfo item = gson.fromJson(dataElement, XLabelInfo.class);
                JsonArray timeRangesJson = dataElement.getAsJsonObject().getAsJsonArray("timeRanges");
                if (timeRangesJson != null) {
                    List<XLabelInfo.Range> ranges = new ArrayList<>();
                    for (int j = 0; j < timeRangesJson.size(); j++) {
                        XLabelInfo.Range range = new XLabelInfo.Range();
                        JsonArray rangeJson = timeRangesJson.get(j).getAsJsonArray();
                        String[] start = rangeJson.get(0).getAsString().split(":");
                        range.start = new XLabelInfo.HourMinute(Integer.parseInt(start[0]), Integer.parseInt(start[1]));
                        String[] end = rangeJson.get(1).getAsString().split(":");
                        XLabelInfo.HourMinute endHourMinute = new XLabelInfo.HourMinute(Integer.parseInt(end[0]), Integer.parseInt(end[1]));
                        if (endHourMinute.hour < range.start.hour) {
                            //跨过了00:00，需要拆分成2段
                            range.end = new XLabelInfo.HourMinute(23, 59);
                            ranges.add(range);

                            range = new XLabelInfo.Range();
                            range.start = new XLabelInfo.HourMinute(0, 0);
                            range.end = endHourMinute;
                            ranges.add(range);
                        } else {
                            range.end = endHourMinute;
                            ranges.add(range);
                        }
                    }
                    item.setTimeRanges(ranges);
                }
                for (String key : keys) {
                    if (map.get(key) != null) {
                        Log.e(TAG, "duplicate key!");
                    }
                    map.put(key, item);
                }
            }

            mXLabelInfoMap = map;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    public synchronized void setXLabelInfoMap(Map<String, XLabelInfo> map) {
        mXLabelInfoMap = map;
    }

    public synchronized XLabelInfo getXLabelInfo(String id) {
        XLabelInfo result = null;
        if (mXLabelInfoMap == null) {
            return result;
        }

        int i = 0;
        for (; i < id.length(); ++i) {
            if (id.charAt(i) >= '0' && id.charAt(i) <= '9') {
                break;
            }
        }
        String idInfo = id.substring(0, i).toLowerCase();
        result = mXLabelInfoMap.get(idInfo);

        return result;
    }

    /**
     * 补齐数据
     *
     * @param itemList
     * @param futureId
     * @return
     */
    public List<TimeLineItem> fixData(List<TimeLineItem> itemList, String futureId) {
        if (itemList == null || itemList.size() == 0) {
            return itemList;
        }

        XLabelInfo xLabelInfo = getXLabelInfo(futureId);
        if (xLabelInfo == null || xLabelInfo.getTimeRanges() == null) {
            return itemList;
        }

        List<TimeLineItem> result = new ArrayList<>(itemList.size());
        for (int i = 0; i < itemList.size() - 1; ++i) {
            TimeLineItem item = itemList.get(i);
            List<FixInfo> toAdd = check(item, itemList.get(i + 1), xLabelInfo);
            result.add(item);
            for (FixInfo info : toAdd) {
                //补齐缺失的
                XLabelInfo.HourMinute hourMinute = info.start;
                for (int j = 0; j < info.count; j++) {
                    TimeLineItem newItem = (TimeLineItem) item.clone();
                    newItem.mHourMinute = String.format("%02d:%02d", hourMinute.hour, hourMinute.minute);
                    try {
                        item.mDate = mFormat.parse(item.mHourMinute);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    result.add(newItem);
                    hourMinute = hourMinute.addOneMinute();
                }
            }
        }
        result.add(itemList.get(itemList.size() - 1));
        return result;
    }

    private List<FixInfo> check(TimeLineItem startItem, TimeLineItem endItem, XLabelInfo xLabelInfo) {
        List<FixInfo> result = new ArrayList<>();
        XLabelInfo.HourMinute start = new XLabelInfo.HourMinute(startItem.mDate.getHours(), startItem.mDate.getMinutes());
        XLabelInfo.HourMinute end = new XLabelInfo.HourMinute(endItem.mDate.getHours(), endItem.mDate.getMinutes());
        if (start.isBefore(end)) {
            return result;
        }

        List<XLabelInfo.Range> timeRanges = xLabelInfo.getTimeRanges();
        int startIndex = -1;
        for (int i = 0; i < timeRanges.size(); ++i) {
            FixInfo fixInfo = null;
            XLabelInfo.Range range = timeRanges.get(i);
            if (range.inRange(start)) {
                startIndex = i;
                fixInfo = new FixInfo();
                fixInfo.start = start.addOneMinute();
                if (range.inRange(end)) {
                    fixInfo.count = end.sub(fixInfo.start);
                    result.add(fixInfo);
                    break;
                } else {
                    fixInfo.count = range.end.sub(start);
                }
            } else if (startIndex >= 0) {
                fixInfo = new FixInfo();
                fixInfo.start = range.start;
                if (range.inRange(end)) {
                    fixInfo.count = end.sub(fixInfo.start);
                    result.add(fixInfo);
                    break;
                } else {
                    fixInfo.count = range.end.sub(range.start) + 1;
                }
            }
            if (fixInfo != null) {
                result.add(fixInfo);
            }
        }

        return result;
    }
}
