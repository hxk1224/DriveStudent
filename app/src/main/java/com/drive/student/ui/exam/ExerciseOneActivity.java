package com.drive.student.ui.exam;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.bean.SubjectOneBean;
import com.drive.student.config.Constant;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.BitmapHelp;
import com.drive.student.util.StringUtil;
import com.drive.student.xutils.BitmapUtils;
import com.drive.student.xutils.DbUtils;
import com.drive.student.xutils.exception.DbException;

import java.util.List;

public class ExerciseOneActivity extends ActivitySupport implements View.OnClickListener {

    private static final String TRAIN_TYPE_SEQUENCE = "sequence_type";
    private static final String TRAIN_TYPE_SIMULATE = "simulate_type";
    private static final String TRAIN_TYPE_RANDOM = "random_type";
    private static final String TRAIN_TYPE_SPECIAL = "special_type";
    private static final String TRAIN_TYPE_WRONG = "wrong_type";
    private static final String TRAIN_TYPE_COLLECTION = "collection_type";

    private static final String ANSWER_A = "A";
    private static final String ANSWER_B = "B";
    private static final String ANSWER_C = "C";
    private static final String ANSWER_D = "D";

    private TextView header_tv_right;
    private TextView subject_type_tv;
    private TextView subject_content_tv;
    private ImageView subject_giv;
    private View answer_c_layout;
    private View answer_d_layout;
    private ImageView answer_a_check_iv;
    private ImageView answer_b_check_iv;
    private ImageView answer_c_check_iv;
    private ImageView answer_d_check_iv;
    private TextView answer_a_tv;
    private TextView answer_b_tv;
    private TextView answer_c_tv;
    private TextView answer_d_tv;
    private TextView analysis_tv;

    private BitmapUtils bmUtils;
    private DbUtils mDbUtils;

