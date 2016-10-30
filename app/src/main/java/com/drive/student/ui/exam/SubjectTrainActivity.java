package com.drive.student.ui.exam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.callback.CommonHandlerCallback;
import com.drive.student.common.CommonHandler;
import com.drive.student.config.Constant;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.StringUtil;
import com.drive.student.view.LoadingDialog;

public class SubjectTrainActivity extends ActivitySupport implements View.OnClickListener, CommonHandlerCallback {
    private static final int REFRESH_DB_SUBJECT = 0x1;
    private static final int REFRESH_DB_SUBJECT_DELAY = 500;

    private static final String TRAIN_TYPE_SEQUENCE = "sequence_type";
    private static final String TRAIN_TYPE_SIMULATE = "simulate_type";
    private static final String TRAIN_TYPE_RANDOM = "random_type";
    private static final String TRAIN_TYPE_SPECIAL = "special_type";
    private static final String TRAIN_TYPE_WRONG = "wrong_type";
    private static final String TRAIN_TYPE_COLLECTION = "collection_type";

    /** 科目一: Constant.SUBJECT_ONE_TRAIN 科目四: Constant.SUBJECT_FOUR_TRAIN */
    private String subjectType = "";
    private LoadingDialog loadingDialog;
    private CommonHandler mHandler = new CommonHandler(this);
    private String mTraiType;
    private String mTrainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_train_activity);
        loadingDialog = getProgressDialog();
        subjectType = getIntent().getStringExtra("subjectType");
        initViews();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        if (Constant.SUBJECT_ONE_TRAIN.equalsIgnoreCase(subjectType)) {
            header_tv_title.setText("科目一");
        } else if (Constant.SUBJECT_FOUR_TRAIN.equalsIgnoreCase(subjectType)) {
            header_tv_title.setText("科目四");
        }
        header.findViewById(R.id.header_tv_back).setOnClickListener(this);
        findViewById(R.id.sequence_tv).setOnClickListener(this);
        findViewById(R.id.simulate_tv).setOnClickListener(this);
        findViewById(R.id.random_tv).setOnClickListener(this);
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
                // 顺序练习
                mTraiType = TRAIN_TYPE_SEQUENCE;
                mTrainTitle = "顺序练习";
                openExerciseDetail();
                break;
            case R.id.simulate_tv:
                // 模拟练习
                mTraiType = TRAIN_TYPE_SIMULATE;
                mTrainTitle = "模拟练习";
                openExerciseDetail();
                break;
            case R.id.random_tv:
                // 随机练习
                mTraiType = TRAIN_TYPE_RANDOM;
                mTrainTitle = "随机练习";
                openExerciseDetail();
                break;
            case R.id.special_tv:
                // 专项练习
                mTraiType = TRAIN_TYPE_SPECIAL;
                mTrainTitle = "专项练习";
                openExerciseDetail();
                break;
            case R.id.wrong_tv:
                // 错题练习
                mTraiType = TRAIN_TYPE_WRONG;
                mTrainTitle = "错题练习";
                openExerciseDetail();
                break;
            case R.id.collection_tv:
                // 收藏练习
                mTraiType = TRAIN_TYPE_COLLECTION;
                mTrainTitle = "收藏练习";
                openExerciseDetail();
                break;
        }
    }

    @Override
    public void commonHandleMessage(Message msg) {
        switch (msg.what) {
            case REFRESH_DB_SUBJECT:
                if (Constant.SUBJECT_ONE_TRAIN.equalsIgnoreCase(subjectType)) {
                    if (spUtil.isSubjectOneStored()) {
                        loadingDialog.dismiss();
                        if (!StringUtil.equalsNull(mTraiType)) {
                            openExerciseDetail();
                        }
                    } else {
                        mHandler.sendEmptyMessageDelayed(REFRESH_DB_SUBJECT, REFRESH_DB_SUBJECT_DELAY);
                    }
                } else if (Constant.SUBJECT_FOUR_TRAIN.equalsIgnoreCase(subjectType)) {
                    if (spUtil.isSubjectFourStored()) {
                        loadingDialog.dismiss();
                        if (!StringUtil.equalsNull(mTraiType)) {
                            openExerciseDetail();
                        }
                    } else {
                        mHandler.sendEmptyMessageDelayed(REFRESH_DB_SUBJECT, REFRESH_DB_SUBJECT_DELAY);
                    }
                }
                break;
        }
    }

    private void openExerciseDetail() {
        if (Constant.SUBJECT_ONE_TRAIN.equalsIgnoreCase(subjectType)) {
            if (spUtil.isSubjectOneStored()) {
                Intent intent = new Intent(this, ExerciseOneActivity.class);
                intent.putExtra("trainTitle", mTrainTitle);
                intent.putExtra("traiType", mTraiType);
                startActivity(intent);
            } else {
                loadingDialog.show();
                mHandler.sendEmptyMessageDelayed(REFRESH_DB_SUBJECT, REFRESH_DB_SUBJECT_DELAY);
            }
        } else if (Constant.SUBJECT_FOUR_TRAIN.equalsIgnoreCase(subjectType)) {
            if (spUtil.isSubjectFourStored()) {
                Intent intent = new Intent(this, ExerciseFourActivity.class);
                intent.putExtra("trainTitle", mTrainTitle);
                intent.putExtra("traiType", mTraiType);
                startActivity(intent);
            } else {
                loadingDialog.show();
                mHandler.sendEmptyMessageDelayed(REFRESH_DB_SUBJECT, REFRESH_DB_SUBJECT_DELAY);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
