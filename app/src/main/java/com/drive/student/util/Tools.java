package com.drive.student.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Tools {

    public static int getSysTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return day;
    }

    /**
     * 产生int型随机数
     *
     * @param n 范围
     * @return
     * @author ting
     * @update 2014-10-29 下午3:54:49
     */
    public static int getRandom(int n) {
        Random random = new Random();
        int rd = random.nextInt(n);
        return rd;
    }

    /**
     * 判断是否阶梯间升级
     *
     * @param level 升级后的等级
     * @return true:梯间 false:梯内
     */
    public static boolean isLadder(int level) {
        switch (level) {
            case 1:
                return true;
            case 4:
                return true;
            case 7:
                return true;
            case 11:
                return true;
            case 15:
                return true;
            default:
                return false;
        }
    }

    /** 判断顶层窗口 */
    public static String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }
}
