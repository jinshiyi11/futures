package com.shuai.futures.ui;

import android.os.Bundle;
import android.view.View;

import com.shuai.futures.R;
import com.shuai.futures.ui.base.BaseTabFragment;
import com.shuai.futures.view.WebViewWrapper;

/**
 *
 */
public class NewsFragment extends BaseTabFragment {
    private WebViewWrapper mWebView;

    public NewsFragment() {
        super(R.layout.fragment_news);
    }

    @Override
    protected void onInit(View root, Bundle savedInstanceState) {
        mWebView = (WebViewWrapper) root.findViewById(R.id.webview_wrapper);
        mWebView.loadUrl("http://finance.sina.cn/stock");
//        mWebView.loadUrl("http://baidu.com");
    }
}
