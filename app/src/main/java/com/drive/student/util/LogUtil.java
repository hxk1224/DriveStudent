package com.drive.student.util;

import android.util.Log;

import com.drive.student.BuildConfig;
import com.drive.student.task.SaveLogTask;

/**
 * 日志打印类:
 */
public class LogUtil {

    // 是否打印日志
    public static boolean isDebug = BuildConfig.DEBUG;

    public static void v(String tag, String msg) {
        if (isDebug) {
            if (msg == null || tag == null) {
                return;
            }
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            if (msg == null || tag == null) {
                return;
            }
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            if (msg == null || tag == null) {
                return;
            }
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            if (msg == null || tag == null) {
                return;
            }
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            if (msg == null || tag == null) {
                return;
            }
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (isDebug) {
            if (msg == null || tag == null) {
                return;
            }
            Log.e(tag, msg, e);
        }
    }

    public static void print(String tag, String msg) {
        if (isDebug) {
            if (msg == null || tag == null) {
                return;
            }
            System.out.println(tag + "==" + msg);
        }
    }

    /**
     * 设置debug 模式
     *
     * @param isDebug true 打印日志 false：不打印
     */
    public static void setiSDebug(boolean isDebug) {
        LogUtil.isDebug = isDebug;
    }

    /***
     * 这个方法作用：打印日志保存到文件
     *
     * @param tag     tag
     * @param e       异常 （异常和错误信息只写一个就行，另个可设置为null）
     * @param msg     错误信息
     * @param logtype (socket) socket类型错误 ( log ) 其他类型错误
     */
    public static void saveLog(String tag, Throwable e, String msg, String logtype) {
        if (isDebug) {
            if (logtype == null || logtype.equals("")) {
                logtype = "log";
            }
            if (msg != null) {
                new SaveLogTask(tag, SaveLogTask.ERROR, msg, logtype).execute();
            }
            if (e != null) {
                new SaveLogTask(tag, SaveLogTask.ERROR, e, logtype).execute();
            }

        }
    }

    /***
     * 这个方法作用：打印指定错误信息
     *
     * @param tag  tag
     * @param msg  错误信息
     * @param type 错误类型文件夹 (socket) socket类型错误； ( log ) 其他类型错误 默认可以不写为null，
     */
    public static void saveLog(String tag, String msg, String type) {
        saveLog(tag, null, msg, type);
    }

    /***
     * 这个方法作用：打印指定错误信息
     *
     * @param tag tag
     * @param msg 错误信息
     */
    public static void saveLog(String tag, String msg) {
        saveLog(tag, null, msg, null);
    }

    /***
     * 这个方法作用：打印异常信息
     *
     * @param tag  tag
     * @param e    错误信息
     * @param type 错误类型文件夹 (socket) socket类型错误； ( log ) 其他类型错误 默认可以不写为null，
     */
    public static void saveLog(String tag, Throwable e, String type) {
        saveLog(tag, e, null, type);
    }

    /***
     * 这个方法作用：打印指定错误信息
     *
     * @param tag tag
     * @param e   错误信息
     */
    public static void saveLog(String tag, Throwable e) {
        saveLog(tag, e, null, null);
    }
}
