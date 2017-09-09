package com.shuai.futures.data;

/**
 * 期货信息
 */
public class FuturesPrice {
    public String mId;
    public String mName;
    public double mCurrentPrice;
    public double mLastdayPrice;

    public void update(FuturesPrice info){
        if(info==null){
            return;
        }

        mName=info.mName;
        mCurrentPrice=info.mCurrentPrice;
        mLastdayPrice=info.mLastdayPrice;
    }

    public double getPercent(){
        double diff=mCurrentPrice - mLastdayPrice;
        if(diff<0.00001){
            return 0;
        }

        return diff/mLastdayPrice;
    }
}
