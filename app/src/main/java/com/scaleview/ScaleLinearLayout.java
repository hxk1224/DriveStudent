package com.scaleview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by lizhennian on 2014/5/29.
 */
public class ScaleLinearLayout extends LinearLayout {
    public ScaleLinearLayout(Context context) {
        super(context);
    }

    public ScaleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("NewApi")
    public ScaleLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        try {
            ScaleCalculator.getInstance(getContext()).scaleViewGroup(this);
        } catch (Exception e) {

        }
    }
}
