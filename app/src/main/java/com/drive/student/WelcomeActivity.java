package com.drive.student;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.drive.student.bean.VersonBean;
import com.drive.student.task.AutoLogin;
import com.drive.student.task.CheckVersionTask;
import com.drive.student.task.CheckVersionTask.CheckVersionListener;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.ui.VersionUpdateActivity;
import com.drive.student.util.BackUtil;
import com.drive.student.util.BitmapCompressUtil;
import com.drive.student.util.FileUtil;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.StringUtil;

import java.io.File;

/**
 * 欢迎界面.
 */
public class WelcomeActivity extends ActivitySupport {
    private static final int _WAIT_TIME = 2000;
    public static final String TAG = "WelcomeActivity";
    private static final int UPDATEREQCODE = 2014;
    /** 是否首次登录 **/
    private boolean isFirstlogin;
    /** 校验版本任务 **/
    private CheckVersionTask checkVersionTask;
    /** 进首页开始时间 **/
    private long starttime;
    private SharePreferenceUtil spUtil;
    private Bitmap welBitmap;
    private Handler mHandler = new Handler();
    private ImageView wel_iv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MainApplication.mVersonBean = null;
        MainApplication.hasNewVersion = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        wel_iv = (ImageView) findViewById(R.id.wel_iv);
        spUtil = new SharePreferenceUtil(this);
        /** 用户是否首次登录 **/
        isFirstlogin = spUtil.getisFirst();
        /** 先初始化存储 获得存储路径 **/
        if (checkStoragePathAndSetBaseApp()) {
            /** 再初始化数据 **/
            initdata();
            if (validateInternet()) {
                checkVersionTask = new CheckVersionTask(this, 0, new CheckVersionListener() {
                    @Override
                    public void toGuidOrAutoLoginHandle() {
                        goToLogin();
                    }

                    @Override
                    public void updateNewVersion(VersonBean bean) {
                        if (!BackUtil.isActivityRunningForground(WelcomeActivity.this, VersionUpdateActivity.class.getName())) {
                            Intent in = new Intent(WelcomeActivity.this, VersionUpdateActivity.class);
                            in.putExtra("bean", bean);
                            startActivityForResult(in, UPDATEREQCODE);
                        }
                    }
                });
                checkVersionTask.execute();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//		MobclickAgent.onPageStart(TAG);
//		MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//		MobclickAgent.onPageEnd(TAG);
//		MobclickAgent.onPause(this);
    }

    /**
     * 离开欢迎页面,去登录
     */
    private void goToLogin() {
        autoLogin();
        if (isFirstlogin) {
            /** 如果用户是首次登录 **/
            long currenttime = System.currentTimeMillis();
            long delayMillis = 0;
            if ((currenttime - starttime) < _WAIT_TIME) {
                delayMillis = _WAIT_TIME - (currenttime - starttime);
            }
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    toGuid();
                }
            }, delayMillis);
        } else {
            autoLogin();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == UPDATEREQCODE) {
            goToLogin();
        }
    }

    private void toGuid() {
        /** 如果用户是首次登录 **/
        Intent intent = new Intent();
        // 将用户的第一次登录状态解除
        spUtil.setIsFirst(false);
        intent.setClass(WelcomeActivity.this, GuideViewActivity.class);// 暂时不适用引导页
        startActivity(intent);
        finish();
    }

    private void autoLogin() {
        AutoLogin autoLogin = new AutoLogin(WelcomeActivity.this, mHandler, starttime);
        autoLogin.submitLogin();
    }

    /**
     * 初始化操作.
     */
    private void initdata() {
        if (!isFirstlogin && !StringUtil.equalsNull(spUtil.getWelPictureId())) {
            String welDir = spUtil.getWelcomePath();
            if (!StringUtil.equalsNull(welDir)) {
                File dir = new File(welDir);
                if (dir.isDirectory()) {
                    File[] files = dir.listFiles();
                    if (files != null && files.length > 0) {
                        for (File file : files) {
                            if (file != null && FileUtil.isPicture(file)) {
                                welBitmap = BitmapCompressUtil.getSmallBitmap(file.getAbsolutePath());
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (null != welBitmap) {
            wel_iv.setImageBitmap(welBitmap);
        } else {
            wel_iv.setImageResource(R.drawable.welcome_bg);
        }
        starttime = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (welBitmap != null && !welBitmap.isRecycled()) {
            welBitmap.recycle();
            welBitmap = null;
        }
        if (checkVersionTask != null) {
            checkVersionTask.cancel(true);
        }
    }

}
