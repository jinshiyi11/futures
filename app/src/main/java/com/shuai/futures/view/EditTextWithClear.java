package com.shuai.futures.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.shuai.futures.R;
import com.shuai.futures.utils.DisplayUtils;


public class EditTextWithClear extends EditText {
	private LinearLayout mWrapperView;
	private ImageView mIvClear;
	private TextWatcher mTextChangeListener;

	public EditTextWithClear(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EditTextWithClear(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EditTextWithClear(Context context) {
		super(context);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		init(getContext());
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(mTextChangeListener!=null)
			removeTextChangedListener(mTextChangeListener);
	}

	private void init(Context context){
		if(mWrapperView!=null)
			return;
		
		mWrapperView=new LinearLayout(context);
		mWrapperView.setOrientation(LinearLayout.HORIZONTAL);
		mIvClear=new ImageView(context);
		
		boolean isFocused=isFocused();
		ViewGroup parent=((ViewGroup)getParent());
		parent.addView(mWrapperView, parent.indexOfChild(this),getLayoutParams());
		parent.removeView(this);	
		mWrapperView.setBackgroundDrawable(getBackground());
		setBackgroundDrawable(null);
		mWrapperView.addView(this, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
		mWrapperView.addView(mIvClear, new LinearLayout.LayoutParams(DisplayUtils.dp2px(getContext(), 38), LayoutParams.MATCH_PARENT, 0));
		mIvClear.setImageResource(R.drawable.edit_clear_btn);
		mIvClear.setScaleType(ScaleType.CENTER_INSIDE);
		
		if(isFocused)
			requestFocus();
		
		mIvClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setText(null);
			}
		});
		
		mTextChangeListener=new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				updateClearButton();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		
		addTextChangedListener(mTextChangeListener);
		updateClearButton();
	}
	
	private void updateClearButton(){
		mIvClear.setVisibility(TextUtils.isEmpty(getText()) ? View.INVISIBLE : View.VISIBLE);
	}
	
	public View getWrapperView(){
		return mWrapperView;
	}
}
