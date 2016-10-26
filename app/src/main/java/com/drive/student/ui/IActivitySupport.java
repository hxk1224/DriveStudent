package com.drive.student.ui;

import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.drive.student.MainApplication;
import com.drive.student.view.LoadingDialog;

/**
 * Activity帮助支持类接口.
 *
 * @author 韩新凯
 */
public interface IActivitySupport {

    /**
     * 会话验证.
     */
    boolean validateToken(String result);

    /**
     * 检查是否存在SD卡.
     */
    boolean checkSDCard();

    /**
     * 获取MainApplication.
     */
    MainApplication getMainApplication();

    /**
     * 校验网络-如果没有网络就弹出设置,并返回true.
     */
    boolean validateInternet();

    /**
     * 校验网络-如果没有网络就返回false.
     */
    boolean hasInternetConnected();

    /**
     * 判断GPS是否已经开启.
     */
    boolean hasLocationGPS();

    /**
     * 判断基站是否已经开启.
     */
    boolean hasLocationNetWork();

    /**
     * 验证手机最大可用存储，并设置成当前应用根路径.
     */
    boolean checkStoragePathAndSetBaseApp();

    /**
     * 显示toast.
     *
     * @param text    内容
     * @param longint 内容显示多长时间
     */
    void showToast(String text, int longint);

    /**
     * 短时间显示toast.
     *
     * @param text
     */
    void showToast(String text);

    /**
     * 获取进度条.
     */
    LoadingDialog getProgressDialog();

    /**
     * 自定义Alertdialog
     *
     * @param v
     */
    AlertDialog showAlertDialog(View v);

    /**
     * 显示错误提示.
     *
     * @param ed
     * @author 韩新凯
     * @update 2014-5-8 下午3:02:55
     */
    void showEorrorOnEditText(EditText ed);
}
