package com.shuai.futures.view.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.shuai.futures.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MyCombinedChart extends CombinedChart {
    /**
     * 是否显示MA5
     */
    private boolean mShowMa5 = true;

    private boolean mShowMa10 = true;

    private boolean mShowMa20 = true;

    private boolean mShowMa30 = true;

    public MyCombinedChart(Context context) {
        super(context);
        initParam();
    }

    public MyCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParam();
    }

    public MyCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initParam();
    }

    private void initParam() {
        setDrawOrder(new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
        });

        setRendererLeftYAxis(new LineYAxisRenderer(mViewPortHandler, mAxisLeft, mLeftAxisTransformer));
        setRendererRightYAxis(new LineYAxisRenderer(mViewPortHandler, mAxisRight, mRightAxisTransformer));

        setScaleEnabled(false);

        setNoDataText(null);
        setDescription(null);
        getLegend().setEnabled(false);
        setScaleEnabled(false);
        setPinchZoom(false);
        setDrawBorders(true);
        setBorderColor(getResources().getColor(R.color.chart_border));

        XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(4, true);
        xAxis.setTextColor(getResources().getColor(R.color.chart_label));
        xAxis.setYOffset(0);

        getAxisRight().setEnabled(false);
        YAxis axisLeft = getAxisLeft();
        axisLeft.setTextColor(getResources().getColor(R.color.chart_label));
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeft.setLabelCount(3, true);

        setMinOffset(0);
        setAutoScaleMinMaxEnabled(true);
    }

    @Override
    public void notifyDataSetChanged() {
        CombinedData combinedData = getData();
        if (combinedData != null) {
            CandleData candleData = combinedData.getCandleData();
            if (candleData != null && candleData.getEntryCount() > 0) {
                LineData lineData = new LineData();
                MyCandleDataSet dataSet = (MyCandleDataSet) candleData.getDataSetByIndex(0);
                List<CandleEntry> values = dataSet.getValues();

                double[] dataList = new double[values.size()];
                for (int i = 0; i < dataList.length; i++) {
                    dataList[i] = (double) values.get(i).getClose();
                }
                if (mShowMa5) {
                    LineDataSet lineDataSet = new LineDataSet(createMa(dataList,5), null);
                    lineDataSet.setColor(getResources().getColor(R.color.chart_ma5));
                    lineDataSet.setDrawCircleHole(false);
                    lineDataSet.setDrawCircles(false);
                    lineData.addDataSet(lineDataSet);
                }

                if (mShowMa10) {
                    LineDataSet lineDataSet = new LineDataSet(createMa(dataList,10), null);
                    lineDataSet.setColor(getResources().getColor(R.color.chart_ma10));
                    lineDataSet.setDrawCircleHole(false);
                    lineDataSet.setDrawCircles(false);
                    lineData.addDataSet(lineDataSet);
                }

                if (mShowMa20) {
                    LineDataSet lineDataSet = new LineDataSet(createMa(dataList,20), null);
                    lineDataSet.setColor(getResources().getColor(R.color.chart_ma20));
                    lineDataSet.setDrawCircleHole(false);
                    lineDataSet.setDrawCircles(false);
                    lineData.addDataSet(lineDataSet);
                }

                if (mShowMa30) {
                    LineDataSet lineDataSet = new LineDataSet(createMa(dataList,30), null);
                    lineDataSet.setColor(getResources().getColor(R.color.chart_ma30));
                    lineDataSet.setDrawCircleHole(false);
                    lineDataSet.setDrawCircles(false);
                    lineData.addDataSet(lineDataSet);
                }

                if (lineData.getDataSetCount() > 0) {
                    combinedData.setData(lineData);
                }
            }
        }
        super.notifyDataSetChanged();
    }

    private List<Entry> createMa(double[] list,int maN) {
        double[] values = ChartUtil.getMa(list, maN);
        List<Entry> entryList = new ArrayList<>(values.length);
        for (int i = 0; i < values.length; i++) {
            entryList.add(new Entry(i, (float) values[i]));
        }
        return entryList;
    }
}
