package com.shuai.futures.view.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 *
 */

public class LineRightAxisValueFormatter implements IAxisValueFormatter {

    private double mBasePrice;
    private DecimalFormat mFormat;

    public LineRightAxisValueFormatter() {
        this.mFormat = new DecimalFormat("0.00%");
    }

    public void setBasePrice(double basePrice) {
        this.mBasePrice = basePrice;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (mBasePrice == 0) {
            //TODO:log
            return "";
        }

        return mFormat.format((value - mBasePrice) / mBasePrice);
    }
}
