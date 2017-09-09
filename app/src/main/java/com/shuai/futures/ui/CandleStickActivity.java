package com.shuai.futures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
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
import com.shuai.futures.ui.base.BaseFragment;
import com.shuai.futures.ui.base.BaseFragmentActivity;
import com.shuai.futures.utils.Utils;
import com.shuai.futures.view.chart.CoupleChartGestureListener;
import com.shuai.futures.view.chart.MyBarChart;
import com.shuai.futures.view.chart.MyBarDataSet;
import com.shuai.futures.view.chart.MyCandleChart;
import com.shuai.futures.view.chart.MyCandleDataSet;
import com.shuai.futures.view.chart.MyLineChart;
import com.shuai.futures.view.chart.MyLineDataSet;
import com.viewpagerindicator.TabPageIndicatorEx;
import com.viewpagerindicator.TabViewInterface;

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
    private ViewPager mViewPager;

    private static final String[] TAB_TITLES = new String[]{"分时", "5分", "15分", "30分", "60分", "日K"};

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
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.pager);
        FragmentPagerAdapter adapter = new OrderTabAdapter(
                getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(TAB_TITLES.length);
        mViewPager.setAdapter(adapter);
        TabPageIndicatorEx indicator = (TabPageIndicatorEx) findViewById(R.id.tbi_order);

        indicator.setViewPager(mViewPager);
//        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int tab) {
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//            }
//        });

        setStatus(LoadingStatus.STATUS_LOADING);
        getFuturesPrice();
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

    class OrderTabAdapter extends FragmentPagerAdapter implements
            TabViewInterface {

        public OrderTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment f;
            if (position == 0) {
                f = new TimeChartFragment();
            } else {
                f = new KlineChartFragment(KlineChartFragment.KlineChartType.values()[position-1]);
            }
            return f;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position % TAB_TITLES.length];
        }

        @Override
        public int getCount() {
            return TAB_TITLES.length;
        }

        @Override
        public View getTabView(ViewGroup container, int position) {
            TextView tvTab = (TextView) getLayoutInflater().inflate(R.layout.tab_kline,
                    container, false);
            tvTab.setText(getPageTitle(position));
            return tvTab;
        }
    }
}
