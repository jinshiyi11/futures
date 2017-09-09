package com.shuai.futures.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.FuturesInfo;
import com.shuai.futures.data.FuturesPrice;
import com.shuai.futures.data.KlineItem;
import com.shuai.futures.data.LoadingStatus;
import com.shuai.futures.protocol.GetFuturesDailyKlineTask;
import com.shuai.futures.protocol.GetFuturesPriceListTask;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.ui.base.BaseFragmentActivity;
import com.shuai.futures.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class CandleStickActivity extends BaseFragmentActivity {
    private Context mContext;
    private String mFuturesId;
    private String mFuturesName;
    private LoadingStatus mStatus = LoadingStatus.STATUS_LOADING;
    private RequestQueue mRequestQueue;

    private ViewGroup mNoNetworkContainer;
    private ViewGroup mLoadingContainer;
    private ViewGroup mMainContainer;
    private TextView mTvTitle;
    private LineChart mRealTimeChart;
    private XAxis mXAxis;
    private YAxis mLeftAxis;
    private YAxis mRightAxis;

    private BarChart mVolumeChart;

    private CandleStickChart mCandelStickChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candlestick);

        mContext = this;
        Intent intent = getIntent();
        mFuturesId = intent.getStringExtra(Constants.EXTRA_FUTURES_ID);
        mFuturesName = intent.getStringExtra(Constants.EXTRA_FUTURES_NAME);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText(mFuturesName);

        mRequestQueue = MyApplication.getRequestQueue();
        mNoNetworkContainer = (ViewGroup) findViewById(R.id.no_network_container);
        mLoadingContainer = (ViewGroup) findViewById(R.id.loading_container);
        mMainContainer = (ViewGroup) findViewById(R.id.main_container);

        mNoNetworkContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setStatus(LoadingStatus.STATUS_LOADING);
                getFuturesPrice();
                getDailyKlineInfo();
            }
        });

        mRealTimeChart = (LineChart) findViewById(R.id.chart_realtime);
        mRealTimeChart.setNoDataText(null);
        mRealTimeChart.setDescription(null);
        mRealTimeChart.getLegend().setEnabled(false);
        mRealTimeChart.setDrawBorders(true);
        mRealTimeChart.setBorderColor(0xffcfd5e0);
        mRealTimeChart.setBorderWidth(1);
        mXAxis = mRealTimeChart.getXAxis();
        mLeftAxis = mRealTimeChart.getAxisLeft();
        mRightAxis = mRealTimeChart.getAxisRight();
        mXAxis.setLabelCount(3, true);
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mLeftAxis.setLabelCount(3, true);
        mLeftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        mRightAxis.setLabelCount(3, true);

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(1, 10));
        entries.add(new Entry(2, 10.6f));
        entries.add(new Entry(3, 19));
        entries.add(new Entry(5, 3));
        entries.add(new Entry(60, 5));

        mXAxis.setAxisMinimum(1);
        mXAxis.setAxisMaximum(100);

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
//        dataSet.setColor(...);
//        dataSet.setValueTextColor(...); // styling, ...
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(2);
        dataSet.setColor(0xff3b7fed);
        dataSet.setFillColor(0xffe2ecfc);
        dataSet.setDrawFilled(true);
        LineData lineData = new LineData(dataSet);
        mRealTimeChart.setData(lineData);
        //mRealTimeChart.invalidate(); // refresh


        mVolumeChart = (BarChart) findViewById(R.id.chart_volume);
        mVolumeChart.setNoDataText(null);
        mVolumeChart.setDescription(null);
        mVolumeChart.getLegend().setEnabled(false);
        mVolumeChart.getXAxis().setEnabled(false);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(1, 100));
        yVals1.add(new BarEntry(2, 40));
        yVals1.add(new BarEntry(5, 12));
        yVals1.add(new BarEntry(8, 342));
        yVals1.add(new BarEntry(11, 23));
        yVals1.add(new BarEntry(16, 65));
        BarDataSet set1 = new BarDataSet(yVals1, "The year 2017");
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setDrawValues(false);
//        data.setValueTextSize(10f);
//        data.setValueTypeface(mTfLight);
//        data.setBarWidth(0.9f);

        mVolumeChart.setData(data);

        mCandelStickChart = (CandleStickChart) findViewById(R.id.chart_candlestick);
        mCandelStickChart.setNoDataText(null);
        mCandelStickChart.setDescription(null);
        mCandelStickChart.getLegend().setEnabled(false);
        mCandelStickChart.setScaleEnabled(false);
        mCandelStickChart.setPinchZoom(false);
        mCandelStickChart.setDrawBorders(true);
        mCandelStickChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mCandelStickChart.getXAxis().setLabelCount(4);
        mCandelStickChart.getAxisRight().setEnabled(false);
        mCandelStickChart.getAxisLeft().setLabelCount(3);
        ArrayList<CandleEntry> candleEntries = new ArrayList<CandleEntry>();

        for (int i = 0; i < 30; i++) {
            float mult = 5;
            float val = (float) (Math.random() * 40) + mult;

            float high = (float) (Math.random() * 9) + 8f;
            float low = (float) (Math.random() * 9) + 8f;

            float open = (float) (Math.random() * 6) + 1f;
            float close = (float) (Math.random() * 6) + 1f;

            boolean even = i % 2 == 0;

            candleEntries.add(new CandleEntry(
                    i, val + high,
                    val - low,
                    even ? val + open : val - open,
                    even ? val - close : val + close
            ));
        }

        CandleDataSet candleDataset = new CandleDataSet(candleEntries, "Data Set");

        candleDataset.setShadowColorSameAsCandle(true);
        candleDataset.setDrawIcons(false);
        candleDataset.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set1.setColor(Color.rgb(80, 80, 80));
        candleDataset.setShadowColor(Color.DKGRAY);
        candleDataset.setShadowWidth(0.7f);
        candleDataset.setDecreasingColor(Color.RED);
        candleDataset.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataset.setIncreasingColor(Color.rgb(122, 242, 84));
        candleDataset.setIncreasingPaintStyle(Paint.Style.STROKE);
        candleDataset.setNeutralColor(Color.BLUE);
        //set1.setHighlightLineWidth(1f);

        CandleData candleData = new CandleData(candleDataset);

        mCandelStickChart.setData(candleData);
        mCandelStickChart.invalidate();

        setStatus(LoadingStatus.STATUS_LOADING);
        getFuturesPrice();
        getDailyKlineInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);
        }
    }

    private void setStatus(LoadingStatus status) {
        mStatus = status;
        switch (status) {
            case STATUS_LOADING:
                mLoadingContainer.setVisibility(View.VISIBLE);
                mMainContainer.setVisibility(View.GONE);
                mNoNetworkContainer.setVisibility(View.GONE);
                break;
            case STATUS_GOT_DATA:
                mLoadingContainer.setVisibility(View.GONE);
                mMainContainer.setVisibility(View.VISIBLE);
                mNoNetworkContainer.setVisibility(View.GONE);
                break;
            case STATUS_NO_NETWORK:
                mLoadingContainer.setVisibility(View.GONE);
                mMainContainer.setVisibility(View.GONE);
                mNoNetworkContainer.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void getFuturesPrice() {
        List<FuturesInfo> futures = new ArrayList<>();
        futures.add(new FuturesInfo(mFuturesId, mFuturesName));
        GetFuturesPriceListTask request = new GetFuturesPriceListTask(mContext, futures, new Response.Listener<List<FuturesPrice>>() {

            @Override
            public void onResponse(List<FuturesPrice> futuresPrices) {
                if (mStatus == LoadingStatus.STATUS_LOADING) {
                    setStatus(LoadingStatus.STATUS_GOT_DATA);
                }

                if (futuresPrices.size() == 0) {
                    //// TODO: 2017/9/9  log
                } else {
                    FuturesPrice price = futuresPrices.get(0);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (mStatus == LoadingStatus.STATUS_LOADING) {
                    setStatus(LoadingStatus.STATUS_NO_NETWORK);
                }

                Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
            }
        }
        );

        request.setTag(this);
        mRequestQueue.add(request);
    }

    private void getDailyKlineInfo() {
        GetFuturesDailyKlineTask request = new GetFuturesDailyKlineTask(mContext, mFuturesId, new Response.Listener<List<KlineItem>>() {

            @Override
            public void onResponse(List<KlineItem> klineItemList) {
                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                for (int i = 0; i < klineItemList.size(); i++) {
                    yVals1.add(new BarEntry(i, klineItemList.get(i).mVolume));
                }
                BarDataSet set1 = new BarDataSet(yVals1, "");
                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                data.setDrawValues(false);
//        data.setValueTextSize(10f);
//        data.setValueTypeface(mTfLight);
//        data.setBarWidth(0.9f);

                mVolumeChart.setData(data);
                mVolumeChart.invalidate();


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

                CandleDataSet candleDataset = new CandleDataSet(candleEntries, "Data Set");

                candleDataset.setShadowColorSameAsCandle(true);
                candleDataset.setDrawIcons(false);
                candleDataset.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set1.setColor(Color.rgb(80, 80, 80));
                candleDataset.setShadowColor(Color.DKGRAY);
                candleDataset.setShadowWidth(0.7f);
                candleDataset.setDecreasingColor(Color.RED);
                candleDataset.setDecreasingPaintStyle(Paint.Style.FILL);
                candleDataset.setIncreasingColor(Color.rgb(122, 242, 84));
                candleDataset.setIncreasingPaintStyle(Paint.Style.STROKE);
                candleDataset.setNeutralColor(Color.BLUE);
                //set1.setHighlightLineWidth(1f);

                CandleData candleData = new CandleData(candleDataset);

                mCandelStickChart.setData(candleData);
                mCandelStickChart.invalidate();

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
