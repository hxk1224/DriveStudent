package com.drive.student.view;

import com.drive.student.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Button;

/**
 * 拍照、从相机中获取图片的Dialog
 *
 * @author 韩新凯
 */
public class CameraDialog extends Dialog {
    private static CameraDialog cameraDialog;

    public CameraDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CameraDialog(Context context, int theme) {
        super(context, theme);
    }

    public CameraDialog(Context context) {
        super(context);
    }

    public static CameraDialog createDialog(Context context) {
        cameraDialog = new CameraDialog(context, R.style.LodingDialog);
        cameraDialog.setContentView(R.layout.dialog_camera);
        cameraDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return cameraDialog;
    }

    /***
     * 得到拍照的按钮
     *
     * @return
     * @author 韩新凯
     * @update 2014-5-9 下午3:28:48
     */
    public Button getCameraBtn() {
        return (Button) cameraDialog.findViewById(R.id.dialog_camera);
    }

    /***
     * 得到从相册的按钮
     *
     * @return
     * @author 韩新凯
     * @update 2014-5-9 下午3:29:44
     */
    public Button getPhotoBtn() {
        return (Button) cameraDialog.findViewById(R.id.dialog_photo);
    }

    /***
     * 得到取消按钮
     *
     * @return
     * @author 韩新凯
     * @update 2014-5-9 下午3:30:40
     */
    public Button getCancelBtn() {
        return (Button) cameraDialog.findViewById(R.id.dialog_cancel);
    }

    /**
     * 设置第一个按钮的文本值
     *
     * @author 韩新凯
     * @update 2014-5-13 上午10:52:07
     */
    public void setCameraText(String text) {
        getCameraBtn().setText(text);
    }

    /***
     * 设置第二个按钮的文本值
     *
     * @author 韩新凯
     * @update 2014-5-13 上午10:53:15
     */
    public void setPhotoText(String text) {
        getPhotoBtn().setText(text);
    }

    /**
     * 设置操作按钮的文本值
     *
     * @param text
     * @author 韩新凯
     * @update 2014-5-13 上午10:54:58
     */
    public void setCancelText(String text) {
        getCancelBtn().setText(text);
    }
}
