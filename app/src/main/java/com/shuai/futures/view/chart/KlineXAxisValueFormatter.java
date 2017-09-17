package com.shuai.futures.view.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.shuai.futures.data.KlineItem;

import java.text.SimpleDateFormat;

/**
 *
 */
public class KlineXAxisValueFormatter implements IAxisValueFormatter {
    private MyCombinedChart mChart;
    private KlineType mKlineType;
    private SimpleDateFormat mFormat;

    public KlineXAxisValueFormatter(MyCombinedChart chart, KlineType klineType) {
        mChart = chart;
        mKlineType = klineType;
        switch (mKlineType) {
            case KRealTime:
                mFormat = new SimpleDateFormat("HH:mm");
                break;
            case K5Daily:
                mFormat = new SimpleDateFormat("HH:mm");
                break;
            case K1Minutes:
                mFormat = new SimpleDateFormat("HH:mm");
                break;
            case K5Minutes:
                mFormat = new SimpleDateFormat("HH:mm");
                break;
            case K15Minutes:
                mFormat = new SimpleDateFormat("MM-dd");
                break;
            case K30Minutes:
                mFormat = new SimpleDateFormat("MM-dd");
                break;
            case K60Minutes:
                mFormat = new SimpleDateFormat("MM-dd");
                break;
            case KDaily:
                mFormat = new SimpleDateFormat("yyyy-MM");
                break;
            case KWeek:
                mFormat = new SimpleDateFormat("yyyy-MM");
                break;
            case KMonth:
                mFormat = new SimpleDateFormat("yyyy-MM");
                break;
            case KYear:
                mFormat = new SimpleDateFormat("yyyy-MM");
                break;
        }
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (mChart.getCandleData() != null && mChart.getCandleData().getDataSetCount() > 0
                && mChart.getCandleData().getEntryCount() > 0) {
            ICandleDataSet dataSet = mChart.getCandleData().getDataSetByIndex(0);
            int index = (int) value - (int) dataSet.getEntryForIndex(0).getX();
            if (index >= 0 && index < dataSet.getEntryCount()) {
                Entry entry = dataSet.getEntryForIndex(index);
                KlineItem data = (KlineItem) entry.getData();

                return mFormat.format(data.mDate);
            }
        }
        return String.valueOf(value);
    }
}
