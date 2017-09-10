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
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.TimeLineItem;
import com.shuai.futures.protocol.GetTimeLineTask;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.ui.base.BaseTabFragment;
import com.shuai.futures.utils.Utils;
import com.shuai.futures.view.chart.CoupleChartGestureListener;
import com.shuai.futures.view.chart.MyBarChart;
import com.shuai.futures.view.chart.MyBarDataSet;
import com.shuai.futures.view.chart.MyLineChart;
import com.shuai.futures.view.chart.MyLineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 分时图
 */
public class TimeLineChartFragment extends BaseTabFragment {
    private RequestQueue mRequestQueue;
    private String mFuturesId;
    private String mFuturesName;

    private MyLineChart mTimeLineChart;
    private MyBarChart mVolumeChart;

    public TimeLineChartFragment() {
        super(R.layout.fragment_time_chart);
    }

    @Override
    protected void onInit(View root, Bundle savedInstanceState) {
        mRequestQueue = MyApplication.getRequestQueue();
        Intent intent = getActivity().getIntent();
        mFuturesId = intent.getStringExtra(Constants.EXTRA_FUTURES_ID);
        mFuturesName = intent.getStringExtra(Constants.EXTRA_FUTURES_NAME);


        mTimeLineChart = (MyLineChart) root.findViewById(R.id.chart_realtime);
        mVolumeChart = (MyBarChart) root.findViewById(R.id.chart_volume);

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
        GetTimeLineTask request=new GetTimeLineTask(mContext,mFuturesId,new Response.Listener<List<TimeLineItem>>(){

            @Override
            public void onResponse(List<TimeLineItem> timeLineItemList) {
                List<Entry> entries = new ArrayList<Entry>();
                for (int i = 0; i < timeLineItemList.size(); i++) {
                    TimeLineItem item = timeLineItemList.get(i);
                    entries.add(new Entry(i, (float) item.mCurrentPrice, item));
                }
                MyLineDataSet dataSet = new MyLineDataSet(entries, "Label"); // add entries to dataset
                LineData lineData = new LineData(dataSet);
                mTimeLineChart.setData(lineData);
                mTimeLineChart.invalidate();

                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                for (int i = 0; i < timeLineItemList.size(); i++) {
                    TimeLineItem item = timeLineItemList.get(i);
                    yVals1.add(new BarEntry(i, item.mVolume, item));
                }
                MyBarDataSet set1 = new MyBarDataSet(yVals1, "");
                set1.setDrawValues(false);
                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);
                BarData data = new BarData(dataSets);
                mVolumeChart.setData(data);
                mVolumeChart.invalidate();

                mTimeLineChart.setOnChartGestureListener(new CoupleChartGestureListener(
                        mTimeLineChart, new Chart[]{mVolumeChart}));
                mVolumeChart.setOnChartGestureListener(new CoupleChartGestureListener(
                        mVolumeChart, new Chart[]{mTimeLineChart}));
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
            }
        });

        request.setTag(this);
        mRequestQueue.add(request);
    }
}
