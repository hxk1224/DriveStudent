package com.drive.student.view;

import android.content.Context;
import android.util.AttributeSet;

import com.scaleview.ScaleTextView;

public class AlwaysMarqueeTextView extends ScaleTextView {
    public AlwaysMarqueeTextView(Context context) {
        super(context);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
