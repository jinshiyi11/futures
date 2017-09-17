package com.shuai.futures.view.chart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuai.futures.R;
import com.shuai.futures.data.KlineItem;

/**
 *
 */
public class KlineHead extends LinearLayout {
    private TextView mTvOpen;
    private TextView mTvClose;
    private TextView mTvHigh;
    private TextView mTvLow;
    private TextView mTvTime;
    private TextView mTvVolume;

    public KlineHead(Context context) {
        this(context, null);
    }

    public KlineHead(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KlineHead(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public KlineHead(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    private void init(){
        inflate(getContext(), R.layout.kline_head, this);
        mTvOpen = (TextView) findViewById(R.id.tv_open);
        mTvClose = (TextView) findViewById(R.id.tv_close);
        mTvHigh = (TextView) findViewById(R.id.tv_high);
        mTvLow = (TextView) findViewById(R.id.tv_low);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvVolume = (TextView) findViewById(R.id.tv_volume);
    }

    public void setData(double lastdayPrice, KlineItem item){
        mTvOpen.setText(String.format("开 %s",item.mOpen));
        mTvClose.setText(String.format("收 %s",item.mClose));
        mTvHigh.setText(String.format("高 %s",item.mHigh));
        mTvLow.setText(String.format("低 %s",item.mLow));
        mTvTime.setText(String.format("%s",item.mDateString));
        mTvVolume.setText(String.format("量 %d",item.mVolume));
    }
}
