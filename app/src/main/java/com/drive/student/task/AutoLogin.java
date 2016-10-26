package com.drive.student.task;

import android.content.Intent;
import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.drive.student.MainApplication;
import com.drive.student.bean.BaseBean;
import com.drive.student.bean.CommonBean;
import com.drive.student.bean.CommonListBean;
import com.drive.student.bean.UserBean;
import com.drive.student.config.Constant;
import com.drive.student.config.UrlConfig;
import com.drive.student.dto.CommonDTO;
import com.drive.student.http.HttpTransferCallBack;
import com.drive.student.http.HttpTransferUtil;
import com.drive.student.http.exception.EmptyResultDataAccessException;
import com.drive.student.http.exception.ServerIOException;
import com.drive.student.http.exception.ServerStatusException;
import com.drive.student.http.exception.UnknownException;
import com.drive.student.manager.NoticeManager;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.ui.MainActivity;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.StringUtil;

import java.util.List;

/**
 * 登录任务，对用户名和密码没有进行加密.
 */
public class AutoLogin {
    public static final String TAG = "AutoLoginTask";
    private String userCode;
    /** 跳转到登录页面 **/
    private ActivitySupport context;
    private long starttime;
    private SharePreferenceUtil spUtil;
    private Handler mHandler;

    public AutoLogin(ActivitySupport context, Handler handler, long starttime) {
        this.starttime = starttime;
        this.context = context;
        mHandler = handler;
        spUtil = new SharePreferenceUtil(context.getApplicationContext());
        getUserInfo();
    }

    private void getUserInfo() {
        userCode = spUtil.getUserCode();
    }

    /**
     * 调转到登录页面
     *
     */
    private void toLogin() {
        spUtil.setToken("");
        NoticeManager.getInstance(context.getApplicationContext()).clearAllNotifation();
        goToMainActivity(2000);
    }

    private void goToMainActivity(long waitTime) {
        long currenttime = System.currentTimeMillis();
        long delayMillis = 0;
        if ((currenttime - starttime) < waitTime) {
            delayMillis = waitTime - (currenttime - starttime);
        }
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        }, delayMillis);
    }

    public void submitLogin() {
        if (StringUtil.isBlank(spUtil.getToken()) || StringUtil.isBlank(userCode)) {
            toLogin();
            return;
        }
        CommonDTO dto = new CommonDTO(UrlConfig.USER_LOGIN_CODE);
        MainApplication.getInstance().setUser(null);
        dto.addParam("userCode", userCode);
        dto.addParam("passWord", "");
        dto.head.userCode = userCode;
        // 使用token做自动登录
        dto.head.token = spUtil.getToken();
        String content = JSON.toJSONString(dto);
        new HttpTransferUtil().sendHttpPost(UrlConfig.ZASION_HOST, content, new HttpTransferCallBack() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(String json) {
                CommonBean<UserBean> base;
                try {
                    base = JSON.parseObject(json, new TypeReference<CommonBean<UserBean>>() {
                    });
                    UserBean bean = (UserBean) verifyResponse("", base);
                    if (bean != null && Constant.RETURN_CODE_OK == base.head.returnCode && !StringUtil.isBlank(base.head.token)) {
                        spUtil.setToken(base.head.token);
                        spUtil.setLoginTime(System.currentTimeMillis());
                        // 登陆成功
                        MainApplication.getInstance().setUser(bean);
                        goToMainActivity(2000);
                    } else {
                        toLogin();
                    }
                } catch (Throwable e) {
                    toLogin();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                toLogin();
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
                                return null;
                            }
                        } else if (models instanceof CommonBean) {// CommonResponse
                            commonData = ((CommonBean<T>) models).data;
                            if (null != commonData) {
                                return commonData;
                            } else { // 空数据异常
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

        });
    }
}