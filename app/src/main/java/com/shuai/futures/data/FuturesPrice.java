package com.shuai.futures.data;

/**
 * 期货信息
 */
public class FuturesPrice {
    public String mName;
    public String mTitle;
    public double mCurrentPrice;
    /**
     * 昨结算
     */
    public double mLastdayPrice;

    public double mHigh;
    public double mLow;
    public double mOpen;
    public int mVolume;

    public void update(FuturesPrice info){
        if(info==null){
            return;
        }

        mTitle =info.mTitle;
        mCurrentPrice=info.mCurrentPrice;
        mLastdayPrice=info.mLastdayPrice;
    }

    public double getPercent(){
        double diff=mCurrentPrice - mLastdayPrice;
        if(Math.abs(diff)<0.00001){
            return 0;
        }

        return diff/mLastdayPrice;
    }
}
