package com.drive.student.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.config.UrlConfig;
import com.drive.student.ui.BaseFragment;
import com.drive.student.util.BitmapHelp;
import com.drive.student.util.LogUtil;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.util.UploadFileUtil;
import com.drive.student.xutils.BitmapUtils;
import com.drive.student.xutils.exception.HttpException;
import com.drive.student.xutils.http.ResponseInfo;
import com.drive.student.xutils.http.callback.RequestCallBack;

public class UserFrag extends BaseFragment implements OnClickListener {
    private static final int SELECT_ICON = 1;
    private View mainView;
    private BitmapUtils mBitmapUtils;
    private SharePreferenceUtil spUtil;
    private TextView user_name_tv;
    private ImageView user_icon_iv;
    private TextView user_home_tv;
    private LinearLayout user_account_ll;

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
        user_name_tv = (TextView) mainView.findViewById(R.id.user_name_tv);
        user_icon_iv = (ImageView) mainView.findViewById(R.id.user_icon_iv);
        user_home_tv = (TextView) mainView.findViewById(R.id.user_home_tv);
        /** 我的账户 */
        user_account_ll = (LinearLayout) mainView.findViewById(R.id.user_account_ll);

        user_account_ll.setOnClickListener(this);
        user_info_rl.setOnClickListener(this);
        feedback_ll.setOnClickListener(this);
        about_ll.setOnClickListener(this);
        guide_ll.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.e("UserFrag", "onStart--->>");
        // 我的位置
        if (StringUtil.isBlank(spUtil.getCity())) {
            user_home_tv.setText("");
        } else {
            user_home_tv.setText(String.format("当前位置：%s", spUtil.getCity()));
        }
        // 用户名
        user_name_tv.setText(mainApplication.getUserCode());
        // 头像
        if (!StringUtil.isBlank(spUtil.getUserIcon())) {
            mBitmapUtils.display(user_icon_iv, spUtil.getUserIcon());
        }
        // 主账号才能看"我的账户"
        if (mainApplication.isMainAccount()) {
            user_account_ll.setVisibility(View.VISIBLE);
        } else {
            user_account_ll.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        /*switch (v.getId()) {
            case R.id.user_info_rl:
                intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.user_account_ll:
                intent = new Intent(getActivity(), UserAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.guide_ll:
                intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
                break;
            case R.id.feedback_ll:
                intent = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.about_ll:
                intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_ICON:
                    // 获取拍照的图片地址
                    if (data != null) {
                        String picUrl = data.getStringExtra(Constant.KEY_UPLOAD_PIC_PATH);
                        if (!TextUtils.isEmpty(picUrl)) {
                            spUtil.setUserIcon(picUrl);
                            mBitmapUtils.display(user_icon_iv, picUrl);
                            // 上传头像照片
                            UploadFileUtil.uploadFile(getActivity(), picUrl, UrlConfig.UPLOAD_HEADER_CODE, mainApplication.getUser().userAccountId, new RequestCallBack<String>() {

                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {

                                }

                                @Override
                                public void onFailure(HttpException error, String msg) {
                                }

                            });
                        }
                    }
                    break;
            }
        }
    }
}
