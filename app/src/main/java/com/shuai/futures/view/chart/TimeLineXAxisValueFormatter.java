package com.shuai.futures.view.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.shuai.futures.data.TimeLineItem;

/**
 *
 */

public class TimeLineXAxisValueFormatter implements IAxisValueFormatter {
    private MyLineChart mChart;

    public TimeLineXAxisValueFormatter(MyLineChart chart) {
        mChart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0
                && mChart.getData().getEntryCount() > 0) {
            ILineDataSet dataSet = mChart.getData().getDataSetByIndex(0);
            int index = (int) value - (int) dataSet.getEntryForIndex(0).getX();
            if (index >= 0 && index < dataSet.getEntryCount()) {
                Entry entry = dataSet.getEntryForIndex(index);
                TimeLineItem data = (TimeLineItem) entry.getData();

                return data.mMinuteSecond;
            }
        }
        return String.valueOf(value);
    }
}
