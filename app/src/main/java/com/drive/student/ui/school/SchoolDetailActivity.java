package com.drive.student.ui.school;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;

public class SchoolDetailActivity extends ActivitySupport implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_detail_activity);
        initViews();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        header_tv_title.setText("驾校信息");
        header.findViewById(R.id.header_tv_back).setOnClickListener(this);

        findViewById(R.id.call_kefu_layout).setOnClickListener(this);
        findViewById(R.id.sign_up_bt).setOnClickListener(this);
        findViewById(R.id.call_bt).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                SchoolDetailActivity.this.finish();
                break;
            case R.id.call_kefu_layout:
                callPhone("");
                break;
            case R.id.sign_up_bt:
                // 我要报名
                openSignUp();
                break;
            case R.id.call_bt:
                // 电话咨询
                callPhone("");
                break;
        }
    }

    private void openSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
