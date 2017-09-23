package com.shuai.futures.view.chart;


import android.graphics.Canvas;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 *
 */
public class LineYAxisRenderer extends YAxisRenderer {
    private double mBasePrice;
    private int mUpColor;
    private int mDownColor;

    public LineYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
    }

    public void setBasePrice(double basePrice) {
        mBasePrice = basePrice;
    }

    public void setColor(int upColor, int downColor) {
        mUpColor = upColor;
        mDownColor = downColor;
    }

    @Override
    protected float[] getTransformedPositions() {
        //positions里面按顺序存放的x,y坐标
        float[] positions = super.getTransformedPositions();
        if (positions != null) {
            if (positions.length >= 2) {
                //使底部的label显示在chart内部,positions[1]表示其y坐标
                positions[1] = positions[1] - mYAxis.getTextSize() * 0.6f;

                if (positions.length >= 4) {
                    //使顶部的label显示在chart内部
                    positions[positions.length - 1] = positions[positions.length - 1] + mYAxis.getTextSize() * 0.9f;
                }
            }
        }
        return positions;
    }

    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        //设置上涨和下跌的颜色

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);

        // draw
        for (int i = from; i < to; i++) {
            String text = mYAxis.getFormattedLabel(i);
            if (mBasePrice > 0) {
                if (mYAxis.mEntries[i] > mBasePrice) {
                    mAxisLabelPaint.setColor(mUpColor);
                } else if (mYAxis.mEntries[i] < mBasePrice) {
                    mAxisLabelPaint.setColor(mDownColor);
                }else{
                    mAxisLabelPaint.setColor(mYAxis.getTextColor());
                }
            }

            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
        }
    }
}
