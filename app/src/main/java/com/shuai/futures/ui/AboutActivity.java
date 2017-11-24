package com.shuai.futures.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.shuai.futures.R;
import com.shuai.futures.ui.base.BaseActivity;
import com.shuai.futures.utils.AppUtils;

public class AboutActivity extends BaseActivity {
    private TextView mTvTitle;
    private TextView mTvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("关于");

        mTvVersion= (TextView) findViewById(R.id.tv_version);
        mTvVersion.setText("v"+AppUtils.getVersionName(mContext));
    }

}
