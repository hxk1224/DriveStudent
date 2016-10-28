package com.drive.student.util;

import android.content.Context;

import com.drive.student.MainApplication;
import com.drive.student.config.UrlConfig;
import com.drive.student.xutils.HttpUtils;
import com.drive.student.xutils.http.RequestParams;
import com.drive.student.xutils.http.callback.RequestCallBack;
import com.drive.student.xutils.http.client.HttpRequest;

import java.io.File;

public class UploadFileUtil {

    /**
     * 上传图片到服务器
     *
     * @param imagePath   图片地址
     * @param requestCode 请求码
     *                    1204上传物流单证 orderId
     *                    1406头像 userAccountId,
     *                    1407营业执照、法人身份证照片 supplierId,
     *                    1408询价单上传照片 inquiryId
     *                    1409售后上传照片 returnId
     *                    1410店铺照片上传 supplierId
     *                    1421商圈图片上传 tradingCircleId
     */
    public static void uploadFile(Context context, final String imagePath, final int requestCode, final String para, RequestCallBack<String> callback) {
        if (StringUtil.isBlank(imagePath)) {
            return;
        }
        final File imageFile = new File(imagePath);/// storage/emulated/0/Android/data/com.drive.student/cameratemp/1437467344578_s.jpg
        if (!imageFile.exists() || !imageFile.isFile()) {
            return;
        }
        String extension = FileUtil.getExtension(imageFile);
        if (!StringUtil.isBlank(extension)) {
            RequestParams params = new RequestParams();
            params.addBodyParameter("supplierId", MainApplication.getInstance().getSupplierId());
            params.addBodyParameter("para", para);
            params.addBodyParameter("osVersion", SystemUtil.getOSVersion());
            params.addBodyParameter("os", UrlConfig.CLIENT);
            params.addBodyParameter("versionCode", SystemUtil.getVerCode(context));
            params.addBodyParameter("image", imageFile);
            params.addBodyParameter("requestCode", requestCode + "");
            params.addBodyParameter("fileType", extension);

            HttpUtils http = HttpUtils.getInstance();
            http.send(HttpRequest.HttpMethod.POST, UrlConfig.DOMAIN_CESHI, params, callback);
        }
    }

}
