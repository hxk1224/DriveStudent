package com.drive.student.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.List;

public class IntentUtil {
    /**
     * 这个方法作用：判断某个action是否存在
     *
     * @param context
     * @param action
     * @return
     * @author 史明松
     * @update 2014-7-10 下午3:30:17
     */
    public static boolean isInstalled(Context context, String action) {
        PackageManager packageManager = context.getPackageManager();
        // 指定要查找的 action
        Intent intent = new Intent(action);
        // 在系统中查询指定的Activity Action
        List resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        return !(resolveInfo == null || resolveInfo.size() == 0);
    }

}
