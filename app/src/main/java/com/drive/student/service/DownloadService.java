package com.drive.student.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.drive.student.R;
import com.drive.student.WelcomeActivity;
import com.drive.student.ui.VersionUpdateActivity.ICallbackResult;
import com.drive.student.util.CustomToast;
import com.drive.student.util.FileUtil;
import com.drive.student.util.LogUtil;
import com.drive.student.xutils.HttpUtils;
import com.drive.student.xutils.exception.HttpException;
import com.drive.student.xutils.http.HttpHandler;
import com.drive.student.xutils.http.ResponseInfo;
import com.drive.student.xutils.http.callback.RequestCallBack;

import java.io.File;

public class DownloadService extends Service {
    private static final int NOTIFY_ID = 641;
    protected static final String TAG = "DownloadService";
    /** 下载进度条 **/
    private int progress;
    /** 是否取消下载 true表示取消下载 **/
    private boolean canceled;
    /** 安装包url **/
    private String apkUrl = "";
    /** 安装包md5 **/
    private String md5 = "";
    /** 下载包安装文件路径 **/
    private String saveFileName = "";
    /** 回调结果 **/
    private ICallbackResult callback;
    /** 下载的Binder **/
    private DownloadBinder binder;
    /** 下载服务是否挂掉 true表示已经挂了，默认false **/
    private boolean serviceIsDestroy = false;
    private Context context;
    /** 下载完成后的apk文件 **/
    private File apkfile;
    private HttpHandler<File> downHandler;
    private Context mContext = this;
    /** 下载失败重试的次数 **/
    private RemoteViews contentview;

    @Override
    public IBinder onBind(Intent intent) {
        apkUrl = intent.getStringExtra("apkUrl");
        md5 = intent.getStringExtra("md5");
        saveFileName = intent.getStringExtra("saveFileName");
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        binder = new DownloadBinder();
        stopForeground(true);// 这个不确定是否有作用
    }

    public class DownloadBinder extends Binder {
        public void start() {
            if (downHandler == null || downHandler.isCancelled()) {
                progress = 0;
                setUpNotification();
                // 下载
                canceled = false;
                downloadApk();
            }
        }

        public void cancel() {
            canceled = true;
        }

        public int getProgress() {
            return progress;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public boolean serviceIsDestroy() {
            return serviceIsDestroy;
        }

        public void cancelNotification() {
            // 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
            // 取消通知
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(NOTIFY_ID);
        }

        public void addCallback(ICallbackResult callback) {
            DownloadService.this.callback = callback;
        }

        public void retryDownload() {
            downloadApk();
        }
    }

    // 通知栏
    private NotificationCompat.Builder mBuilder;

    /**
     * 创建通知
     */
    private void setUpNotification() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        // 下面两句是 在按home后，点击通知栏，返回之前activity 状态;
        // 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        mBuilder = new NotificationCompat.Builder(
                context);
        mBuilder.setWhen(System.currentTimeMillis());
        // now apply the latestEventInfo fields
        mBuilder.setSmallIcon(R.drawable.app_icon);
        // 放置在"正在运行"栏目中
        mBuilder.setOngoing(true);
        mBuilder.setTicker("开始下载");
        mBuilder.setAutoCancel(true);
        contentview = new RemoteViews(getPackageName(), R.layout.download_notification_layout);
        contentview.setTextViewText(R.id.name, "新版本正在下载...");
        // 指定个性化视图
        mBuilder.setContent(contentview);
        mBuilder.setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFY_ID, mBuilder.build());
    }

    /**
     * 下载完成.
     */
    private void downloadComplete() {
        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(
                    context);
        }
        mBuilder.setWhen(System.currentTimeMillis());
        // now apply the latestEventInfo fields
        mBuilder.setSmallIcon(R.drawable.app_icon);
        // 放置在"正在运行"栏目中
        mBuilder.setAutoCancel(true);
        mBuilder.setTicker("下载完成");
        mBuilder.setAutoCancel(true);
        contentview.setTextViewText(R.id.name, "文件已下载完毕,请安装！");
        // 指定个性化视图
        mBuilder.setContent(null);
        Intent intent = new Intent(mContext, WelcomeActivity.class);
        // 告知已完成
        intent.putExtra("completed", "yes");
        // 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
        mBuilder.setContentIntent(PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        serviceIsDestroy = true;
        stopSelf();// 停掉服务自身
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFY_ID, mBuilder.build());
        // 下载完毕 取消通知
        nm.cancel(NOTIFY_ID);
        // 下载完了，cancelled也要设置
        canceled = true;
    }

    /**
     * 下载apk.
     */
    private void downloadApk() {
        // 弹出下载界面的对话框，下载完之后替换安装
        HttpUtils http = HttpUtils.getInstance();
        http.configUserAgent("Android");
        downHandler = http.download(apkUrl, saveFileName, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        int progress = (int) (((float) current / total) * 100);
                        String message = "";
                        if (progress < 100) {
                            message = "已下载" + progress + "%";
                        } else {
                            message = "恭喜，已下载完成,请安装！";
                        }
                        callback.OnBackResult(progress, message);
                        if (contentview == null) {
                            contentview = new RemoteViews(getPackageName(), R.layout.download_notification_layout);
                        }
                        contentview.setTextViewText(R.id.tv_progress, message);
                        contentview.setProgressBar(R.id.progressbar, 100, progress, false);
                        mBuilder.setContent(contentview);
                        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        nm.notify(NOTIFY_ID, mBuilder.build());
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // maybe the file has downloaded completely
                        if (error.getExceptionCode() == 416) {
                            apkfile = new File(saveFileName);
                            if (md5.equals(FileUtil.getFileMD5(apkfile))) {
                                downloadComplete();
                                installApk();
                                return;
                            }
                            apkfile.delete();
                        }

                        /** 如果次数耗尽就终止下载服务，清除通知栏任务 **/
                        // 下载完毕 取消通知
                        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        nm.cancel(NOTIFY_ID);
                        callback.OnBackResult(null, ICallbackResult.BACK_RESULT_FAILED);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {

                        apkfile = responseInfo.result;
                        String localApkMd5 = FileUtil.getFileMD5(apkfile);
                        LogUtil.e(TAG, "版本升级，升级包MD5：" + md5 + "，下载文件MD5：" + localApkMd5);
                        if (!md5.equals(localApkMd5)) {
                            apkfile.delete();
                            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            nm.cancel(NOTIFY_ID);
                            callback.OnBackResult(null, ICallbackResult.BACK_RESULT_FAILED);
                            return;
                        }
                        downloadComplete();
                        installApk();
                        CustomToast.showToast(context, "下载完成！", Toast.LENGTH_LONG);
                    }
                });

    }

    /**
     * 安装apk .
     */
    private void installApk() {
        /** 如果文件为空或者不存在就重新开启下载 **/
        if (apkfile == null || !apkfile.exists()) {
            downloadApk();
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        callback.OnBackResult(null, ICallbackResult.BACK_RESULT_FINISH);
    }

}
