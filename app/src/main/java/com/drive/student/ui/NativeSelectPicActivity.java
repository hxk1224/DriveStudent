package com.drive.student.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.umeng.analytics.MobclickAgent;
import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.util.CustomToast;
import com.drive.student.util.NativeImageUtils;
import com.drive.student.util.localphoto.SelectLocalPhotoActivity;
import com.drive.student.xutils.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 选择文件操作类.
 */
public class NativeSelectPicActivity extends ActivitySupport implements OnClickListener {
    /************* 使用照相机拍照获取图片 *****************/
    public static final int SELECT_PIC_BY_TACK_PHOTO = 2001;
    /************* 使用相册中的图片 *****************/
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2002;
    /************* 去剪裁的图片 *****************/
    public static final int CUP_PIC_CODE = 2003;

    /** 弹出层 **/
    private LinearLayout dialogLayout;
    /** 上一个Intent **/
    private Intent lastIntent;
    /** 照片存放路径目录 **/
    private String cameraTempPathDir = null;
    /** 拍照文件绝对路径 **/
    private File cameraTempFile = null;
    /** 拍照截取文件绝对路径 **/
    private File cutTempFile = null;
    String cutTempPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_pic_layout);
        dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
        dialogLayout.setOnClickListener(this);
        /** 拍照 **/
        Button takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
        takePhotoBtn.setOnClickListener(this);
        /** 选择图库 **/
        Button pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photo);
        pickPhotoBtn.setOnClickListener(this);
        /** 取消 **/
        Button cancelBtn = (Button) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
        lastIntent = getIntent();
        cameraTempPathDir = spUtil.getCameraTempPath();
        File dirFile = new File(cameraTempPathDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        if (savedInstanceState != null) {
            String pathCamera = savedInstanceState.getString("cameraTempPath");
            if (pathCamera != null && !pathCamera.equals("")) {
                cameraTempFile = new File(pathCamera);
            }
            String pathCut = savedInstanceState.getString("cutTempPath");
            if (pathCut != null && !pathCut.equals("")) {
                cutTempFile = new File(pathCut);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (cameraTempFile != null && cameraTempFile.exists()) {
            outState.putString("cameraTempPath", cameraTempFile.getAbsolutePath());
        }

        if (cutTempFile != null && cutTempFile.exists()) {
            outState.putString("cutTempPath", cutTempFile.getAbsolutePath());
        }
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_layout:
                setResult(Activity.RESULT_CANCELED, lastIntent);
                finish();
                break;
            case R.id.btn_take_photo:
                if (checkSDCard()) {
                    StatService.onEvent(NativeSelectPicActivity.this, "takepic", "拍照");
                    MobclickAgent.onEvent(NativeSelectPicActivity.this, "takepic", "拍照");
                    takePhoto();
                } else {
                    CustomToast.showToast(NativeSelectPicActivity.this, "请检查有无可用存储卡", Toast.LENGTH_LONG);
                }
                break;
            case R.id.btn_pick_photo:
                if (checkSDCard()) {
                    pickPhoto();
                } else {
                    CustomToast.showToast(NativeSelectPicActivity.this, "请检查有无可用存储卡", Toast.LENGTH_LONG);
                }
                break;
            default:
                setResult(Activity.RESULT_CANCELED, lastIntent);
                finish();
                break;
        }
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());

        cameraTempFile = new File(cameraTempPathDir + timeStamp + ".jpg");
        if (!cameraTempFile.exists()) {
            try {
                cameraTempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri outputFileUri = Uri.fromFile(cameraTempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setClass(NativeSelectPicActivity.this, SelectLocalPhotoActivity.class);
        // 跳转到相册
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialogLayout.setVisibility(View.GONE);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CUP_PIC_CODE) {// 剪裁返回
                if (NativeImageUtils.cropImageUri != null) {
                    // 可以直接显示图片,或者进行其他处理(如压缩等)
                    LogUtils.i("部分手机存在剪切到的图片打不开,例如中兴");
                    cutTempPath = getRealPathFromURI(NativeImageUtils.cropImageUri);
                    lastIntent.putExtra(Constant.KEY_UPLOAD_PIC_PATH, cutTempPath);
                    setResult(Activity.RESULT_OK, lastIntent);
                    finish();
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            } else if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {// 选择图库
                String selePicPath = data.getStringExtra("LocalPhoto");// 获取本地选择到的图片
                NativeImageUtils.cropImage(this, Uri.fromFile(new File(selePicPath)));
            } else if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {// 拍照
                NativeImageUtils.cropImage(this, Uri.fromFile(cameraTempFile));

            }
        } else {// 用户取消退出
            setResult(Activity.RESULT_CANCELED, lastIntent);
            finish();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }
}
