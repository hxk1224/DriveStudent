package com.scaleview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lizhennian on 2014/5/30.
 */
public class ScaleViewPager extends ViewPager {
    public ScaleViewPager(Context context) {
        super(context);
    }

    public ScaleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        try {
            ScaleCalculator.getInstance(getContext()).scaleViewGroup(this);
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }
}
