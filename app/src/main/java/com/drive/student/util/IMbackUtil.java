package com.drive.student.util;

import com.alibaba.fastjson.JSON;
import com.drive.student.config.Constant;
import com.drive.student.config.UrlConfig;
import com.drive.student.dto.ChatDTO;
import com.drive.student.http.HttpTransferCallBack;
import com.drive.student.http.HttpTransferUtil;

public class IMbackUtil {
    public static final String SYS_TYPE = "sys_msg_type";
    public static final String NORMAL_TYPE = "normal_msg_type";
    public static final String INQUIRY_BACK_MSG = "inquiry_back_msg";
    public static final String ORDER_BACK_MSG = "order_back_msg";
    public static final String POST_SALE_BACK_MSG = "post_sale_back_msg";

    /**
     * 给后台发送回执消息
     *
     * @param supplierId 账号id UserBean.userMbrOrgId.
     * @param id         询价单-inquiryId，订单-orderId，售后单-returnId
     * @param backType   回执消息位置，询价单-INQUIRY_BACK_MSG，订单-ORDER_BACK_MSG，售后单-POST_SALE_BACK_MSG
     * @param msgType    回执消息类型，普通消息-NORMAL_TYPE，系统消息-SYS_TYPE
     */
    public static void sendMessageBack(String supplierId, String id, String backType, String msgType) {
        ChatDTO dto = new ChatDTO();
        if (NORMAL_TYPE.equals(msgType)) {
            dto.msgCode = Constant.PUSH_MSG_BACK;
        } else if (SYS_TYPE.equals(msgType)) {
            dto.msgCode = Constant.PUSH_SYS_MSG_BACK;
        } else {
            return;
        }
        if (INQUIRY_BACK_MSG.equals(backType)) {
            dto.inquiryId = id;
        } else if (POST_SALE_BACK_MSG.equals(backType)) {
            dto.returnId = id;
        } else if (ORDER_BACK_MSG.equals(backType)) {
            dto.orderId = id;
        } else {
            return;
        }
        dto.supplierId = supplierId;
        String content = JSON.toJSONString(dto);
        new HttpTransferUtil().sendHttpPostUotEncrypt(UrlConfig.ZASION_SEND_MESSAGE_TO_REPAIR, content, new HttpTransferCallBack() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String json) {// "successFlag" : "1" 成功

            }

            @Override
            public void onFailure() {
            }

        });
    }
}
