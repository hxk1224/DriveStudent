package com.drive.student.task;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.drive.student.MainApplication;
import com.drive.student.R;
import com.drive.student.bean.BaseBean;
import com.drive.student.bean.UserBean;
import com.drive.student.bean.VersionBean;
import com.drive.student.config.Constant;
import com.drive.student.config.UrlConfig;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.CustomToast;
import com.drive.student.util.LogUtil;
import com.drive.student.util.StringUtil;

import java.util.List;

/**
 * 版本校验任务.
 */
public class CheckVersionTask extends AsyncTask<String, Void, String> {
    public static final String TAG = "CheckVersionTask";
    /** Context上下文 **/
    private ActivitySupport context;
    /** 版本校验监听 **/
    private CheckVersionListener checkVersionListener;
    private int type;

    public CheckVersionTask(ActivitySupport context, int type, CheckVersionListener checkVersionListener) {
        this.checkVersionListener = checkVersionListener;
        this.context = context;
        this.type = type;
    }

    @Override
    protected String doInBackground(String... params) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e1) {
            pi = null;
        }
        if (pi == null) {
            return null;
        }
//        UserBean bean = ((MainApplication) context.getApplication()).getUser();
//        CommonDTO dto = new CommonDTO(UrlConfig.CHECK_UPDATE_CODE);
//        if (bean != null) {
//            dto.addParam("userCode", bean.userCode);
//        }
//        dto.addParam("version", pi.versionCode + "");
//        dto.addParam("os", "android");
//        dto.addParam("osVersion", Build.VERSION.RELEASE);
//        String content = JSON.toJSONString(dto);
//        String result = HttpPoolingManager.sendHttpPost(UrlConfig.ZASION_HOST, content);
//        LogUtil.e(TAG, "check Version resultJson === " + result);
        return "result";
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        checkVersionListener.toGuidOrAutoLoginHandle();
//        CommonBean<VersionBean> common = null;
//        try {
//            common = JSON.parseObject(result, new TypeReference<CommonBean<VersonBean>>() {
//            });
//            VersionBean bean = (VersionBean) verifyResponse(common);
//            if (bean == null || Constant.UPDATE_NEEDLESS == bean.status || StringUtil.equalsNull(bean.pakageUrl)) {
//                if (type == 0) {
//                    checkVersionListener.toGuidOrAutoLoginHandle();
//                } else {
//                    CustomToast.showToast(context, "当前已经是最新版本了", Toast.LENGTH_LONG);
//                }
//                return;
//            }
//            checkVersionListener.updateNewVersion(bean);
//        } catch (Throwable t) {
//            CustomToast.showToast(context, context.getString(R.string.server_net_error), Toast.LENGTH_LONG);
//            if (type == 0) {
//                checkVersionListener.toGuidOrAutoLoginHandle();
//            }
//        }

    }

    /**
     * 校验版本监听.
     */
    public interface CheckVersionListener {
        /**
         * 跳转到引导或登录界面.
         */
        void toGuidOrAutoLoginHandle();

        /**
         * 立刻更新.
         */
        void updateNewVersion(VersionBean bean);

    }
}
