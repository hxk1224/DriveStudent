package com.drive.student.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.drive.student.R;
import com.drive.student.bean.VersionBean;
import com.drive.student.config.Constant;
import com.drive.student.service.DownloadService;
import com.drive.student.service.DownloadService.DownloadBinder;
import com.drive.student.util.CustomToast;
import com.drive.student.util.StringUtil;

/**
 * 更新弹出层.
 */
public class VersionUpdateActivity extends ActivitySupport implements OnClickListener {

    /**** 更新对话框 ***/
    private View updateDialog;
    private Button okBtn;
    private Button cannelBtn;
    private TextView messageV;
    private View linev;

    /** 下载DownloadBinder **/
    protected DownloadBinder downloadBinder;
    /** 下载Service是否绑定 **/
    protected boolean downServiceIsBinded;

    /** 下载进度对话框 **/
    private View downProgressDialog;
    /** 下载进度对话框进度条 **/
    private ProgressBar downBar;
    /** 下载进度对话框 信息 **/
    private TextView downBarMsg;

    private VersionBean newVersion;
    private View retry_line;
    /** 重试和退出按钮载体 **/
    private LinearLayout retry_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.version_update);
        newVersion = (VersionBean) getIntent().getSerializableExtra("bean");
        initViews();
        initDataAndListener();
    }

    private void initDataAndListener() {
        messageV.setText(newVersion.pakageContent);// 可能会改变
        if (Constant.UPDATE_FORCE == newVersion.status) {
            linev.setVisibility(View.GONE);
            cannelBtn.setVisibility(View.GONE);
        } else {
            cannelBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    finish();
                }
            });
        }
        okBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                updateDialog.setVisibility(View.GONE);
                downProgressDialog.setVisibility(View.VISIBLE);

                showToast("正在下载中，请稍候...");
                String savePath = spUtil.getUpdatePath();
                String saveFileName = spUtil.getUpdatePath() + "CarSupplier_" + newVersion.pakageVersionName + ".apk";

                Intent it = new Intent(VersionUpdateActivity.this, DownloadService.class);
                it.putExtra("apkUrl", newVersion.pakageUrl);
                it.putExtra("md5", newVersion.md5);
                it.putExtra("savePath", savePath);
                it.putExtra("saveFileName", saveFileName);
                bindService(it, conn, Context.BIND_AUTO_CREATE);

            }
        });
    }

    private void initViews() {
        updateDialog = findViewById(R.id.update_dialog);
        okBtn = (Button) findViewById(R.id.yes_update_btn);
        cannelBtn = (Button) findViewById(R.id.no_update_btn);
        linev = findViewById(R.id.fengx1);
        messageV = (TextView) findViewById(R.id.message);
        TextView tv_title = (TextView) findViewById(R.id.title);
        if (StringUtil.equalsNull(newVersion.pakageVersionName)) {
            tv_title.setText(getResources().getString(R.string.update_new_version));
        } else {
            tv_title.setText(getResources().getString(R.string.update_new_version) + newVersion.pakageVersionName);
        }
        downProgressDialog = findViewById(R.id.down_dialog);
        downBar = (ProgressBar) findViewById(R.id.down_progressbar);
        downBarMsg = (TextView) findViewById(R.id.id_tv_loadingmsg);
        messageV.setMovementMethod(ScrollingMovementMethod.getInstance());
        retry_line = findViewById(R.id.retry_line);
        retry_layout = (LinearLayout) findViewById(R.id.retry_layout);
        Button retryBtn = (Button) findViewById(R.id.update_retry_btn);
        Button exitBtn = (Button) findViewById(R.id.update_exit_btn);
        retryBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * ServiceConnection连接
     */
    protected ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            downServiceIsBinded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadBinder) service;
            // 开始下载
            downServiceIsBinded = true;
            downloadBinder.addCallback(callback);
            downloadBinder.start();
        }
    };
    /**
     * DownloadService回调结果
     */
    protected ICallbackResult callback = new ICallbackResult() {

        @Override
        public void OnBackResult(Integer progress, String message) {
            if (BACK_RESULT_FINISH.equals(message)) {
                mainApplication.exitForceWithService();
                return;
            } else if (BACK_RESULT_FAILED.equals(message)) {
                showRetry();
                downBarMsg.setText("下载发生错误，请重试");
                return;
            }
            downBar.setProgress(progress);
            downBarMsg.setText(message);

        }

    };

    /**
     * 功能:重试布局显示
     */
    private void showRetry() {
        retry_layout.setVisibility(View.VISIBLE);
        retry_line.setVisibility(View.VISIBLE);
    }

    /**
     * 功能:重试布局隐藏
     */
    private void dismissRetry() {
        retry_layout.setVisibility(View.GONE);
        retry_line.setVisibility(View.GONE);
    }

    public interface ICallbackResult {
        /** 下载服务结束 **/
        String BACK_RESULT_FINISH = "back_result_finish";
        String BACK_RESULT_FAILED = "back_result_failed";

        void OnBackResult(Integer progress, String message);
    }

    @Override
    public void onDestroy() {
        if (downServiceIsBinded) {
            unbindService(conn);
        }
        if ((downloadBinder != null) && downloadBinder.isCanceled()) {
            Intent it = new Intent(this, DownloadService.class);
            stopService(it);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_retry_btn:
                if (!hasInternetConnected()) {
                    CustomToast.showToast(VersionUpdateActivity.this, "网络已经断开连接，请检查网络!", Toast.LENGTH_SHORT);
                    return;
                }
                dismissRetry();
                downBarMsg.setText("开始下载");
                downloadBinder.retryDownload();
                break;
            case R.id.update_exit_btn:
                mainApplication.exitForceWithService();
                break;
        }
    }
}
