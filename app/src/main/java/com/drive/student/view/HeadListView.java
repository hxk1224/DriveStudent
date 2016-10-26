/**
 * @author 韩新凯
 * @version 创建时间：2014-8-18 下午1:24:25
 */

package com.drive.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

class HeadListView extends ListView {

    /**
     * @param context
     */
    public HeadListView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public HeadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public HeadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
