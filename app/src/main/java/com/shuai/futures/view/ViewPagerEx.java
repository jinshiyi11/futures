package com.shuai.futures.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 是否允许滑动切换page
 */
public class ViewPagerEx extends ViewPager {
    private boolean mAllowDrag=true;

    public ViewPagerEx(Context context) {
        super(context);
    }

    public ViewPagerEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAllowDrag(boolean allow){
        mAllowDrag=allow;
    }

    public boolean isAllowDrag(){
        return mAllowDrag;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!mAllowDrag){
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
