package com.drive.student.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.drive.student.R;
import com.drive.student.ui.MainActivity;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.SoundUtil;
import com.drive.student.util.VibratorUtil;

/**
 * 发送通知管理
 */
public class NoticeManager {
    private static final int GLOBAL_NOTIFICATION_ID = 999;
    private static NoticeManager noticeManager = null;
    private Context context;

    private NoticeManager(Context context) {
        this.context = context;
    }

    public static NoticeManager getInstance(Context context) {
        if (noticeManager == null) {
            noticeManager = new NoticeManager(context);
        }
        return noticeManager;
    }

    // 默认显示的的Notification
    public void showDefaultNotification(String title, String content, Intent intent, int notifactionid) {
        SharePreferenceUtil spUtil = new SharePreferenceUtil(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(title);
        builder.setAutoCancel(true);
        builder.setLights(0xff00ff00, 300, 1000);
        // now apply the latestEventInfo fields
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.app_icon);
        // 添加声音效果
         builder.setDefaults(Notification.DEFAULT_SOUND);
        // 使用消息声音
        SoundUtil.getInstance().playMsgSound();
        // 将该通知显示为默认View
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifactionid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(notifactionid, builder.build());
        long[] vibrate = {0, 300, 150, 300, 150, 300, 150, 300};
        VibratorUtil.Vibrate(context, vibrate, false);
    }

    /** 全局显示的Notification,防止进程被系统杀掉 */
    public void showGlobalNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setWhen(System.currentTimeMillis());
        String title = context.getResources().getString(R.string.app_name);
        builder.setTicker(title);
        builder.setLights(0xff00ff00, 300, 1000);
        // now apply the latestEventInfo fields
        builder.setContentTitle(title);
        builder.setContentText(title);
        builder.setSmallIcon(R.drawable.app_icon);
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        // 添加声音效果
        // builder.setDefaults(Notification.DEFAULT_SOUND);
        // 将该通知显示为默认View
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, GLOBAL_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(GLOBAL_NOTIFICATION_ID, builder.build());
    }

    public void clearAllNotifation() {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        showGlobalNotification();
    }

    /***
     * 清除指定通知
     *
     * @param id notification id
     */
    public void clearNotifationWithID(int id) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(id);
    }

    /***
     * 删除制定id集合的通知
     *
     * @param ids 要清楚的notification id数组
     */
    public void clearNotifationWithIDs(String[] ids) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            for (String id : ids) {
                mNotificationManager.cancel(Integer.parseInt(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
