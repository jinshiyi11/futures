package com.shuai.futures.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.Constants;
import com.shuai.futures.ui.base.BaseTabFragment;
import com.shuai.futures.view.chart.MyBarChart;
import com.shuai.futures.view.chart.MyCandleChart;
import com.shuai.futures.view.chart.MyLineChart;
import com.shuai.futures.view.chart.MyLineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class TimeChartFragment extends BaseTabFragment {
    private RequestQueue mRequestQueue;
    private String mFuturesId;
    private String mFuturesName;

    private MyLineChart mRealTimeChart;

    public TimeChartFragment() {
        super(R.layout.fragment_time_chart);
    }

    @Override
    protected void onInit(View root, Bundle savedInstanceState) {
        mRequestQueue = MyApplication.getRequestQueue();
        Intent intent = getActivity().getIntent();
        mFuturesId = intent.getStringExtra(Constants.EXTRA_FUTURES_ID);
        mFuturesName = intent.getStringExtra(Constants.EXTRA_FUTURES_NAME);


        mRealTimeChart = (MyLineChart) root.findViewById(R.id.chart_realtime);
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1, 10));
        entries.add(new Entry(2, 10.6f));
        entries.add(new Entry(3, 19));
        entries.add(new Entry(5, 3));
        entries.add(new Entry(60, 5));

        MyLineDataSet dataSet = new MyLineDataSet(entries, "Label"); // add entries to dataset
        LineData lineData = new LineData(dataSet);
        mRealTimeChart.setData(lineData);
    }
}
