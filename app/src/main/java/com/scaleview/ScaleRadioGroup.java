package com.scaleview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

/**
 * Created by lizhennian on 2014/5/30.
 */
public class ScaleRadioGroup extends RadioGroup {

    public ScaleRadioGroup(Context context) {
        super(context);
    }

    public ScaleRadioGroup(Context context, AttributeSet attrs) {
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
}
