package com.scaleview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.drive.student.util.LogUtil;
import com.drive.student.util.ReflectionUtils;

import java.lang.reflect.Field;

public class ScaleCalculator {
    // 基础像素密度density为1.0， 宽720， 高1280
    private static final float BASE_HEIGHT = 1280.0f;
    private static final float BASE_WIDTH = 720.0f;
    private static final float BASE_DENSITY = 1.0f;

    private static float mCurrentHeight = BASE_HEIGHT;
    private static float mCurrentWidth = BASE_WIDTH;
    private static float mCurrentDensity = BASE_DENSITY;

    private static ScaleCalculator mInstance;

    private ScaleCalculator(Context context) {
        this.getScreenSize(context);
    }

    private static ScaleCalculator init(Context context) {
        if (mInstance == null) {
            mInstance = new ScaleCalculator(context);
        }
        return mInstance;
    }

    public static ScaleCalculator getInstance(Context context) {
        if (mInstance == null) {
            return init(context);
        }
        return mInstance;
    }

    /**
     * Get the current device screen size
     *
     * @param context
     */
    private void getScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        mCurrentDensity = displayMetrics.density;
        LogUtil.e("ScreenSize", "width = " + displayMetrics.widthPixels);
        LogUtil.e("ScreenSize", "height = " + displayMetrics.heightPixels);
        if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
            mCurrentWidth = correctWidth(displayMetrics.heightPixels);
            mCurrentHeight = this.correctHeight(displayMetrics.widthPixels);
        } else {
            mCurrentWidth = correctWidth(displayMetrics.widthPixels);
            mCurrentHeight = this.correctHeight(displayMetrics.heightPixels);
        }
    }

    /**
     * Scale text size
     *
     * @param pixel
     * @return
     */
    public final int scaleTextSize(float pixel) {
        if (pixel < 0.0F) {
            return 0;
        }
        if (this.isBaseSize()) {
            return (int) pixel;
        }
        if (mCurrentWidth / BASE_WIDTH >= mCurrentHeight / BASE_HEIGHT) {
            return this.scaleHeight((int) pixel);
        }
        return this.scaleWidth((int) pixel);
    }

    public final void scaleViewGroup(ViewGroup viewGroup) {
        if (viewGroup == null || this.isBaseSize()) {
            return;
        }
        int i = viewGroup.getChildCount();
        for (int j = 0; j < i; j++) {
            this.scaleView(viewGroup.getChildAt(j));
        }
    }

    public final void scaleView(View view) {
        if (view == null || this.isBaseSize()) {
            return;
        }

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            int height = layoutParams.height;
            int width = layoutParams.width;
            if (height > 0 && !this.isBaseHeight()) {
                height = this.scaleHeight(height);
                layoutParams.height = height;
            }
            if (width > 0 && !this.isBaseWidth()) {
                width = this.scaleWidth(width);
                layoutParams.width = width;
            }
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                this.scaleMargin((ViewGroup.MarginLayoutParams) layoutParams);
            }
        }
        this.scalePadding(view);
    }

    private void scaleMargin(
            ViewGroup.MarginLayoutParams marginLayoutParams) {

        if (marginLayoutParams == null || this.isBaseSize()) {
            return;
        }
        if (!this.isBaseWidth()) {
            if (marginLayoutParams.leftMargin != 0) {
                marginLayoutParams.leftMargin = this
                        .scaleWidth(marginLayoutParams.leftMargin);
            }
            if (marginLayoutParams.rightMargin != 0) {
                marginLayoutParams.rightMargin = this
                        .scaleWidth(marginLayoutParams.rightMargin);
            }
        }

        if (!this.isBaseHeight()) {
            if (marginLayoutParams.topMargin != 0) {
                marginLayoutParams.topMargin = this
                        .scaleHeight(marginLayoutParams.topMargin);
            }
            if (marginLayoutParams.bottomMargin != 0) {
                marginLayoutParams.bottomMargin = this
                        .scaleHeight(marginLayoutParams.bottomMargin);
            }
        }

    }

    private void scalePadding(View view) {
        if (view == null || this.isBaseSize()) {
            return;
        }
        int paddingLeft = view.getPaddingLeft();
        int paddingTop = view.getPaddingTop();
        int paddingRight = view.getPaddingRight();
        int paddingBottom = view.getPaddingBottom();

        if (!this.isBaseWidth()) {
            if (paddingLeft > 0) {
                paddingLeft = this.scaleWidth(paddingLeft);
            }
            if (paddingRight > 0) {
                paddingRight = this.scaleWidth(paddingRight);
            }
            int minimumWidth = this.getMinimumWidth(view);
            if (minimumWidth > 0) {
                minimumWidth = this.scaleWidth(minimumWidth);
                view.setMinimumWidth(minimumWidth);
            }
            if (view instanceof TextView) {
                int miniWidth = this.getMinWidth((TextView) view);
                if (miniWidth > 0) {
                    miniWidth = this.scaleWidth(miniWidth);
                    ((TextView) view).setMinWidth(miniWidth);
                }
            }
        }
        if (!this.isBaseHeight()) {
            if (paddingBottom > 0) {
                paddingBottom = this.scaleHeight(paddingBottom);
            }
            if (paddingTop > 0) {
                paddingTop = this.scaleHeight(paddingTop);
            }
            int minimumHeight = this.getMinimumHeight(view);
            if (minimumHeight > 0) {
                minimumHeight = this.scaleHeight(minimumHeight);
                view.setMinimumHeight(minimumHeight);
            }
            if (view instanceof TextView) {
                int miniHeight = this.getMinHeight((TextView) view);
                if (miniHeight > 0) {
                    miniHeight = this.scaleHeight(miniHeight);
                    ((TextView) view).setMinHeight(miniHeight);
                }
            }

        }

        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    @SuppressLint("NewApi")
    private Integer getMinimumWidth(View view) {
        if (this.isIceCreamSandwichMR1OrOld()) {// api <= 15
            try {
                Field field = View.class.getDeclaredField("mMinWidth");
                Object ob = ReflectionUtils.getFieldValueSafely(field, view);
                return (Integer) ob;
            } catch (Exception e) {
                return view.getWidth();
            }
        } else {
            return view.getMinimumWidth();
        }
    }

    @SuppressLint("NewApi")
    private Integer getMinimumHeight(View view) {
        if (this.isIceCreamSandwichMR1OrOld()) {// api <= 15
            try {
                Field field = View.class.getDeclaredField("mMinHeight");
                Object ob = ReflectionUtils.getFieldValueSafely(field, view);
                return (Integer) ob;
            } catch (Exception e) {
                return view.getHeight();
            }
        } else {
            return view.getMinimumHeight();
        }
    }

    @SuppressLint("NewApi")
    private Integer getMinWidth(TextView view) {
        if (this.isIceCreamSandwichMR1OrOld()) {// api <= 15
            try {
                Field field = TextView.class.getDeclaredField("mMinWidth");
                Object ob = ReflectionUtils.getFieldValueSafely(field, view);
                return (Integer) ob;
            } catch (Exception e) {
                return view.getWidth();
            }
        } else {
            return view.getMinWidth();
        }
    }

    @SuppressLint("NewApi")
    private Integer getMinHeight(TextView view) {
        if (this.isIceCreamSandwichMR1OrOld()) {// api <= 15
            try {
                Field field = TextView.class.getDeclaredField("mMinimum");
                Object ob = ReflectionUtils.getFieldValueSafely(field, view);
                return (Integer) ob;
            } catch (Exception e) {
                return view.getHeight();
            }
        } else {
            return view.getMinHeight();
        }
    }

    /**
     * 判断api等级
     *
     * @return true：api等级 <= 15
     * false：api等级 > 15
     */
    private boolean isIceCreamSandwichMR1OrOld() {
        int version = getAndroidSDKVersion();
        // api <= 15
        return version <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    /**
     * 获取SDK version
     *
     * @return SDK version
     */
    public static int getAndroidSDKVersion() {
        int version;
        try {
            version = Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
            version = 0;
        }
        return version;
    }

    private int correctHeight(int height) {
        if ((height >= 1100) && (height <= 1280)) {
            height = 1280;
        }
        return height;
    }

    private int correctWidth(int width) {
        if ((width >= 672) && (width <= 720)) {
            width = 720;
        }
        return width;
    }

    public final int scaleWidth(int pixel) {
        return Math.round(this.adpaterDensity(pixel) * mCurrentWidth
                / BASE_WIDTH);
    }

    public final int scaleHeight(int pixel) {
        return Math.round(this.adpaterDensity(pixel) * mCurrentHeight
                / BASE_HEIGHT);
    }

    private int adpaterDensity(float pixel) {
        return (int) (0.5F + pixel / mCurrentDensity * BASE_DENSITY);
    }

    private boolean isBaseSize() {
        return (this.isBaseHeight()) && (this.isBaseWidth());
    }

    private boolean isBaseHeight() {
        return (mCurrentHeight / BASE_HEIGHT == mCurrentDensity / BASE_DENSITY);
    }

    private boolean isBaseWidth() {
        return (mCurrentWidth / BASE_WIDTH == mCurrentDensity / BASE_DENSITY);
    }

}