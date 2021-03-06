package com.drive.student.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.bean.LocationBean;
import com.drive.student.ui.BaseFragment;
import com.drive.student.util.BitmapHelp;
import com.drive.student.util.LogUtil;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.xutils.BitmapUtils;

public class UserFrag extends BaseFragment implements OnClickListener {
    private BitmapUtils mBitmapUtils;
    private SharePreferenceUtil spUtil;
    private View mainView;
    private TextView user_name_tv;
    private ImageView user_icon_iv;
    private TextView user_home_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.user_frag, null);
        mBitmapUtils = BitmapHelp.getHeadPortrait(getActivity().getApplicationContext());
        spUtil = new SharePreferenceUtil(getActivity().getApplicationContext());
        initViews();
        return mainView;
    }

    private void initViews() {
        View user_info_rl = mainView.findViewById(R.id.user_info_rl);
        View feedback_ll = mainView.findViewById(R.id.feedback_ll);
        View about_ll = mainView.findViewById(R.id.about_ll);
        View guide_ll = mainView.findViewById(R.id.guide_ll);
        user_icon_iv = (ImageView) mainView.findViewById(R.id.user_icon_iv);
        user_name_tv = (TextView) mainView.findViewById(R.id.user_name_tv);
        user_icon_iv = (ImageView) mainView.findViewById(R.id.user_icon_iv);
        user_home_tv = (TextView) mainView.findViewById(R.id.user_home_tv);

        user_info_rl.setOnClickListener(this);
        feedback_ll.setOnClickListener(this);
        about_ll.setOnClickListener(this);
        guide_ll.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.e("UserFrag", "onStart--->>");
        LocationBean location = spUtil.getUserLocation();
        if(location == null){
            location = spUtil.getHomeLoaction();
        }
        // 我的位置
        if (location != null) {
            user_home_tv.setText(String.format("%s %s %s", location.province, location.city, location.district));
        } else {
            user_home_tv.setText("当前位置：未定位到位置");
        }
        // TODO 用户名
        // user_name_tv.setText(mainApplication.getUserCode());
        // 头像
        if (!StringUtil.isBlank(spUtil.getUserIcon())) {
            mBitmapUtils.display(user_icon_iv, spUtil.getUserIcon());
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.user_info_rl:
                intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.guide_ll:
                openGuid();
                break;
            case R.id.feedback_ll:
                openFeedBack();
                break;
            case R.id.about_ll:
                openAbout();
                break;
        }
    }

    private void openAbout() {
        Intent intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);
    }

    private void openFeedBack() {
        Intent intent = new Intent(getActivity(), FeedBackActivity.class);
        startActivity(intent);
    }

    private void openGuid() {
        Intent intent = new Intent(getActivity(), UserGuideActivity.class);
        startActivity(intent);
    }

}
