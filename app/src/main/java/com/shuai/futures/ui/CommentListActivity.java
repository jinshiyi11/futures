package com.shuai.futures.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.adapter.CommentAdapter;
import com.shuai.futures.data.Comment;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.LoadingStatus;
import com.shuai.futures.data.Stat;
import com.shuai.futures.logic.UserManager;
import com.shuai.futures.protocol.GetCommentListTask;
import com.shuai.futures.protocol.ProtocolUtils;
import com.shuai.futures.protocol.SubmitCommentTask;
import com.shuai.futures.ui.base.BaseActivity;
import com.shuai.futures.utils.NavigateUtils;
import com.shuai.futures.utils.UiUtils;
import com.shuai.futures.utils.Utils;
import com.shuai.futures.view.CommentEditText;


import java.util.ArrayList;

/**
 * 评论界面
 */
public class CommentListActivity extends BaseActivity implements
        OnClickListener, CommentAdapter.OnItemClickListener {
    private static final int PAGE_COUNT = 20;
    private UserManager mAccountManager;
    private RequestQueue mRequestQueue;

    private String mFuturesId;

    private ViewGroup mNoNetworkContainer;
    private ViewGroup mLoadingContainer;
    private ViewGroup mMainContainer;

    private ImageView mIvCamera;
    private PullToRefreshListView mListView;
    /**
     * 普通评论列表
     */
    private ArrayList<Comment> mCommentList = new ArrayList<>();
    /**
     * 置顶评论列表
     */
    private ArrayList<Comment> mTopCommentList = new ArrayList<>();

    private CommentAdapter mAdapter;
    private CommentEditText mEtComment;
    private Button mBtnSubmitComment;

    private Comment mSelectedComment;

    private boolean mSubmitCommentAfterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment_list);

        mRequestQueue = MyApplication.getRequestQueue();
        mAccountManager = UserManager.getInstance();

        mFuturesId = getIntent().getStringExtra(Constants.EXTRA_FUTURES_ID);

        mNoNetworkContainer = (ViewGroup) findViewById(R.id.no_network_container);
        mLoadingContainer = (ViewGroup) findViewById(R.id.loading_container);
        mMainContainer = (ViewGroup) findViewById(R.id.main_container);

        mNoNetworkContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getData(true);
            }
        });

        mIvCamera = (ImageView) findViewById(R.id.iv_camera);
        mListView = (PullToRefreshListView) findViewById(R.id.lv_comment);
        mEtComment = (CommentEditText) findViewById(R.id.et_comment);
        mBtnSubmitComment = (Button) findViewById(R.id.btn_submit_comment);

        mListView.setMode(Mode.BOTH);
        mAdapter = new CommentAdapter(mContext, mCommentList);
        mAdapter.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        mIvCamera.setOnClickListener(this);
        mBtnSubmitComment.setOnClickListener(this);

        mEtComment.setOnKeyPreImeListener(new CommentEditText.OnKeyPreImeListener() {

            @Override
            public boolean onKeyPreIme(int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && mSelectedComment != null) {
                    resetComment();
                    UiUtils.hiddenSoftInput(mContext, mEtComment);
                    return true;
                }
                return false;
            }
        });

        mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData(false);
            }
        });

        getData(true);
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == Constants.ACTIVITY_REQUEST_EDIT_COMMENT) {
            getData(true);
        }

        if (requestCode == Constants.ACTIVITY_REQUEST_LOGIN) {
            if (resultCode == RESULT_OK && mSubmitCommentAfterLogin) {
                onSubmitClick();
            }
            mSubmitCommentAfterLogin = false;
        }
    }

    private void setStatus(LoadingStatus status) {
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

    private void getData(final boolean isPullDown) {
        if (mCommentList.size() == 0)
            setStatus(LoadingStatus.STATUS_LOADING);

        boolean after = isPullDown;
        long startCommentId = Long.MAX_VALUE;
        if (mCommentList.size() > 0) {
            if (isPullDown) {
                startCommentId = mCommentList.get(0).getCommentId();
            } else {
                startCommentId = mCommentList.get(mCommentList.size() - 1).getCommentId();
            }
        } else {
            startCommentId = Long.MAX_VALUE;
            after = false;
        }

        GetCommentListTask request = new GetCommentListTask(mContext, mFuturesId, startCommentId, PAGE_COUNT, after, new Listener<ArrayList<Comment>>() {

            @Override
            public void onResponse(ArrayList<Comment> comments) {
                mListView.onRefreshComplete();

                if (isPullDown) {
                    if (comments.size() > 0) {
                        int index = 0;
                        if (mTopCommentList != null)
                            index = mTopCommentList.size();
                        mCommentList.addAll(index, comments);
                    } else {
                        if (mCommentList.size() > 0)
                            Toast.makeText(mContext, "暂无更多评论", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCommentList.addAll(comments);
                }

                if (comments.size() > 0)
                    mAdapter.notifyDataSetChanged();

                setStatus(LoadingStatus.STATUS_GOT_DATA);
            }
        }, new ErrorListener() {

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_camera: {
                //Stat.onEvent(mContext, Stat.EVENT_COMMENT_LIST_CAMERA_CLICK);
//                Intent intent = new Intent(mContext, SubmitCommentActivity.class);
//                intent.putExtra(Constants.EXTRA_INSURANCE_ID, mFuturesId);
//                startActivityForResult(intent, Constants.ACTIVITY_REQUEST_EDIT_COMMENT);
                break;
            }

            case R.id.btn_submit_comment: {
                //Stat.onEvent(mContext, Stat.EVENT_COMMENT_LIST_SUBMIT_COMMENT_CLICK);
                onSubmitClick();
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void OnItemClick(Comment comment) {
        mSelectedComment = comment;
        mEtComment.setHint("回复" + mSelectedComment.getCommentUserNickName());

        mEtComment.requestFocus();
        UiUtils.showSoftInput(mContext, mEtComment);
    }

    private void resetComment() {
        mEtComment.setText(null);
        mEtComment.setHint("我也来说两句");
        mSelectedComment = null;
    }

    private void onSubmitClick() {
        final String comment = mEtComment.getEditableText().toString();
        if (TextUtils.isEmpty(comment)) {
            Utils.showShortToast(mContext, "请输入评论内容");
            return;
        }

        boolean logined = UserManager.getInstance().isLogined();
        if (!logined) {
            mSubmitCommentAfterLogin = true;
            NavigateUtils.showLoginActivity(mContext, null, Constants.ACTIVITY_REQUEST_LOGIN);
            return;
        }

        UiUtils.hiddenSoftInput(mContext, mEtComment);
        showProgressDialog();

        long pid = -1;
        long ppid = -1;
        if (mSelectedComment != null) {
            pid = mSelectedComment.getCommentId();
            if (mSelectedComment.getPpid() == Comment.INVALID_ID) {
                ppid = pid;
            } else {
                ppid = mSelectedComment.getPpid();
            }
        }

        SubmitCommentTask request = new SubmitCommentTask(mContext, mFuturesId, pid, ppid,
                comment, null, new Listener<Void>() {
            @Override
            public void onResponse(Void response) {
                hideProgressDialog();
                Utils.showShortToast(mContext, "评论发送成功");

                resetComment();
                getData(true);
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Utils.showShortToast(mContext, ProtocolUtils
                        .getErrorInfo(error).getErrorMessage());
            }
        });

        request.setTag(this);
        mRequestQueue.add(request);
    }

}
