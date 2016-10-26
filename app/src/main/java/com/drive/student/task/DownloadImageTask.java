package com.drive.student.task;

import android.content.Context;

import com.drive.student.xutils.HttpUtils;
import com.drive.student.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 下载图片
 */
public class DownloadImageTask {

    public static void downLoadImage(Context context, String picUrl, String picDir, String picName, RequestCallBack<File> callBack) {
        HttpUtils http = HttpUtils.getInstance();
        http.configUserAgent("Android");
        http.download(picUrl, picDir + picName, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                callBack);
    }

}
