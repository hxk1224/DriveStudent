package com.drive.student.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

public class SystemUtil {

    private static final String TAG = SystemUtil.class.getName();

    private static String sMac;

    // get prop command
    private static final String WIFI = "wifi";

    /*
     * get version code of some package
     * @param ctx context
     * packageName the package name, for example, "com.mobilewrongbook"
     * @return version code
     */
    public static String getPackageName(Context ctx) {
        if (ctx != null) {
            return ctx.getPackageName();
        }

        return "";
    }

    /*
     * get version code of some package
     * @param ctx context
     * packageName the package name, for example, "com.mobilewrongbook"
     * @return version code
     */
    public static int getVersionCode(Context ctx, final String packageName) {
        try {
            PackageInfo packInfo = ctx.getPackageManager().getPackageInfo(packageName, 0);
            return packInfo.versionCode;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /*
     * get version name of some package
     * @param ctx context
     * packageName the package name, for example, "com.mobilewrongbook"
     * @return version name
     */
    public static String getVersionName(Context ctx, final String packageName) {
        try {
            PackageInfo packInfo = ctx.getPackageManager().getPackageInfo(packageName, 0);
            return packInfo.versionName;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * 获取操作系统版本4.0.4
     *
     * @return
     */
    public static String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取SDK version
     *
     * @return SDK version
     */
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            version = 0;
        }
        return version;
    }

    /**
     * 用户类型（1.4以前为：设备唯一标示）
     * 1.4以前为did，1.4版后变更为user_flag
     * 统计版本1.4后修改为用户类型。如果是新增用户上报n 如果是升级用户上报u 如果是活跃用户上报 - （不区分大小写）
     */
    public static String getUserType(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        } else {
            return "";
        }
    }

    /*
     * 当前网络信息
     */
    public static NetworkInfo getAvailableNetWorkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.isAvailable()) {
            return activeNetInfo;
        } else {
            return null;
        }
    }

    /**
     * 获取联网方式
     *
     * @param context
     * @return
     */
    public static String getNetType(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            String typeName = info.getTypeName().toLowerCase(Locale.getDefault()); // WIFI/MOBILE
            if (!typeName.equals(WIFI)) {
                typeName = info.getTypeName().toLowerCase(Locale.getDefault());
            }
            return typeName;
        } catch (Exception e) {
            return "-";
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable() && mWiFiNetworkInfo.isConnected();
            }
        }
        return false;
    }

    public static boolean isSDcardExsit() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static String getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(date);
        return time;
    }

    public static String getVerName(Context c) {
        PackageManager pm = c.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(c.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
            e.printStackTrace();
            return "error";
        }
        if (pi == null) {
            return "error1";
        }
        String versionName = pi.versionName;
        if (versionName == null) {
            return "not set";
        }
        return versionName;
    }

    public static String getVerCode(Context c) {
        PackageManager pm = c.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(c.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
            e.printStackTrace();
            return "error";
        }
        if (pi == null) {
            return "error1";
        }
        int versionCode = pi.versionCode;

        return String.valueOf(versionCode);
    }

    public static int getVersionCode(Context c) {
        PackageManager pm = c.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(c.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
            e.printStackTrace();
            return -1;
        }
        if (pi == null) {
            return -1;
        }
        return pi.versionCode;
    }

    public static String getScreenSize(Context context) {
        // 获取屏幕分辨率
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        return screenWidth + "@" + screenHeight;
    }

    /**
     * screenSizes[0] = Width,
     * screenSizes[1] = Height;
     */
    public static int[] getScreen(Context context) {
        int[] screenSizes = new int[2];
        // 获取屏幕分辨率
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        screenSizes[0] = display.getWidth();
        screenSizes[1] = display.getHeight();
        return screenSizes;
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        String deviceId = tm.getDeviceId();
        if (StringUtil.isBlank(deviceId)) {
            deviceId = "";
        }
        return deviceId;
    }

    public static String getIpAddress(Context context) {
        String ip = getLocalIpAddress(context);
        if (!StringUtil.equalsNull(ip) && !"0.0.0.0".equals(ip)) {
            return ip;
        }
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ip = inetAddress.getHostAddress().toString();
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ip;
    }

    public static String getLocalIpAddress(Context context) {
        String ip = "";
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = String.format(Locale.getDefault(), "%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        } catch (Exception e) {
            ip = "";
            e.printStackTrace();
        }
        return ip;
    }

    public static String getMid(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        String AndroidID = android.provider.Settings.System.getString(context.getContentResolver(), "android_id");
        String serialNo = getDeviceSerialForMid2();
        String m2 = getMD5Str("" + imei + AndroidID + serialNo);
        return m2;
    }

    private static String getDeviceSerialForMid2() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
        }
        return serial;
    }

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    /**
     * 获取mac地址
     */
    private static final String GETPROP = "getprop";
    private static final String LOCALMAC = "net.local.mac";
    private static final String IFCONFIG = "busybox ifconfig";
    private static final String HWADDR = "HWaddr";

    public static String getMacAddress(Context context) {// get mac from share
        if (!StringUtil.equalsNull(sMac)) {
            return sMac;
        }
        String result = "";
        result = getWifiMac(context);
        if (!StringUtil.equalsNull(result)) {
            sMac = result;
            return result;
        }
        result = getLocalMacAddress();
        if (!StringUtil.equalsNull(result)) {
            sMac = result;
            return result;
        }

        String Mac = "";
        result = callCmd(GETPROP, LOCALMAC);
        if (TextUtils.isEmpty(result)) {
            result = callCmd(IFCONFIG, HWADDR);
        }

        if (StringUtil.isBlank(result)) {
            sMac = "";
            return sMac;
        }

        if (result.length() > 0 && result.contains(HWADDR) == true) {
            Mac = result.substring(result.indexOf(HWADDR) + 6, result.length() - 1);
            if (Mac.length() > 1) {
                Mac = Mac.replaceAll(" ", "");
            }
        }
        return sMac;
    }

    public static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            // 执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine()) != null && line.contains(filter) == false) {
            }
            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getWifiMac(Context context) {
        String ipAddress = "";
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            ipAddress = wifiInfo.getMacAddress();
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * 获取Mac地址方法
     *
     * @return
     */
    public static String getLocalMacAddress() {
        String Mac = "";
        try {
            String path = "sys/class/net/wlan0/address";
            if ((new File(path)).exists()) {
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192];
                int byteCount = fis.read(buffer);
                if (byteCount > 0) {
                    Mac = new String(buffer, 0, byteCount, "utf-8");
                }
                LogUtil.d(TAG, "getLocalMacAddress:wifi--" + Mac);
            }
            if (StringUtil.equalsNull(Mac)) {
                path = "sys/class/net/eth0/address";
                FileInputStream fis_name = new FileInputStream(path);
                byte[] buffer_name = new byte[8192];
                int byteCount_name = fis_name.read(buffer_name);
                if (byteCount_name > 0) {
                    Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
                }
                LogUtil.d(TAG, "getLocalMacAddress:eth0--" + Mac);
            }
        } catch (Exception io) {
            io.printStackTrace();
        }
        if (StringUtil.equalsNull(Mac)) {
            return "";
        }
        if (Mac.length() > 1) {
            Mac = Mac.replaceAll(" ", "");
        }
        LogUtil.d(TAG, "getLocalMacAddress:mac--" + Mac);
        return Mac.trim();
    }

}
