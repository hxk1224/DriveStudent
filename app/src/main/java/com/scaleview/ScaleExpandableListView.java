package com.scaleview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by lizhennian on 2014/5/30.
 */
public class ScaleExpandableListView extends ExpandableListView {
    public ScaleExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleExpandableListView(Context context, AttributeSet attrs,
                                   int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScaleExpandableListView(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.setIndicatorBounds(w - 56, w);
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
