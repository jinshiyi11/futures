package com.shuai.futures.view.chart;

import com.shuai.futures.data.TimeLineItem;

/**
 *
 */
public interface OnTimeLineHighlightListener {

    void onTimeLineHighlighted(TimeLineItem item);

    void onTimeLineUnhightlighted();
}
