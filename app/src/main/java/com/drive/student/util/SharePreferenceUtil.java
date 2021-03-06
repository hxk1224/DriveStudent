package com.drive.student.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.location.BDLocation;
import com.drive.student.R;
import com.drive.student.bean.LocationBean;
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

    /** 自动定位用户位置 */
    public void setUserLocation(BDLocation bdLocation) {

        String longitude = bdLocation.getLongitude() + "";
        String latitude = bdLocation.getLatitude() + "";
        String province = bdLocation.getProvince();
        String city = bdLocation.getCity();
        String district = bdLocation.getDistrict();
        String street = bdLocation.getStreet();
        String streetNumber = bdLocation.getStreetNumber();

        if (!TextUtils.isEmpty(province) && province.contains("省")) {
            province = province.substring(0, province.lastIndexOf("省"));
        } else if (!TextUtils.isEmpty(province) && province.contains("市")) {
            province = province.substring(0, province.lastIndexOf("市"));
        }
        if (!TextUtils.isEmpty(city) && city.contains("市")) {
            city = city.substring(0, city.lastIndexOf("市"));
        }

        LocationBean bean = new LocationBean();
        bean.province = province;
        bean.city = city;
        bean.district = district;
        bean.street = street;
        bean.streetNumber = streetNumber;
        // 经度
        bean.longitude = longitude;
        // 纬度
        bean.latitude = latitude;
        String json = JSON.toJSONString(bean);
        editor.putString("user_location", json);
        editor.commit();
    }

    public LocationBean getUserLocation(){
        LocationBean bean = null;
        String json = sp.getString("user_location", "");
        if(!StringUtil.equalsNull(json)) {
            bean = JSON.parseObject(json, new TypeReference<LocationBean>(){
            });
        }
        return bean;
    }

    public void setHomeLoaction(LocationBean location) {
        String json = JSON.toJSONString(location);
        editor.putString("home_location", json);
        editor.commit();
    }

    public LocationBean getHomeLoaction() {
        LocationBean bean;
        String json = sp.getString("home_location", "");
        if (!StringUtil.equalsNull(json)) {
            bean = JSON.parseObject(json, new TypeReference<LocationBean>() {
            });
        } else {
            bean = getUserLocation();
        }
        return bean;
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

    /** 获取用户head icon */
    public String getUserIcon() {
        return sp.getString("user_icon", "");
    }

    /** 保存token */
    public void setToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    /** 获取token */
    public String getToken() {
        return sp.getString("token", "");
    }

    /** 设置科目一练习题保存到数据库的状态 */
    public void setSubjectOneStored(boolean bool) {
        editor.putBoolean("subject_one_saved", bool);
        editor.commit();
    }

    /** 科目一练习题是否已保存到数据库 */
    public boolean isSubjectOneStored() {
        return sp.getBoolean("subject_one_saved", false);
    }

    /** 设置科目四练习题保存到数据库的状态 */
    public void setSubjectFourStored(boolean bool) {
        editor.putBoolean("subject_four_saved", bool);
        editor.commit();
    }

    /** 科目四练习题是否已保存到数据库 */
    public boolean isSubjectFourStored() {
        return sp.getBoolean("subject_four_saved", false);
    }

}
