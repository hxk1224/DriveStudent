package com.scaleview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lizhennian on 2014/5/29.
 */
public class ScaleMargueeTextView extends TextView {
    public ScaleMargueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTextSize(getTextSize());
    }

    public ScaleMargueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextSize(getTextSize());
    }

    public ScaleMargueeTextView(Context context) {
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
            textSize = ScaleCalculator.getInstance(getContext()).scaleTextSize(textSize);
        } catch (Exception e) {
        }
        super.setTextSize(unit, textSize);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
