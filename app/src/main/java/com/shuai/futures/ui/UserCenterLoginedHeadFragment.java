package com.shuai.futures.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuai.futures.MyApplication;
import com.shuai.futures.R;
import com.shuai.futures.data.UserInfo;
import com.shuai.futures.logic.UserManager;
import com.shuai.futures.protocol.GetUserProfileTask;
import com.shuai.futures.ui.base.BaseFragment;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


public class UserCenterLoginedHeadFragment extends BaseFragment implements OnClickListener {
	private Context mContext;
	private RelativeLayout mRlHead;
	private Button mBtnUserProfile;
	private TextView mTvNickname;
	private ImageView mIvHead;
	
	private RequestQueue mRequestQueue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		mContext=getActivity();
		View view = inflater.inflate(R.layout.fragment_user_center_head_logined, container, false);
		
		mRequestQueue = MyApplication.getRequestQueue();
		
		mRlHead=(RelativeLayout) view.findViewById(R.id.rl_head);
		mBtnUserProfile=(Button) view.findViewById(R.id.btn_user_profile);
		mTvNickname=(TextView) view.findViewById(R.id.tv_nickname);
		mIvHead=(ImageView) view.findViewById(R.id.iv_head);
		mBtnUserProfile.setOnClickListener(this);
		mRlHead.setOnClickListener(this);
		
		EventBus.getDefault().register(this);
		onUserInfoUpdate();
		
		GetUserProfileTask request=new GetUserProfileTask(mContext, null);
		request.setTag(this);
		mRequestQueue.add(request);
		return view;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
		mRequestQueue.cancelAll(this);
	}
	
	@Subscribe
	public void onEvent(UserInfo userInfo){
		onUserInfoUpdate();
	}
	
	private void onUserInfoUpdate(){
		UserInfo info= UserManager.getInstance().getUserInfo();
		String url=info.getHeadImageUrl();
		if(!TextUtils.isEmpty(url)){
			ImageLoader.getInstance().displayImage(url, mIvHead);
		}
		
		String nickname=info.getNickName();
		if(!TextUtils.isEmpty(nickname)){
			mTvNickname.setText(nickname);
		}
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id) {
		case R.id.rl_head:
		case R.id.btn_user_profile:
		{
//			Stat.onEvent(mContext, Stat.EVENT_USER_CENTER_USER_PROFILE_CLICK);
//			Intent intent=new Intent(mContext, UserProfileActivity.class);
//			mContext.startActivity(intent);
		}
			break;

		default:
			break;
		}
		
	}
	
}
