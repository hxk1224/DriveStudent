package com.drive.student.task;

import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.drive.student.config.Constant;
import com.drive.student.config.UrlConfig;
import com.drive.student.dto.CommonDTO;
import com.drive.student.http.HttpPoolingManager;
import com.drive.student.util.LogUtil;
import com.drive.student.util.SharePreferenceUtil;

import org.json.JSONObject;

/**
 * 聊天常用语任务.
 */
public class LoadCommonTextTask extends AsyncTask<String, Void, String> {
    public static final String TAG = "LoadCommonTextTask";
    private final SharePreferenceUtil spUtil;


    public LoadCommonTextTask(Context context) {
        spUtil = new SharePreferenceUtil(context);
    }

    @Override
    protected String doInBackground(String... params) {
        CommonDTO dto = new CommonDTO(UrlConfig.CHAT_COMMEN_TEXT_CODE);
        String content = JSON.toJSONString(dto);
        String result = HttpPoolingManager.sendHttpPost(UrlConfig.ZASION_HOST, content);
        LogUtil.e(TAG, "LoadCommonTextTask resultJson === " + result);
        return result;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONObject head = obj.getJSONObject("head");
            int returnCode = head.getInt("returnCode");
            if (Constant.RETURN_CODE_OK == returnCode) {
                String data = obj.getString("data");
                spUtil.setChatCommonText(data);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
