package com.drive.student.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.scaleview.ScaleEditText;

public class DeleteEditText extends ScaleEditText {
    private Drawable mRightDrawable;
    boolean isHasFocus;
    //构造方法1
    public DeleteEditText(Context context) {
        super(context);
        init();
    }

    //构造方法2
    public DeleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    //构造方法3
    public DeleteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        Drawable drawables[] = this.getCompoundDrawables();//本方法获取控件上下左右四个方位插入的图片
        mRightDrawable = drawables[2];
        this.addTextChangedListener(new TextWatcherImpl());
        this.setOnFocusChangeListener(new OnFocusChangeImpl());

        setClearDrawableVisible(false);//初始设置所有右边图片不可见
    }
    private class OnFocusChangeImpl implements  OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isHasFocus = hasFocus;
            if (isHasFocus) {//如果获取焦点
                boolean isNoNull = getText().toString().length() >= 1;
                setClearDrawableVisible(isNoNull);
            }else{
                setClearDrawableVisible(false);
            }
        }
    }
    //本方法控制右边图片的显示与否
    private void setClearDrawableVisible(boolean isNoNull) {
        Drawable rightDrawable;
        if (isNoNull) {
            rightDrawable = mRightDrawable;
        } else {
            rightDrawable = null;
        }
        // 使用代码设置该控件left, top, right, and bottom处的图标
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], rightDrawable,
                getCompoundDrawables()[3]);
    }
    private class TextWatcherImpl implements TextWatcher {
        //下面是三个要覆写的方法
        @Override
        public void afterTextChanged(Editable s) {//内容输入后
            boolean isNoNull = getText().toString().length() >= 1;
            setClearDrawableVisible(isNoNull);
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //内容输入前
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //内容输入中
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int length1 = getWidth() - getPaddingRight();//删除图片右侧到EditText控件最左侧距离
                int length2 = getWidth() - getTotalPaddingRight();//删除图片左侧到EditText控件最左侧距离
                boolean isClean = (event.getX() > length2)
                        && (event.getX() < length1);
                if (isClean) {
                    setText("");
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}