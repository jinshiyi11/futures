package com.shuai.futures.view.chart;


import android.content.Context;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.shuai.futures.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MyBarDataSet extends BarDataSet {
    private Context mContext;
    private int mUpColor;
    private int mDownColor;


    public MyBarDataSet(Context context, List<BarEntry> yVals, String label) {
        super(yVals, label);
        mContext = context;
        mUpColor = mContext.getResources().getColor(R.color.up);
        mDownColor = mContext.getResources().getColor(R.color.down);
        init();
    }

    private void init() {
        setDrawValues(false);
        List<Integer> colors = new ArrayList<>();
        colors.add(mUpColor);
        colors.add(mDownColor);
        setColors(colors);
    }

    @Override
    public List<Integer> getColors() {
        return super.getColors();
    }

    @Override
    public int getColor(int index) {
        BarEntry entry = getEntryForIndex(index);
        boolean isUp = (boolean) entry.getData();
        if (isUp) {
            return mUpColor;
        } else {
            return mDownColor;
        }
    }
}
