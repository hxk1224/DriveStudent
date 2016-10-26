package com.drive.student.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * 判断activity 后台运行，等行为的工具类。
 *
 * @author 韩新凯
 */
public class BackUtil {
    /**
     * 判断程序时候后台运行
     *
     * @param context
     * @param pageName
     */
    public static boolean isBackgroundRunning(Context context, String pageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<RunningAppProcessInfo> processList = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo process : processList) {
            if (process.processName.startsWith(pageName)) {
                boolean isBackground = process.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND && process.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE;
                boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
                return isBackground || isLockedState;
            }
        }
        return false;
    }

    /**
     * 判断某个activity是否前台运行
     *
     * @param mContext
     * @param activityClassName 要判断的activity 的class
     */
    public static boolean isActivityRunningForground(Context mContext, String activityClassName) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if (info != null && info.size() > 0) {
            ComponentName component = info.get(0).topActivity;
            if (activityClassName.equals(component.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        return !TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName());

    }

}
