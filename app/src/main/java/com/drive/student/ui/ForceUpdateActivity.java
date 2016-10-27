package com.drive.student.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.drive.student.MainApplication;
import com.drive.student.R;
import com.drive.student.bean.VersionBean;
import com.drive.student.task.CheckVersionTask;
import com.drive.student.task.CheckVersionTask.CheckVersionListener;
import com.drive.student.util.BackUtil;

/**
 * 制升级弹出层.
 *
 * @author 韩新凯
 */
public class ForceUpdateActivity extends ActivitySupport implements OnClickListener {
    protected MainApplication mainApplication;
    private Button reLoginBtn, exitBtn;
    private String message;
    private Intent intent;
    private TextView tv_message;
    /** 校验版本任务 **/
    private CheckVersionTask checkVersionTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_time_out);
        if (!hasInternetConnected()) {
            finish();
        }
        mainApplication = MainApplication.getInstance();
        initData();
        initView();
    }

    public void initData() {
        intent = getIntent();
        message = intent.getStringExtra("message");
    }

    private void initView() {
        reLoginBtn = (Button) findViewById(R.id.relogin_btn);
        reLoginBtn.setText("退出");
        exitBtn = (Button) findViewById(R.id.exit_btn);
        exitBtn.setText("去升级");
        reLoginBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        tv_message = (TextView) findViewById(R.id.message);
        if (!TextUtils.isEmpty(message)) {
            tv_message.setText(message);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return false;
    }

    private void checkVersion() {
        if (!hasInternetConnected()) {
            return;
        }
        checkVersionTask = new CheckVersionTask(this, 1, new CheckVersionListener() {

            @Override
            public void updateNewVersion(VersionBean bean) {

                // 保证Activity没有被销毁
                if (!BackUtil.isActivityRunningForground(ForceUpdateActivity.this, VersionUpdateActivity.class.getName())) {
                    Intent in = new Intent(ForceUpdateActivity.this, VersionUpdateActivity.class);
                    in.putExtra("bean", bean);
                    startActivity(in);
                    finish();
                }
            }

            @Override
            public void toGuidOrAutoLoginHandle() {

            }
        });
        checkVersionTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (checkVersionTask != null) {
            checkVersionTask.cancel(true);
            checkVersionTask = null;
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.relogin_btn:
                mainApplication.exit();
                break;
            case R.id.exit_btn:
                checkVersion();
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
