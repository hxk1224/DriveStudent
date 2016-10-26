package com.drive.student.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.drive.student.R;
import com.drive.student.bean.UserBean;
import com.drive.student.config.Constant;
import com.drive.student.config.UrlConfig;

import java.io.File;

/**
 * SharePreference工具类.
 */
@SuppressLint("CommitPrefEdits")
public class SharePreferenceUtil {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String name = "GLOBAL_SET";
    private Context context;

    public SharePreferenceUtil(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public SharePreferenceUtil(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /** 获取应用的Base路径. */
    public String getBasePath() {
        String basePath = getStoragePath() + context.getString(R.string.dir);
        return basePath;
    }

    /** HttpCache 缓存目录 */
    public String getHttpCachePath() {
        String updatePath = getBasePath() + context.getString(R.string.http_cache_dir);
        return updatePath;
    }

    /** 获取应用的更新路径. */
    public String getUpdatePath() {
        return getStoragePath() + context.getString(R.string.download);
    }

    /** 获取欢迎页路径. */
    public String getWelcomePath() {
        return getBasePath() + context.getString(R.string.welcome);
    }

    /**
     * 获取应用的BitmapCache路径.
     */
    public String getBitmapCachePath() {
        String bitmapCachePath = getBasePath() + context.getString(R.string.bitmap_cache);
        File dirFile = new File(bitmapCachePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return bitmapCachePath;
    }

    /** 获取应用的语音缓存路径 */
    public String getAudioCachePath() {
        String audioPath = getBasePath() + context.getString(R.string.audio_cache);
        File dirFile = new File(audioPath);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return audioPath;
    }

    /** 获取应用的文件下载缓存路径 */
    public String getFileCachePath() {
        String filePath = getBasePath() + context.getString(R.string.file_cache);
        File dirFile = new File(filePath);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return filePath;
    }

    /** 获取应用的拍照临时保存的路径 */
    public String getCameraTempPath() {
        String cameraTempPath = getBasePath() + context.getString(R.string.camera_temp);
        File dirFile = new File(cameraTempPath);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return cameraTempPath;
    }

    /** 存储卡路径 */
    public void setStoragePath(String value) {
        editor.putString(Constant.STORAGE_PATH, value);
        editor.commit();
    }

    public String getStoragePath() {
        return sp.getString(Constant.STORAGE_PATH, null);
    }

    /**
     * 自动定位用户位置
     *
     * @param longitude     经度
     * @param latitude      纬度
     * @param province      省
     * @param city          市
     * @param district      区县
     * @param street        街道
     * @param street_number 街道号
     */
    public void setUserLoc(String longitude, String latitude, String province, String city, String district, String street, String street_number) {
        if (!TextUtils.isEmpty(province) && province.contains("省")) {
            province = province.substring(0, province.lastIndexOf("省"));
        } else if (!TextUtils.isEmpty(province) && province.contains("市")) {
            province = province.substring(0, province.lastIndexOf("市"));
        }
        if (!TextUtils.isEmpty(city) && city.contains("市")) {
            city = city.substring(0, city.lastIndexOf("市"));
        }
        editor.putString(Constant.LONGITUDE_AUTO, longitude);
        editor.putString(Constant.LATITUDE_AUTO, latitude);
        editor.putString(Constant.STREET_NUMBER, street_number);
        editor.putString(Constant.PROVINCE_S, province);
        editor.putString(Constant.CITY_S, city);
        editor.putString(Constant.DISTRICT, district);
        editor.putString(Constant.STREET, street);
        editor.putString(Constant.STREET_NUMBER, street_number);
        editor.commit();
    }

    public String getProvince() {
        return sp.getString(Constant.PROVINCE_S, null);
    }

    public String getCity() {
        return sp.getString(Constant.CITY_S, null);
    }

    public String getDistrict() {
        return sp.getString(Constant.DISTRICT, null);
    }

    public String getStreet() {
        return sp.getString(Constant.STREET, null);
    }

    public String getStreetNumber() {
        return sp.getString(Constant.STREET_NUMBER, null);
    }

    /** 经度 */
    public String getLongitude() {
        return sp.getString(Constant.LONGITUDE_AUTO, null);
    }

    /** 纬度 */
    public String getLatitude() {
        return sp.getString(Constant.LATITUDE_AUTO, null);
    }

    /** 是否第一次运行本应用. 默认是第一次登录 */
    public boolean getisFirst() {
        return sp.getBoolean(Constant.IS_FIRST, true);
    }

    public void setIsFirst(boolean isFirst) {
        editor.putBoolean(Constant.IS_FIRST, isFirst);
        editor.commit();
    }

    /** 功能:存贮临时图片 */
    public void setTempQuizImg(String imgPath) {
        editor.putString("quizimg", imgPath);
        editor.commit();
    }

    /** 保存用户UserCode，保存的是手机号 */
    public void setUserCode(String userCode) {
        editor.putString("userCode", userCode);
        editor.commit();
    }

    /** 获取用户UserCode */
    public String getUserCode() {
        return sp.getString("userCode", "");
    }

    public void setUser(UserBean userBean) {
        String userStr;
        if (userBean == null) {
            userStr = "";
            setUserIcon("");
        } else {
            setUserIcon(userBean.facePicUrl);
            userStr = JSONObject.toJSONString(userBean);
        }
        userStr = new DES().authcode(userStr, DES.ENCODE, UrlConfig.KEY);
        editor.putString("UserBean", userStr);
        editor.commit();
    }

    public UserBean getUser() {
        String userStr = sp.getString("UserBean", "");
        if (StringUtil.equalsNull(userStr)) {
            return null;
        }
        userStr = new DES().authcode(userStr, DES.DECODE, UrlConfig.KEY);
        UserBean userBean;
        try {
            userBean = JSON.parseObject(userStr, new TypeReference<UserBean>() {
            });
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
        return userBean;
    }

    /**
     * 保存用户head icon
     */
    public void setUserIcon(String icon) {
        editor.putString("user_icon", icon);
        editor.commit();
    }

    /**
     * 获取用户head icon
     */
    public String getUserIcon() {
        return sp.getString("user_icon", "");
    }

    /**
     * 保存token
     */
    public void setToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    /**
     * 获取token
     */
    public String getToken() {
        return sp.getString("token", "");
    }

}
