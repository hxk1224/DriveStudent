package com.scaleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @description：和ScrollView一起用，显示左右的item。
 * Created by zz_mac on 16/1/8.
 */
public class ScaleExpandGridView extends ScaleGridView {
    public ScaleExpandGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleExpandGridView(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
