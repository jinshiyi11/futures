package com.shuai.futures.view.chart;


import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.shuai.futures.data.IUpDown;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MyBarDataSet extends BarDataSet {
    private static final int UP_COLOR=0xffd0402d;
    private static final int DOWN_COLOR=0xff17b03e;
    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
        init();
    }

    private void init(){
        setDrawValues(false);
        List<Integer> colors=new ArrayList<>();
        colors.add(UP_COLOR);
        colors.add(DOWN_COLOR);
        setColors(colors);
    }

    @Override
    public List<Integer> getColors() {
        return super.getColors();
    }

    @Override
    public int getColor(int index) {
        BarEntry entry = getEntryForIndex(index);
        IUpDown iUpDown= (IUpDown) entry.getData();
        if(iUpDown.isUp()){
            return UP_COLOR;
        }else{
            return DOWN_COLOR;
        }
    }
}
