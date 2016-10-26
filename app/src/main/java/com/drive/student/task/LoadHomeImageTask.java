package com.drive.student.task;

import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.drive.student.bean.BaseBean;
import com.drive.student.bean.CommonBean;
import com.drive.student.bean.CommonListBean;
import com.drive.student.bean.WelHomePictureBean;
import com.drive.student.config.Constant;
import com.drive.student.config.UrlConfig;
import com.drive.student.dto.CommonDTO;
import com.drive.student.http.HttpPoolingManager;
import com.drive.student.http.exception.EmptyResultDataAccessException;
import com.drive.student.http.exception.ServerIOException;
import com.drive.student.http.exception.ServerStatusException;
import com.drive.student.http.exception.UnknownException;
import com.drive.student.util.FileUtil;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.xutils.exception.HttpException;
import com.drive.student.xutils.http.ResponseInfo;
import com.drive.student.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.util.List;

public class LoadHomeImageTask extends AsyncTask<String, Void, String> {
    public static final String TAG = "LoadHomeImageTask";
    /** Context上下文 **/
    private Context mContext;
    private SharePreferenceUtil spUtil;
    private int homePicSize;

    public LoadHomeImageTask(Context context) {
        this.mContext = context;
        spUtil = new SharePreferenceUtil(context);
    }

    @Override
    protected String doInBackground(String... params) {
        CommonDTO dto = new CommonDTO(UrlConfig.GET_HOME_PICTURES_CODE);
        String content = JSON.toJSONString(dto);
        String result = HttpPoolingManager.sendHttpPost(UrlConfig.ZASION_HOST, content);
        return result;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String json) {
        if (StringUtil.equalsNull(json)) {
            return;
        }
        CommonBean<WelHomePictureBean> brandBase = null;
        try {
            brandBase = JSON.parseObject(json, new TypeReference<CommonBean<WelHomePictureBean>>() {
            });
            final WelHomePictureBean bean = (WelHomePictureBean) verifyResponse(brandBase);
            if (bean != null) {
                if (bean.openPage != null && bean.openPage.picUrls != null && bean.openPage.picUrls.size() > 0 && !spUtil.getWelPictureId().equals(bean.openPage.id)) {// 开屏页
                    // 图片有更新
                    if (bean.openPage.picUrls.size() > 0) {
                        String picUrl = bean.openPage.picUrls.get(0);
                        if (!StringUtil.equalsNull(picUrl)) {
                            final String picName = picUrl.substring(picUrl.lastIndexOf("/") + 1);
                            DownloadImageTask.downLoadImage(mContext, picUrl, spUtil.getWelcomePath(), picName, new RequestCallBack<File>() {

                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onLoading(long total, long current, boolean isUploading) {
                                }

                                @Override
                                public void onSuccess(ResponseInfo<File> responseInfo) {
                                    spUtil.setWelPictureId(bean.openPage.id);
                                    FileUtil.deleteOtherFile(spUtil.getWelcomePath(), picName);
                                }

                                @Override
                                public void onFailure(HttpException error, String msg) {
                                    FileUtil.deleteFile(spUtil.getWelcomePath() + picName);
                                }

                            });
                        }
                    }
                }
                if (bean.homePage != null && bean.homePage.picUrls != null && bean.homePage.picUrls.size() > 0 && !spUtil.getHomePictureId().equals(bean.homePage.id)) {
                    homePicSize = bean.homePage.picUrls.size();
                    for (String picUrl : bean.homePage.picUrls) {
                        String picName = picUrl.substring(picUrl.lastIndexOf("/") + 1);
                        String dir = spUtil.getWelcomePath() + bean.homePage.id + "/";
                        FileUtil.deleteOtherFile(dir, "");
                        DownloadImageTask.downLoadImage(mContext, picUrl, dir, picName, new RequestCallBack<File>() {

                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
                            }

                            @Override
                            public void onSuccess(ResponseInfo<File> responseInfo) {
                                homePicSize--;
                                if (homePicSize <= 0) {
                                    spUtil.setHomePictureId(bean.homePage.id);
                                }
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                            }

                        });
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证接口数据
     */
    protected <T> Object verifyResponse(BaseBean models) throws ServerStatusException, EmptyResultDataAccessException, UnknownException, ServerIOException {
        return this.verifyResponse("", models);
    }

    /**
     * 验证接口数据
     */
    protected <T> Object verifyResponse(String request, BaseBean models) throws ServerStatusException, EmptyResultDataAccessException, UnknownException, ServerIOException {
        List<T> commonList;
        T commonData;
        if (null != models && models.head != null) {
            if (models.head.returnCode == Constant.RETURN_CODE_OK) {
                // 返回正常
                if (models instanceof CommonListBean) { // CommonListResponse
                    commonList = ((CommonListBean<T>) models).data;
                    if (null != commonList) {
                        return commonList;
                    } else { // 空数据异常
//						throw new EmptyResultDataAccessException(request);
                        return null;
                    }
                } else if (models instanceof CommonBean) {// CommonResponse
                    commonData = ((CommonBean<T>) models).data;
                    if (null != commonData) {
                        return commonData;
                    } else { // 空数据异常
//						throw new EmptyResultDataAccessException(request);
                        return null;
                    }

                } else if (models instanceof BaseBean) {
                    return true;
                } else { // 用于扩展
                    throw new UnknownException(request);
                }
            } else {
                // 服务器状态异常
                int code = models.head.returnCode;
                String message = models.head.message;
                if (code == 0) {// code 没有配置0 返回0说明没有返回code值
                    throw new ServerIOException(request); // 服务器IO异常
                }
                throw new ServerStatusException(code + ":" + message);
            }
        } else { // 服务器IO异常
            throw new ServerIOException(request);
        }
    }
}
