package com.shuai.futures.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.shuai.futures.R;
import com.shuai.futures.data.Config;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.LoginStateChanged;
import com.shuai.futures.data.Stat;
import com.shuai.futures.logic.UserManager;
import com.shuai.futures.ui.base.BaseFragment;
import com.shuai.futures.utils.AppUtils;
import com.shuai.futures.utils.CommonUtils;
import com.shuai.futures.utils.NavigateUtils;
import com.shuai.futures.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class SettingsFragment extends BaseFragment {
    private Context mContext;
    private LinearLayout mLlModifyPassword;
    private RelativeLayout mRlRate;
    private RelativeLayout mRlFeedback;
    private RelativeLayout mRlUpdate;
    private TextView mTvVersion;
    private RelativeLayout mRlAbout;
    private RelativeLayout mRlServicePhone;
    private LinearLayout mLlLogout;
//    private UmengUpdateListener mUpdateListener;

    public int messageToIndex;
    public int selectedIndex;
    private ProgressDialog progressDialog;
    private boolean progressShow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ((TextView) view.findViewById(R.id.tv_title)).setText("设置");
        mLlModifyPassword = (LinearLayout) view.findViewById(R.id.ll_modify_password);
        mRlRate = (RelativeLayout) view.findViewById(R.id.rl_rate);
        mRlFeedback = (RelativeLayout) view.findViewById(R.id.rl_feedback);
        mRlUpdate = (RelativeLayout) view.findViewById(R.id.rl_update);
        mTvVersion = (TextView) view.findViewById(R.id.tv_version);
        mRlAbout = (RelativeLayout) view.findViewById(R.id.rl_about);
        mRlServicePhone = (RelativeLayout) view
                .findViewById(R.id.rl_service_phone);
        mLlLogout = (LinearLayout) view.findViewById(R.id.ll_logout);
        mRlRate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.showMarket(mContext);
            }
        });
        updateUi();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mUpdateListener = null;
//        UmengUpdateAgent.setUpdateListener(null);
        EventBus.getDefault().unregister(this);

    }

    @Subscribe
    public void onEvent(LoginStateChanged state) {
        updateUi();
    }

    private void updateUi() {
        boolean isLogin = UserManager.getInstance().isLogined();
        if (isLogin) {
            mLlModifyPassword.setVisibility(View.VISIBLE);
            mLlLogout.setVisibility(View.VISIBLE);

            mLlModifyPassword.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
//					Intent intent = new Intent(mContext,
//							ModifyPasswordActivity.class);
//					startActivity(intent);

                    Intent intent = new Intent(mContext,
                            RegisterByPhoneActivity.class);
                    intent.putExtra(Constants.EXTRA_IS_MODIFY_APSSWORD, true);
                    startActivity(intent);
                }
            });

            mLlLogout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    UserManager.getInstance().logout(true);
                    Utils.showShortToast(mContext, "已退出");

                    NavigateUtils.showLoginActivity(mContext,null);
                    getActivity().finish();
                }
            });
        } else {
            mLlModifyPassword.setVisibility(View.GONE);
            mLlLogout.setVisibility(View.GONE);
        }

        mRlFeedback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mTvVersion.setText(String.format("当前版本%1$s",
                AppUtils.getVersionName(mContext)));


        mRlUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        mRlAbout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AboutActivity.class);
                startActivity(intent);
            }
        });

        mRlServicePhone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + getString(R.string.service_phone).replace("-", "")));
                startActivity(intent);
            }
        });
    }

   /* private ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressShow = false;
                }
            });
        }
        return progressDialog;
    }*/

}
