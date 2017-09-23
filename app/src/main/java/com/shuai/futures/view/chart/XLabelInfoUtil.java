package com.shuai.futures.view.chart;

import com.shuai.futures.MyApplication;

import java.util.List;
import java.util.Map;

import static android.R.id.list;

/**
 *
 */

public class XLabelInfoUtil {
    public static XLabelInfo getXLabelInfo(String id) {
        XLabelInfo result = null;
        int i = 0;
        for (; i < id.length(); ++i) {
            if (id.charAt(i) >= '0' && id.charAt(i) <= '9') {
                break;
            }
        }
        String idInfo = id.substring(0, i).toLowerCase();
        Map<String, XLabelInfo> map = MyApplication.getXLabelMap();
        result = map.get(idInfo);

        return result;
    }
}
