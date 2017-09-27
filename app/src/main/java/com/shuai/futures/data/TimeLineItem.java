package com.shuai.futures.data;

import java.util.Date;

/**
 *
 */
public class TimeLineItem implements Cloneable {
    public String mHourMinute;
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

    public String getHourMinute() {
        return mHourMinute;
    }

    public void setHourMinute(String hourMinute) {
        this.mHourMinute = hourMinute;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public double getCurrentPrice() {
        return mCurrentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.mCurrentPrice = currentPrice;
    }

    public double getAveragePrice() {
        return mAveragePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.mAveragePrice = averagePrice;
    }

    public int getTurnover() {
        return mTurnover;
    }

    public void setTurnover(int turnover) {
        this.mTurnover = turnover;
    }

    public int getVolume() {
        return mVolume;
    }

    public void setVolume(int volume) {
        this.mVolume = volume;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
