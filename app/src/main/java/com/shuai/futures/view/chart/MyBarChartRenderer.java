package com.shuai.futures.view.chart;


import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 *
 */

public class MyBarChartRenderer extends BarChartRenderer {
    /**
     * path that is used for drawing highlight-lines (drawLines(...) cannot be used because of dashes)
     */
    private Path mHighlightLinePath = new Path();

    /** the width of the highlight indicator lines */
    protected float mHighlightLineWidth = 0.5f;

    /** the path effect for dashed highlight-lines */
    protected DashPathEffect mHighlightDashPathEffect = null;

    public MyBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);

        mHighlightLineWidth = Utils.convertDpToPixel(0.5f);
        mHighlightPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {
        // super.drawHighlighted(c, indices);
        BarData data = mChart.getBarData();

        for (Highlight high : indices) {

            IBarDataSet set = data.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            Entry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), e.getY() * mAnimator
                    .getPhaseY());

            high.setDraw((float) pix.x, (float) pix.y);

            // draw the lines
            drawHighlightLines(c, (float) pix.x, (float) pix.y, set);
        }
    }

    protected void drawHighlightLines(Canvas c, float x, float y, IBarDataSet set) {

        // set color and stroke-width
        mHighlightPaint.setColor(set.getHighLightColor());
        mHighlightPaint.setStrokeWidth(mHighlightLineWidth/*set.getHighlightLineWidth()*/);

        // draw highlighted lines (if enabled)
        mHighlightPaint.setPathEffect(mHighlightDashPathEffect/*set.getDashPathEffectHighlight()*/);

        // create vertical path
        mHighlightLinePath.reset();
        mHighlightLinePath.moveTo(x, mViewPortHandler.contentTop());
        mHighlightLinePath.lineTo(x, mViewPortHandler.contentBottom());

        c.drawPath(mHighlightLinePath, mHighlightPaint);
    }
}
