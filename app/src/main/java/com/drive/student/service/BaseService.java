package com.drive.student.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * 这个类的作用:做一些系统的后台任务，除了socket的一些操作
 */
@SuppressLint("NewApi")
public class BaseService extends Service {
    protected Context context = BaseService.this;
    // 电源锁
    WakeLock mWakeLock;

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    protected void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "");
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    // 释放设备电源锁
    protected void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

}
