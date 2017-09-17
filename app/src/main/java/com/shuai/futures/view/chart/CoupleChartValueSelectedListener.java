package com.shuai.futures.view.chart;


import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

/**
 *
 */
public class CoupleChartValueSelectedListener implements OnChartValueSelectedListener {
    private OnChartValueSelectedListener mListener;
    private Chart mDestChart;

    public CoupleChartValueSelectedListener(Chart destChart,OnChartValueSelectedListener listener) {
        this.mListener = listener;
        this.mDestChart = destChart;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        //TODO:优化
        Highlight highlight = new Highlight(h.getX(), Float.NaN, h.getDataSetIndex());
        if(mDestChart instanceof CombinedChart) {
            CombinedData data = ((CombinedChart) mDestChart).getData();
            highlight.setDataIndex(data.getDataIndex(data.getCandleData()));
        }

        mDestChart.highlightValue(highlight, false);
        if (mListener != null) {
            mListener.onValueSelected(e, h);
        }
    }

    @Override
    public void onNothingSelected() {
        mDestChart.highlightValue(null, false);
        if (mListener != null) {
            mListener.onNothingSelected();
        }
    }
}
