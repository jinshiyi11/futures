package com.shuai.futures.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shuai.futures.R;


/**
 * 保持图像宽高比不变，一边不变，另一边被缩放
 */
public class WrapContentImageView extends ImageView {
	private boolean mWrapWidth;
	private boolean mWrapHeight;

	public WrapContentImageView(Context context) {
		super(context);
        mWrapHeight=true;
	}

	public WrapContentImageView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public WrapContentImageView(Context context, AttributeSet attrs,
                                int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WrapContentImageView);
		mWrapWidth=a.getBoolean(R.styleable.WrapContentImageView_wrapWidth, false);
		mWrapHeight=a.getBoolean(R.styleable.WrapContentImageView_wrapHeight, true);
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Call super() so that resolveUri() is called.
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// If there's no drawable we can just use the result from super.
		if (getDrawable() == null)
			return;

//		final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//		final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

		int w = getDrawable().getIntrinsicWidth();
		int h = getDrawable().getIntrinsicHeight();
		if (w <= 0)
			return;
		
		if (h <= 0)
			return;

		// Desired aspect ratio of the view's contents (not including padding)
		float desiredAspect = (float) w / (float) h;

		int pleft = getPaddingLeft();
		int pright = getPaddingRight();
		int ptop = getPaddingTop();
		int pbottom = getPaddingBottom();

		// Get the sizes that ImageView decided on.
		int widthSize = getMeasuredWidth();
		int heightSize = getMeasuredHeight();

		if (mWrapWidth) {
			// Resize the width to the height, maintaining aspect ratio.
			int newWidth = (int) (desiredAspect * (heightSize - ptop - pbottom))
					+ pleft + pright;
			setMeasuredDimension(newWidth, heightSize);
		} else if (mWrapHeight) {
			int newHeight = (int) ((widthSize - pleft - pright) / desiredAspect)
					+ ptop + pbottom;
			setMeasuredDimension(widthSize, newHeight);
		}
	}

}
