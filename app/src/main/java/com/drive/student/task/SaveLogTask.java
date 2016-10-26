package com.drive.student.task;

import android.os.AsyncTask;

import com.drive.student.MainApplication;
import com.drive.student.bean.UserBean;
import com.drive.student.util.DateUtil;
import com.drive.student.util.SharePreferenceUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * 保存和收集系统报错日志.
 */
public class SaveLogTask extends AsyncTask<Void, Void, Void> {
    private String userId = null;
    private String msg = null;
    private String tag = null;
    private String logLevel = null;
    private String logType = "log";
    public static String DEBUG = "debug";
    public static String INFO = "info";
    public static String ERROR = "error";
    public static String WARN = "warn";

    public SaveLogTask() {

    }

    public SaveLogTask(String tag, String logLevel, Throwable ex, String logType) {
        this.logLevel = logLevel;
        this.tag = tag;
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        this.msg = sw.toString();
        this.logType = logType;
    }

    public SaveLogTask(String tag, String logLevel, String msg, String logType) {
        this.logLevel = logLevel;
        this.tag = tag;
        this.msg = msg;
        this.logType = logType;
    }

    @Override
    protected Void doInBackground(Void... params) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n" + tag);
        sb.append("\n" + msg);
        sb.append("\n============================================================");
        if (MainApplication.getInstance() != null) {
            UserBean userBean = MainApplication.getInstance().getUser();
            if (userBean != null) {
                userId = userBean.userAccountId;
            }
            try {
                SharePreferenceUtil spUtil = new SharePreferenceUtil(MainApplication.getInstance().getApplicationContext());
                saveFile(sb.toString(), spUtil.getBasePath() + "/" + logType + "/" + DateUtil.date2Str(new Date(), "MMddHHmmssSSS") + ".txt");
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 保存日志文件文件
     *
     * @param toSaveString
     * @param filePath
     */
    public void saveFile(String toSaveString, String filePath) {
        try {
            File saveFile = new File(filePath);
            if (!saveFile.exists()) {
                File dir = new File(saveFile.getParent());
                dir.mkdirs();
                saveFile.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(saveFile);
            outStream.write(toSaveString.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}