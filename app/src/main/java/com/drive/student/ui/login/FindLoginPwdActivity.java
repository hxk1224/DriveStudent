package com.drive.student.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.CustomToast;
import com.drive.student.util.StringUtil;
import com.drive.student.util.ValidateUtil;
import com.drive.student.view.LoadingDialog;
import com.drive.student.xutils.ViewUtils;
import com.drive.student.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

/***
 * 找回密码页面.
 */
public class FindLoginPwdActivity extends ActivitySupport implements OnClickListener {
    /***** 头部信息、返回 *******/
    @ViewInject(R.id.header_tv_back)
    private TextView header_tv_back;
    /***** 头部信息、title *******/
    @ViewInject(R.id.header_tv_title)
    private TextView header_tv_title;
    /**** 手机号 *****/
    private EditText phone_et;
    /**** 验证码 *****/
    private EditText et_auth_code = null;
    /**** 密码 *****/
    private EditText et_pwd = null;
    /***** 再次输入密码 ****/
    @ViewInject(R.id.reinput_new_pwd_et)
    private EditText reinput_new_pwd_et;
    /**** 点击第二步，获取验证码按钮 *****/
    private Button btn_to_get_authcode = null;
    /**** 点击第二步，提交信息 *****/
    private long starttime;
    private Timer timer = null;
    private MyTimerTask myTimerTask = null;
    private LoadingDialog lodingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.find_login_pwd_activity);
        ViewUtils.inject(this);
        initView();
        lodingDialog = LoadingDialog.createDialog(this);
    }

    private void initView() {
        header_tv_back.setVisibility(View.VISIBLE);
        header_tv_title.setText("找回登录密码");
        header_tv_back.setOnClickListener(this);

        phone_et = (EditText) findViewById(R.id.phone_et);
        et_auth_code = (EditText) findViewById(R.id.auth_code_et);
        btn_to_get_authcode = (Button) findViewById(R.id.get_code_bt);
        btn_to_get_authcode.setOnClickListener(this);
        et_pwd = (EditText) findViewById(R.id.new_pwd_et);
        TextView ib_submit = (TextView) findViewById(R.id.submit_tv);
        ib_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phone = phone_et.getText().toString();
        switch (v.getId()) {
            case R.id.get_code_bt:
                if (TextUtils.isEmpty(phone)) {
                    showToastInThread("请输入手机号!");
                    break;
                }
                if (hasInternetConnected()) {
                    getVerifyCode(phone);
                    // 倒计时开始
                    starttime = System.currentTimeMillis() / 1000;
                    timer = new Timer();
                    myTimerTask = new MyTimerTask();
                    timer.schedule(myTimerTask, 0, 1000);
                }
                break;
            case R.id.submit_tv:
                String verifyCode = et_auth_code.getText().toString().trim();// 验证码
                String pwd = et_pwd.getText().toString();// 密码
                if (checkEmpty(phone, verifyCode, pwd)) {
                    break;
                }
                if (!checkPwdValide()) {
                    break;
                }
                if (verifyCode.length() != 6) {
                    CustomToast.showToast(this, R.string.register_hint_verify_ilegal, Toast.LENGTH_SHORT);
                    break;
                }
                if (hasInternetConnected()) {
                    submitNewPwd(phone, verifyCode, pwd);
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
        String pwd = et_pwd.getText().toString().trim();// 密码
        String repwd = reinput_new_pwd_et.getText().toString().trim();// 密码
        if (StringUtil.isBlank(pwd)) {
            CustomToast.showToast(this, "请输入密码", 0);
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

    /**
     * 提交找回密码信息.
     *
     * @param userName    用户名
     * @param verifyCode  验证码
     * @param newPassword 密码
     */
    private void submitNewPwd(final String userName, String verifyCode, String newPassword) {
        //TODO
    }

    /**
     * 检查手机号，验证码，密码是否为空.
     *
     * @param phone      手机号
     * @param verifyCode 验证码
     * @param pwd        密码
     * @return true 有空数据
     */
    private boolean checkEmpty(String phone, String verifyCode, String pwd) {
        if (StringUtil.equalsNull(phone)) {
            showToastInThread("请输入手机号!");
            return true;
        } else if (StringUtil.equalsNull(verifyCode)) {
            showToastInThread(R.string.register_hint_empty_verity);
            return true;
        } else if (StringUtil.equalsNull(pwd)) {
            showToastInThread(R.string.register_hint_empty_pwd);
            return true;
        }
        return false;
    }

    /**
     * 获取验证码.
     *
     * @param userName 用户名
     */
    private void getVerifyCode(String userName) {
        // TODO
        cancleTimer();
    }

    private void cancleTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (myTimerTask != null) {
            myTimerTask.cancel();
        }
        timer = null;
        myTimerTask = null;
        btn_to_get_authcode.setTextColor(getResources().getColor(android.R.color.black));
        btn_to_get_authcode.setClickable(true);
        btn_to_get_authcode.setText(R.string.register_get_authcode);
    }

    // 定时器任务
    class MyTimerTask extends TimerTask {
        // 每隔一秒刷新按钮
        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    long havetime = starttime + 60 - System.currentTimeMillis() / 1000;
                    if (havetime > 0) {
                        btn_to_get_authcode.setTextColor(getResources().getColor(R.color.text_gray));
                        btn_to_get_authcode.setText(getResources().getString(R.string.register_again_authcode_before) + havetime + getResources().getString(R.string.register_again_authcode_after));
                        btn_to_get_authcode.setClickable(false);
                    } else {
                        if (timer != null) {
                            timer.cancel();
                        }
                        if (myTimerTask != null) {
                            myTimerTask.cancel();
                        }
                        timer = null;
                        myTimerTask = null;
                        btn_to_get_authcode.setTextColor(getResources().getColor(android.R.color.black));
                        btn_to_get_authcode.setClickable(true);
                        btn_to_get_authcode.setText(R.string.register_get_authcode);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {// 及时关闭
            timer.cancel();
            timer = null;
        }
        if (myTimerTask != null) {
            myTimerTask.cancel();
            myTimerTask = null;
        }
        if (lodingDialog != null) {
            lodingDialog.cancel();
            lodingDialog = null;
        }
    }
}
