package com.shuai.futures.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.TimeLineItem;
import com.shuai.futures.protocol.GetTimeLineTask;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.protocol.XDataProcesser;
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
import com.shuai.futures.protocol.XLabelInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 分时图
 */
public class TimeLineChartFragment extends BaseTabFragment implements OnChartValueSelectedListener {
    public static final String KEY_LASTDAY_PRICE = "key_lastday_price";
    private RequestQueue mRequestQueue;
    private Handler mHandler = new Handler();
    private String mFuturesName;
    private String mFuturesTitle;

    private MyLineChart mTimeLineChart;
    private MyBarChart mVolumeChart;

    private double mLastdayPrice;

    private int mRefreshInterval = 5000;//30 * 1000;
    private int mDataCount = 0;

    private OnTimeLineHighlightListener mHighlightListener;

    private Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            getTimeLineInfo();
        }
    };

    public TimeLineChartFragment() {
        super(R.layout.fragment_time_chart);
    }

    @Override
    protected void onInit(View root, Bundle savedInstanceState) {
        mRequestQueue = MyApplication.getRequestQueue();
        Intent intent = getActivity().getIntent();
        mFuturesName = intent.getStringExtra(Constants.EXTRA_FUTURES_NAME);
        mFuturesTitle = intent.getStringExtra(Constants.EXTRA_FUTURES_TITLE);

        mLastdayPrice = getArguments().getDouble(KEY_LASTDAY_PRICE);
        mTimeLineChart = (MyLineChart) root.findViewById(R.id.chart_realtime);
        mVolumeChart = (MyBarChart) root.findViewById(R.id.chart_volume);

        mTimeLineChart.setKlineType(KlineType.KRealTime);
        mTimeLineChart.setBasePrice(mLastdayPrice);
        XLabelInfo xLabelInfo = XDataProcesser.getInstance().getXLabelInfo(mFuturesName);
        mTimeLineChart.setXLabelInfo(xLabelInfo);

        //设置期货交易时长
        if (xLabelInfo != null) {
            XAxis timeXAxis = mTimeLineChart.getXAxis();
            //防止第一个和最后一个只画一半
            timeXAxis.setAxisMinimum(0 - timeXAxis.getSpaceMin());
            timeXAxis.setAxisMaximum(xLabelInfo.getXcount() + timeXAxis.getSpaceMax());

            XAxis volXAxis = mVolumeChart.getXAxis();
            volXAxis.setAxisMinimum(0 - volXAxis.getSpaceMin());
            volXAxis.setAxisMaximum(xLabelInfo.getXcount() + volXAxis.getSpaceMax());
        } else {
            XAxis timeXAxis = mTimeLineChart.getXAxis();
            timeXAxis.setAxisMinimum(0 - timeXAxis.getSpaceMin());
            timeXAxis.setAxisMaximum(420 + timeXAxis.getSpaceMax());

            XAxis volXAxis = mVolumeChart.getXAxis();
            volXAxis.setAxisMinimum(0 - volXAxis.getSpaceMin());
            volXAxis.setAxisMaximum(420 + volXAxis.getSpaceMax());
        }

        mTimeLineChart.setOnChartValueSelectedListener(new CoupleChartValueSelectedListener(mVolumeChart, this));
        mVolumeChart.setOnChartValueSelectedListener(new CoupleChartValueSelectedListener(mTimeLineChart, this));

        mTimeLineChart.setOnChartGestureListener(new CoupleChartGestureListener(
                mTimeLineChart, new Chart[]{mVolumeChart}));
        mVolumeChart.setOnChartGestureListener(new CoupleChartGestureListener(
                mVolumeChart, new Chart[]{mTimeLineChart}));

        getTimeLineInfo();
    }

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);
        }
        super.onDestroyView();
    }

    private void refreshInfo() {
        mHandler.removeCallbacks(mRefreshRunnable);
        mHandler.postDelayed(mRefreshRunnable, mRefreshInterval);
    }

    private void getTimeLineInfo() {
        GetTimeLineTask request = new GetTimeLineTask(mContext, mFuturesName, new Response.Listener<List<TimeLineItem>>() {

            @Override
            public void onResponse(List<TimeLineItem> timeLineItemList) {
                if (timeLineItemList == null || timeLineItemList.size() == 0) {
                    return;
                }

                int size = timeLineItemList.size();
                if (size > 0 && size > mDataCount) {
                    timeLineItemList = timeLineItemList.subList(mDataCount, size);
                    mDataCount += timeLineItemList.size();
                } else {
                    refreshInfo();
                    return;
                }

                LineData lineData = mTimeLineChart.getData();
                LineDataSet currentPriceDataSet;
                LineDataSet averagePriceDataSet;
                if (lineData == null) {
                    List<Entry> currentPriceEntries = new ArrayList<Entry>();
                    List<Entry> averagePriceEntries = new ArrayList<Entry>();

                    currentPriceDataSet = new MyLineDataSet(mContext, currentPriceEntries, "current price"); // add currentPriceEntries to dataset
                    lineData = new LineData(currentPriceDataSet);
                    averagePriceDataSet = new LineDataSet(averagePriceEntries, "average price");
                    averagePriceDataSet.setDrawCircleHole(false);
                    averagePriceDataSet.setDrawCircles(false);
                    averagePriceDataSet.setDrawValues(false);
                    averagePriceDataSet.setLineWidth(1);
                    averagePriceDataSet.setColor(getResources().getColor(R.color.chart_average_price));
                    lineData.addDataSet(averagePriceDataSet);
                    mTimeLineChart.setData(lineData);
                } else {
                    List<ILineDataSet> dataSets = lineData.getDataSets();
                    currentPriceDataSet = (LineDataSet) dataSets.get(0);
                    averagePriceDataSet = (LineDataSet) dataSets.get(1);
                }

                int count = currentPriceDataSet.getEntryCount();
                for (int i = 0; i < timeLineItemList.size(); i++) {
                    TimeLineItem item = timeLineItemList.get(i);
                    currentPriceDataSet.addEntry(new Entry(count + i, (float) item.mCurrentPrice, item));
                    averagePriceDataSet.addEntry(new Entry(count + i, (float) item.mAveragePrice));
                }

                lineData.notifyDataChanged();
                mTimeLineChart.notifyDataSetChanged();
                mTimeLineChart.invalidate();

                BarData barData = mVolumeChart.getData();
                BarDataSet set1;
                if (barData == null) {
                    List<BarEntry> yVals1 = new ArrayList<BarEntry>();
                    set1 = new MyBarDataSet(mContext, yVals1, "");
                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                    dataSets.add(set1);
                    barData = new BarData(dataSets);
                    mVolumeChart.setData(barData);
                } else {
                    set1 = (BarDataSet) barData.getDataSets().get(0);
                }

                for (int i = 0; i < timeLineItemList.size(); i++) {
                    TimeLineItem item = timeLineItemList.get(i);
                    set1.addEntry(new BarEntry(i, item.mVolume, item.mCurrentPrice >= mLastdayPrice));
                }

                barData.notifyDataChanged();
                mVolumeChart.notifyDataSetChanged();
                mVolumeChart.invalidate();

                refreshInfo();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
                refreshInfo();
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
            Entry entry = dataSet.getEntryForIndex((int) h.getX() - (int) dataSet.getEntryForIndex(0).getX());
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
