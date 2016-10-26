package com.drive.student.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class CustomeDialogUtil {

    /**
     * 获取带自定义layout的Dialog
     *
     * @param context  上下文环境
     * @param layoutId 布局id
     * @param gravity  dialog的位置 如：Gravity.BOTTOM
     * @param hPercent dialog高度占屏幕高度的百分比0~1
     * @param wPercent dialog宽度占屏幕宽度的百分比0~1
     * @return AlertDialog对象
     */
    public static AlertDialog showDialog(Context context, int layoutId, int gravity, float hPercent, float wPercent) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(layoutId);
        // 将对话框的大小按屏幕大小的百分比设置
        if (!(context instanceof Activity)) {
            return null;
        }
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * hPercent); // 高度设置为屏幕的hPercent
        p.width = (int) (d.getWidth() * wPercent); // 宽度设置为屏幕的wPercent
        p.gravity = gravity;
        window.setAttributes(p);
        return dialog;
    }

    /**
     * 获取带自定义layout的Dialog，使用默认Gravity
     *
     * @param context  上下文环境
     * @param layoutId 布局id
     * @param hPercent dialog高度占屏幕高度的百分比0~1
     * @param wPercent dialog宽度占屏幕宽度的百分比0~1
     * @return AlertDialog对象
     */
    public static AlertDialog showDialog(Context context, int layoutId, float hPercent, float wPercent) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(layoutId);
        // 将对话框的大小按屏幕大小的百分比设置
        if (!(context instanceof Activity)) {
            return null;
        }
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * hPercent); // 高度设置为屏幕的hPercent
        p.width = (int) (d.getWidth() * wPercent); // 宽度设置为屏幕的wPercent
        window.setAttributes(p);
        return dialog;
    }

    /**
     * 获取带自定义layout的Dialog，Dialog高度为自适应。
     *
     * @param context  上下文环境
     * @param layoutId 布局id
     * @param gravity  dialog的位置 如：Gravity.BOTTOM
     * @param wPercent dialog宽度占屏幕宽度的百分比0~1
     * @return AlertDialog对象
     */
    public static AlertDialog showDialog(Context context, int layoutId, int gravity, float wPercent) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(layoutId);
        // 将对话框的大小按屏幕大小的百分比设置
        if (!(context instanceof Activity)) {
            return null;
        }
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth() * wPercent); // 宽度设置为屏幕的wPercent
        p.gravity = gravity;
        window.setAttributes(p);
        return dialog;
    }

    /**
     * 获取带自定义layout的Dialog，Dialog高度为自适应，使用默认Gravity
     *
     * @param context  上下文环境
     * @param layoutId 布局id
     * @param wPercent dialog宽度占屏幕宽度的百分比0~1
     * @return AlertDialog对象
     */
    public static AlertDialog showDialog(Context context, int layoutId, float wPercent) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(layoutId);
        // 将对话框的大小按屏幕大小的百分比设置
        if (!(context instanceof Activity)) {
            return null;
        }
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth() * wPercent); // 宽度设置为屏幕的wPercent
        window.setAttributes(p);
        return dialog;
    }

}
