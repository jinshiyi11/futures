package com.shuai.futures.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shuai.futures.R;
import com.shuai.futures.ui.base.BaseFragment;
import com.shuai.futures.utils.NavigateUtils;

/**
 *
 */
public class UserCenterSettingsFragment extends BaseFragment implements View.OnClickListener {
    private Context mContext;
    private RelativeLayout mRlFollowed;
    private RelativeLayout mRlSettings;
    private RelativeLayout mRlAbout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_user_center_settings,
                container, false);
        mRlFollowed = (RelativeLayout) view.findViewById(R.id.rl_followed);
        mRlSettings = (RelativeLayout) view.findViewById(R.id.rl_settings);
        mRlAbout = (RelativeLayout) view.findViewById(R.id.rl_about);

        mRlFollowed.setOnClickListener(this);
        mRlSettings.setOnClickListener(this);
        mRlAbout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.rl_followed:
                NavigateUtils.showTab(mContext,NavigateUtils.TAB_FOLLOWED);
                break;
            case R.id.rl_settings:
                NavigateUtils.showSettingsActivity(mContext);
                break;
            case R.id.rl_about:
                NavigateUtils.showAboutActivity(mContext);
                break;
        }

    }
}
