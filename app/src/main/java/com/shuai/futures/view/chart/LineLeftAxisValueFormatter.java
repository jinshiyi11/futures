package com.shuai.futures.view.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 *
 */

public class LineLeftAxisValueFormatter implements IAxisValueFormatter {

    private DecimalFormat mFormat;

    public LineLeftAxisValueFormatter() {
        this.mFormat = new DecimalFormat("#.##");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value);
    }
}
