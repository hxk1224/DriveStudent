package com.drive.student.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.drive.student.R;

/** 确认提示框. */
public class ConfirmDialog extends Dialog {
    /** Context上下文 **/
    private Context context = null;
    private static ConfirmDialog confirmDialog = null;

    private ConfirmDialog(Context context) {
        super(context);
        this.context = context;
    }

    private ConfirmDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public static ConfirmDialog createDialog(Context context) {
        confirmDialog = new ConfirmDialog(context, R.style.LodingDialog);
        confirmDialog.setContentView(R.layout.dialog_confirm);
        confirmDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return confirmDialog;
    }

    /**
     * 设置标题.
     */
    public ConfirmDialog setDialogTitle(String strTitle) {
        TextView title = (TextView) confirmDialog.findViewById(R.id.title);
        if (title != null) {
            title.setText(strTitle);
        }
        return confirmDialog;
    }

    /**
     * 设置标题.
     */
    public ConfirmDialog setDialogTitle(Integer titleId) {
        TextView title = (TextView) confirmDialog.findViewById(R.id.title);
        if (title != null) {
            title.setText(context.getString(titleId));
        }
        return confirmDialog;
    }

    /**
     * 设置弹出框提示内容
     */
    public ConfirmDialog setDialogMessage(String strMessage) {
        TextView message = (TextView) confirmDialog.findViewById(R.id.message);
        if (message != null) {
            message.setText(strMessage);
        }
        return confirmDialog;
    }

    /**
     * 设置弹出框提示内容
     */
    public ConfirmDialog setDialogMessage(Integer messageId) {
        TextView message = (TextView) confirmDialog.findViewById(R.id.message);
        if (message != null) {
            message.setText(context.getString(messageId));
        }
        return confirmDialog;
    }

    /**
     * 设置按钮1监听.
     *
     * @param text                   按钮文字
     * @param colorId                按钮颜色
     * @param onButton1ClickListener 按钮监听
     */
    public void setOnButton1ClickListener(String text, Integer colorId, final OnButton1ClickListener onButton1ClickListener) {
        Button button1 = (Button) confirmDialog.findViewById(R.id.button1);
        if (colorId != null) {
            button1.setTextColor(context.getResources().getColor(colorId));
        }
        confirmDialog.findViewById(R.id.bt_fx).setVisibility(View.VISIBLE);
        button1.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text)) {
            button1.setText(text);

        }
        button1.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onButton1ClickListener.onClick(view, confirmDialog);
            }

        });
    }

    /**
     * 设置按钮1监听.
     *
     * @param textId                 按钮文字
     * @param colorId                按钮颜色
     * @param onButton1ClickListener 按钮监听
     */
    public void setOnButton1ClickListener(Integer textId, Integer colorId, final OnButton1ClickListener onButton1ClickListener) {
        Button button1 = (Button) confirmDialog.findViewById(R.id.button1);
        if (colorId != null) {
            button1.setTextColor(context.getResources().getColor(colorId));
        }
        confirmDialog.findViewById(R.id.bt_fx).setVisibility(View.VISIBLE);
        button1.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(context.getString(textId))) {
            button1.setText(context.getString(textId));

        }
        button1.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onButton1ClickListener.onClick(view, confirmDialog);
            }

        });
    }

    /**
     * 设置按钮2监听.
     *
     * @param text                   按钮文字
     * @param colorId                按钮颜色
     * @param onButton2ClickListener 按钮监听
     */
    public void setOnButton2ClickListener(String text, Integer colorId, final OnButton2ClickListener onButton2ClickListener) {
        Button button2 = (Button) confirmDialog.findViewById(R.id.button2);
        if (colorId != null) {
            button2.setTextColor(context.getResources().getColor(colorId));
        }
        confirmDialog.findViewById(R.id.bt_fx).setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text)) {
            button2.setText(text);
        }
        button2.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButton2ClickListener.onClick(view, confirmDialog);
            }

        });
    }

    /**
     * 设置按钮2监听.
     *
     * @param textId                 按钮文字
     * @param colorId                按钮颜色
     * @param onButton2ClickListener 按钮监听
     */
    public void setOnButton2ClickListener(Integer textId, Integer colorId, final OnButton2ClickListener onButton2ClickListener) {
        Button button2 = (Button) confirmDialog.findViewById(R.id.button2);
        if (colorId != null) {
            button2.setTextColor(context.getResources().getColor(colorId));
        }
        confirmDialog.findViewById(R.id.bt_fx).setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(context.getString(textId))) {
            button2.setText(context.getString(textId));
        }
        button2.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButton2ClickListener.onClick(view, confirmDialog);
            }

        });
    }

    /**
     * 按钮1监听.
     */
    public interface OnButton1ClickListener {
        void onClick(View view, DialogInterface dialog);
    }

    /**
     * 按钮2监听.
     */
    public interface OnButton2ClickListener {
        void onClick(View view, DialogInterface dialog);
    }
}
