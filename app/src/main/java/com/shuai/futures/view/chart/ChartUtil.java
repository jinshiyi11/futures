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
     * @param dataList 数据源
     * @param maN      几日均线
     * @return 均线数据
     */
    public static double[] getMa(double[] dataList, int maN) {
        double[] result = null;
        if (dataList == null) {
            return result;
        }

        int nLen = dataList.length;
        result = new double[nLen];
        if (nLen > 0) {
            double sum = 0;
            for (int i = 0; i < nLen; ++i) {
                if (i < maN - 1) {
                    result[i] = dataList[i];
                } else if (i == maN - 1) {
                    for (int j = 0; j < maN; j++) {
                        sum += dataList[j];
                    }
                    result[i] = sum / maN;
                } else {
                    // i > maN - 1
                    sum = sum + dataList[i] - dataList[i - maN];
                    result[i] = sum / maN;
                }
            }
        }
        return result;
    }
}
