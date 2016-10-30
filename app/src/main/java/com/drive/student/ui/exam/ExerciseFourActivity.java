package com.drive.student.ui.exam;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.bean.SubjectFourBean;
import com.drive.student.config.Constant;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.xutils.DbUtils;
import com.drive.student.xutils.exception.DbException;

import java.util.List;

public class ExerciseFourActivity extends ActivitySupport implements View.OnClickListener {

    private static final String TRAIN_TYPE_SEQUENCE = "sequence_type";
    private static final String TRAIN_TYPE_SIMULATE = "simulate_type";
    private static final String TRAIN_TYPE_RANDOM = "random_type";
    private static final String TRAIN_TYPE_SPECIAL = "special_type";
    private static final String TRAIN_TYPE_WRONG = "wrong_type";
    private static final String TRAIN_TYPE_COLLECTION = "collection_type";

    private TextView header_tv_right;
    private String mTrainTitle;
    private String mTraiType;
    private DbUtils mDbUtils;
    private List<SubjectFourBean> mList;
    private int currIndex;
    private TextView subject_type_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_detail_activity);
        mTrainTitle = getIntent().getStringExtra("trainTitle");
        mTraiType = getIntent().getStringExtra("traiType");
        mDbUtils = DbUtils.create(this, Constant.DB_NAME);
        queryData();
        initViews();
        setNumGuid();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        header_tv_title.setText(mTrainTitle);
        header_tv_right = (TextView) header.findViewById(R.id.header_tv_right);

        subject_type_tv = (TextView) findViewById(R.id.subject_type_tv);

        findViewById(R.id.previous_tv).setOnClickListener(this);
        findViewById(R.id.next_tv).setOnClickListener(this);
    }

    /** 显示当前题目位置和题目总数量 */
    private void setNumGuid() {
        if (mList != null && mList.size() > 0) {
            header_tv_right.setText((currIndex + 1) + "/" + mList.size());
        }
    }

    private void queryData() {
        try {
            switch (mTraiType) {
                case TRAIN_TYPE_SEQUENCE:
                    // 顺序练习
                    mList = mDbUtils.findAll(SubjectFourBean.class);
                    break;
                case TRAIN_TYPE_SIMULATE:
                    // 模拟练习
                    mList = mDbUtils.findAll(SubjectFourBean.class);
                    break;
                case TRAIN_TYPE_RANDOM:
                    // 随机练习
                    mList = mDbUtils.findAll(SubjectFourBean.class);
                    break;
                case TRAIN_TYPE_SPECIAL:
                    // 专项练习
                    mList = mDbUtils.findAll(SubjectFourBean.class);
                    break;
                case TRAIN_TYPE_WRONG:
                    // 错题练习
                    mList = mDbUtils.findAll(SubjectFourBean.class);
                    break;
                case TRAIN_TYPE_COLLECTION:
                    // 收藏练习
                    mList = mDbUtils.findAll(SubjectFourBean.class);
                    break;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void setItem() {
        setNumGuid();
        SubjectFourBean four = mList.get(currIndex);
        if (Constant.SUBJECT_JUDGE_TYPE.equalsIgnoreCase(four.subject_type)) {
            // 判断题:隐藏C/D选项
            subject_type_tv.setText("判断题");
        } else if (Constant.SUBJECT_CHOICE_TYPE.equalsIgnoreCase(four.subject_type)) {
            // 单选题
            subject_type_tv.setText("单选题");
        } else if (Constant.SUBJECT_MULTIPLE_CHOICE_TYPE.equalsIgnoreCase(four.subject_type)) {
            // 多选题
            subject_type_tv.setText("多选题");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_tv:
                // 上一题
                if (currIndex != 0) {
                    currIndex = currIndex - 1;
                    setItem();
                } else {
                    showToastInThread("没有上一题了!");
                }
                break;
            case R.id.next_tv:
                // 下一题
                if (currIndex != mList.size() - 1) {
                    currIndex = currIndex + 1;
                    setItem();
                } else {
                    showToastInThread("没有下一题了!");
                }
                break;
        }
    }
}
