package com.drive.student.ui.teacher;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;

public class TeacherDetailActivity extends ActivitySupport implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_detail_activity);
        initViews();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        header_tv_title.setText("教练信息");
        header.findViewById(R.id.header_tv_back).setOnClickListener(this);

        findViewById(R.id.call_iv).setOnClickListener(this);
        findViewById(R.id.subject_two_bt).setOnClickListener(this);
        findViewById(R.id.subject_three_bt).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                TeacherDetailActivity.this.finish();
                break;
            case R.id.call_iv:
                callPhone("");
                break;
            case R.id.subject_two_bt:
                // TODO 预约科目二
                break;
            case R.id.subject_three_bt:
                // TODO 预约科目三
                break;
        }
    }
}
