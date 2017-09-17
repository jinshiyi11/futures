package com.shuai.futures.view.chart;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.shuai.futures.R;

import java.util.List;

/**
 *
 */

public class MyCandleDataSet extends CandleDataSet {
    private Context mContext;
    public MyCandleDataSet(Context context,List<CandleEntry> yVals, String label) {
        super(yVals, label);
        mContext=context;
        init();
    }

    protected void init(){
        setDrawValues(false);
        setShadowColorSameAsCandle(true);
        setDrawIcons(false);
        setAxisDependency(YAxis.AxisDependency.LEFT);
//        set1.setColor(Color.rgb(80, 80, 80));
        setShadowColor(Color.DKGRAY);
        setShadowWidth(0.7f);
        setDecreasingColor(mContext.getResources().getColor(R.color.down));
        setDecreasingPaintStyle(Paint.Style.FILL);
        setIncreasingColor(mContext.getResources().getColor(R.color.up));
        setIncreasingPaintStyle(Paint.Style.FILL);
        setNeutralColor(Color.BLUE);
        //set1.setHighlightLineWidth(1f);
        setHighLightColor(mContext.getResources().getColor(R.color.chart_highlight));

    }
}
