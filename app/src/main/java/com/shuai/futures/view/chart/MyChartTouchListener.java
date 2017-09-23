package com.shuai.futures.view.chart;


import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;

/**
 *
 */

public class MyChartTouchListener extends BarLineChartTouchListener {
    private boolean mInHighlightMode;

    /**
     * Constructor with initialization parameters.
     *
     * @param chart               instance of the chart
     * @param touchMatrix         the touch-matrix of the chart
     * @param dragTriggerDistance the minimum movement distance that will be interpreted as a "drag" gesture in dp (3dp equals
     */
    public MyChartTouchListener(BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>> chart, Matrix touchMatrix, float dragTriggerDistance) {
        super(chart, touchMatrix, dragTriggerDistance);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean result = true;
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        if (!(mInHighlightMode && action == MotionEvent.ACTION_MOVE)) {
            result = super.onTouch(v, event);
        }
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mInHighlightMode = false;
                mChart.highlightValue(null, true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mInHighlightMode) {
                    mLastGesture = ChartGesture.DRAG;

                    if (mChart.isHighlightPerDragEnabled())
                        performHighlightDrag(event);
                }
                break;
        }
        return result;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e);

        if (mTouchMode == DRAG) {
            //已经进入滑动模式，滑动模式和highlight二选一
            return;
        }

        mInHighlightMode = true;
        Highlight h = mChart.getHighlightByTouchPoint(e.getX(), e.getY());
        performHighlight(h, e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);

//        mInHighlightMode = true;
//
//        Highlight h = mChart.getHighlightByTouchPoint(e.getX(), e.getY());
//        performHighlight(h, e);

    }

    /**
     * Highlights upon dragging, generates callbacks for the selection-listener.
     *
     * @param e
     */
    private void performHighlightDrag(MotionEvent e) {
        if (!mInHighlightMode) {
            return;
        }

        Highlight h = mChart.getHighlightByTouchPoint(e.getX(), e.getY());

        if (h != null && !h.equalTo(mLastHighlighted)) {
            mLastHighlighted = h;
            mChart.highlightValue(h, true);
        }
    }
}
