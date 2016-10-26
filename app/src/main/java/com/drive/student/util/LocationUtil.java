package com.drive.student.util;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class LocationUtil {

    private static final String TAG = "LocationUtil";

    /**
     * 定位我的位置
     *
     * @author 韩新凯
     * @update 2015年6月29日 下午11:03:23
     */
    public static void getUserLocation(Context context) {
        final LocationClient locationClient = new LocationClient(context.getApplicationContext()); // 声明LocationClient类
        MyLocationListener myListener = new MyLocationListener(context) {

            @Override
            void closeLocListener(MyLocationListener listener) {
                closeLocSer(locationClient, listener);
            }
        };
        locationClient.registerLocationListener(myListener); // 注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Battery_Saving);// 设置定位模式
        /*返回国测局经纬度坐标系 coor=gcj02
		  返回百度墨卡托坐标系 coor=bd09
		  返回百度经纬度坐标系 coor=bd09ll*/
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(60 * 1000);// 设置发起定位请求的间隔时间为1min
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setProdName("com.drive.student");// 设置产品线名称
        locationClient.setLocOption(option);
        locationClient.start();
        if (locationClient != null && locationClient.isStarted()) {
			/*
			0：正常发起了定位。
			1：服务没有启动。
			2：没有监听函数。
			6：请求间隔过短。 前后两次请求定位时间间隔不能小于1000ms。*/
            int type = locationClient.requestLocation();
            Log.e(TAG, "LOCATION TYPE IS " + type);
        } else {
            Log.e(TAG, "mLocationClient is null or not started");
        }
    }

    static abstract class MyLocationListener implements BDLocationListener {
        private Context mContext;

        public MyLocationListener(Context context) {
            mContext = context;
        }

        abstract void closeLocListener(MyLocationListener listener);

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

			/*MyLocationData locData = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude()).build();
			*/
            if (bdLocation != null) {
                String msg = bdLocation.getCountry() + "-" + bdLocation.getProvince() + "-" + bdLocation.getCity() + "-" + bdLocation.getDistrict() + "-" + bdLocation.getStreet() + "-" + bdLocation.getStreetNumber();
                String addrStr = bdLocation.getAddrStr();
                String location = "纬度：" + bdLocation.getAltitude() + ", 经度：" + bdLocation.getLongitude();
                Log.e(TAG, "onReceiveLocation--》位置：" + msg + ", addrStr：" + addrStr + ", " + location);
                SharePreferenceUtil spUtil = new SharePreferenceUtil(mContext);
                spUtil.setUserLoc(bdLocation.getLongitude() + "", bdLocation.getLatitude() + "", bdLocation.getProvince(), bdLocation.getCity(), bdLocation.getDistrict(), bdLocation.getStreet(), bdLocation.getStreetNumber());
            }
            closeLocListener(this);
        }
    }

    private static void closeLocSer(LocationClient locationClient, MyLocationListener myListener) {
        if (locationClient != null) {
            if (myListener != null) {
                Log.e(TAG, "closeLocSer --> unRegisterLocationListener");
                locationClient.unRegisterLocationListener(myListener);
            }
            Log.e(TAG, "closeLocSer --> stop locationClient");
            locationClient.stop();
        }
    }
}
