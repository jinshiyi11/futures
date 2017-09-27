package com.shuai.futures.view.chart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuai.futures.R;
import com.shuai.futures.data.TimeLineItem;
import com.shuai.futures.utils.ColorUtils;

/**
 *
 */
public class TimeLineHead extends LinearLayout {
    private TextView mTvTime;
    private TextView mTvPrice;
    private TextView mTvPercent;
    private TextView mTvVolume;

    public TimeLineHead(Context context) {
        this(context, null);
    }

    public TimeLineHead(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeLineHead(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TimeLineHead(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    private void init() {
        inflate(getContext(), R.layout.time_line_head, this);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvPercent = (TextView) findViewById(R.id.tv_percent);
        mTvVolume = (TextView) findViewById(R.id.tv_volume);
    }

    @SuppressWarnings("deprecation")
    public void setData(double lastdayPrice, TimeLineItem item) {
        double diff = item.mCurrentPrice - lastdayPrice;
        int textColor = getResources().getColor(R.color.up);
        if (diff < 0) {
            textColor = getResources().getColor(R.color.down);
        }

        String tpl=getResources().getString(R.string.price_tpl);
        String color= ColorUtils.toHtmlColor(textColor);

        mTvTime.setText(item.mHourMinute);
        mTvPrice.setText(Html.fromHtml(String.format(tpl,"价 ",color,String.valueOf(item.mCurrentPrice))));
        double percent = diff / lastdayPrice * 100;
        String percentText=diff >= 0 ? String.format("+%.2f%%", percent) : String.format("%.2f%%", percent);
        mTvPercent.setText(Html.fromHtml(String.format(tpl,"幅 ",color,percentText)));

        mTvVolume.setText(String.format("量 %s", item.mVolume));
    }

    private void getText(String pre,String htmlColor,String text){

    }


}
