package com.drive.student.task;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.drive.student.bean.SubjectFourBean;
import com.drive.student.bean.SubjectOneBean;
import com.drive.student.config.Constant;
import com.drive.student.util.LogUtil;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.xutils.DbUtils;
import com.drive.student.xutils.db.sqlite.Selector;
import com.drive.student.xutils.exception.DbException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.drive.student.config.Constant.SUBJECT_ONE_TXT;

/**
 * 解析题目数据并保存到数据库
 */
public class SaveSubjectExcerciseTask {
    private static final String TAG = "SaveSubjectExcerciseTask";

    private Context mContext;
    private DbUtils mDbUtils;
    private SharePreferenceUtil spUtil;

    public SaveSubjectExcerciseTask(Context context) {
        mContext = context;
        mDbUtils = DbUtils.create(context, Constant.DB_NAME);
        spUtil = new SharePreferenceUtil(context);
    }

    public void saveSubjectExcerciseToDb() {
        // 把asset里面是练习题保存到数据库
        try {
            if (!mDbUtils.tableIsExist(SubjectOneBean.class)) {
                LogUtil.e(TAG, "科目一没有数据,保存数据到数据库-->>");
                saveDataTask(Constant.SUBJECT_ONE_TXT);
            } else {
                queryData();
                LogUtil.e(TAG, "科目一有数据-->>");
            }
            if (!mDbUtils.tableIsExist(SubjectFourBean.class)) {
                LogUtil.e(TAG, "科目四没有数据,保存数据到数据库-->>");
                saveDataTask(Constant.SUBJECT_FOUR_TXT);
            } else {
                queryData();
                LogUtil.e(TAG, "科目四有数据-->>");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void saveDataTask(String subjectName) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                LogUtil.e(TAG, "开始保存练习题到数据库--->>");
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                return saveData(subjectName);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                LogUtil.e(TAG, "完成保存练习题到数据库--->>");
                if (result) {
                    spUtil.setSubjectStored(true);
                    queryData();
                }
            }
        }.execute();
    }

    private boolean saveData(String subjectName) {
        if (StringUtil.equalsNull(subjectName)) {
            return false;
        }
        AssetManager asset = mContext.getAssets();
        try {
            InputStream input = asset.open(subjectName);
            // ByteArrayOutputStream相当于内存输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4 * 1024];
            int len;
            // 将输入流转移到内存输出流中
            while ((len = input.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }
            // 将内存流转换为字符串
            String jsonStr = new String(out.toByteArray());
            if (SUBJECT_ONE_TXT.equalsIgnoreCase(subjectName)) {
                ArrayList<SubjectOneBean> list = JSON.parseObject(jsonStr, new TypeReference<ArrayList<SubjectOneBean>>() {
                });
                for (SubjectOneBean bean : list) {
                    mDbUtils.save(bean);
                }
            } else if (Constant.SUBJECT_FOUR_TXT.equalsIgnoreCase(subjectName)) {
                ArrayList<SubjectFourBean> list = JSON.parseObject(jsonStr, new TypeReference<ArrayList<SubjectFourBean>>() {
                });
                for (SubjectFourBean bean : list) {
                    mDbUtils.save(bean);
                }
            }
            input.close();
            // 获取解析出来的数据
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void queryData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<SubjectOneBean> allList = mDbUtils.findAll(SubjectOneBean.class);
                    if (allList != null) {
                        LogUtil.e(TAG, "科目一所有题目一共有 " + allList.size() + " 条数据-->>>");
                    }
                    List<SubjectOneBean> list = mDbUtils.findAll(Selector.from(SubjectOneBean.class).where("subject_type", "=", Constant.SUBJECT_CHOICE_TYPE));
                    if (list != null) {
                        LogUtil.e(TAG, "科目一选择题一共有 " + list.size() + " 条数据-->>>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    List<SubjectFourBean> allList = mDbUtils.findAll(SubjectFourBean.class);
                    if (allList != null) {
                        LogUtil.e(TAG, "科目四所有题目一共有 " + allList.size() + " 条数据-->>>");
                    }
                    List<SubjectFourBean> list = mDbUtils.findAll(Selector.from(SubjectFourBean.class).where("subject_type", "=", Constant.SUBJECT_CHOICE_TYPE));
                    if (list != null) {
                        LogUtil.e(TAG, "科目四选择题一共有 " + list.size() + " 条数据-->>>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).run();
    }
}