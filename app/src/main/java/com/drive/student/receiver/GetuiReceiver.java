package com.drive.student.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

public class GetuiReceiver extends BasePushReceiver {
    private static final String TAG = "GetuiReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.e(TAG, "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid")
                byte[] payload = bundle.getByteArray("payload");
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                Log.e(TAG, "第三方回执接口调用" + (result ? "成功" : "失败"));
                if (payload != null) {
                    String extraJson = new String(payload);
                    Log.e(TAG, "receiver payload : " + extraJson);
                    hanlderPushMsg(context, extraJson);
                }
                break;
            /*case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                break;

            case PushConsts.THIRDPART_FEEDBACK:
            *//*
             * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
			 * String result = bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 * 
			 * Log.d(TAG, "appid = " + appid); Log.d(TAG, "taskid = " +
			 * taskid); Log.d(TAG, "actionid = " + actionid); Log.d(TAG,
			 * "result = " + result); Log.d(TAG, "timestamp = " + timestamp);
			 *//*
                break;*/
        }
    }

}
