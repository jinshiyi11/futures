package com.shuai.futures.view.chart;

import com.shuai.futures.data.KlineItem;
import com.shuai.futures.data.TimeLineItem;

/**
 *
 */
public interface OnKlineHighlightListener {

    void onKlineHighlighted(KlineItem item);

    void onKlineUnhightlighted();
}
