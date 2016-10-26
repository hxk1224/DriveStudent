package com.drive.student.callback;

import android.os.Message;

/** 配合CommonHandler一起使用, 处理接收到的消息 */
public interface CommonHandlerCallback {
    void commonHandleMessage(Message msg);
}
