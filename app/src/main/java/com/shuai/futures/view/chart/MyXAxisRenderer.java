package com.shuai.futures.view.chart;

import android.graphics.Canvas;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.shuai.futures.data.KlineItem;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.max;

/**
 *
 */

public class MyXAxisRenderer extends XAxisRenderer {
    private Chart mChart;
    private KlineType mKlineType;

    public MyXAxisRenderer(Chart chart, KlineType klineType, ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
        mChart = chart;
        mKlineType = klineType;
    }

    @Override
    protected void computeAxisValues(float min, float max) {
        computeAxisValuesInternal(min, max);
        computeSize();
    }

    private void computeAxisValuesInternal(float min, float max) {
        float yMin = min;
        float yMax = max;

        // int labelCount = mAxis.getLabelCount();
        double range = Math.abs(yMax - yMin);

        if (/*labelCount == 0 ||*/ range <= 0 || Double.isInfinite(range)
                || mChart.getData() == null || mChart.getData().getEntryCount() == 0) {
            mAxis.mEntries = new float[]{};
            mAxis.mCenteredEntries = new float[]{};
            mAxis.mEntryCount = 0;
            return;
        }

        switch (mKlineType) {
            case KRealTime:
                break;
            case K5Daily:
                break;
            case K1Minutes:
                break;
            case K5Minutes:
                computeAxisValues5Minutes(min, max);
                break;
            case K15Minutes:
                computeAxisValuesXMinutes(min, max);
                break;
            case K30Minutes:
                computeAxisValuesXMinutes(min, max);
                break;
            case K60Minutes:
                computeAxisValuesXHours(min, max);
                break;
            case KDaily:
                computeAxisValuesXDaily(min, max);
                break;
            case KWeek:
                break;
            case KMonth:
                break;
            case KYear:
                break;
        }
    }

    private void computeAxisValues5Minutes(float min, float max) {
        ArrayList<Float> list = new ArrayList<>();
        int iMax = (int) max;
        ICandleDataSet dataSet = ((CombinedChart) mChart).getData().getCandleData().getDataSetByIndex(0);
        for (int i = (int) min; i <= iMax; i++) {
            if (i >= 0 && i < dataSet.getEntryCount()) {
                KlineItem item = (KlineItem) dataSet.getEntryForIndex(i).getData();
                if ((item.mDate.getHours() == 10
                        || item.mDate.getHours() == 14
                        || item.mDate.getHours() == 22)
                        && item.mDate.getMinutes() == 0) {
                    list.add((float) i);
                }
            }
        }

        if (mAxis.mEntries.length < list.size()) {
            // Ensure stops contains at least numStops elements.
            mAxis.mEntries = new float[list.size()];
        }

        for (int i = 0; i < list.size(); i++) {
            mAxis.mEntries[i] = list.get(i);
        }
        mAxis.mEntryCount = list.size();
    }

    private void computeAxisValuesXMinutes(float min, float max) {
        ArrayList<Integer> list = new ArrayList<>();
        int iMax = (int) max;
        int lastDay = Integer.MIN_VALUE;
        ICandleDataSet dataSet = ((CombinedChart) mChart).getData().getCandleData().getDataSetByIndex(0);
        int firstIndex = (int) dataSet.getEntryForIndex(0).getX();
        for (int i = (int) min; i <= iMax; i++) {
            if (i >= firstIndex && i < dataSet.getEntryCount()) {
                KlineItem item = (KlineItem) dataSet.getEntryForIndex(i - firstIndex).getData();
                if (lastDay == Integer.MIN_VALUE) {
                    lastDay = item.mDate.getDay();
                } else {
                    if (item.mDate.getDay() != lastDay) {
                        list.add(i);
                        lastDay = item.mDate.getDay();
                    }
                }
            }
        }

        if (mAxis.mEntries.length < list.size()) {
            // Ensure stops contains at least numStops elements.
            mAxis.mEntries = new float[list.size()];
        }

        for (int i = 0; i < list.size(); i++) {
            mAxis.mEntries[i] = list.get(i);
        }
        mAxis.mEntryCount = list.size();
    }

