package com.shuai.futures.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shuai.futures.R;


/**
 * 正在加载界面
 */
public class LoadingView extends LinearLayout {
	private Context mContext;
	private ImageView mIvLoading;
	private Animation mAnimation;
//	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LoadingView(Context context) {
		super(context);
		init();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		if(mIvLoading!=null && mAnimation!=null){
			mIvLoading.clearAnimation();
		}
		super.onDetachedFromWindow();
	}

	private void init(){
		mContext=getContext();
		View view= LayoutInflater.from(mContext).inflate(R.layout.layout_loading, this, true);
		mIvLoading=(ImageView) view.findViewById(R.id.iv_loading);
		
		if(!isInEditMode()){
			mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
	//		LinearInterpolator lin = new LinearInterpolator();  
	//		mAnimation.setInterpolator(lin);  
			mIvLoading.startAnimation(mAnimation);
		}
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		 if (mAnimation != null && mIvLoading != null && mAnimation.hasStarted()) {  
			 mIvLoading.clearAnimation();  
			 mIvLoading.startAnimation(mAnimation);  
		    }  
	}
	
	

	
}
