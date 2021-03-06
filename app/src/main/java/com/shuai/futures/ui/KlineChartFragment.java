package com.shuai.futures.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.KlineItem;
import com.shuai.futures.protocol.GetFuturesKlineTask;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.protocol.XueqiuApi;
import com.shuai.futures.ui.base.BaseTabFragment;
import com.shuai.futures.utils.RetrofitUtil;
import com.shuai.futures.utils.StockUtil;
import com.shuai.futures.utils.Utils;
import com.shuai.futures.view.chart.CoupleChartGestureListener;
import com.shuai.futures.view.chart.CoupleChartValueSelectedListener;
import com.shuai.futures.view.chart.KlineType;
import com.shuai.futures.view.chart.MyBarChart;
import com.shuai.futures.view.chart.MyBarDataSet;
import com.shuai.futures.view.chart.MyCandleDataSet;
import com.shuai.futures.view.chart.MyCombinedChart;
import com.shuai.futures.view.chart.OnKlineHighlightListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 *
 */
public class KlineChartFragment extends BaseTabFragment implements OnChartValueSelectedListener {
    public static final String KEY_KLINE_CHART_TYPE = "key_kline_chart_type";
    private RequestQueue mRequestQueue;
    private String mFuturesName;
    private String mFuturesTitle;

    private MyCombinedChart mKlineChart;
    private MyBarChart mVolumeChart;
    private KlineType mKlineType;
    private Disposable mDisposable;

    private OnKlineHighlightListener mHighlightListener;

    public KlineChartFragment() {
        super(R.layout.fragment_kline_chart);

    }

    @Override
    protected void onInit(View root, Bundle savedInstanceState) {
        mRequestQueue = MyApplication.getRequestQueue();
        Intent intent = getActivity().getIntent();
        mFuturesName = intent.getStringExtra(Constants.EXTRA_FUTURES_NAME);
        mFuturesTitle = intent.getStringExtra(Constants.EXTRA_FUTURES_TITLE);
        mKlineType = (KlineType) getArguments().getSerializable(KEY_KLINE_CHART_TYPE);

        mKlineChart = (MyCombinedChart) root.findViewById(R.id.chart_kline);
        mVolumeChart = (MyBarChart) root.findViewById(R.id.chart_volume);

        mKlineChart.setScaleXEnabled(true);
        mKlineChart.setScaleYEnabled(false);
        mKlineChart.getViewPortHandler().setMaximumScaleX(10);
        mVolumeChart.setScaleXEnabled(true);
        mVolumeChart.setScaleYEnabled(false);
        mVolumeChart.getViewPortHandler().setMaximumScaleX(10);

        mKlineChart.setKlineType(mKlineType);
        mKlineChart.setOnChartValueSelectedListener(new CoupleChartValueSelectedListener(mVolumeChart, this));
        mVolumeChart.setOnChartValueSelectedListener(new CoupleChartValueSelectedListener(mKlineChart, this));
        switch (StockUtil.getStockType(mFuturesName)){
            case Futures:
                getFuturesKlineInfo();
                break;
            case Stock:
                getStockKlineInfo();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);
        }
        super.onDestroyView();
    }

    private void getFuturesKlineInfo() {
        GetFuturesKlineTask request = new GetFuturesKlineTask(mContext, mFuturesName, mKlineType, new Response.Listener<List<KlineItem>>() {

            @Override
            public void onResponse(List<KlineItem> klineItemList) {
                onResponseSuccess(klineItemList);
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

    private void getStockKlineInfo() {
        Retrofit retrofit = RetrofitUtil.getRetrofit();
        XueqiuApi api = retrofit.create(XueqiuApi.class);
        mDisposable = api.getKline(mFuturesName,"30m","normal",
                1513438165122L,240,
                "xq_a_token=95b69ccb71a54ebf3d7060a84a72b45015fead7f;")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<KlineItem>>() {
            @Override
            public void accept(List<KlineItem> items) throws Exception {
                onResponseSuccess(items);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //todo:
                Log.e(TAG,"",throwable);
                //Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
            }
        });
    }

    private void onResponseSuccess(List<KlineItem> klineItemList) {
        ArrayList<CandleEntry> candleEntries = new ArrayList<CandleEntry>();
        for (int i = 0; i < klineItemList.size(); i++) {
            KlineItem item = klineItemList.get(i);
            candleEntries.add(new CandleEntry(
                    i, (float) item.mHigh,
                    (float) item.mLow,
                    (float) item.mOpen,
                    (float) item.mClose,
                    item
            ));
        }
        CombinedData combinedData = new CombinedData();
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
            yVals1.add(new BarEntry(i, item.mVolume, item.mClose >= item.mOpen));
        }
        MyBarDataSet set1 = new MyBarDataSet(mContext, yVals1, "");
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

    public void setHighlightListener(OnKlineHighlightListener listener) {
        mHighlightListener = listener;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (mHighlightListener != null) {
            //Entry entry = mKlineChart.getCandleData().getEntryForHighlight(h);

            ICandleDataSet dataSet = mKlineChart.getCandleData().getDataSetByIndex(0);
            Entry entry = dataSet.getEntryForIndex((int) h.getX() - (int) dataSet.getEntryForIndex(0).getX());
            mHighlightListener.onKlineHighlighted((KlineItem) entry.getData());

        }
    }

    @Override
    public void onNothingSelected() {
        if (mHighlightListener != null) {
            mHighlightListener.onKlineUnhightlighted();
        }
    }
}
