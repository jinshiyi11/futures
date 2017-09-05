package com.shuai.futures.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class UiUtils {

	/**
	 * 弹出软键盘
	 * 
	 * @param context
	 * @param view
	 */
	public static void showSoftInput(final Context context, final View view) {
		if (view == null) {
			return;
		}

		// InputMethodManager imm = (InputMethodManager)
		// context.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.showSoftInput(view, 0);

		view.requestFocus();
		view.postDelayed(new Runnable() {

			@Override
			public void run() {
				((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
						.showSoftInput(view, 0);
			}
		}, 200);
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 * @param view
	 */
	public static void hiddenSoftInput(Context context, View view) {
		if (view == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

    public static void moveCursorToEnd(EditText editText){
        if(editText==null)
            return;

        editText.setSelection(editText.getText().length());
    }
}
