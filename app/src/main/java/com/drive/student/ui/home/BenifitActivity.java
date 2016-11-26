package com.drive.student.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;

public class BenifitActivity extends ActivitySupport {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.benifit_activity);

        initViews();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        header_tv_title.setText("优惠活动");
        header.findViewById(R.id.header_tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BenifitActivity.this.finish();
            }
        });
    }
}
