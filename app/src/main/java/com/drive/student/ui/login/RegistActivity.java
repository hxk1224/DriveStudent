package com.drive.student.ui.login;

import android.content.Intent;
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
import com.drive.student.ui.MainActivity;
import com.drive.student.util.CheckLegalUtil;
import com.drive.student.util.CustomToast;
import com.drive.student.util.StringUtil;
import com.drive.student.util.ValidateUtil;
import com.drive.student.view.LoadingDialog;
import com.drive.student.xutils.ViewUtils;
import com.drive.student.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

public class RegistActivity extends ActivitySupport implements OnClickListener {
    /***** 手机号文本框 ****/
    @ViewInject(R.id.register_et_telephone)
    private EditText register_et_telephone;
    /***** 验证码文本框 ****/
    @ViewInject(R.id.register_et_auth_code)
    private EditText register_et_auth_code;
    /***** 验证码获取按钮 ****/
    @ViewInject(R.id.register_to_get_code)
    private Button register_button_to_get_code;
    /***** 密码 ****/
    @ViewInject(R.id.register_et_pwd)
    private EditText register_et_pwd;
    /***** 再次输入密码 ****/
    @ViewInject(R.id.register_et_reinput_pwd)
    private EditText register_et_reinput_pwd;
    /***** 提交按钮 ****/
    @ViewInject(R.id.register_ib_submit)
    private TextView register_ib_submit;
    /***** 头部信息、返回 *******/
    @ViewInject(R.id.header_tv_back)
    private TextView header_tv_back;
    /***** 头部信息、right *******/
    @ViewInject(R.id.header_tv_right)
    private TextView header_tv_right;
    /***** 头部信息、title *******/
    @ViewInject(R.id.header_tv_title)
    private TextView header_tv_title;
    private long starttime;
    private Timer timer;
    private MyTimerTask myTimerTask = null;
    private LoadingDialog lodingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.user_regist);
        lodingDialog = LoadingDialog.createDialog(this);
        ViewUtils.inject(this);
        initView();
    }

    /**
     * 初始化控件、设置监听事件
     */
    private void initView() {
        header_tv_back.setVisibility(View.VISIBLE);
        header_tv_title.setText(R.string.register_title);
        register_et_telephone.setHint("请输入手机号");

        register_button_to_get_code.setOnClickListener(this);
        register_ib_submit.setOnClickListener(this);
        header_tv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phone;
        switch (v.getId()) {
            case R.id.register_to_get_code:
                phone = register_et_telephone.getText().toString().trim();
                if (!CheckLegalUtil.checkTelephone(phone)) {
                    CustomToast.showToast(this, R.string.hint_input_true_telephone, Toast.LENGTH_SHORT);
                    break;
                }
                // 请求服务器获取验证码
                if (hasInternetConnected()) {
                    // 倒计时开始
                    starttime = System.currentTimeMillis() / 1000;
                    timer = new Timer();
                    myTimerTask = new MyTimerTask();
                    timer.schedule(myTimerTask, 0, 1000);
                    getVerifyCode(phone);
                }
                break;
            case R.id.register_ib_submit:
                phone = register_et_telephone.getText().toString().trim();
                String captchas = register_et_auth_code.getText().toString().trim();// 验证码
                String pwd = register_et_pwd.getText().toString().trim();// 密码
                if (checkEmpty(phone, captchas, pwd)) {
                    break;
                }
                if (!checkPwdValide()) {
                    break;
                }
                if (captchas.length() != 6) {
                    CustomToast.showToast(this, R.string.register_hint_verify_ilegal, Toast.LENGTH_SHORT);
                    break;
                }
                if (hasInternetConnected()) {
                    submitRegister(phone, captchas, pwd);
                }
                break;
            case R.id.header_tv_back:
                finish();
                break;
        }
    }

    /** 校验密码 */
    private boolean checkPwdValide() {
        String pwd = register_et_pwd.getText().toString().trim();// 密码
        String repwd = register_et_reinput_pwd.getText().toString().trim();// 密码
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
     * 检查手机号，验证码，密码是否为空.
     *
     * @param telephone  手机号
     * @param verifyCode 验证码
     * @param pwd        密码
     * @return true 有空数据
     */
    private boolean checkEmpty(String telephone, String verifyCode, String pwd) {
        if (TextUtils.isEmpty(telephone)) {
            CustomToast.showToast(this, R.string.register_hint_empty_telephone, Toast.LENGTH_SHORT);
            return true;
        } else if (TextUtils.isEmpty(verifyCode)) {
            CustomToast.showToast(this, R.string.register_hint_empty_verity, Toast.LENGTH_SHORT);
            return true;
        } else if (TextUtils.isEmpty(pwd)) {
            CustomToast.showToast(this, R.string.register_hint_empty_pwd, Toast.LENGTH_SHORT);
            return true;
        }
        return false;
    }

    /**
     * 提交注册信息.
     *
     * @param phone    手机号
     * @param captchas 验证码
     * @param pwd      密码
     */
    private void submitRegister(final String phone, String captchas, final String pwd) {
        showToastInThread("注册成功!");
        Intent intent = new Intent(RegistActivity.this, MainActivity.class);
        startActivity(intent);
        RegistActivity.this.finish();
    }

    /**
     * 获取验证码.
     *
     * @param telephone 手机号
     */
    private void getVerifyCode(String telephone) {
        // TODO
        cancleTimer();
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
                        register_button_to_get_code.setTextColor(getResources().getColor(R.color.text_gray));
                        register_button_to_get_code.setText(getResources().getString(R.string.register_again_authcode_before) + havetime + getResources().getString(R.string.register_again_authcode_after));
                        register_button_to_get_code.setClickable(false);
                    } else {
                        cancleTimer();
                    }
                }

            });
        }
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
        register_button_to_get_code.setTextColor(getResources().getColor(android.R.color.black));
        register_button_to_get_code.setClickable(true);
        register_button_to_get_code.setText(R.string.register_get_authcode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {// 及时关闭
            timer.cancel();
        }
        if (myTimerTask != null) {
            myTimerTask.cancel();
        }
        if (lodingDialog != null) {
            lodingDialog.cancel();
        }
    }
}
