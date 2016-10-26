package com.drive.student.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;
import com.umeng.analytics.MobclickAgent;
import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.util.localphoto.SelectLocalPhotoActivity;
import com.drive.student.util.localphoto.SelectLocalPhotoListActivity;
import com.drive.student.view.CropperView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * 选择文件操作类.
 * 选择多张图片添加代码：
 * //是否选择多张图片
 * intent.putExtra("SELECT_MULTIPLE",true);
 */

public class SelectPicActivity extends ActivitySupport implements OnClickListener {
    private static final String TAG = "SelectPicActivity";

    /************* 使用照相机拍照获取图片 *****************/
    public static final int SELECT_PIC_BY_TACK_PHOTO = 2001;

    /************* 使用相册中的单张图片 *****************/
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2002;

    /************* 使用相册中的多张图片 *****************/
    public static final int SELECT_PIC_BY_PICK_PHOTO_LIST = 2000;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_pic_layout);
        dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
        dialogLayout.setOnClickListener(this);
        Button takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
        takePhotoBtn.setOnClickListener(this);
        Button pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photo);
        pickPhotoBtn.setOnClickListener(this);
        Button cancelBtn = (Button) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
        lastIntent = getIntent();
        cameraTempPathDir = spUtil.getCameraTempPath();

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_layout:
                setResult(Activity.RESULT_CANCELED, lastIntent);
                finish();
                break;
            case R.id.btn_take_photo:
                if (checkSDCard()) {
                    StatService.onEvent(SelectPicActivity.this, "takepic", "拍照");
                    MobclickAgent.onEvent(SelectPicActivity.this, "takepic", "拍照");
                    takePhoto();
                } else {
                    showToastInThread("请检查有无可用存储卡");
                }
                break;
            case R.id.btn_pick_photo:
                if (checkSDCard()) {
                    boolean multiple = getIntent().getBooleanExtra("SELECT_MULTIPLE", false);
                    if (multiple) {
                        pickPhotoList();
                    } else {
                        pickPhoto();
                    }
                } else {
                    showToastInThread("请检查有无可用存储卡");
                }
                break;
            default:
                setResult(Activity.RESULT_CANCELED, lastIntent);
                finish();
                break;
        }
    }

    /** 拍照获取图片 */
    private void takePhoto() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        File dirFile = new File(cameraTempPathDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
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

    /** 从相册中取单张图片 */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setClass(SelectPicActivity.this, SelectLocalPhotoActivity.class);
        // 跳转到相册
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    /** 从相册中取多张图片 */
    private void pickPhotoList() {
        Intent intent = new Intent();
        intent.setClass(SelectPicActivity.this, SelectLocalPhotoListActivity.class);
        intent.putExtra("Max_Pic_Count", getIntent().getIntExtra("Max_Pic_Count", Constant.MAX_UPLOAD_PIC_SIZE));
        // 跳转到相册
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO_LIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialogLayout.setVisibility(View.GONE);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CUP_PIC_CODE) {// 剪裁返回
                String cutTempPath = data.getStringExtra("CutTempPath");// 获取剪裁后的文件路径
                File cutTempFile = new File(cutTempPath);
                if (cutTempFile.exists()) {// 如果剪裁后的文件存在
                    lastIntent.putExtra(Constant.KEY_UPLOAD_PIC_PATH, cutTempPath);
                    setResult(Activity.RESULT_OK, lastIntent);
                    finish();
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            } else if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {// 选择图库-单张图片
                String selePicPath = data.getStringExtra("LocalPhoto");// 获取本地选择到的图片
                Intent intent = new Intent();
                intent.setClass(SelectPicActivity.this, CropperView.class);
                intent.putExtra("PhotoType", 2);
                intent.putExtra("CropperTempPath", selePicPath);
                // 跳转到裁剪页面
                startActivityForResult(intent, CUP_PIC_CODE);
//				// 对选择好的照片进行压缩操作
//				new CompressTask(new File(selePicPath), CompressTask.PHOTO_TYPE).execute();
            } else if (requestCode == SELECT_PIC_BY_PICK_PHOTO_LIST) {// 选择图库-多张图片
                ArrayList<String> photoList = data.getStringArrayListExtra("LocalPhotoList");// 获取本地选择到的图片
                lastIntent.putExtra(Constant.KEY_UPLOAD_PIC_PATH_LIST, photoList);
                setResult(Activity.RESULT_OK, lastIntent);
                finish();
            } else if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {// 拍照
                Intent intent = new Intent();
                intent.setClass(SelectPicActivity.this, CropperView.class);
                intent.putExtra("CropperTempPath", cameraTempFile.getAbsolutePath());
                intent.putExtra("PhotoType", 1);
                // 跳转到裁剪页面
                startActivityForResult(intent, CUP_PIC_CODE);
//				// 对拍好的照片进行压缩操作
//				new CompressTask(cameraTempFile, CompressTask.CAMERA_TYPE).execute();
            }
        } else {// 用户取消退出
            setResult(Activity.RESULT_CANCELED, lastIntent);
            finish();
        }
    }

}
