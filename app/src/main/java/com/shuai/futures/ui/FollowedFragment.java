package com.shuai.futures.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shuai.futures.R;
import com.shuai.futures.ui.base.BaseTabFragment;

/**
 * 我的关注
 */
public class FollowedFragment extends BaseTabFragment {
    private TextView mTvTest;

    public FollowedFragment() {
        super(R.layout.fragment_followed);
    }

    @Override
    protected void onInit(View root, Bundle savedInstanceState) {
        mTvTest = (TextView) root.findViewById(R.id.tv_test);
        mTvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,CandleStickActivity.class);
                startActivity(intent);
            }
        });

    }
}
