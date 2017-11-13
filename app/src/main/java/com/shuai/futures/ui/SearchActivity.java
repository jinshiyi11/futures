package com.shuai.futures.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.adapter.SearchResultAdapter;
import com.shuai.futures.data.DataManager;
import com.shuai.futures.data.FuturesInfo;
import com.shuai.futures.logic.UserManager;
import com.shuai.futures.protocol.AddFollowedFuturesTask;
import com.shuai.futures.protocol.ErrorInfo;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.protocol.SearchFuturesTask;
import com.shuai.futures.ui.base.BaseFragmentActivity;
import com.shuai.futures.utils.UiUtils;
import com.shuai.futures.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 期货搜索页
 */
public class SearchActivity extends BaseFragmentActivity implements SearchResultAdapter.AddFollowListener {
    private RequestQueue mRequestQueue;
    private DataManager mDataManager;
    private UserManager mUserManager;
    private Button mBtnCancel;
    private SearchView mSearchView;
    private ListView mLvSearchResult;
    private SearchResultAdapter mAdapter;
    private List<FuturesInfo> mFuturesList = new ArrayList<>();

    private WorkHandler mHandler = new WorkHandler();
    private Object mSearchTag = new Object();

    private class WorkHandler extends Handler {
        public static final int MESSAGE_DO_QUERY = 1;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_DO_QUERY:
                    String key = (String) msg.obj;
                    SearchFuturesTask request = new SearchFuturesTask(mContext, key, new Response.Listener<List<FuturesInfo>>() {
                        @Override
                        public void onResponse(List<FuturesInfo> futuresInfoList) {
                            mFuturesList.clear();
                            mFuturesList.addAll(futuresInfoList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
                        }
                    });

                    request.setTag(mSearchTag);
                    mRequestQueue.add(request);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = MyApplication.getRequestQueue();
        mDataManager = DataManager.getInstance();
        mUserManager = UserManager.getInstance();
        setContentView(R.layout.activity_search);

        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearchView = (SearchView) findViewById(R.id.sv_search);
        mLvSearchResult = (ListView) findViewById(R.id.lv_search_result);
        mSearchView.setQueryHint("搜索期货");
        mSearchView.setIconifiedByDefault(false);

        //TODO:为什么调用setIconified(false)不能呼出软键盘？
        //mSearchView.setIconified(false);
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    UiUtils.showSoftInput(mContext,view.findFocus());
                }
            }
        });

        mAdapter = new SearchResultAdapter(mContext, mFuturesList);
        mLvSearchResult.setAdapter(mAdapter);
        mAdapter.setAddFollowListener(this);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSearchResult();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getSearchResult();
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);
        }
    }

    private void getSearchResult() {
        CharSequence query = mSearchView.getQuery();
        mRequestQueue.cancelAll(mSearchTag);
        mHandler.removeMessages(WorkHandler.MESSAGE_DO_QUERY);
        if (TextUtils.isEmpty(query)) {
            mAdapter.clear();
        } else {
            mHandler.sendMessageDelayed(Message.obtain(mHandler,
                    WorkHandler.MESSAGE_DO_QUERY, query.toString()), 500);
        }
    }

    @Override
    public void onAddFollow(final FuturesInfo item) {
        if (mDataManager.isFollowedFutures(item.mId)) {
            return;
        }

        if (mUserManager.isLogined()) {
            AddFollowedFuturesTask requset = new AddFollowedFuturesTask(mContext, item.mId,
                    new Response.Listener<ErrorInfo>() {
                        @Override
                        public void onResponse(ErrorInfo errorInfo) {
                            mDataManager.addFollowedFutures(item);
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(mContext, R.string.add_follow_success, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
                        }
                    });
            requset.setTag(this);
            mRequestQueue.add(requset);
        } else {
            mDataManager.addFollowedFutures(item);
            mAdapter.notifyDataSetChanged();
            Utils.showShortToast(mContext, R.string.add_follow_success);
        }
    }
}
