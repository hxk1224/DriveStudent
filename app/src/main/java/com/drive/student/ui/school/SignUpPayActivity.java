package com.drive.student.ui.school;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;

public class SignUpPayActivity extends ActivitySupport implements View.OnClickListener {

    /** 报名费 */
    private TextView sing_up_fee_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_pay_activity);
        initViews();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        header_tv_title.setText("支付报名费");
        header.findViewById(R.id.header_tv_back).setOnClickListener(this);

        sing_up_fee_tv = (TextView) findViewById(R.id.sing_up_fee_tv);

        findViewById(R.id.ali_pay_bt).setOnClickListener(this);
        findViewById(R.id.wx_pay_bt).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                SignUpPayActivity.this.finish();
                break;
            case R.id.ali_pay_bt:
                // TODO 支付宝支付

                break;
            case R.id.wx_pay_bt:
                // TODO 微信支付

                break;
        }
    }

    private void confirmSignUp() {
        Intent intent = new Intent(this, SignUpPayActivity.class);
        startActivity(intent);
    }
}
