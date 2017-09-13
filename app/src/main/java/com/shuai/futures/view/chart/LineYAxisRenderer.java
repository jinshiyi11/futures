package com.shuai.futures.view.chart;


import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 *
 */
public class LineYAxisRenderer extends YAxisRenderer {
    public LineYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
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
}
