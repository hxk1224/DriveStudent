package com.scaleview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableRow;

/**
 * Created by lizhennian on 2014/5/30.
 */
public class ScaleTableRow extends TableRow {
    public ScaleTableRow(Context context) {
        super(context);
    }

    public ScaleTableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleTableRow(Context context, AttributeSet attrs, int defStyle) {
        // super(context, attrs, defStyle);
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
