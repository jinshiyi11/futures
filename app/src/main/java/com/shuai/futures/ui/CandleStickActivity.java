package com.shuai.futures.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.FuturesInfo;
import com.shuai.futures.data.FuturesPrice;
import com.shuai.futures.data.KlineItem;
import com.shuai.futures.data.LoadingStatus;
import com.shuai.futures.data.TimeLineItem;
import com.shuai.futures.protocol.GetFuturesPriceListTask;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.ui.base.BaseFragment;
import com.shuai.futures.ui.base.BaseFragmentActivity;
import com.shuai.futures.utils.Utils;
import com.shuai.futures.view.ViewPagerEx;
import com.shuai.futures.view.chart.KlineHead;
import com.shuai.futures.view.chart.KlineType;
import com.shuai.futures.view.chart.OnKlineHighlightListener;
import com.shuai.futures.view.chart.OnTimeLineHighlightListener;
import com.shuai.futures.view.chart.TimeLineHead;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TabPageIndicatorEx;
import com.viewpagerindicator.TabViewInterface;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CandleStickActivity extends BaseFragmentActivity implements OnTimeLineHighlightListener, OnKlineHighlightListener {
    private Context mContext;
    private String mFuturesId;
    private String mFuturesName;
    private LoadingStatus mStatus = LoadingStatus.STATUS_LOADING;
    private RequestQueue mRequestQueue;
    private Handler mHandler = new Handler();

    private ViewGroup mNoNetworkContainer;
    private ViewGroup mLoadingContainer;
    private ViewGroup mMainContainer;
    private TextView mTvTitle;
    private ViewPagerEx mViewPager;
    FragmentPagerAdapter mAdapter;
    private TabPageIndicatorEx mIndicator;

    private TextView mTvCurrentPrice;
    private TextView mTvDiff;
    private TextView mTvPercent;
    private TextView mTvOpen;
    private TextView mTvClose;
    private TextView mTvHigh;
    private TextView mTvLow;

    private TimeLineHead mTimelineHead;
    private KlineHead mKlineHead;

    private FuturesPrice mPriceInfo;

    private Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            getFuturesPrice();
        }
    };

    private static final String[] TAB_TITLES = new String[]{"分时", "5分", "15分", "30分", "60分", "日K"};
    private static final KlineType[] KLINE_TYPES = {null, KlineType.K5Minutes, KlineType.K15Minutes, KlineType.K30Minutes, KlineType.K60Minutes, KlineType.KDaily,};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candlestick);

        mContext = this;
        Intent intent = getIntent();
        mFuturesId = intent.getStringExtra(Constants.EXTRA_FUTURES_ID);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("期货");

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

        mTvCurrentPrice = (TextView) findViewById(R.id.tv_current_price);
        mTvDiff = (TextView) findViewById(R.id.tv_diff);
        mTvPercent = (TextView) findViewById(R.id.tv_percent);
        mTvOpen = (TextView) findViewById(R.id.tv_open);
        mTvClose = (TextView) findViewById(R.id.tv_close);
        mTvHigh = (TextView) findViewById(R.id.tv_high);
        mTvLow = (TextView) findViewById(R.id.tv_low);

        mTimelineHead = (TimeLineHead) findViewById(R.id.ll_time_line_head);
        mKlineHead = (KlineHead) findViewById(R.id.ll_kline_head);
        mViewPager = (ViewPagerEx) findViewById(R.id.pager);
        mViewPager.setAllowDrag(false);
        mAdapter = new OrderTabAdapter(
                getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(TAB_TITLES.length);
        mIndicator = (TabPageIndicatorEx) findViewById(R.id.tbi_order);

        setStatus(LoadingStatus.STATUS_LOADING);
        getFuturesPrice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(null);
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
        futures.add(new FuturesInfo(mFuturesId));
        GetFuturesPriceListTask request = new GetFuturesPriceListTask(mContext, futures, new Response.Listener<List<FuturesPrice>>() {

            @Override
            public void onResponse(List<FuturesPrice> futuresPrices) {
                mPriceInfo = futuresPrices.get(0);
                if (mFuturesName == null) {
                    mFuturesName = mPriceInfo.mName;
                    mTvTitle.setText(mFuturesName);
                }
                int textColor = getResources().getColor(R.color.up);
                if (mPriceInfo.mCurrentPrice < mPriceInfo.mLastdayPrice) {
                    textColor = getResources().getColor(R.color.down);
                }

                mTvCurrentPrice.setText(String.valueOf(mPriceInfo.mCurrentPrice));
                mTvCurrentPrice.setTextColor(textColor);

                double diff = mPriceInfo.mCurrentPrice - mPriceInfo.mLastdayPrice;
                mTvDiff.setText(diff >= 0 ? String.format("+%.2f", diff) : String.format("%.2f", diff));
                mTvDiff.setTextColor(textColor);
                mTvPercent.setText(diff >= 0 ? String.format("+%.2f%%", mPriceInfo.getPercent() * 100) : String.format("%.2f%%", mPriceInfo.getPercent() * 100));
                mTvPercent.setTextColor(textColor);

                mTvHigh.setText(String.valueOf(mPriceInfo.mHigh));
                mTvLow.setText(String.valueOf(mPriceInfo.mLow));
                mTvOpen.setText(String.valueOf(mPriceInfo.mOpen));
                mTvClose.setText(String.valueOf(mPriceInfo.mLastdayPrice));

                if (mStatus == LoadingStatus.STATUS_LOADING) {
                    setStatus(LoadingStatus.STATUS_GOT_DATA);

                    mViewPager.setAdapter(mAdapter);
                    mIndicator.setViewPager(mViewPager);
                }
                refreshInfo();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (mStatus == LoadingStatus.STATUS_LOADING) {
                    setStatus(LoadingStatus.STATUS_NO_NETWORK);
                    Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
                } else {
                    refreshInfo();
                }
            }
        }
        );

        request.setTag(this);
        mRequestQueue.add(request);
    }

    private void refreshInfo() {
        mHandler.removeCallbacks(mRefreshRunnable);
        mHandler.postDelayed(mRefreshRunnable, 1000);
    }

    @Override
    public void onTimeLineHighlighted(TimeLineItem item) {
        if (mTimelineHead.getVisibility() != View.VISIBLE) {
            mTimelineHead.setVisibility(View.VISIBLE);
            mIndicator.setVisibility(View.GONE);
        }
        mTimelineHead.setData(mPriceInfo.mLastdayPrice, item);
    }

    @Override
    public void onTimeLineUnhightlighted() {
        if (mTimelineHead.getVisibility() == View.VISIBLE) {
            mTimelineHead.setVisibility(View.GONE);
            mIndicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onKlineHighlighted(KlineItem item) {
        if (mKlineHead.getVisibility() != View.VISIBLE) {
            mKlineHead.setVisibility(View.VISIBLE);
            mIndicator.setVisibility(View.GONE);
        }
        mKlineHead.setData(mPriceInfo.mLastdayPrice, item);
    }

    @Override
    public void onKlineUnhightlighted() {
        if (mKlineHead.getVisibility() == View.VISIBLE) {
            mKlineHead.setVisibility(View.GONE);
            mIndicator.setVisibility(View.VISIBLE);
        }
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
                f = new TimeLineChartFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble(TimeLineChartFragment.KEY_LASTDAY_PRICE, mPriceInfo.mLastdayPrice);
                f.setArguments(bundle);

                ((TimeLineChartFragment) f).setHighlightListener(CandleStickActivity.this);
            } else {
                f = new KlineChartFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(KlineChartFragment.KEY_KLINE_CHART_TYPE, KLINE_TYPES[position]);
                f.setArguments(bundle);
                ((KlineChartFragment) f).setHighlightListener(CandleStickActivity.this);
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
