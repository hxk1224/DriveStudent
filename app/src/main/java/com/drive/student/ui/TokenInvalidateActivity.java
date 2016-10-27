package com.drive.student.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.ui.login.LoginActivity;

/**
 * 会话超时.
 */
public class TokenInvalidateActivity extends ActivitySupport implements OnClickListener {
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_time_out);
        if (!hasInternetConnected()) {
            finish();
        }
        initData();
        initView();
    }

    public void initData() {
        message = getIntent().getStringExtra("message");
    }

    private void initView() {
        Button reLoginBtn = (Button) findViewById(R.id.relogin_btn);
        Button exitBtn = (Button) findViewById(R.id.exit_btn);
        reLoginBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        TextView tv_message = (TextView) findViewById(R.id.message);
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.relogin_btn:
                Intent in = new Intent(TokenInvalidateActivity.this, LoginActivity.class);
                in.putExtra("Token_Invalid", true);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                break;
            case R.id.exit_btn:
                mainApplication.exit();
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
