package com.drive.student.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.drive.student.R;

import java.util.ArrayList;

public class KeyboardPayDialog {
    private FragmentActivity mActivity;
    private ArrayList<String> pwsList;
    private KeyBoardListener mListener;

    private TextView pwd_1_tv;
    private TextView pwd_2_tv;
    private TextView pwd_3_tv;
    private TextView pwd_4_tv;
    private TextView pwd_5_tv;
    private TextView pwd_6_tv;
    private String mTitle;
    private String mNoticeMsg;
    private AlertDialog keyboardDialog;
    private Handler mHandler = new Handler();

    public KeyboardPayDialog(FragmentActivity activity, String title, String msg, KeyBoardListener listener) {
        mActivity = activity;
        mListener = listener;
        pwsList = new ArrayList<>();
        keyboardDialog = new Builder(mActivity).create();
        mTitle = title;
        mNoticeMsg = msg;
    }

    public void setCancelable(boolean cancelable) {
        keyboardDialog.setCancelable(cancelable);
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        keyboardDialog.setOnCancelListener(listener);
    }

    public void show() {
        keyboardDialog.show();
        Window window = keyboardDialog.getWindow();
        window.setContentView(R.layout.key_board_pay);

		/*
         * 将对话框的大小按屏幕大小的百分比设置
		 */
        WindowManager m = mActivity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
//		p.height = (ScaleCalculator.getInstance(this).scaleHeight((int) getResources().getDimension(R.dimen.dimen_200dp))); // 高度设置
        p.height = d.getHeight(); // 高度设置
        p.width = d.getWidth(); // 宽度设置
//		p.gravity = Gravity.BOTTOM;
        window.setAttributes(p);

        TextView title_tv = (TextView) window.findViewById(R.id.title_tv);
        TextView msg_tv = (TextView) window.findViewById(R.id.msg_tv);
        if (!StringUtil.isBlank(mTitle)) {
            title_tv.setText(mTitle);
        }
        if (!StringUtil.isBlank(mNoticeMsg)) {
            msg_tv.setText(mNoticeMsg);
        }

        pwd_1_tv = (TextView) window.findViewById(R.id.pwd_1_tv);
        pwd_2_tv = (TextView) window.findViewById(R.id.pwd_2_tv);
        pwd_3_tv = (TextView) window.findViewById(R.id.pwd_3_tv);
        pwd_4_tv = (TextView) window.findViewById(R.id.pwd_4_tv);
        pwd_5_tv = (TextView) window.findViewById(R.id.pwd_5_tv);
        pwd_6_tv = (TextView) window.findViewById(R.id.pwd_6_tv);

        pwd_6_tv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mHandler.removeCallbacksAndMessages(null);
                if (!StringUtil.isBlank(s.toString().trim())) {
                    mHandler.postDelayed(callBackR, 0);
                }
            }
        });

        TextView _0_tv = (TextView) window.findViewById(R.id._0_tv);
        TextView _1_tv = (TextView) window.findViewById(R.id._1_tv);
        TextView _2_tv = (TextView) window.findViewById(R.id._2_tv);
        TextView _3_tv = (TextView) window.findViewById(R.id._3_tv);
        TextView _4_tv = (TextView) window.findViewById(R.id._4_tv);
        TextView _5_tv = (TextView) window.findViewById(R.id._5_tv);
        TextView _6_tv = (TextView) window.findViewById(R.id._6_tv);
        TextView _7_tv = (TextView) window.findViewById(R.id._7_tv);
        TextView _8_tv = (TextView) window.findViewById(R.id._8_tv);
        TextView _9_tv = (TextView) window.findViewById(R.id._9_tv);

        TextView delete_tv = (TextView) window.findViewById(R.id.delete_tv);
        delete_tv.setOnClickListener(clickListener);
        TextView back_tv = (TextView) window.findViewById(R.id.back_tv);
        back_tv.setOnClickListener(clickListener);

        _0_tv.setOnClickListener(clickListener);
        _1_tv.setOnClickListener(clickListener);
        _2_tv.setOnClickListener(clickListener);
        _3_tv.setOnClickListener(clickListener);
        _4_tv.setOnClickListener(clickListener);
        _5_tv.setOnClickListener(clickListener);
        _6_tv.setOnClickListener(clickListener);
        _7_tv.setOnClickListener(clickListener);
        _8_tv.setOnClickListener(clickListener);
        _9_tv.setOnClickListener(clickListener);
    }

    private Runnable callBackR = new Runnable() {

        @Override
        public void run() {
            mListener.completeInput(getPwd());
        }
    };

    OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String key = "";
            switch (v.getId()) {
                case R.id._0_tv:
                    key = "0";
                    break;
                case R.id._1_tv:
                    key = "1";
                    break;
                case R.id._2_tv:
                    key = "2";
                    break;
                case R.id._3_tv:
                    key = "3";
                    break;
                case R.id._4_tv:
                    key = "4";
                    break;
                case R.id._5_tv:
                    key = "5";
                    break;
                case R.id._6_tv:
                    key = "6";
                    break;
                case R.id._7_tv:
                    key = "7";
                    break;
                case R.id._8_tv:
                    key = "8";
                    break;
                case R.id._9_tv:
                    key = "9";
                    break;
                case R.id.back_tv:
                    keyboardDialog.cancel();
                    return;
                case R.id.delete_tv:
                    if (pwsList.size() > 0) {
                        pwsList.remove(pwsList.size() - 1);
                    }
                    break;
            }
            if (!StringUtil.isBlank(key) && pwsList.size() < 6) {
                pwsList.add(key);
            }
            refreshPwd();
        }
    };

    public void clearPwd() {
        if (pwsList.size() > 0) {
            pwsList.clear();
            refreshPwd();
        }
    }

    private void refreshPwd() {
        pwd_1_tv.setText("");
        pwd_2_tv.setText("");
        pwd_3_tv.setText("");
        pwd_4_tv.setText("");
        pwd_5_tv.setText("");
        pwd_6_tv.setText("");
        if (pwsList.size() > 0) {
            for (int i = 0; i < pwsList.size(); i++) {
                switch (i) {
                    case 0:
                        pwd_1_tv.setText(pwsList.get(i));
                        break;
                    case 1:
                        pwd_2_tv.setText(pwsList.get(i));
                        break;
                    case 2:
                        pwd_3_tv.setText(pwsList.get(i));
                        break;
                    case 3:
                        pwd_4_tv.setText(pwsList.get(i));
                        break;
                    case 4:
                        pwd_5_tv.setText(pwsList.get(i));
                        break;
                    case 5:
                        pwd_6_tv.setText(pwsList.get(i));
                        break;
                }
            }
        }
    }

    private String getPwd() {
        String pwd = "";
        if (pwsList != null && pwsList.size() == 6) {
            for (String s : pwsList) {
                pwd += s;
            }
        }
        return pwd;
    }

    public void dismiss() {
        keyboardDialog.dismiss();
    }

}
