package com.drive.student.ui.exam;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.config.Constant;

public class SubjectTrainActivity extends Activity implements View.OnClickListener {

    private String subjectType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_train_activity);
        subjectType = getIntent().getStringExtra("subjectType");
        initViews();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        if (Constant.SUBJECT_ONE_TRAIN.equalsIgnoreCase(subjectType)) {
            header_tv_title.setText("科目一");
        } else if (Constant.SUBJECT_THREE_TRAIN.equalsIgnoreCase(subjectType)) {
            header_tv_title.setText("科目三");
        }
        header.findViewById(R.id.header_tv_back).setOnClickListener(this);
        findViewById(R.id.sequence_tv).setOnClickListener(this);
        findViewById(R.id.sequence_tv).setOnClickListener(this);
        findViewById(R.id.sequence_tv).setOnClickListener(this);
        findViewById(R.id.sequence_tv).setOnClickListener(this);
        findViewById(R.id.sequence_tv).setOnClickListener(this);
        findViewById(R.id.sequence_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                SubjectTrainActivity.this.finish();
                break;
            case R.id.sequence_tv:
                // TODO 顺序练习
                break;
            case R.id.simulate_tv:
                // TODO 模拟练习
                break;
            case R.id.random_tv:
                // TODO 随机练习
                break;
            case R.id.special_tv:
                // TODO 专项练习
                break;
            case R.id.wrong_tv:
                // TODO 错题练习
                break;
            case R.id.collection_tv:
                // TODO 收藏练习
                break;
        }
    }
}
