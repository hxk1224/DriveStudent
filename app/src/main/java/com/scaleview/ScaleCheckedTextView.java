package com.scaleview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

/**
 * Created by lizhennian on 2014/5/29.
 */
public class ScaleCheckedTextView extends CheckedTextView {
    public ScaleCheckedTextView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        setTextSize(getTextSize());
    }

    public ScaleCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextSize(getTextSize());
    }

    public ScaleCheckedTextView(Context context) {
        super(context);
        setTextSize(getTextSize());
    }

    @Override
    public void setTextSize(float textSize) {
        setTextSize(0, textSize);
    }

    @Override
    public void setTextSize(int unit, float textSize) {
        try {
            textSize = ScaleCalculator.getInstance(getContext()).scaleTextSize(
                    textSize);
        } catch (Exception e) {
        }
        super.setTextSize(unit, textSize);
    }
}
