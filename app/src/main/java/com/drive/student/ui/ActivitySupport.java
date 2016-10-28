package com.drive.student.ui;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.drive.student.MainApplication;
import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.config.UrlConfig;
import com.drive.student.manager.NoticeManager;
import com.drive.student.ui.login.LoginActivity;
import com.drive.student.util.BackUtil;
import com.drive.student.util.CustomToast;
import com.drive.student.util.LogUtil;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.view.ConfirmDialog;
import com.drive.student.view.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cn.jpush.android.api.JPushInterface;

/**
 * Actity 工具支持类
 */
public class ActivitySupport extends FragmentActivity implements IActivitySupport {
    private static final String TAG = "ActivitySupport";
    /**
     * 共享存储工具类
     **/
    protected SharePreferenceUtil spUtil;
    /**
     * Application
     **/
    protected MainApplication mainApplication;
    /**
     * Notify管理类
     **/
    protected NotificationManager notificationManager;
    /**
     * 错误提示页面
     **/
    protected View errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        super.onCreate(savedInstanceState);
        initData();
    }

    /**
     * 初始化数据.
     */
    private void initData() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mainApplication = (MainApplication) getApplication();
        spUtil = new SharePreferenceUtil(this);
        mainApplication.addActivity(this);
    }

    /**
     * 跳转到登陆页面
     */
    public boolean goLogin() {
        if (mainApplication.getUser() == null || StringUtil.isBlank(mainApplication.getUserCode())) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onResume(this);
        /**友盟统计*/
        // MobclickAgent.onPageStart(getClass().getName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长

        /** 如果之前已show出过了,要记得取消,不然会造成对话框重复弹出 **/
        if (MainApplication.hasNewVersion && (MainApplication.mVersonBean != null) && (!BackUtil.isActivityRunningForground(ActivitySupport.this, VersionUpdateActivity.class.getName()))) {
            if (Constant.UPDATE_NEEDLESS != MainApplication.mVersonBean.status) {
                Intent in = new Intent(ActivitySupport.this, VersionUpdateActivity.class);
                in.putExtra("bean", MainApplication.mVersonBean);
                startActivity(in);
            }
        }
        MainApplication.mVersonBean = null;
        MainApplication.hasNewVersion = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 与StatService.onPause(this);
         */
        StatService.onPause(this);
        /** 友盟统计 */
        // MobclickAgent.onPageEnd(getClass().getName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mainApplication.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean validateToken(String result) {
        result = StringUtil.doEmpty(result);
        if (!StringUtil.isEmpty(result) && result.contains("code")) {
            try {
                JSONObject obj = new JSONObject(result);
                int code = obj.getInt("code");
                if (Constant.TOKEN_INVALIDATE == code) {
                    // token失效时，清除通知栏，防止通过通知越过登陆进入系统
                    NoticeManager.getInstance(this).clearAllNotifation();
                    mainApplication.setUser(null);
                    spUtil.setToken("");
                    if (!BackUtil.isActivityRunningForground(this, TokenInvalidateActivity.class.getName())) {
                        Intent intent = new Intent(mainApplication, TokenInvalidateActivity.class);
                        if (obj.has("message")) {
                            intent.putExtra("message", obj.getString("message"));
                        } else {
                            intent.putExtra("message", "");
                        }
                        startActivity(intent);
                    }
                    return false;
                } else if (Constant.UPDATE_FORCE == code) {// 验证接口的版本信息,强制用户升级
                    // 清除通知栏，防止通过通知越过登陆进入系统
                    stopJpush();
                    // 清除通知栏，防止通过通知越过登陆进入系统
                    NoticeManager.getInstance(this).clearAllNotifation();
                    if (!BackUtil.isActivityRunningForground(this, ForceUpdateActivity.class.getName())) {
                        Intent intent = new Intent(mainApplication, ForceUpdateActivity.class);
                        if (obj.has("message")) {
                            intent.putExtra("message", obj.getString("message"));
                        } else {
                            intent.putExtra("message", "");
                        }
                        startActivity(intent);
                    }
                    return false;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 网络错误
     *
     * @param frame
     * @param setText
     */
    public void showNetWorkError(FrameLayout frame, final SetText setText) {
        showExceptionView(frame, null, 1, setText);

    }

    /**
     * 功能：数据为空
     *
     * @param frame
     * @param errorMsg
     * @param settext
     */
    public void showEmpty(FrameLayout frame, String errorMsg, final SetText settext) {
        showExceptionView(frame, errorMsg, 2, settext);
    }

    /**
     * 功能：服务器数据错误等
     *
     * @param frame
     * @param settext
     */
    public void showServiceError(FrameLayout frame, final SetText settext) {
        showExceptionView(frame, null, 0, settext);
    }

    /**
     * 这个方法作用:服务器错误、网络错误、数据为空等
     *
     * @param frame
     * @param errorMsg 错误信息
     * @param settext  如果只写onclick事件，其他的都有默认的，其他方法可根据需求自定义。
     * @param type     0:服务器数据错误、1：网络不可用、2：数据为空
     */
    public void showExceptionView(FrameLayout frame, String errorMsg, int type, final SetText settext) {
        // frame.removeAllViews();
        if (errorView != null) {
            frame.removeView(errorView);
            errorView = null;
        }
        if (type == Constant.SER_ERR_CODE) {// 服务器数据错误等
            errorView = View.inflate(this, R.layout.view_amicable, null);
        } else if (type == Constant.NET_ERR_CODE) {// 网络不可用
            errorView = View.inflate(this, R.layout.view_amicable_empty, null);

        } else {// 数据为空
            errorView = View.inflate(this, R.layout.view_amicable_empty, null);
            TextView errorMsgV = (TextView) errorView.findViewById(R.id.error_msg);
            if (StringUtil.notEmpty(errorMsg)) {
                errorMsgV.setText(errorMsg);
            }
            ImageView iv = (ImageView) errorView.findViewById(R.id.iv_res);
            iv.setImageResource(R.drawable.loading_null);
        }

        errorView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (null != settext) {
                    settext.onClick();
                }

            }
        });
        frame.addView(errorView);
    }

    /**
     * 这个方法作用:移除错误页面
     */
    public void removeErrorView(FrameLayout frame) {
        if (null != errorView) {
            frame.removeView(errorView);
            errorView = null;
        }

    }

    public interface SetText {
        /**
         * 这个方法作用：点击事件
         */
        void onClick();
    }

    @Override
    public boolean hasInternetConnected() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        showToastInThread(R.string.check_connection);
        return false;
    }

    @Override
    public boolean validateInternet() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            openWirelessSet();
            return false;
        } else {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        openWirelessSet();
        return false;
    }

    @Override
    public boolean hasLocationGPS() {
        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public boolean hasLocationNetWork() {
        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public boolean checkStoragePathAndSetBaseApp() {
        String storagePath = null;
        List<Long> memorySize = null;
        Map<Long, String> storageMap = new HashMap<>();

        // 如果可以检测到SD卡返回SD卡路径，否则就反射得到最大可以用的机身存储
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            StorageManager sm = (StorageManager) this.getSystemService(Context.STORAGE_SERVICE);
            String[] paths;
            try {
                paths = (String[]) sm.getClass().getMethod("getVolumePaths").invoke(sm);
                for (String path : paths) {
                    StatFs stat = new StatFs(path);
                    long blockSize = stat.getBlockSize();
                    long availableBlocks = stat.getAvailableBlocks();
                    long storageSize = availableBlocks * blockSize;
                    if (storageSize > 0) {
                        memorySize = new ArrayList<Long>();
                        memorySize.add(storageSize);
                        storageMap.put(storageSize, path);
                    }
                }
                if (memorySize != null) {
                    storagePath = storageMap.get(Collections.max(memorySize));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (storagePath == null) {
            storagePath = this.getFilesDir().getAbsolutePath();

        }
        spUtil.setStoragePath(storagePath);
        return true;
    }

    public void openWirelessSet() {
        ConfirmDialog dialog = ConfirmDialog.createDialog(this);
        dialog.setDialogTitle(R.string.prompt);
        dialog.setDialogMessage(R.string.check_connection);
        dialog.setCancelable(false);
        dialog.setOnButton1ClickListener(R.string.settings, R.color.white, new ConfirmDialog.OnButton1ClickListener() {
            @Override
            public void onClick(View view, DialogInterface dialog) {
                dialog.cancel();
                Intent intent;
                try {
                    intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        dialog.setOnButton2ClickListener(R.string.exit, R.color.white, new ConfirmDialog.OnButton2ClickListener() {
            @Override
            public void onClick(View view, DialogInterface dialog) {
                dialog.cancel();
                mainApplication.exit();
            }
        });
        dialog.show();
    }

    /**
     * 显示toast
     */
    @Override
    public void showToast(String text, int longint) {
        CustomToast.showToast(this, text, longint);
    }

    @Override
    public void showToast(String text) {
        CustomToast.showToast(this, text, Toast.LENGTH_SHORT);
    }

    protected void showToastInThread(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                CustomToast.showToast(ActivitySupport.this, msg, 0);
            }
        });
    }

    protected void showToastInThread(final int msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                CustomToast.showToast(ActivitySupport.this, msg, 0);
            }
        });
    }

    /**
     * 显示键盘事件
     */
    public void showKeyboard(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 显示键盘事件
     */
    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getCurrentFocus() != null) {
            inputMethodManager.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 显示键盘事件
     */
    public void showKeyboardForced() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getCurrentFocus() != null) {
            inputMethodManager.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 关闭键盘事件
     */
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 强制关闭键盘
     *
     * @param et
     */
    public void hideKeyboardForced(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != et) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    @Override
    public LoadingDialog getProgressDialog() {
        LoadingDialog lodingDialog = LoadingDialog.createDialog(this);
        lodingDialog.setCancelable(false);
        return lodingDialog;
    }

    @Override
    public MainApplication getMainApplication() {
        return mainApplication;
    }

    /**
     * 自定义Alertdialog
     */
    @Override
    public AlertDialog showAlertDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        return builder.create();
    }

    @Override
    public void showEorrorOnEditText(EditText ed1) {
        ed1.setError("不允许为空");
        ed1.setFocusable(true);
        ed1.requestFocus();
        Animation shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
        ed1.startAnimation(shakeAnim);

    }

    /***
     * 设置极光推送，别名，标签等。
     */
    protected void initJpush() {
        if (!StringUtil.equalsNull(mainApplication.getSupplierId())) {
            String alias = mainApplication.getSupplierId();
            Set<String> tags = new TreeSet<>();
            switch (UrlConfig.DOMAIN) {
                case UrlConfig.DOMAIN_OFFICIAL:
                    //正式环境
                    tags.add(getString(R.string.car_student_push_tag));
                    break;
                case UrlConfig.DOMAIN_CESHI:
                    //58测试环境
                    alias = "ceshi_" + alias;
                    tags.add(getString(R.string.car_student_push_tag));
                    break;
            }
            JPushInterface.init(mainApplication);
            JPushInterface.resumePush(mainApplication);
            JPushInterface.setAliasAndTags(mainApplication, alias, tags);
            LogUtil.e("Jpush", "initJpush-->>OK-->alias = " + alias);
        } else {
            LogUtil.e("Jpush", "initJpush-->>FAIL");
            stopJpush();
        }
    }

    /** 停止极光推送消息 */
    protected void stopJpush() {
        Set<String> tags = new TreeSet<>();
        tags.add("-");
        JPushInterface.setAliasAndTags(mainApplication, "-", tags);
        JPushInterface.stopPush(mainApplication);
        LogUtil.e("Jpush", "stopPush-->>");
    }
}