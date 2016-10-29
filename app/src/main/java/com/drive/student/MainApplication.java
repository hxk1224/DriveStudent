package com.drive.student;

import android.app.Activity;
import android.app.Application;
import android.util.DisplayMetrics;

import com.drive.student.bean.UserBean;
import com.drive.student.bean.VersionBean;
import com.drive.student.common.GlobalThreadPool;
import com.drive.student.manager.NoticeManager;
import com.drive.student.task.SaveSubjectExcerciseTask;
import com.drive.student.util.FileUtil;
import com.drive.student.util.LocationUtil;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.SoundUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.util.SystemUtil;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * MainApplication基类.
 */
public class MainApplication extends Application {
    /** 当前用户信息 **/
    private UserBean userBean;
    /** 所有当前打开的Activity列表 **/
    public List<WeakReference<Activity>> activityList = new LinkedList<>();
    public SharePreferenceUtil spUtil = null;
    public static MainApplication mainApplication;
    public static boolean hasNewVersion = false;
    public static VersionBean mVersonBean = null;
    private static DisplayMetrics dm = new DisplayMetrics();

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        mainApplication = this;
        spUtil = new SharePreferenceUtil(getApplicationContext());
        LocationUtil.getUserLocation(getApplicationContext());
        /** NOTE：正式版本需要把下面代码设置为false */
        MobclickAgent.setDebugMode(false);
        MobclickAgent.openActivityDurationTrack(false);
        /**NOTE: 初始化SoundUtil,点击键盘的时候加载语音数据会导致卡顿*/
        SoundUtil.getInstance();
    }

    /**
     * 添加Activity到容器中.
     *
     * @param activity Activity页面
     */
    public void addActivity(Activity activity) {
        activityList.add(new WeakReference<>(activity));
    }

    /**
     * 把Activity从容器中移除.
     */
    public void removeActivity(Activity activity) {
        for (WeakReference<Activity> weak : activityList) {
            if (activity == weak.get()) {
                activityList.remove(weak);
                break;
            }
        }
    }

    /**
     * 退出APP.
     */
    public void exit() {
        SoundUtil.getInstance().release();
        GlobalThreadPool.shutdownPool();
        FileUtil.deleteTmpCameraFileInThread(spUtil.getCameraTempPath());
        NoticeManager.getInstance(this).clearAllNotifation();
        clearActivities();
        System.exit(0);
    }

    /**
     * 遍历所有Activity并finish.
     */
    public void clearActivities() {
        for (WeakReference<Activity> weak : activityList) {
            if (weak != null) {
                weak.get().finish();
            }
        }
    }

    /***
     * 这个方法作用：强制退出， 杀死服务，并杀死activity
     */
    public void exitForceWithService() {
        // 清空application的user
        setUser(null);
        exit();
    }

    public static MainApplication getInstance() {
        return mainApplication;
    }

    public String getUserCode() {
        UserBean userBean = getUser();
        if (userBean != null && !StringUtil.equalsNull(userBean.userCode)) {
            return userBean.userCode;
        }
        return "";
    }

    public String getUserPhone() {
        UserBean userBean = getUser();
        if (userBean != null && !StringUtil.equalsNull(userBean.phone)) {
            return userBean.phone;
        }
        return "";
    }

    public String getUserName() {
        UserBean userBean = getUser();
        if (userBean != null && !StringUtil.equalsNull(userBean.userName)) {
            return userBean.userName;
        }
        return "";
    }

    public String getCityCode() {
        UserBean userBean = getUser();
        if (userBean != null && !StringUtil.equalsNull(userBean.cityCode)) {
            return userBean.cityCode;
        }
        return "";
    }

    /** 头像 */
    public String getUserFacePicUrl() {
        return spUtil.getUserIcon();
    }

    public UserBean getUser() {
        if (userBean == null) {
            userBean = spUtil.getUser();
        }
        return userBean;
    }

    public void setUser(UserBean userBean) {
        this.userBean = userBean;
        spUtil.setUser(userBean);
    }

    public String getSupplierId() {
        if (getUser() != null) {
            return getUser().userMbrOrgId;
        }
        return "";
    }

    public boolean isKefu() {
        return getUser() != null && "1".equalsIgnoreCase(getUser().userType);
    }

    /** 是否设置了支付密码 */
    public boolean isAddPayPwd() {
        return getUser() != null && getUser().addPayPwd;
    }

    /** 聊天是否允许联系买家 */
    public boolean isChatShowBuyer() {
        return getUser() != null && getUser().vipSee;
    }

    public String getToken() {
        return spUtil.getToken();
    }

    public int getApkVersionCode() {
        return SystemUtil.getVersionCode(getApplicationContext());
    }

    public String getOsVersion() {
        return SystemUtil.getOSVersion();
    }

    public static DisplayMetrics getDisplayMetrics() {
        if (dm == null) {
            dm = new DisplayMetrics();
        }
        return dm;
    }

}
