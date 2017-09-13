package com.shuai.futures.view.chart;

import com.github.mikephil.charting.data.CandleEntry;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class ChartUtil {
    /**
     * 计算移动均线
     *
     * @param dataList
     * @param maN
     * @return
     */
    public static double[] getMa(double[] dataList, int maN) {
        double[] result = null;
        if (dataList == null) {
            return result;
        }

        int nLen = dataList.length;
        result = new double[nLen];
        if (nLen > 0) {
            for (int i = 0; i < nLen; ++i) {
                if (i < maN - 1) {
                    result[i] = dataList[i];
                } else {
                    double sum = 0;
                    for (int j = i - maN + 1; j <= i; j++) {
                        sum += dataList[j];
                    }
                    result[i] = sum / maN;
                }
            }
        }
        return result;
    }
}
