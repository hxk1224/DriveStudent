package com.drive.student.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.drive.student.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class HeadIconUtil {

    private static final String TAG = "HeadIconUtil";
    private String filepath;// 存在本地的头像数据路径
    private Context context;
    private SharePreferenceUtil sp;

    public HeadIconUtil(Context context) {
        this.context = context;
        sp = new SharePreferenceUtil(context);
        filepath = sp.getBasePath() + context.getResources().getString(R.string.headicon);
        Log.e(TAG, "filePath = " + filepath);
    }

    // bitmap 工具 取头像的
    public Bitmap getFileBitmap(String bitmapName, Bitmap bitmap, String userId) {

        File file1 = new File(filepath + userId + "/");
        Bitmap decodeStream = null;
        Bitmap bitmap1;
        if (!file1.exists()) {
            file1.mkdirs();
        }
        File f = new File(filepath + userId + "/" + "headicon.jpg");
        if (!f.exists()) {
            BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.drawable.head_default_icon);
            bitmap1 = bd.getBitmap();
            return bitmap1;
        }
        try {
            FileInputStream fis = new FileInputStream(f);
            decodeStream = BitmapFactory.decodeStream(fis);
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return decodeStream;
    }

    // bitmap 工具 存头像的
    public void saveFileBitmap(String bitmapName, Bitmap bitmap, String userId) {

        File file1 = new File(filepath + userId + "/");
        if (!file1.exists()) {
            file1.mkdirs();
        }
        File f = new File(filepath + userId + "/" + "headicon");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(f);
            // 30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0
            bitmap.compress(CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}
