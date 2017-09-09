package com.shuai.futures.view.chart;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

/**
 *
 */

public class MyLineDataSet extends LineDataSet {
    public MyLineDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
        init();
    }
    
    private void init(){
        setDrawCircleHole(false);
        setDrawCircles(false);
        setDrawValues(false);
        setLineWidth(2);
        setColor(0xff3b7fed);
        setFillColor(0xffe2ecfc);
        setDrawFilled(true);
    }
}