    private void computeAxisValuesXHours(float min, float max) {
        ArrayList<Integer> list = new ArrayList<>();
        int iMax = (int) max;
        int lastDay = Integer.MIN_VALUE;
        int lastX = -0x6fffffff;
        ICandleDataSet dataSet = ((CombinedChart) mChart).getData().getCandleData().getDataSetByIndex(0);
        int firstIndex = (int) dataSet.getEntryForIndex(0).getX();
        for (int i = (int) min; i <= iMax; i++) {
            if (i >= firstIndex && i < dataSet.getEntryCount()) {
                KlineItem item = (KlineItem) dataSet.getEntryForIndex(i - firstIndex).getData();
                if (lastDay == Integer.MIN_VALUE) {
                    lastDay = item.mDate.getDay();
                    lastX = i;
                } else {
                    if (item.mDate.getDay() != lastDay && i - lastX > 10) {
                        list.add(i);
                        lastDay = item.mDate.getDay();
                        lastX = i;
                    }
                }
            }
        }

        if (mAxis.mEntries.length < list.size()) {
            // Ensure stops contains at least numStops elements.
            mAxis.mEntries = new float[list.size()];
        }

        for (int i = 0; i < list.size(); i++) {
            mAxis.mEntries[i] = list.get(i);
        }
        mAxis.mEntryCount = list.size();
    }

    private void computeAxisValuesXDaily(float min, float max) {
        ArrayList<Integer> list = new ArrayList<>();
        int iMax = (int) max;
        int lastMonth = Integer.MIN_VALUE;
        ICandleDataSet dataSet = ((CombinedChart) mChart).getData().getCandleData().getDataSetByIndex(0);
        int firstIndex = (int) dataSet.getEntryForIndex(0).getX();
        for (int i = (int) min; i <= iMax; i++) {
            if (i >= firstIndex && i < dataSet.getEntryCount()) {
                KlineItem item = (KlineItem) dataSet.getEntryForIndex(i - firstIndex).getData();
                if (lastMonth == Integer.MIN_VALUE) {
                    lastMonth = item.mDate.getMonth();
                } else {
                    if (item.mDate.getMonth() != lastMonth) {
                        list.add(i);
                        lastMonth = item.mDate.getMonth();
                    }
                }
            }
        }

        if (mAxis.mEntries.length < list.size()) {
            // Ensure stops contains at least numStops elements.
            mAxis.mEntries = new float[list.size()];
        }

        for (int i = 0; i < list.size(); i++) {
            mAxis.mEntries[i] = list.get(i);
        }
        mAxis.mEntryCount = list.size();
    }

    @Override
    public void renderAxisLabels(Canvas c) {
        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;

        float yoffset = mXAxis.getYOffset();

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        MPPointF pointF = MPPointF.getInstance(0, 0);
        if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
            pointF.x = 0.5f;
            pointF.y = 0.0f;
            drawLabels(c, mViewPortHandler.contentBottom() + yoffset, pointF);
        } else {
            throw new IllegalArgumentException("not implement");
        }
        MPPointF.recycleInstance(pointF);
    }

    /**
     * draws the x-labels on the specified y-position
     *
     * @param pos
     */
    @Override
    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {

        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();

        float[] positions = new float[mXAxis.mEntryCount * 2];

        for (int i = 0; i < positions.length; i += 2) {

            // only fill x values
            if (centeringEnabled) {
                positions[i] = mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i] = mXAxis.mEntries[i / 2];
            }
        }

        mTrans.pointValuesToPixel(positions);

        for (int i = 0; i < positions.length; i += 2) {

            float x = positions[i];

            if (mViewPortHandler.isInBoundsX(x)) {

                String label = mXAxis.getValueFormatter().getFormattedValue(mXAxis.mEntries[i / 2], mXAxis);

                if (mXAxis.isAvoidFirstLastClippingEnabled()) {

                    // avoid clipping of the last
                    if (i / 2 == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);

                        if (width > mViewPortHandler.offsetRight() * 2
                                && x + width > mViewPortHandler.getChartWidth())
                            x = x - width / 2 - mAxis.getXOffset();

                        // avoid clipping of the first
                    } else if (i == 0) {
                        //TODO:这里x坐标会出现大于0但是很接近0的小数
                        if (x <= 1) {
                            float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                            x += width / 2 + mAxis.getXOffset();
                        }

                    }
                }

                drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);
            }
        }
    }
}
