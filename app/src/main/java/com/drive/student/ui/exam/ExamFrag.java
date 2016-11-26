package com.drive.student.ui.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.ui.BaseFragment;
import com.drive.student.ui.school.SchoolListActivity;

public class ExamFrag extends BaseFragment implements View.OnClickListener {
    public static final int CHANGE_AD_PIC = 6;
    public static final int AD_TIME_DELAY = 3 * 1000;
    private static int IMAGE_IDS[] = {R.drawable.home_ad_1, R.drawable.home_ad_2, R.drawable.home_ad_3, R.drawable.home_ad_4};

    private View mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.exam_frag, null);
        initViews();
        return mainView;
    }

    private void initViews() {
        // 报名 约考 科目一科目四练习
        TextView sign_up_tv = (TextView) mainView.findViewById(R.id.sign_up_tv);
        sign_up_tv.setOnClickListener(this);
        TextView examination_tv = (TextView) mainView.findViewById(R.id.examination_tv);
        examination_tv.setOnClickListener(this);
        TextView subject_one_tv = (TextView) mainView.findViewById(R.id.subject_one_tv);
        subject_one_tv.setOnClickListener(this);
        TextView subject_four_tv = (TextView) mainView.findViewById(R.id.subject_four_tv);
        subject_four_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_tv:
                openSignPage();
                break;
            case R.id.examination_tv:
                showToastInThread("约考试!");
                break;
            case R.id.subject_one_tv:
                showToastInThread("科目一!");
                openSubjectTrain(Constant.SUBJECT_ONE_TRAIN);
                break;
            case R.id.subject_four_tv:
                showToastInThread("科目四!");
                openSubjectTrain(Constant.SUBJECT_FOUR_TRAIN);
                break;
        }
    }

    private void openSignPage(){
        Intent intent = new Intent(getActivity(), SchoolListActivity.class);
        getActivity().startActivity(intent);
    }

    /** 打开科目一和科目三练习题页面 */
    private void openSubjectTrain(String subjectType) {
        Intent intent = new Intent(getActivity(), SubjectTrainActivity.class);
        intent.putExtra("subjectType", subjectType);
        getActivity().startActivity(intent);
    }

}
