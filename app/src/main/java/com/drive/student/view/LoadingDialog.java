package com.drive.student.view;

import com.drive.student.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 请在此处简要描述此类所实现的功能。因为这项注释主要是为了在IDE环境中生成tip帮助，务必简明扼要
 * <p/>
 * 请在此处详细描述类的功能、调用方法、注意事项、以及与其它类的关系.
 **/
public class LoadingDialog extends Dialog {

    private static LoadingDialog customProgressDialog = null;

    private LoadingDialog(Context context) {
        super(context);
    }

    private LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public static LoadingDialog createDialog(Context context) {
        customProgressDialog = new LoadingDialog(context, R.style.LodingDialog);
        customProgressDialog.setContentView(R.layout.dialog_loding);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return customProgressDialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (customProgressDialog == null) {
            return;
        }
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    /**
     * setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    public LoadingDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return customProgressDialog;
    }

    @Override
    public void show() {
        super.show();
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

}
