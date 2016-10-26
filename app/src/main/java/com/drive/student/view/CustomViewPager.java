package com.drive.student.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
    private boolean willIntercept = false;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (willIntercept) {
            try {
                // 这个地方直接返回true会很卡
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (willIntercept) {
            return super.onTouchEvent(ev);
        }
        return true;
    }

    /**
     * 设置ViewPager是否拦截点击事件
     *
     * @param value true, ViewPager拦截点击事件
     *              false, ViewPager将不能滑动，ViewPager的子View可以获得点击事件
     *              主要受影响的点击事件为横向滑动
     */
    public void setTouchIntercept(boolean value) {
        willIntercept = value;
    }

}