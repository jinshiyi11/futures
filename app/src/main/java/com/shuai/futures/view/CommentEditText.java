package com.shuai.futures.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class CommentEditText extends EditText {
	private OnKeyPreImeListener mListener;
	
	public interface OnKeyPreImeListener{
		public boolean onKeyPreIme(int keyCode, KeyEvent event);
	}

	public CommentEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CommentEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CommentEditText(Context context) {
		super(context);
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if(mListener!=null){
			boolean result=mListener.onKeyPreIme(keyCode, event);
			if(result)
				return true;
		}
		return super.onKeyPreIme(keyCode, event);
	}
	
	public void setOnKeyPreImeListener(OnKeyPreImeListener listener){
		mListener=listener;
	}

}
