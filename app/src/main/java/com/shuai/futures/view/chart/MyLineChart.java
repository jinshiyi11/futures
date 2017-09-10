package com.shuai.futures.view.chart;


import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

/**
 *
 */

public class MyLineChart extends LineChart {
    public MyLineChart(Context context) {
        super(context);
        initParams();
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }

    public MyLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initParams();
    }

    protected void initParams() {
        setScaleEnabled(false);

        setNoDataText(null);
        setDescription(null);
        getLegend().setEnabled(false);
        setDrawBorders(true);
        setBorderColor(0xffcfd5e0);
        setBorderWidth(1);
        XAxis xAxis = getXAxis();
        YAxis axisLeft = getAxisLeft();
        YAxis axisRight = getAxisRight();
        xAxis.setLabelCount(3, true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axisLeft.setLabelCount(3, true);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRight.setLabelCount(3, true);
        axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        setMinOffset(0);
        setAutoScaleMinMaxEnabled(true);
    }
}
