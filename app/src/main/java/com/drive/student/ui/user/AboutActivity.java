package com.drive.student.ui.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drive.student.R;
import com.drive.student.bean.VersionBean;
import com.drive.student.config.UrlConfig;
import com.drive.student.task.CheckVersionTask;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.ui.VersionUpdateActivity;
import com.drive.student.util.BackUtil;
import com.drive.student.util.CustomToast;
import com.drive.student.util.SystemUtil;


public class AboutActivity extends ActivitySupport implements OnClickListener {

    private CheckVersionTask checkVersionTask;
    private TextView version_update_hint_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_about);
        initView();
        checkVersion(true);
    }

    private void initView() {
        // header
        View header = findViewById(R.id.header);
        View left = header.findViewById(R.id.header_tv_back);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        TextView title = (TextView) header.findViewById(R.id.header_tv_title);
        title.setText("关于");
        if (!UrlConfig.DOMAIN.equals("http://www.chehe180.com/")) {
            // 内网
            TextView domain_tv = (TextView) findViewById(R.id.domain_tv);
            domain_tv.setVisibility(View.VISIBLE);
            domain_tv.setText(String.format("测试环境：%s", UrlConfig.DOMAIN));
        }

        // 版本号
        TextView version_tv = (TextView) findViewById(R.id.version_tv);
        version_tv.setText(String.format("%s%s %s", getString(R.string.hint_version), SystemUtil.getVerCode(this), SystemUtil.getVerName(this)));

        // 新版本检测
        RelativeLayout user_about_vu_rl = (RelativeLayout) findViewById(R.id.user_about_vu_rl);
        user_about_vu_rl.setOnClickListener(this);
        version_update_hint_tv = (TextView) findViewById(R.id.version_update_hint_tv);

        RelativeLayout contact_us_rl = (RelativeLayout) findViewById(R.id.contact_us_rl);
        contact_us_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_tv_back:
                AboutActivity.this.finish();
                break;
            case R.id.user_about_vu_rl:
                // 版本升级检测
                checkVersion(false);
                break;
            case R.id.contact_us_rl:
                Intent intent = new Intent(AboutActivity.this, ContactUsActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 检测版本更新
     */
    private void checkVersion(final boolean start) {
        if (checkVersionTask != null && checkVersionTask.getStatus() == AsyncTask.Status.RUNNING) {
            CustomToast.showToast(AboutActivity.this, "请不要重复检查", Toast.LENGTH_SHORT);
        } else {
            if (!hasInternetConnected()) {
                CustomToast.showToast(AboutActivity.this, "没有网络!", Toast.LENGTH_SHORT);
                return;
            }
            checkVersionTask = new CheckVersionTask(this, 1, new CheckVersionTask.CheckVersionListener() {

                @Override
                public void updateNewVersion(VersionBean bean) {
                    if (start) {
                        version_update_hint_tv.setText(R.string.version_update_new);
                        version_update_hint_tv.setTextColor(getResources().getColor(R.color.char_bg));
                    } else {
                        // 保证Activity没有被销毁
                        if (!BackUtil.isActivityRunningForground(AboutActivity.this, VersionUpdateActivity.class.getName())) {
                            Intent in = new Intent(AboutActivity.this, VersionUpdateActivity.class);
                            in.putExtra("bean", bean);
                            startActivity(in);
                        }
                    }
                }

                @Override
                public void toGuidOrAutoLoginHandle() {

                }
            });
            checkVersionTask.execute();
        }
    }

}
