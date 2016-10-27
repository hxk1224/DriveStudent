package com.drive.student.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.drive.student.MainApplication;
import com.drive.student.bean.VersionBean;
import com.drive.student.config.Constant;
import com.drive.student.ui.VersionUpdateActivity;
import com.drive.student.util.BackUtil;
import com.drive.student.util.LogUtil;
import com.drive.student.util.SystemUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class BasePushReceiver extends BroadcastReceiver {
    private static final String TAG = "BasePushReceiver";

    public BasePushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    protected void hanlderPushMsg(Context context, String extraJson) {
        try {
            JSONObject resultJson = new JSONObject(extraJson);
            LogUtil.e(TAG, "[BasePushReceiver] onReceive resultJson === " + resultJson);
            String msgCode = resultJson.getString("msgCode");
            LogUtil.e(TAG, "[BasePushReceiver] onReceive msgCode === " + msgCode);
            if (Constant.PUSH_UPDATE.equalsIgnoreCase(msgCode)) {
                // 更新版本
                handlerUpdate(context, resultJson);
            }
        } catch (JSONException e) {
            LogUtil.e(TAG, "", e);
        }
    }

    /** 更新版本 */
    private void handlerUpdate(Context context, JSONObject resultJson) throws JSONException {
        VersionBean versionBean = new VersionBean();
        versionBean.status = resultJson.getInt("status");
        versionBean.pakageUrl = resultJson.getString("pakageUrl");
        versionBean.pakageContent = resultJson.getString("pakageContent");
        versionBean.md5 = resultJson.getString("md5");
        versionBean.pakageSize = resultJson.getString("pakageSize");
        versionBean.pakageVersionName = resultJson.getString("pakageVersionName");
        versionBean.versionCode = resultJson.getInt("versionCode");
        MainApplication.mVersonBean = versionBean;
        int localVer = SystemUtil.getVersionCode(context);
        if (localVer > 0 && localVer < versionBean.versionCode) {
            // 有新版本
            if (versionBean.status != Constant.UPDATE_NEEDLESS) {
                if (BackUtil.isRunningForeground(context)) {// 前台
                    Intent i = new Intent(context, VersionUpdateActivity.class);
                    i.putExtra("bean", versionBean);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else {// 后台 ，或者没有运行
                    MainApplication.mVersonBean = versionBean;
                    MainApplication.hasNewVersion = true;
                }
            }
        }
    }
}
