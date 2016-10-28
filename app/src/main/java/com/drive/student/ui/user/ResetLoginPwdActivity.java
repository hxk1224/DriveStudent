package com.drive.student.ui.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.CustomToast;
import com.drive.student.util.StringUtil;
import com.drive.student.util.ValidateUtil;
import com.drive.student.view.LoadingDialog;
import com.drive.student.xutils.ViewUtils;
import com.drive.student.xutils.view.annotation.ViewInject;

public class ResetLoginPwdActivity extends ActivitySupport implements OnClickListener {
    /***** 头部信息、返回 *******/
    @ViewInject(R.id.header_tv_back)
    private TextView header_tv_back;
    /***** 头部信息、title *******/
    @ViewInject(R.id.header_tv_title)
    private TextView header_tv_title;
    private LoadingDialog lodingDialog = null;
    private EditText old_pwd_et;
    private EditText new_pwd_et;
    private TextView reinput_new_pwd_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_login_pwd_activity);
        ViewUtils.inject(this);
        lodingDialog = LoadingDialog.createDialog(this);
        initView();
    }

    private void initView() {
        header_tv_back.setVisibility(View.VISIBLE);
        header_tv_title.setText("修改登录密码");
        header_tv_back.setOnClickListener(this);

        old_pwd_et = (EditText) findViewById(R.id.old_pwd_et);
        new_pwd_et = (EditText) findViewById(R.id.new_pwd_et);
        reinput_new_pwd_et = (EditText) findViewById(R.id.reinput_new_pwd_et);
        TextView submit_tv = (TextView) findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_tv:
                String oldPwd = old_pwd_et.getText().toString();// 密码
                if (StringUtil.isBlank(oldPwd)) {
                    CustomToast.showToast(this, "请输入旧密码", 0);
                    break;
                }
                String pwd = new_pwd_et.getText().toString();// 密码
                if (!checkPwdValide()) {
                    break;
                }
                if (hasInternetConnected()) {
                    submitNewPwd(oldPwd, pwd);
                }
                break;
            case R.id.header_tv_back:
                finish();
                break;
        }
    }

    /**
     * 校验密码
     */
    private boolean checkPwdValide() {
        String pwd = new_pwd_et.getText().toString().trim();// 密码
        String repwd = reinput_new_pwd_et.getText().toString().trim();// 密码
        if (StringUtil.isBlank(pwd)) {
            CustomToast.showToast(this, "请输入新密码", 0);
            return false;
        }
        if (!ValidateUtil.isPasswordValid(pwd)) {
            CustomToast.showToast(this, R.string.pwd_valide_notice, 0);
            return false;
        }
        if (!pwd.equals(repwd)) {
            CustomToast.showToast(this, "两次输入的密码不一致,请重新输入!", 0);
            return false;
        }
        return true;
    }

    private void submitNewPwd(String oldPwd, String newPwd) {
        showToastInThread("新密码提交成功");
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lodingDialog != null) {
            lodingDialog.cancel();
            lodingDialog = null;
        }
    }
}
