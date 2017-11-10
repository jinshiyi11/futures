package com.shuai.futures.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.adapter.FuturesListAdapter;
import com.shuai.futures.data.FuturesInfo;
import com.shuai.futures.data.FuturesPrice;
import com.shuai.futures.data.LoadingStatus;
import com.shuai.futures.protocol.GetFollowListTask;
import com.shuai.futures.protocol.GetFuturesListTask;
import com.shuai.futures.protocol.GetFuturesPriceListTask;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.ui.base.BaseTabFragment;
import com.shuai.futures.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的自选
 */
public class FollowFragment extends BaseTabFragment {
    private LoadingStatus mStatus = LoadingStatus.STATUS_LOADING;
    private RequestQueue mRequestQueue;
    private Handler mHandler = new Handler();
    private ViewGroup mNoNetworkContainer;
    private ViewGroup mLoadingContainer;
    private ViewGroup mMainContainer;
    private ImageView mIvSearch;

    private PullToRefreshListView mListView;
    private List<FuturesInfo> mFuturesInfoList;
    private List<FuturesPrice> mFuturesPriceList = new ArrayList<>();
    private FuturesListAdapter mAdapter;
    private Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            getFuturesPrice();
        }
    };

    public FollowFragment() {
        super(R.layout.fragment_follow);
    }

    @Override
    protected void onInit(View root, Bundle savedInstanceState) {
        mRequestQueue = MyApplication.getRequestQueue();
        mNoNetworkContainer = (ViewGroup) root.findViewById(R.id.no_network_container);
        mLoadingContainer = (ViewGroup) root.findViewById(R.id.loading_container);
        mMainContainer = (ViewGroup) root.findViewById(R.id.main_container);


        mNoNetworkContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setStatus(LoadingStatus.STATUS_LOADING);
                getFollowList();
            }
        });

        mIvSearch = (ImageView) root.findViewById(R.id.iv_search);
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearchActivity();
            }
        });

        mListView = (PullToRefreshListView) root.findViewById(R.id.listview);
        View headerView = getActivity().getLayoutInflater().inflate(R.layout.market_header, mListView.getRefreshableView(), false);
        mListView.getRefreshableView().addHeaderView(headerView);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getFollowList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        mAdapter = new FuturesListAdapter(mContext, mFuturesPriceList);
        mListView.setAdapter(mAdapter);
        setStatus(LoadingStatus.STATUS_LOADING);
        getFollowList();
    }

    private void gotoSearchActivity() {
        Intent intent = new Intent(mContext, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);
        }
        super.onDestroyView();
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

    private void updateEmptyView() {
        if (mFuturesPriceList.size() > 0)
            return;

        View view = mListView.getRefreshableView().getEmptyView();
        //是否已设置emptyView
        if (view != null)
            return;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View emptyView = inflater.inflate(R.layout.empty_follow_list, mListView, false);
        emptyView.findViewById(R.id.ll_add_follow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearchActivity();
            }
        });
        mListView.setEmptyView(emptyView);
    }

    private void getFollowList() {
        GetFollowListTask request = new GetFollowListTask(mContext, new Response.Listener<List<FuturesInfo>>() {

            @Override
            public void onResponse(List<FuturesInfo> result) {
                mListView.onRefreshComplete();

                mFuturesInfoList = result;
                getFuturesPrice();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mListView.onRefreshComplete();
                if (mAdapter.getCount() == 0) {
                    setStatus(LoadingStatus.STATUS_NO_NETWORK);
                }

                Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
            }
        });

        request.setTag(this);
        mRequestQueue.add(request);
    }

    private void getFuturesPrice() {
        if (mFuturesInfoList == null || mFuturesInfoList.size() == 0) {
            return;
        }

        GetFuturesPriceListTask request = new GetFuturesPriceListTask(mContext, mFuturesInfoList, new Response.Listener<List<FuturesPrice>>() {

            @Override
            public void onResponse(List<FuturesPrice> futuresPrices) {
                if (mStatus == LoadingStatus.STATUS_LOADING) {
                    setStatus(LoadingStatus.STATUS_GOT_DATA);
                }

                mFuturesPriceList = futuresPrices;
                mAdapter.notifyDataSetChanged();
                updateEmptyView();
                refreshList();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (mAdapter.getCount() == 0) {
                    setStatus(LoadingStatus.STATUS_NO_NETWORK);
                    Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
                } else {
                    refreshList();
                }
            }
        }
        );

        request.setTag(this);
        mRequestQueue.add(request);
    }

    private void refreshList() {
        mHandler.removeCallbacks(mRefreshRunnable);
        mHandler.postDelayed(mRefreshRunnable, 1000);
    }
}
