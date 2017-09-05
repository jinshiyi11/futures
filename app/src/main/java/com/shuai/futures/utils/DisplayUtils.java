package com.shuai.futures.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtils {
    public static float getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        return displayMetrics.widthPixels;
    }

    public static float getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        return displayMetrics.heightPixels;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(displayMetrics);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            try {
                displayMetrics.widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                displayMetrics.heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception ex) {
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            try {
                //API>=17才有
                display.getRealMetrics(displayMetrics);
            } catch (Exception ex) {
            }

        return displayMetrics;
    }
	
	/**
	 * 单位px转换成单位sp(字体)
	 * @return
	 */
	public static int px2sp(Context context, int pxVaule) {
		return (int)(pxVaule / getDisplayMetrics(context).scaledDensity + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 */
	public static int sp2px(Context context, float spValue) {
		return (int) (spValue * getDisplayMetrics(context).scaledDensity + 0.5f);
	}

	/**
	 * 将px转换成dp值
	 * @return
	 */
	public static int px2dp(Context context, int pxVaule) {
		return (int)(pxVaule / getDisplayMetrics(context).density + 0.5f);
	}

	/**
	 * 将dp值转换为px值
	 * 
	 */
	public static int dp2px(Context context, float dpValue) {
		return (int) (dpValue * getDisplayMetrics(context).density + 0.5f);
	}
}
