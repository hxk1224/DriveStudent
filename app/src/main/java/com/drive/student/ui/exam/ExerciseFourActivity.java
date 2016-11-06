package com.drive.student.ui.exam;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.bean.SubjectFourBean;
import com.drive.student.config.Constant;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.BitmapHelp;
import com.drive.student.util.StringUtil;
import com.drive.student.view.LoadingDialog;
import com.drive.student.xutils.BitmapUtils;
import com.drive.student.xutils.DbUtils;
import com.drive.student.xutils.HttpUtils;
import com.drive.student.xutils.exception.DbException;
import com.drive.student.xutils.exception.HttpException;
import com.drive.student.xutils.http.ResponseInfo;
import com.drive.student.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ExerciseFourActivity extends ActivitySupport implements View.OnClickListener {

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
    private GifImageView subject_giv;
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
    private List<SubjectFourBean> mList;
    private int currIndex;
    private Button mulchoice_submit_bt;
    private LoadingDialog loadingDialog;

    interface GifDownloadCallback {
        void callback(String gifPath);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_detail_activity);
        loadingDialog = getProgressDialog();
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
        subject_giv = (GifImageView) findViewById(R.id.subject_giv);

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

        mulchoice_submit_bt = (Button) findViewById(R.id.mulchoice_submit_bt);

        mulchoice_submit_bt.setOnClickListener(this);

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
        clearAnswer();
        SubjectFourBean four = mList.get(currIndex);
        answer_c_layout.setVisibility(View.VISIBLE);
        answer_d_layout.setVisibility(View.VISIBLE);

        analysis_tv.setVisibility(View.GONE);
        mulchoice_submit_bt.setVisibility(View.GONE);

        subject_content_tv.setText(four.subject_title);

        if (Constant.SUBJECT_JUDGE_TYPE.equalsIgnoreCase(four.subject_type)) {
            // 判断题:隐藏C/D选项
            subject_type_tv.setText("判断题");
            answer_c_layout.setVisibility(View.GONE);
            answer_d_layout.setVisibility(View.GONE);

            answer_a_tv.setText("A：正确");
            answer_b_tv.setText("B：错误");
            answer_c_tv.setText("");
            answer_d_tv.setText("");
        } else {
            // 单选题和多选题
            subject_type_tv.setText("单选题");
            answer_a_tv.setText("A：" + four.answer_a);
            answer_b_tv.setText("B：" + four.answer_b);
            answer_c_tv.setText("C：" + four.answer_c);
            answer_d_tv.setText("D：" + four.answer_d);
            if (Constant.SUBJECT_MULTIPLE_CHOICE_TYPE.equalsIgnoreCase(four.subject_type)) {
                subject_type_tv.setText("多选题");
                mulchoice_submit_bt.setVisibility(View.VISIBLE);
            }
        }

        if (!StringUtil.equalsNull(four.subject_img)) {
            subject_giv.setVisibility(View.VISIBLE);
            if (four.subject_img.endsWith(".gif")) {
                // GIF 动图要先下载
                downloadGif(four.subject_img, new GifDownloadCallback() {
                    @Override
                    public void callback(String gifPath) {
                        if (!StringUtil.equalsNull(gifPath)) {
                            subject_giv.setImageURI(Uri.fromFile(new File(gifPath)));
                        }
                    }
                });
            } else {
                bmUtils.display(subject_giv, four.subject_img);
            }
        } else {
            subject_giv.setVisibility(View.GONE);
        }

        analysis_tv.setText(four.subject_analysis);

        checkAnswer();

        setNumGuid();
    }

    /** 检查答案是否正确 */
    private void checkAnswer() {
        SubjectFourBean four = mList.get(currIndex);
        if (four.selectdAnswers != null && four.selectdAnswers.size() > 0) {
            if (!Constant.SUBJECT_MULTIPLE_CHOICE_TYPE.equalsIgnoreCase(four.subject_type)) {
                boolean isExist = false;
                for (String existAnswer : four.selectdAnswers) {
                    if (existAnswer.equalsIgnoreCase(four.answer_true)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    four.selectdAnswers.add(four.answer_true);
                }
            } else {
                String trueAnswerArr[] = four.answer_true.split("/");
                for (String trueAnswer : trueAnswerArr) {
                    boolean isExist = false;
                    for (String existAnswer : four.selectdAnswers) {
                        if (existAnswer.equalsIgnoreCase(trueAnswer)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        if (four.lossAnswers == null || four.lossAnswers.size() <= 0) {
                            four.lossAnswers = new ArrayList<>();
                        }
                        four.lossAnswers.add(trueAnswer);
                    }
                }
            }
            for (String answer : four.selectdAnswers) {
                checkAnswer(answer);
            }
            if (Constant.SUBJECT_MULTIPLE_CHOICE_TYPE.equalsIgnoreCase(four.subject_type)) {
                if (four.lossAnswers != null && four.lossAnswers.size() > 0) {
                    for (String answer : four.lossAnswers) {
                        showLossAnswer(answer);
                    }
                }
            }
            analysis_tv.setVisibility(View.VISIBLE);
        }
    }

    private void showLossAnswer(String answer) {
        if (!StringUtil.equalsNull(answer)) {
            switch (answer) {
                case ANSWER_A:
                    answer_a_check_iv.setImageResource(R.drawable.right_green_hollow);
                    break;
                case ANSWER_B:
                    answer_b_check_iv.setImageResource(R.drawable.right_green_hollow);
                    break;
                case ANSWER_C:
                    answer_c_check_iv.setImageResource(R.drawable.right_green_hollow);
                    break;
                case ANSWER_D:
                    answer_d_check_iv.setImageResource(R.drawable.right_green_hollow);
                    break;
            }
        }
    }

    private void checkAnswer(String answer) {
        if (!StringUtil.equalsNull(answer)) {
            SubjectFourBean four = mList.get(currIndex);
            switch (answer) {
                case ANSWER_A:
                    if (four.answer_true.contains(answer)) {
                        answer_a_check_iv.setImageResource(R.drawable.right_green);
                    } else {
                        answer_a_check_iv.setImageResource(R.drawable.wrong_red);
                    }
                    break;
                case ANSWER_B:
                    if (four.answer_true.contains(answer)) {
                        answer_b_check_iv.setImageResource(R.drawable.right_green);
                    } else {
                        answer_b_check_iv.setImageResource(R.drawable.wrong_red);
                    }
                    break;
                case ANSWER_C:
                    if (four.answer_true.contains(answer)) {
                        answer_c_check_iv.setImageResource(R.drawable.right_green);
                    } else {
                        answer_c_check_iv.setImageResource(R.drawable.wrong_red);
                    }
                    break;
                case ANSWER_D:
                    if (four.answer_true.contains(answer)) {
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
                ExerciseFourActivity.this.finish();
                break;
            case R.id.mulchoice_submit_bt:
                // 多选题提交按钮
                setItem();
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
                SubjectFourBean four = mList.get(currIndex);
                String trueAnswerArr[] = four.answer_true.split("/");
                if (four.selectdAnswers == null || four.selectdAnswers.size() <= 0) {
                    four.selectdAnswers = Arrays.asList(trueAnswerArr);
                } else {
                    for (String trueAnswer : trueAnswerArr) {
                        boolean isExist = false;
                        for (String existAnswer : four.selectdAnswers) {
                            if (existAnswer.equalsIgnoreCase(trueAnswer)) {
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            four.selectdAnswers.add(trueAnswer);
                        }
                    }
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

    private void choseAnswer(String answer) {
        SubjectFourBean four = mList.get(currIndex);
        if (Constant.SUBJECT_MULTIPLE_CHOICE_TYPE.equalsIgnoreCase(four.subject_type)) {
            // 多选题
            if (four.selectdAnswers != null && four.selectdAnswers.size() > 0) {
                for (String selectAnswer : four.selectdAnswers) {
                    if (selectAnswer.equalsIgnoreCase(answer)) {
                        four.selectdAnswers.remove(answer);
                        return;
                    }
                }
                four.selectdAnswers.add(answer);
            } else {
                if (four.selectdAnswers == null) {
                    four.selectdAnswers = new ArrayList<>();
                }
                four.selectdAnswers.add(answer);
            }
            for (String selectAnswer : four.selectdAnswers) {
                switch (selectAnswer) {
                    case ANSWER_A:
                        answer_a_check_iv.setImageResource(R.drawable.circle_green);
                        break;
                    case ANSWER_B:
                        answer_b_check_iv.setImageResource(R.drawable.circle_green);
                        break;
                    case ANSWER_C:
                        answer_c_check_iv.setImageResource(R.drawable.circle_green);
                        break;
                    case ANSWER_D:
                        answer_d_check_iv.setImageResource(R.drawable.circle_green);
                        break;
                }
            }
        } else {
            if (four.selectdAnswers == null) {
                four.selectdAnswers = new ArrayList<>();
            }
            four.selectdAnswers.clear();
            four.selectdAnswers.add(answer);
            setItem();
        }
    }

    protected void downloadGif(String gifUrl, GifDownloadCallback callback) {
        String saveFileName = gifUrl.substring(gifUrl.lastIndexOf("/") + 1);
        String cacheFile = checkFile(saveFileName);
        if (!StringUtil.isEmpty(cacheFile)) {
            if (callback != null) {
                callback.callback(cacheFile);
            }
            return;
        }
        HttpUtils http = HttpUtils.getInstance();
        http.configUserAgent("Android");
        http.download(gifUrl, spUtil.getFileCachePath() + saveFileName, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                        loadingDialog.show();
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        loadingDialog.dismiss();
                        String gifPath = responseInfo.result.getAbsolutePath();
                        if (callback != null) {
                            callback.callback(gifPath);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        loadingDialog.dismiss();
                        showToastInThread("文件下载失败！");
                        if (callback != null) {
                            callback.callback("");
                        }
                    }

                });
    }

    /**
     * 检查是否已经下载过文件了，如果已经下载过了返回文件地址
     *
     * @param fileName 文件名
     */
    private String checkFile(String fileName) {
        File file = new File(spUtil.getFileCachePath());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (fileName.equals(f.getName())) {
                    return f.getAbsolutePath();
                }
            }
        }
        return null;
    }

}