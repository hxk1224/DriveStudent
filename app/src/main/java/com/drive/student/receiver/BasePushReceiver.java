package com.drive.student.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.drive.student.InstallOpenActivity;
import com.drive.student.MainApplication;
import com.drive.student.bean.VersionBean;
import com.drive.student.config.Constant;
import com.drive.student.manager.NoticeManager;
import com.drive.student.ui.MainActivity;
import com.drive.student.ui.VersionUpdateActivity;
import com.drive.student.util.BackUtil;
import com.drive.student.util.IMbackUtil;
import com.drive.student.util.LogUtil;
import com.drive.student.util.SystemUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class BasePushReceiver extends BroadcastReceiver {
    private static final String TAG = "BasePushReceiver";
    private static final String NOTICE_TITLE = "车和汽配(卖家版)";

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
            } else if (Constant.PUSH_BUSINESS_CHAT.equalsIgnoreCase(msgCode)) {
                // 商圈在线聊天
                handlerBusinessChat(context, resultJson);
            } else if (Constant.PUSH_INQUIRY_CHAT.equalsIgnoreCase(msgCode)) {
                // 询价单在线聊天以及系统通知
                handlerInquiryChat(context, resultJson);
            } else if (Constant.PUSH_ORDER_CHAT.equalsIgnoreCase(msgCode)) {
                // 订单在线聊天
                handlerOrderChat(context, resultJson);
            } else if (Constant.PUSH_NEW_INQUIQY.equalsIgnoreCase(msgCode)) {
                // 新的询价单
                handlerNewInquiry(context, resultJson);
            } else if (Constant.PUSH_NEW_ORDER.equalsIgnoreCase(msgCode)) {
                // 新的订单
                handlerNewOrder(context, resultJson);
            } else if (Constant.PUSH_ORDER_PAY.equalsIgnoreCase(msgCode)) {
                // 推送订单支付
                handlerOrderPay(context, resultJson);
            } else if (Constant.PUSH_NEW_POST_SALE.equalsIgnoreCase(msgCode) || Constant.PUSH_POST_SALE_NEW_STATUS.equalsIgnoreCase(msgCode)) {
                // 新的售后单或者售后单状态有更新
                handlerPostStatus(context, resultJson, msgCode);
            } else if (Constant.PUSH_POST_SALE_NEW_MESSGE.equalsIgnoreCase(msgCode)) {
                // 申请的售后状态在线聊天有新的消息
                handlerPostChat(context, resultJson);
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
                    // 此处为模拟数据
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

    /** 商圈在线聊天 */
    private void handlerBusinessChat(Context context, JSONObject resultJson) throws JSONException {
        /* 对方ID	receiverId
          内容	content */
        //NOTE：刷新首页消息数量
        context.sendBroadcast(new Intent(Constant.HOME_REFRESH_NUM_ACTION));
        String content = resultJson.getString("content");
        String msgType = "";
        if (MainApplication.getInstance() != null && MainApplication.getInstance().getUser() != null && BackUtil.isActivityRunningForground(context, BusinessChatDetailActivity.class.getName())) {
            // 商圈聊天明细页面
            Intent intent = new Intent(Constant.RECEIVE_BUSINESS_MSG_ACTION);
            String receiveId = resultJson.getString("receiveId");
            intent.putExtra("message", content);
            intent.putExtra("receiveId", receiveId);
            try {
                // msgType   "1"-普通消息，"2"-语音消息, "3"-图片消息
                // msgLength 语音时长(单位秒s)大于0的整形
                // audioPath 语音地址
                msgType = resultJson.getString("msgType");
                intent.putExtra("msgType", msgType);
                String fileUrl = resultJson.getString("fileUrl");
                intent.putExtra("fileUrl", fileUrl);
                int msgLength = resultJson.getInt("msgLength");
                intent.putExtra("msgLength", msgLength);
                String audioPath = resultJson.getString("audioPath");
                intent.putExtra("audioPath", audioPath);
                String picUrl = resultJson.getString("picUrl");
                intent.putExtra("picUrl", picUrl);
                String spicUrl = resultJson.getString("spicUrl");
                intent.putExtra("spicUrl", spicUrl);
                // 商圈交易id
                String transferId = resultJson.getString("transferId");
                intent.putExtra("transferId", transferId);
            } catch (Throwable t) {

            }
            context.sendBroadcast(intent);
        } else {
            Intent in;
            NoticeManager manager = NoticeManager.getInstance(context);
            if (MainApplication.getInstance() == null) {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() != null && MainActivity.mIsActive) {
                // App已启动并且已登录 刷新商圈聊天列表
                context.sendBroadcast(new Intent(Constant.BUSINESS_REFRESH_CHAT_LIST_ACTION));
                if ("5".equalsIgnoreCase(msgType)) {
                    context.sendBroadcast(new Intent(Constant.BUSINESS_REFRESH_TRADE_LIST_ACTION));
                }
                in = new Intent(context, BusinessAreaActivity.class);
                in.putExtra("WHICH_PAGE", 1);
            } else if (MainApplication.getInstance().getUser() == null && MainActivity.mIsActive) {
                // App已启动并且未登录
                in = new Intent(context, LoginActivity.class);
            } else if (MainApplication.getInstance().getUser() == null && !MainActivity.mIsActive) {
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() == null) {
                in = new Intent(context, MainActivity.class);
            } else {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            }
            // 发通知栏
            manager.showDefaultNotification(NOTICE_TITLE, content, in, Constant.NOTIFY_BUSINESS_NEW_MESSAGE);
        }
    }

    /** 询价单在线聊天以及系统通知 */
    private void handlerInquiryChat(Context context, JSONObject resultJson) throws JSONException {
        //NOTE：刷新首页新消息数量
        context.sendBroadcast(new Intent(Constant.HOME_REFRESH_NUM_ACTION));
        // 刷新询价单列表
        context.sendBroadcast(new Intent(Constant.REFRESH_INQUIRY_LIST));
        String content = resultJson.getString("content");
        String inquirySupplierId = resultJson.getString("inquirySupplierId");
        String inquiryId = resultJson.getString("inquiryId");
        String inquiryCode = resultJson.getString("inquiryCode");
        if (MainApplication.getInstance() != null && MainApplication.getInstance().getUser() != null && BackUtil.isActivityRunningForground(context, InquiryDetailSalerActivity.class.getName())) {
            // 在询价单明细页面
            Intent intent = new Intent(Constant.RECEIVE_INQUIRY_MSG_ACTION);
            intent.putExtra("message", content);
            intent.putExtra("inquirySupplierId", inquirySupplierId);
            intent.putExtra("inquiryId", inquiryId);
            intent.putExtra("inquiryCode", inquiryCode);
            intent.putExtra("msgGlobalType", IMbackUtil.NORMAL_TYPE);
            // msgType;"1"-普通消息，"2"-语音消息
            // msgLength; 语音时长(单位秒s)大于0的整形
            // audioPath 语音地址
            try {
                String msgType = resultJson.getString("msgType");
                intent.putExtra("msgType", msgType);
                String fileUrl = resultJson.getString("fileUrl");
                intent.putExtra("fileUrl", fileUrl);
                int msgLength = resultJson.getInt("msgLength");
                intent.putExtra("msgLength", msgLength);
                String audioPath = resultJson.getString("audioPath");
                intent.putExtra("audioPath", audioPath);
                String picUrl = resultJson.getString("picUrl");
                intent.putExtra("picUrl", picUrl);
                String spicUrl = resultJson.getString("spicUrl");
                intent.putExtra("spicUrl", spicUrl);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            context.sendBroadcast(intent);
        } else {
            Intent in;
            NoticeManager manager = NoticeManager.getInstance(context);
            if (MainApplication.getInstance() == null) {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() != null && MainActivity.mIsActive) {
                // App已启动并且已登录
                in = new Intent(context, MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("FRAG_WHICH", 1);
                // 发通知栏
            } else if (MainApplication.getInstance().getUser() == null && MainActivity.mIsActive) {
                // App已启动并且未登录
                in = new Intent(context, LoginActivity.class);
                // 发通知栏
            } else if (MainApplication.getInstance().getUser() == null && !MainActivity.mIsActive) {
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() == null) {
                in = new Intent(context, MainActivity.class);
            } else {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            }
            manager.showDefaultNotification(NOTICE_TITLE, content, in, Constant.NOTIFY_INQUIRY_NEW_MESSAGE);
        }
    }

    /** 订单在线聊天 */
    private void handlerOrderChat(Context context, JSONObject resultJson) throws JSONException {
        //NOTE：刷新首页询价单新消息数量
        context.sendBroadcast(new Intent(Constant.HOME_REFRESH_NUM_ACTION));
        String content = resultJson.getString("content");
        if (MainApplication.getInstance() != null && MainApplication.getInstance().getUser() != null && BackUtil.isActivityRunningForground(context, OrderDetailSalerActivity.class.getName())) {
            // 在订单明细页面
            Intent intent = new Intent(Constant.RECEIVE_ORDER_MSG_ACTION);
            String orderId = resultJson.getString("orderId");
            intent.putExtra("message", content);
            intent.putExtra("orderId", orderId);
            // msgType;"1"-普通消息，"2"-语音消息
            // msgLength; 语音时长(单位秒s)大于0的整形
            // audioPath 语音地址
            try {
                String msgType = resultJson.getString("msgType");
                intent.putExtra("msgType", msgType);
                String fileUrl = resultJson.getString("fileUrl");
                intent.putExtra("fileUrl", fileUrl);
                int msgLength = resultJson.getInt("msgLength");
                intent.putExtra("msgLength", msgLength);
                String audioPath = resultJson.getString("audioPath");
                intent.putExtra("audioPath", audioPath);
                String picUrl = resultJson.getString("picUrl");
                intent.putExtra("picUrl", picUrl);
                String spicUrl = resultJson.getString("spicUrl");
                intent.putExtra("spicUrl", spicUrl);
            } catch (Throwable t) {

            }
            context.sendBroadcast(intent);
        } else {
            Intent in;
            NoticeManager manager = NoticeManager.getInstance(context);
            if (MainApplication.getInstance() == null) {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() != null && MainActivity.mIsActive) {
                // App已启动并且已登录
                context.sendBroadcast(new Intent(Constant.REFRESH_ORDER_LIST));
                in = new Intent(context, MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("FRAG_WHICH", 2);
                // 发通知栏
            } else if (MainApplication.getInstance().getUser() == null && MainActivity.mIsActive) {
                // App已启动并且未登录
                in = new Intent(context, LoginActivity.class);
                // 发通知栏
            } else if (MainApplication.getInstance().getUser() == null && !MainActivity.mIsActive) {
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() == null) {
                in = new Intent(context, MainActivity.class);
            } else {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            }
            manager.showDefaultNotification(NOTICE_TITLE, content, in, Constant.NOTIFY_ORDER_NEW_MESSAGE);
        }
    }

    /** 新的询价单 */
    private void handlerNewInquiry(Context context, JSONObject resultJson) throws JSONException {
        String content = resultJson.getString("content");
        String inquirySupplierId = resultJson.getString("inquirySupplierId");
        String inquiryId = resultJson.getString("inquiryId");
        String inquiryCode = resultJson.getString("inquiryCode");
        Intent in;
        NoticeManager manager = NoticeManager.getInstance(context);
        if (MainApplication.getInstance() == null) {
            // 应用没有启动
            in = new Intent(context, InstallOpenActivity.class);
        } else if (MainApplication.getInstance().getUser() != null && MainActivity.mIsActive) {
            //NOTE：刷新首页询价单新消息数量
            context.sendBroadcast(new Intent(Constant.HOME_REFRESH_NUM_ACTION));
            // 刷新询价单列表
            Intent refreshInquiryList = new Intent(Constant.REFRESH_INQUIRY_LIST);
            context.sendBroadcast(refreshInquiryList);
            // App已启动并且已登录
            in = new Intent(context, InquiryDetailSalerActivity.class);
            in.putExtra("inquirySupplierId", inquirySupplierId);
            in.putExtra("inquiryId", inquiryId);
            in.putExtra("inquiryCode", inquiryCode);
        } else if (MainApplication.getInstance().getUser() == null && MainActivity.mIsActive) {
            // App已启动并且未登录
            in = new Intent(context, LoginActivity.class);
        } else if (MainApplication.getInstance().getUser() == null && !MainActivity.mIsActive) {
            in = new Intent(context, InstallOpenActivity.class);
        } else if (MainApplication.getInstance().getUser() == null) {
            in = new Intent(context, MainActivity.class);
        } else {
            // 应用没有启动
            in = new Intent(context, InstallOpenActivity.class);
        }
        manager.showDefaultNotification(NOTICE_TITLE, content, in, Constant.NOTIFY_NEW_INQUIRY);
    }

    /** 新的订单 */
    private void handlerNewOrder(Context context, JSONObject resultJson) throws JSONException {
        //NOTE：刷新首页询价单新消息数量
        context.sendBroadcast(new Intent(Constant.HOME_REFRESH_NUM_ACTION));
        String content = resultJson.getString("content");
        String orderId = resultJson.getString("orderId");
        String orderNo = resultJson.getString("orderNo");
        Intent in;
        NoticeManager manager = NoticeManager.getInstance(context);
        if (MainApplication.getInstance() == null) {
            // 应用没有启动
            in = new Intent(context, InstallOpenActivity.class);
        } else if (MainApplication.getInstance().getUser() != null && MainActivity.mIsActive) {
            Intent refreshOrderList = new Intent(Constant.REFRESH_ORDER_LIST);
            context.sendBroadcast(refreshOrderList);
            // App已启动并且已登录
            in = new Intent(context, OrderDetailSalerActivity.class);
            in.putExtra("orderId", orderId);
            in.putExtra("orderNo", orderNo);
            in.putExtra("content", content);
        } else if (MainApplication.getInstance().getUser() == null && MainActivity.mIsActive) {
            // App已启动并且未登录
            in = new Intent(context, LoginActivity.class);
        } else if (MainApplication.getInstance().getUser() == null && !MainActivity.mIsActive) {
            in = new Intent(context, InstallOpenActivity.class);
        } else if (MainApplication.getInstance().getUser() == null) {
            in = new Intent(context, MainActivity.class);
        } else {
            // 应用没有启动
            in = new Intent(context, InstallOpenActivity.class);
        }
        manager.showDefaultNotification(NOTICE_TITLE, content, in, Constant.NOTIFY_NEW_ORDER);
    }

    /** 推送订单支付 */
    private void handlerOrderPay(Context context, JSONObject resultJson) throws JSONException {
        //NOTE：刷新首页询价单新消息数量
        context.sendBroadcast(new Intent(Constant.HOME_REFRESH_NUM_ACTION));
        String content = resultJson.getString("content");
        String orderId = resultJson.getString("orderId");
        String orderNo = resultJson.getString("orderNo");
        if (MainApplication.getInstance() != null && MainApplication.getInstance().getUser() != null && BackUtil.isActivityRunningForground(context, OrderDetailSalerActivity.class.getName())) {
            // 在订单明细页面
            Intent messageIntent = new Intent(Constant.RECEIVE_ORDER_PAY_ACTION);
            messageIntent.putExtra("orderId", orderId);
            messageIntent.putExtra("orderNo", orderNo);
            messageIntent.putExtra("content", content);
            context.sendBroadcast(messageIntent);
            // 刷新订单列表
            Intent refreshInquiryList = new Intent(Constant.REFRESH_ORDER_LIST);
            context.sendBroadcast(refreshInquiryList);
        } else {
            Intent in;
            NoticeManager manager = NoticeManager.getInstance(context);
            if (MainApplication.getInstance() == null) {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() != null && MainActivity.mIsActive) {
                // 刷新订单列表
                Intent refreshInquiryList = new Intent(Constant.REFRESH_ORDER_LIST);
                context.sendBroadcast(refreshInquiryList);
                // App已启动并且已登录
                in = new Intent(context, OrderDetailSalerActivity.class);
                in.putExtra("orderId", orderId);
                in.putExtra("orderNo", orderNo);
            } else if (MainApplication.getInstance().getUser() == null && MainActivity.mIsActive) {
                // App已启动并且未登录
                in = new Intent(context, LoginActivity.class);
            } else if (MainApplication.getInstance().getUser() == null && !MainActivity.mIsActive) {
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() == null) {
                in = new Intent(context, MainActivity.class);
            } else {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            }
            manager.showDefaultNotification(NOTICE_TITLE, content, in, Constant.NOTIFY_ORDER_PAY);
        }
    }

    /** 新的售后单或者售后单状态有更新 */
    private void handlerPostStatus(Context context, JSONObject resultJson, String msgCode) throws JSONException {
        String content = resultJson.getString("content");
        String returnId = resultJson.getString("returnId");
        String orderId = resultJson.getString("orderId");
        String orderNo = resultJson.getString("orderNo");
        if (Constant.PUSH_POST_SALE_NEW_STATUS.equalsIgnoreCase(msgCode) && MainApplication.getInstance() != null && MainApplication.getInstance().getUser() != null && BackUtil.isActivityRunningForground(context, PostSaleDetailSalerActivity.class.getName())) {
            // 在售后单明细页面
            Intent messageIntent = new Intent(Constant.RECEIVE_POST_SALE_ACTION);
            messageIntent.putExtra("content", content);
            messageIntent.putExtra("returnId", returnId);
            messageIntent.putExtra("orderId", orderId);
            messageIntent.putExtra("orderNo", orderNo);
            context.sendBroadcast(messageIntent);
        } else {
            Intent in;
            NoticeManager manager = NoticeManager.getInstance(context);
            if (MainApplication.getInstance() == null) {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() != null && MainActivity.mIsActive) {
                // App已启动并且已登录
                // 个人中心显示有新的消息
                Intent showPostMsg = new Intent(Constant.POST_SALE_SHOW_MSG_NOTICE_ACTION);
                context.sendBroadcast(showPostMsg);
                in = new Intent(context, PostSaleDetailSalerActivity.class);
                in.putExtra("content", content);
                in.putExtra("returnId", returnId);
                in.putExtra("orderId", orderId);
                in.putExtra("orderNo", orderNo);
            } else if (MainApplication.getInstance().getUser() == null && MainActivity.mIsActive) {
                // App已启动并且未登录
                in = new Intent(context, LoginActivity.class);
            } else if (MainApplication.getInstance().getUser() == null && !MainActivity.mIsActive) {
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() == null) {
                in = new Intent(context, MainActivity.class);
            } else {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            }
            manager.showDefaultNotification(NOTICE_TITLE, content, in, Constant.NOTIFY_POST_SALE_NEW_STATUS);
        }
    }

    /** 申请的售后状态在线聊天有新的消息 */
    private void handlerPostChat(Context context, JSONObject resultJson) throws JSONException {
        String content = resultJson.getString("content");
        String returnId = resultJson.getString("returnId");
        String orderId = resultJson.getString("orderId");
        String orderNo = resultJson.getString("orderNo");
        if (MainApplication.getInstance() != null && MainApplication.getInstance().getUser() != null && BackUtil.isActivityRunningForground(context, PostSaleDetailSalerActivity.class.getName())) {
            // 在售后单明细页面
            // 个人中心显示有新的消息
            Intent showPostMsg = new Intent(Constant.POST_SALE_SHOW_MSG_NOTICE_ACTION);
            context.sendBroadcast(showPostMsg);
            Intent intent = new Intent(Constant.RECEIVE_MESSAGE_POST_ACTION);
            intent.putExtra("content", content);
            intent.putExtra("returnId", returnId);
            intent.putExtra("orderId", orderId);
            intent.putExtra("orderNo", orderNo);
//							msgType;"1"-普通消息，"2"-语音消息
//							msgLength; 语音时长(单位秒s)大于0的整形
//							audioPath 语音地址
            try {
                String msgType = resultJson.getString("msgType");
                intent.putExtra("msgType", msgType);
                String fileUrl = resultJson.getString("fileUrl");
                intent.putExtra("fileUrl", fileUrl);
                int msgLength = resultJson.getInt("msgLength");
                intent.putExtra("msgLength", msgLength);
                String audioPath = resultJson.getString("audioPath");
                intent.putExtra("audioPath", audioPath);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            context.sendBroadcast(intent);
        } else {
            Intent in;
            NoticeManager manager = NoticeManager.getInstance(context);
            if (MainApplication.getInstance() == null) {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() != null && MainActivity.mIsActive) {
                // App已启动并且已登录
                // 个人中心显示有新的消息
                Intent showPostMsg = new Intent(Constant.POST_SALE_SHOW_MSG_NOTICE_ACTION);
                context.sendBroadcast(showPostMsg);
                in = new Intent(context, PostSaleDetailSalerActivity.class);
                in.putExtra("content", content);
                in.putExtra("returnId", returnId);
                in.putExtra("orderId", orderId);
                in.putExtra("orderNo", orderNo);
                in.putExtra("fromType", "new_msg");
            } else if (MainApplication.getInstance().getUser() == null && MainActivity.mIsActive) {
                // App已启动并且未登录
                in = new Intent(context, LoginActivity.class);
            } else if (MainApplication.getInstance().getUser() == null && !MainActivity.mIsActive) {
                in = new Intent(context, InstallOpenActivity.class);
            } else if (MainApplication.getInstance().getUser() == null) {
                in = new Intent(context, MainActivity.class);
            } else {
                // 应用没有启动
                in = new Intent(context, InstallOpenActivity.class);
            }
            manager.showDefaultNotification(NOTICE_TITLE, content, in, Constant.NOTIFY_POST_SALE_NEW_MESSAGE);
        }
    }
}
