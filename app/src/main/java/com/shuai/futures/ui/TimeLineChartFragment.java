package com.shuai.futures.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.TimeLineItem;
import com.shuai.futures.protocol.GetTimeLineTask;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.ui.base.BaseTabFragment;
import com.shuai.futures.utils.Utils;
import com.shuai.futures.view.chart.CoupleChartGestureListener;
import com.shuai.futures.view.chart.CoupleChartValueSelectedListener;
import com.shuai.futures.view.chart.KlineType;
import com.shuai.futures.view.chart.MyBarChart;
import com.shuai.futures.view.chart.MyBarDataSet;
import com.shuai.futures.view.chart.MyLineChart;
import com.shuai.futures.view.chart.MyLineDataSet;
import com.shuai.futures.view.chart.OnTimeLineHighlightListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 分时图
 */
public class TimeLineChartFragment extends BaseTabFragment implements OnChartValueSelectedListener {
    public static final String KEY_LASTDAY_PRICE = "key_lastday_price";
    private RequestQueue mRequestQueue;
    private String mFuturesId;
    private String mFuturesName;

    private MyLineChart mTimeLineChart;
    private MyBarChart mVolumeChart;

    private double mLastdayPrice;

    //public int MAX_ENTRY_COUNT_=376;//459

    private OnTimeLineHighlightListener mHighlightListener;

    public TimeLineChartFragment() {
        super(R.layout.fragment_time_chart);
    }

    @Override
    protected void onInit(View root, Bundle savedInstanceState) {
        mRequestQueue = MyApplication.getRequestQueue();
        Intent intent = getActivity().getIntent();
        mFuturesId = intent.getStringExtra(Constants.EXTRA_FUTURES_ID);
        mFuturesName = intent.getStringExtra(Constants.EXTRA_FUTURES_NAME);

        mLastdayPrice = getArguments().getDouble(KEY_LASTDAY_PRICE);
        mTimeLineChart = (MyLineChart) root.findViewById(R.id.chart_realtime);
        mVolumeChart = (MyBarChart) root.findViewById(R.id.chart_volume);

        mTimeLineChart.setKlineType(KlineType.KRealTime);
        mTimeLineChart.setBasePrice(mLastdayPrice);

        mTimeLineChart.setOnChartValueSelectedListener(new CoupleChartValueSelectedListener(mVolumeChart, this));
        mVolumeChart.setOnChartValueSelectedListener(new CoupleChartValueSelectedListener(mTimeLineChart, this));

        getTimeLineInfo();
    }

    @Override
    public void onDestroyView() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);
        }
        super.onDestroyView();
    }

    private void getTimeLineInfo() {
        GetTimeLineTask request = new GetTimeLineTask(mContext, mFuturesId, new Response.Listener<List<TimeLineItem>>() {

            @Override
            public void onResponse(List<TimeLineItem> timeLineItemList) {
                List<Entry> entries = new ArrayList<Entry>();
                for (int i = 0; i < timeLineItemList.size(); i++) {
                    TimeLineItem item = timeLineItemList.get(i);
                    entries.add(new Entry(i, (float) item.mCurrentPrice, item));
                }
                MyLineDataSet dataSet = new MyLineDataSet(mContext, entries, "Label"); // add entries to dataset
                LineData lineData = new LineData(dataSet);
                mTimeLineChart.setData(lineData);
                //TODO：期货交易时长
                mTimeLineChart.getXAxis().setAxisMaximum(420);
                mTimeLineChart.invalidate();

                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                for (int i = 0; i < timeLineItemList.size(); i++) {
                    TimeLineItem item = timeLineItemList.get(i);
                    yVals1.add(new BarEntry(i, item.mVolume, item.mCurrentPrice >= mLastdayPrice));
                }
                MyBarDataSet set1 = new MyBarDataSet(mContext, yVals1, "");
                set1.setDrawValues(false);
                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);
                BarData data = new BarData(dataSets);
                mVolumeChart.setData(data);
                //TODO：期货交易时长
                mVolumeChart.getXAxis().setAxisMaximum(420);
                mVolumeChart.invalidate();

                mTimeLineChart.setOnChartGestureListener(new CoupleChartGestureListener(
                        mTimeLineChart, new Chart[]{mVolumeChart}));
                mVolumeChart.setOnChartGestureListener(new CoupleChartGestureListener(
                        mVolumeChart, new Chart[]{mTimeLineChart}));
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
            }
        });

        request.setTag(this);
        mRequestQueue.add(request);
    }

    public void setHighlightListener(OnTimeLineHighlightListener listener) {
        mHighlightListener = listener;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (mHighlightListener != null) {
            // Entry entry = mTimeLineChart.getData().getEntryForHighlight(h);
            ILineDataSet dataSet = mTimeLineChart.getData().getDataSetByIndex(0);
            Entry entry = dataSet.getEntryForIndex((int)h.getX()-(int)dataSet.getEntryForIndex(0).getX());
            mHighlightListener.onTimeLineHighlighted((TimeLineItem) entry.getData());
        }
    }

    @Override
    public void onNothingSelected() {
        if (mHighlightListener != null) {
            mHighlightListener.onTimeLineUnhightlighted();
        }
    }
}
