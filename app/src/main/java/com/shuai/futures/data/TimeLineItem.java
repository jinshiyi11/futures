package com.shuai.futures.data;

import java.util.Date;

/**
 *
 */
public class TimeLineItem {
    public String mMinuteSecond;
    public Date mDate;
    public double mCurrentPrice;
    public double mAveragePrice;
    /**
     * 成交
     */
    public int mTurnover;

    /**
     * 持仓
     */
    public int mVolume;
}
