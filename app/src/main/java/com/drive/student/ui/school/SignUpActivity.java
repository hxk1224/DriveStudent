package com.drive.student.ui.school;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;

public class SignUpActivity extends ActivitySupport implements View.OnClickListener {

    private TextView school_name_tv;
    private TextView school_area_tv;
    private EditText real_name_et;
    private EditText id_et;
    private EditText phone_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        initViews();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        header_tv_title.setText("报名详情");
        header.findViewById(R.id.header_tv_back).setOnClickListener(this);

        school_name_tv = (TextView) findViewById(R.id.school_name_tv);
        school_area_tv = (TextView) findViewById(R.id.school_area_tv);

        real_name_et = (EditText) findViewById(R.id.real_name_et);
        id_et = (EditText) findViewById(R.id.id_et);
        phone_et = (EditText) findViewById(R.id.phone_et);

        findViewById(R.id.confirm_bt).setOnClickListener(this);
        findViewById(R.id.call_bt).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                SignUpActivity.this.finish();
                break;
            case R.id.confirm_bt:
                // 确认报名
                confirmSignUp();
                break;
            case R.id.call_bt:
                // 电话咨询
                callPhone("");
                break;
        }
    }

    private void confirmSignUp() {
        Intent intent = new Intent(this, SignUpPayActivity.class);
        startActivity(intent);
    }
}
