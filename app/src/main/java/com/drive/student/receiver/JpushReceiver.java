package com.drive.student.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.drive.student.util.LogUtil;

import cn.jpush.android.api.JPushInterface;

public class JpushReceiver extends BasePushReceiver {
    private static final String TAG = "JpushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogUtil.d(TAG, "[JpushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {// 升级推送
            // 接收到推送下来的自定义消息
            // String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extraJson = bundle.getString(JPushInterface.EXTRA_EXTRA);
            hanlderPushMsg(context, extraJson);
        }
        /* else if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            // JPush用户注册成功
            // 接收Registration Id
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            // send the Registration Id to your server...
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            // 接收到推送下来的通知
            // 接收到推送下来的通知的ID
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            // 用户点击打开了通知
            *//*Intent i = new Intent(context, WelcomeActivity.class);
            i.putExtras(bundle);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);*//*
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            // 用户收到RICH PUSH CALLBACK
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LogUtil.w(TAG, "[JpushReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            LogUtil.d(TAG, "[JpushReceiver] Unhandled intent - " + intent.getAction());
        }*/

    }

    /*
    打印所有的 intent extra 数据，内容如下：
    key:cn.jpush.android.PUSH_ID, value:304523822
    key:cn.jpush.android.NOTIFICATION_TYPE, value:0
    key:app, value:com.jiandan.submithomework
    key:cn.jpush.android.ALERT, value:测试推送-这是推送内容
    key:cn.jpush.android.EXTRA, value:{"message":"测试推送","附加字段3":"ccc","附加字段2":"bbb","附加字段4":"ddd","code":"0","success":"true","附加字段1":"aaa"}
    key:cn.jpush.android.NOTIFICATION_ID, value:304523822
    key:cn.jpush.android.NOTIFICATION_CONTENT_TITLE, value:这是推送测试标题
    key:cn.jpush.android.MSG_ID, value:304523822
     */
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}
