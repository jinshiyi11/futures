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
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.KlineItem;
import com.shuai.futures.protocol.GetFuturesDailyKlineTask;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.ui.base.BaseTabFragment;
import com.shuai.futures.utils.Utils;
import com.shuai.futures.view.chart.CoupleChartGestureListener;
import com.shuai.futures.view.chart.MyBarChart;
import com.shuai.futures.view.chart.MyBarDataSet;
import com.shuai.futures.view.chart.MyCandleDataSet;
import com.shuai.futures.view.chart.MyCombinedChart;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class KlineChartFragment extends BaseTabFragment {
    public static final String  KEY_KLINE_CHART_TYPE="key_kline_chart_type";
    private RequestQueue mRequestQueue;
    private String mFuturesId;
    private String mFuturesName;

    private MyCombinedChart mKlineChart;
    private MyBarChart mVolumeChart;
    private KlineChartType mKlineType;

    public enum KlineChartType {
        K5Minutes,
        K15Minutes,
        K30Minutes,
        K60Minutes,
        KDaily
    }

    public KlineChartFragment() {
        super(R.layout.fragment_kline_chart);

    }

    @Override
    protected void onInit(View root, Bundle savedInstanceState) {
        mRequestQueue = MyApplication.getRequestQueue();
        Intent intent = getActivity().getIntent();
        mFuturesId = intent.getStringExtra(Constants.EXTRA_FUTURES_ID);
        mFuturesName = intent.getStringExtra(Constants.EXTRA_FUTURES_NAME);
        mKlineType = (KlineChartType) getArguments().getSerializable(KEY_KLINE_CHART_TYPE);



        mKlineChart = (MyCombinedChart) root.findViewById(R.id.chart_kline);
        mVolumeChart = (MyBarChart) root.findViewById(R.id.chart_volume);

        getDailyKlineInfo();
    }

    @Override
    public void onDestroyView() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);
        }
        super.onDestroyView();
    }

    private void getDailyKlineInfo() {
        GetFuturesDailyKlineTask request = new GetFuturesDailyKlineTask(mContext, mFuturesId,mKlineType, new Response.Listener<List<KlineItem>>() {

            @Override
            public void onResponse(List<KlineItem> klineItemList) {
                ArrayList<CandleEntry> candleEntries = new ArrayList<CandleEntry>();
                for (int i = 0; i < klineItemList.size(); i++) {
                    KlineItem item = klineItemList.get(i);
                    candleEntries.add(new CandleEntry(
                            i, (float) item.mHigh,
                            (float) item.mLow,
                            (float) item.mOpen,
                            (float) item.mClose
                    ));
                }
                CombinedData combinedData=new CombinedData();
                MyCandleDataSet candleDataset = new MyCandleDataSet(mContext, candleEntries, "Data Set");
                CandleData candleData = new CandleData(candleDataset);
                combinedData.setData(candleData);
                mKlineChart.setData(combinedData);
                mKlineChart.setVisibleXRangeMaximum(60);
                mKlineChart.moveViewToX(klineItemList.size());
                mKlineChart.invalidate();

                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                for (int i = 0; i < klineItemList.size(); i++) {
                    KlineItem item = klineItemList.get(i);
                    yVals1.add(new BarEntry(i, item.mVolume, item.mClose>=item.mOpen));
                }
                MyBarDataSet set1 = new MyBarDataSet(mContext,yVals1, "");
                set1.setDrawValues(false);
                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);
                BarData data = new BarData(dataSets);
                mVolumeChart.setData(data);
                //TODO:setVisibleXRangeMaximum封装到控件里面
                mVolumeChart.setVisibleXRangeMaximum(60);
                mVolumeChart.moveViewToX(klineItemList.size());
                mVolumeChart.invalidate();

                mKlineChart.setOnChartGestureListener(new CoupleChartGestureListener(
                        mKlineChart, new Chart[]{mVolumeChart}));
                mVolumeChart.setOnChartGestureListener(new CoupleChartGestureListener(
                        mVolumeChart, new Chart[]{mKlineChart}));

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
}
