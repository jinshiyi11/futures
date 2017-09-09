package com.shuai.futures.view.chart;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;

/**
 *
 */

public class MyBarChart extends BarChart {
    public MyBarChart(Context context) {
        super(context);
        initParams();
    }

    public MyBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams();
    }

    public MyBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initParams();
    }

    protected void initParams() {
//        setDoubleTapToZoomEnabled(false);
//        setPinchZoom(false);
        setScaleEnabled(false);
        setNoDataText(null);
        setDescription(null);
        getLegend().setEnabled(false);
        getXAxis().setEnabled(false);
        YAxis axisLeft = getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        YAxis axisRight = getAxisRight();
        axisRight.setDrawGridLines(false);
        axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        setDrawBorders(true);
        setMinOffset(0);

        setAutoScaleMinMaxEnabled(true);
//        setVisibleXRangeMaximum(60);
    }

//    @Override
//    public void setData(BarData data) {
//        if(data!=null){
//            data.setBarWidth(6);
//        }
//        super.setData(data);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
