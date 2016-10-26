package com.drive.student.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.ui.MainActivity;
import com.drive.student.util.CustomToast;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.view.LoadingDialog;
import com.drive.student.xutils.ViewUtils;
import com.drive.student.xutils.view.annotation.ViewInject;

public class LoginActivity extends ActivitySupport implements OnClickListener {
    protected static final String TAG = "LoginActivity";
    /***** 忘记密码按钮 *******/
    @ViewInject(R.id.login_tv_forget_pwd)
    private TextView login_tv_forget;
    /***** 登陆按钮 *******/
    @ViewInject(R.id.login_ib_submit)
    private TextView login_ib_submit;
    /***** 输入手机号 *******/
    @ViewInject(R.id.login_et_name)
    private EditText login_et_name;
    /***** 输入密码 *******/
    @ViewInject(R.id.login_et_pwd)
    private EditText et_pwd;
    /***** 头部信息、返回 *******/
    @ViewInject(R.id.header_tv_back)
    private TextView header_tv_back;
    /***** 头部信息、title *******/
    @ViewInject(R.id.header_tv_title)
    private TextView header_tv_title;
    /***** 头部信息、注册 *******/
    @ViewInject(R.id.header_tv_right)
    private TextView header_tv_register;

    private LoadingDialog lodingDialog = null;
    private SharePreferenceUtil spUtil;
    private boolean isTokenInvalid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        spUtil = new SharePreferenceUtil(getApplicationContext());
        ViewUtils.inject(this);
        if (getIntent() != null) {
            isTokenInvalid = getIntent().getBooleanExtra("Token_Invalid", false);
        }
        initViews();
    }

    /** 初始化布局、监听事件 */
    private void initViews() {
        if (!StringUtil.isBlank(spUtil.getUserCode())) {
            login_et_name.setText(spUtil.getUserCode());
        }
        // 对header布局中的控件进行操作
        header_tv_title.setText(R.string.app_name);
        header_tv_register.setVisibility(View.VISIBLE);
        header_tv_register.setText(R.string.login_header_register);
        login_tv_forget.setOnClickListener(this);
        login_ib_submit.setOnClickListener(this);
        header_tv_register.setOnClickListener(this);
        header_tv_back.setOnClickListener(this);
        lodingDialog = LoadingDialog.createDialog(this);
        lodingDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                close();
                break;
            case R.id.login_tv_forget_pwd:// 忘记密码
                findLoginPwd();
                break;
            case R.id.login_ib_submit:// 登录按钮
                if (!checkInputContent()) {
                    break;
                }
                if (hasInternetConnected()) {
                    String telephone = login_et_name.getText().toString();
                    String pwd = et_pwd.getText().toString();
                    submitLogin(telephone, pwd);
                }
                break;
            case R.id.header_tv_right:// 注册按钮
                goRegist();
                break;
        }
    }

    private void goRegist() {
        Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
        startActivity(intent);
    }

    private void findLoginPwd() {
        Intent intent = new Intent(LoginActivity.this, FindLoginPwdActivity.class);
        startActivity(intent);
    }

    /** 检查用户的密码和手机号是否为空. */
    private boolean checkInputContent() {
        if (TextUtils.isEmpty(login_et_name.getText().toString().trim()) || TextUtils.isEmpty(et_pwd.getText().toString().trim())) {
            CustomToast.showToast(this, R.string.login_hint_empty, Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    /**
     * 将登陆信息提交服务器.
     *
     * @param telephone 手机号
     * @param pwd       密码
     */
    private void submitLogin(final String telephone, final String pwd) {
        if (!hasInternetConnected()) {
            return;
        }
        // TODO
    }

    @Override
    public void onBackPressed() {
        close();
    }

    private void close() {
        if (isTokenInvalid) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("FRAG_WHICH", 0);
            startActivity(intent);
        }
        LoginActivity.this.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lodingDialog != null) {
            lodingDialog.cancel();
        }
    }

}
