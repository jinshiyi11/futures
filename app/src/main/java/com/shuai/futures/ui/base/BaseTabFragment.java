package com.shuai.futures.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;

import com.shuai.futures.R;

/**
 * 用于优化放在ViewPager中的Fragment，在切换到该Fragment对应的tab时才inflate
 */
public abstract class BaseTabFragment extends BaseFragment {

	protected Context mContext;
	private Handler mHandler = new Handler();

	private boolean mInitialized;
	private ViewStub mViewStub;
	private int mLayoutResource;

	public BaseTabFragment(int layoutResource) {
		mLayoutResource = layoutResource;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
		mInitialized=false;
		mContext=getActivity();
		View view = inflater.inflate(R.layout.fragment_stub, container, false);

		mViewStub = (ViewStub) view.findViewById(R.id.viewStub);

		mViewStub.setLayoutResource(mLayoutResource);
		mViewStub.setOnInflateListener(new OnInflateListener() {

			@Override
			public void onInflate(ViewStub stub, View inflated) {
				mInitialized=true;
				onInit(inflated, savedInstanceState);
			}
		});


        return view;
	}
	
	@Override
	public void onDestroyView() {
		mInitialized=false;
		super.onDestroyView();
	}
	
	protected boolean isInitialized(){
		return mInitialized;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if(isVisibleToUser && !mInitialized){
			mInitialized=true;
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					mViewStub.setVisibility(View.VISIBLE);
				}
			});
		}
	}
	
	protected abstract void onInit(View root, Bundle savedInstanceState);

}
