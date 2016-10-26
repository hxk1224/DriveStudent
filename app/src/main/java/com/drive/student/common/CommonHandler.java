package com.drive.student.common;

import android.os.Handler;
import android.os.Message;

import com.drive.student.callback.CommonHandlerCallback;

import java.lang.ref.WeakReference;

/**通用的Handler, 不需要再去new Handler()*/
public class CommonHandler extends Handler {
    private WeakReference<CommonHandlerCallback> wref;

    public CommonHandler(CommonHandlerCallback callback) {
        wref = new WeakReference<>(callback);
    }

    @Override
    public void handleMessage(Message msg) {
        wref.get().commonHandleMessage(msg);
    }
}
