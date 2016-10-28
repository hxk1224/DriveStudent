package com.drive.student.ui.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.manager.NoticeManager;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.ui.MainActivity;
import com.drive.student.ui.SelectPicActivity;
import com.drive.student.util.BitmapHelp;
import com.drive.student.util.CustomeDialogUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.xutils.BitmapUtils;

public class UserInfoActivity extends ActivitySupport implements OnClickListener {
    private static final int SELECT_ICON = 1;

    private BitmapUtils mBitmapUtils;
    private ImageView header_icon_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);
        mBitmapUtils = BitmapHelp.getHeadPortrait(this);
        initView();
    }

    private void initView() {
        // header
        View header = findViewById(R.id.header);
        View left = header.findViewById(R.id.header_tv_back);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        TextView title = (TextView) header.findViewById(R.id.header_tv_title);
        title.setText("我的信息");
        // 头像
        header_icon_iv = (ImageView) findViewById(R.id.header_icon_iv);
        header_icon_iv.setOnClickListener(this);
        if (!StringUtil.isBlank(spUtil.getUserIcon())) {
            mBitmapUtils.display(header_icon_iv, spUtil.getUserIcon());
        }

        TextView user_name_tv = (TextView) findViewById(R.id.user_name_tv);
        TextView user_sex_tv = (TextView) findViewById(R.id.user_sex_tv);
        TextView user_address_tv = (TextView) findViewById(R.id.user_address_tv);

        header_icon_iv.setOnClickListener(this);
        findViewById(R.id.change_login_pwd_tv).setOnClickListener(this);
        findViewById(R.id.logout_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                UserInfoActivity.this.finish();
                break;
            case R.id.header_icon_iv:// 更改头像
                openSelectPic();
                break;
            case R.id.logout_tv:
                showExitDialog();
                break;
            case R.id.change_login_pwd_tv:
                openChangePwd();
                break;
        }
    }

    /** 修改登录密码 */
    private void openChangePwd() {
        Intent intent = new Intent(this, ResetLoginPwdActivity.class);
        startActivityForResult(intent, SELECT_ICON);
    }

    /** 打开选取/拍照页面 */
    private void openSelectPic() {
        Intent intent = new Intent(this, SelectPicActivity.class);
        startActivityForResult(intent, SELECT_ICON);
    }

    private void showExitDialog() {
        AlertDialog dialog = CustomeDialogUtil.showDialog(this, R.layout.exit, 0.75f);
        if (dialog != null) {
            TextView logout_tv = (TextView) dialog.findViewById(R.id.logout_tv);
            TextView exit_app_tv = (TextView) dialog.findViewById(R.id.exit_app_tv);
            logout_tv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // NOTE:退出登录不需要走接口，清理掉内存中缓存的用户登录信息就行
                    stopJpush();
                    NoticeManager.getInstance(UserInfoActivity.this).clearAllNotifation();
                    spUtil.setToken("");
                    Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                    mainApplication.setUser(null);
                    UserInfoActivity.this.finish();
                }
            });
            exit_app_tv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mainApplication.exit();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_ICON:
                    // 获取拍照的图片地址
                    if (data != null) {
                        String picUrl = data.getStringExtra(Constant.KEY_UPLOAD_PIC_PATH);
                        if (!TextUtils.isEmpty(picUrl)) {
                            spUtil.setUserIcon(picUrl);
                            mBitmapUtils.display(header_icon_iv, picUrl);
                        }
                    }
                    break;
            }
        }
    }

}
