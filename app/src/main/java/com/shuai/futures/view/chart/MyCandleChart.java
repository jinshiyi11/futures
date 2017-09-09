package com.shuai.futures.view.chart;


import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import static android.R.attr.label;

/**
 *
 */
public class MyCandleChart extends CandleStickChart {
    public MyCandleChart(Context context) {
        super(context);
        initParams();
    }

    public MyCandleChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }

    public MyCandleChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initParams();
    }

    protected void initParams(){
        setScaleEnabled(false);

        setNoDataText(null);
        setDescription(null);
        getLegend().setEnabled(false);
        setScaleEnabled(false);
        setPinchZoom(false);
        setDrawBorders(true);
        XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(4);
        getAxisRight().setEnabled(false);
        YAxis axisLeft = getAxisLeft();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeft.setLabelCount(3);

        setMinOffset(0);
        setAutoScaleMinMaxEnabled(true);

//        setVisibleXRangeMaximum(60);
    }
}
