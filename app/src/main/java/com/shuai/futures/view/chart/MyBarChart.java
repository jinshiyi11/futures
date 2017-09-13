package com.shuai.futures.view.chart;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.shuai.futures.R;

import java.text.DecimalFormat;

/**
 *
 */

public class MyBarChart extends BarChart {
    public static class YAxisValueFormatter implements IAxisValueFormatter {
        private YAxis.AxisDependency mDependency;
        private DecimalFormat mFormat;

        public YAxisValueFormatter(YAxis.AxisDependency dependency) {
            this.mDependency = dependency;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            String result = "";
            float[] entries = axis.mEntries;
            if (entries != null && entries.length >= 2) {
                if (mDependency == YAxis.AxisDependency.LEFT) {
                    if (value == entries[entries.length - 1]) {
                        result = "持仓量";
                    }
                } else {
                    if (mFormat == null) {
                        mFormat = new DecimalFormat("#.##");
                    }
                    if (value == entries[entries.length - 1]) {
                        result = mFormat.format(value);
                    }
                }
            }

            return result;
        }
    }

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
        setRendererLeftYAxis(new LineYAxisRenderer(mViewPortHandler, mAxisLeft, mLeftAxisTransformer));
        setRendererRightYAxis(new LineYAxisRenderer(mViewPortHandler, mAxisRight, mRightAxisTransformer));
//        setDoubleTapToZoomEnabled(false);
//        setPinchZoom(false);
        setScaleEnabled(false);
        setNoDataText(null);
        setDescription(null);
        setBorderColor(getResources().getColor(R.color.chart_border));
        getLegend().setEnabled(false);

        getXAxis().setEnabled(false);
        YAxis axisLeft = getAxisLeft();
        axisLeft.setTextColor(getResources().getColor(R.color.chart_label));
        axisLeft.setDrawGridLines(false);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeft.setLabelCount(1, true);
        axisLeft.setValueFormatter(new YAxisValueFormatter(YAxis.AxisDependency.LEFT));

        YAxis axisRight = getAxisRight();
        axisRight.setTextColor(getResources().getColor(R.color.chart_label));
        axisRight.setDrawGridLines(false);
        axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRight.setLabelCount(1, true);
        axisRight.setValueFormatter(new YAxisValueFormatter(YAxis.AxisDependency.RIGHT));

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
