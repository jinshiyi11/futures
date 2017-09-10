package com.shuai.futures.data;

/**
 *
 */
public class TimeLineItem implements IUpDown {
    public String mMinuteSecond;
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

    @Override
    public boolean isUp() {
        return true;
    }
}
