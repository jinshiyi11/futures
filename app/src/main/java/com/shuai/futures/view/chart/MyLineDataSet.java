package com.shuai.futures.view.chart;


import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.shuai.futures.R;

import java.util.List;

/**
 *
 */

public class MyLineDataSet extends LineDataSet {
    private Context mContext;
    public MyLineDataSet(Context context, List<Entry> yVals, String label) {
        super(yVals, label);
        mContext = context;
        init();
    }

    private void init() {
        setDrawCircleHole(false);
        setDrawCircles(false);
        setDrawValues(false);
        setLineWidth(1);
        setColor(0xff3b7fed);
        setFillColor(0xffe2ecfc);
        setFillAlpha(255);
        setDrawFilled(true);
        setHighLightColor(mContext.getResources().getColor(R.color.chart_highlight));
    }
}
