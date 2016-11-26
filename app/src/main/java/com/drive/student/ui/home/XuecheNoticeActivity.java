package com.drive.student.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;

public class XuecheNoticeActivity extends ActivitySupport {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xueche_notice_activity);

        initViews();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        header_tv_title.setText("报名须知");
        header.findViewById(R.id.header_tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuecheNoticeActivity.this.finish();
            }
        });
    }
}
