package com.shuai.futures.data;

/**
 *
 */
public class KlineItem implements IUpDown {
    public String mDate;
    public double mOpen;
    public double mClose;
    public double mHigh;
    public double mLow;
    public int mVolume;

    @Override
    public boolean isUp() {
        return mClose >= mOpen;
    }
}
