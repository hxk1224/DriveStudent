package com.drive.student;

import com.drive.student.util.LogUtil;

import java.lang.Thread.UncaughtExceptionHandler;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return mCatchHandler;
    }

    private static CrashHandler mCatchHandler = new CrashHandler();

    private MainApplication application;

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtil.e(TAG, "CrashHandler Exception", ex);
        // 程序崩溃了,悄悄记录下错误日志发送出去,然后猥琐的退出
//		try {
//			Thread.sleep(4000);
//		} catch (InterruptedException e) {
//		}
        application.exit();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void init(MainApplication application) {
        LogUtil.e(TAG, "CrashHandler init-->>");
        this.application = application;
        application.getApplicationContext();
        // 获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

}