    private String mTrainTitle;
    private String mTraiType;
    private List<SubjectOneBean> mList;
    private int currIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_detail_activity);
        mTrainTitle = getIntent().getStringExtra("trainTitle");
        mTraiType = getIntent().getStringExtra("traiType");
        mDbUtils = DbUtils.create(this, Constant.DB_NAME);
        bmUtils = BitmapHelp.getPhotoBitmap(this);
        queryData();
        initViews();
        setItem();
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        header_tv_title.setText(mTrainTitle);
        header_tv_right = (TextView) header.findViewById(R.id.header_tv_right);
        header.findViewById(R.id.header_tv_back).setOnClickListener(this);

        subject_type_tv = (TextView) findViewById(R.id.subject_type_tv);
        subject_content_tv = (TextView) findViewById(R.id.subject_content_tv);
        subject_giv = (ImageView) findViewById(R.id.subject_giv);

        View answer_a_layout = findViewById(R.id.answer_a_layout);
        View answer_b_layout = findViewById(R.id.answer_b_layout);
        answer_c_layout = findViewById(R.id.answer_c_layout);
        answer_d_layout = findViewById(R.id.answer_d_layout);

        answer_a_check_iv = (ImageView) findViewById(R.id.answer_a_check_iv);
        answer_b_check_iv = (ImageView) findViewById(R.id.answer_b_check_iv);
        answer_c_check_iv = (ImageView) findViewById(R.id.answer_c_check_iv);
        answer_d_check_iv = (ImageView) findViewById(R.id.answer_d_check_iv);

        answer_a_tv = (TextView) findViewById(R.id.answer_a_tv);
        answer_b_tv = (TextView) findViewById(R.id.answer_b_tv);
        answer_c_tv = (TextView) findViewById(R.id.answer_c_tv);
        answer_d_tv = (TextView) findViewById(R.id.answer_d_tv);

        analysis_tv = (TextView) findViewById(R.id.analysis_tv);

        answer_a_layout.setOnClickListener(this);
        answer_b_layout.setOnClickListener(this);
        answer_c_layout.setOnClickListener(this);
        answer_d_layout.setOnClickListener(this);

        findViewById(R.id.previous_tv).setOnClickListener(this);
        findViewById(R.id.answer_tv).setOnClickListener(this);
        findViewById(R.id.collect_tv).setOnClickListener(this);
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
                    mList = mDbUtils.findAll(SubjectOneBean.class);
                    break;
                case TRAIN_TYPE_SIMULATE:
                    // 模拟练习
                    mList = mDbUtils.findAll(SubjectOneBean.class);
                    break;
                case TRAIN_TYPE_RANDOM:
                    // 随机练习
                    mList = mDbUtils.findAll(SubjectOneBean.class);
                    break;
                case TRAIN_TYPE_SPECIAL:
                    // 专项练习
                    mList = mDbUtils.findAll(SubjectOneBean.class);
                    break;
                case TRAIN_TYPE_WRONG:
                    // 错题练习
                    mList = mDbUtils.findAll(SubjectOneBean.class);
                    break;
                case TRAIN_TYPE_COLLECTION:
                    // 收藏练习
                    mList = mDbUtils.findAll(SubjectOneBean.class);
                    break;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void setItem() {
        clearAnswer();
        SubjectOneBean one = mList.get(currIndex);
        answer_c_layout.setVisibility(View.VISIBLE);
        answer_d_layout.setVisibility(View.VISIBLE);

        analysis_tv.setVisibility(View.GONE);

        subject_content_tv.setText(one.subject_title);

        if (Constant.SUBJECT_JUDGE_TYPE.equalsIgnoreCase(one.subject_type)) {
            // 判断题:隐藏C/D选项
            subject_type_tv.setText("判断题");
            answer_c_layout.setVisibility(View.GONE);
            answer_d_layout.setVisibility(View.GONE);

            answer_a_tv.setText("A：正确");
            answer_b_tv.setText("B：错误");
            answer_c_tv.setText("");
            answer_d_tv.setText("");
        } else if (Constant.SUBJECT_CHOICE_TYPE.equalsIgnoreCase(one.subject_type)) {
            // 单选题
            subject_type_tv.setText("单选题");
            answer_a_tv.setText("A：" + one.answer_a);
            answer_b_tv.setText("B：" + one.answer_b);
            answer_c_tv.setText("C：" + one.answer_c);
            answer_d_tv.setText("D：" + one.answer_d);
        }

        if (!StringUtil.equalsNull(one.subject_img)) {
            subject_giv.setVisibility(View.VISIBLE);
            bmUtils.display(subject_giv, one.subject_img);
        } else {
            subject_giv.setVisibility(View.GONE);
        }

        analysis_tv.setText(one.subject_analysis);

        checkAnswer();

        setNumGuid();
    }

    /** 检查答案是否正确 */
    private void checkAnswer() {
        SubjectOneBean one = mList.get(currIndex);
        if(!StringUtil.equalsNull(one.selectdAnswer)) {
            if (one.answer_true.equalsIgnoreCase(one.selectdAnswer)) {
                checkAnswer(one.selectdAnswer);
            } else {
                checkAnswer(one.selectdAnswer);
                checkAnswer(one.answer_true);
            }
            analysis_tv.setVisibility(View.VISIBLE);
        }
    }

    private void checkAnswer(String answer) {
        if (!StringUtil.equalsNull(answer)) {
            SubjectOneBean one = mList.get(currIndex);
            switch (answer) {
                case ANSWER_A:
                    if (answer.equalsIgnoreCase(one.answer_true)) {
                        answer_a_check_iv.setImageResource(R.drawable.right_green);
                    } else {
                        answer_a_check_iv.setImageResource(R.drawable.wrong_red);
                    }
                    break;
                case ANSWER_B:
                    if (answer.equalsIgnoreCase(one.answer_true)) {
                        answer_b_check_iv.setImageResource(R.drawable.right_green);
                    } else {
                        answer_b_check_iv.setImageResource(R.drawable.wrong_red);
                    }
                    break;
                case ANSWER_C:
                    if (answer.equalsIgnoreCase(one.answer_true)) {
                        answer_c_check_iv.setImageResource(R.drawable.right_green);
                    } else {
                        answer_c_check_iv.setImageResource(R.drawable.wrong_red);
                    }
                    break;
                case ANSWER_D:
                    if (answer.equalsIgnoreCase(one.answer_true)) {
                        answer_d_check_iv.setImageResource(R.drawable.right_green);
                    } else {
                        answer_d_check_iv.setImageResource(R.drawable.wrong_red);
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                ExerciseOneActivity.this.finish();
                break;
            case R.id.answer_a_layout:
                choseAnswer(ANSWER_A);
                break;
            case R.id.answer_b_layout:
                choseAnswer(ANSWER_B);
                break;
            case R.id.answer_c_layout:
                choseAnswer(ANSWER_C);
                break;
            case R.id.answer_d_layout:
                choseAnswer(ANSWER_D);
                break;
            case R.id.previous_tv:
                // 上一题
                if (currIndex != 0) {
                    currIndex = currIndex - 1;
                    setItem();
                } else {
                    showToastInThread("没有上一题了!");
                }
                break;
            case R.id.answer_tv:
                // 解析
                SubjectOneBean one = mList.get(currIndex);
                if (StringUtil.equalsNull(one.selectdAnswer)) {
                    one.selectdAnswer = one.answer_true;
                }
                setItem();
                break;
            case R.id.collect_tv:
                // 收藏

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

    private void clearAnswer() {
        answer_a_check_iv.setImageResource(R.drawable.circle_gray);
        answer_b_check_iv.setImageResource(R.drawable.circle_gray);
        answer_c_check_iv.setImageResource(R.drawable.circle_gray);
        answer_d_check_iv.setImageResource(R.drawable.circle_gray);
    }

    /** 选择答案 */
    private void choseAnswer(String answer) {
        SubjectOneBean one = mList.get(currIndex);
        one.selectdAnswer = answer;
        setItem();
    }

}