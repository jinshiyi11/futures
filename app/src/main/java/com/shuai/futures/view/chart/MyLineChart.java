package com.shuai.futures.view.chart;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.shuai.futures.R;
import com.shuai.futures.protocol.XLabelInfo;

/**
 *
 */
public class MyLineChart extends LineChart {
    private double mBasePrice = 0;
    private LineYAxisRenderer mLeftRender;
    private LineYAxisRenderer mRightRender;
    private LineLeftAxisValueFormatter mLeftValueFormatter;
    private LineRightAxisValueFormatter mRightValueFormatter;
    private KlineType mKlineType;


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
        mRenderer = new MyLineChartRenderer(this, mAnimator, mViewPortHandler);
        mChartTouchListener=new MyChartTouchListener(this, mViewPortHandler.getMatrixTouch(), 3f);
        setHighlightPerTapEnabled(false);
        mLeftRender=new LineYAxisRenderer(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
        setRendererLeftYAxis(mLeftRender);
        mRightRender=new LineYAxisRenderer(mViewPortHandler, mAxisRight, mRightAxisTransformer);
        setRendererRightYAxis(mRightRender);

        setScaleEnabled(false);

        setNoDataText(null);
        setDescription(null);
        getLegend().setEnabled(false);
        setDrawBorders(true);
        setBorderColor(getResources().getColor(R.color.chart_border));
        setBorderWidth(1);

        XAxis xAxis = getXAxis();
        xAxis.setValueFormatter(new TimeLineXAxisValueFormatter(this));
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextColor(getResources().getColor(R.color.chart_label));
        xAxis.setLabelCount(3, true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setYOffset(0);
        xAxis.setGridColor(getResources().getColor(R.color.chart_grid));

        YAxis axisLeft = getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setTextColor(getResources().getColor(R.color.chart_label));
        axisLeft.setLabelCount(3, true);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        mLeftValueFormatter=new LineLeftAxisValueFormatter();
        axisLeft.setValueFormatter(mLeftValueFormatter);

        YAxis axisRight = getAxisRight();
        axisRight.setLabelCount(3, true);
        axisRight.setTextColor(getResources().getColor(R.color.chart_label));
        axisRight.setDrawGridLines(false);
        axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        mRightValueFormatter =new LineRightAxisValueFormatter();
        axisRight.setValueFormatter(mRightValueFormatter);

        setMinOffset(0);
        setExtraBottomOffset(5);
        setAutoScaleMinMaxEnabled(true);
    }

    public void setBasePrice(double basePrice) {
        mBasePrice = basePrice;
        mRightValueFormatter.setBasePrice(mBasePrice);
        mLeftRender.setBasePrice(mBasePrice);
        mRightRender.setBasePrice(mBasePrice);
        int upColor=getResources().getColor(R.color.up);
        int downColor=getResources().getColor(R.color.down);
        mLeftRender.setColor(upColor,downColor);
        mRightRender.setColor(upColor,downColor);

        LimitLine baseLine = new LimitLine((float) mBasePrice);
        baseLine.setLineColor(getResources().getColor(R.color.chart_base_line));
        baseLine.enableDashedLine(5f, 5f, 0f);
        getAxisLeft().removeAllLimitLines();
        getAxisLeft().addLimitLine(baseLine);
    }

    public void setKlineType(KlineType klineType) {
        mKlineType = klineType;

//        getXAxis().setValueFormatter(new KlineXAxisValueFormatter(this, mKlineType));
        setXAxisRenderer(new MyXAxisRenderer(this, mKlineType, mViewPortHandler, mXAxis, mLeftAxisTransformer));
    }

    public void setXLabelInfo(XLabelInfo xLabelInfo) {
        MyXAxisRenderer xAxisRenderer = (MyXAxisRenderer) getRendererXAxis();
        xAxisRenderer.setXLabelInfo(xLabelInfo);
    }

    @Override
    public void notifyDataSetChanged() {
        LineData data = getData();
        if (data != null && data.getEntryCount() > 0 && mBasePrice > 0) {
            float yMin = data.getYMin();
            float yMax = data.getYMax();
            float absMax = (float) Math.abs(yMax - mBasePrice);
            float absMin = (float) Math.abs(yMin - mBasePrice);

            if (absMax > absMin) {
                yMin = (float) (mBasePrice - absMax);
            } else {
                yMax = (float) (mBasePrice + absMin);

            }

            getAxisLeft().setAxisMinimum(yMin);
            getAxisRight().setAxisMinimum(yMin);
            getAxisLeft().setAxisMaximum(yMax);
            getAxisRight().setAxisMaximum(yMax);
        }
        super.notifyDataSetChanged();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
