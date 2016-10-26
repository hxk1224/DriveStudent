/**
 * @author 韩新凯
 * @version 创建时间：2015-3-12 上午10:16:20
 */

package com.drive.student.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.CustomToast;
import com.drive.student.util.localphoto.SelectLocalPhotoActivity;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectPicPopupWindow extends ActivitySupport implements OnClickListener {

    private static final String TAG = "SelectPicActivity";

    /************* 使用照相机拍照获取图片 *****************/
    public static final int SELECT_PIC_BY_TACK_PHOTO = 2001;

    /************* 使用相册中的图片 *****************/
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2002;

    /************* 去剪裁的图片 *****************/
    public static final int CUP_PIC_CODE = 2003;

    /**从Intent获取图片路径的KEY**/
    public static final String KEY_PHOTO_PATH = "photo_path";

    /**弹出层**/
    private LinearLayout dialogLayout;

    /**拍照\选择图库\取消**/
    private Button takePhotoBtn, pickPhotoBtn, cancelBtn;

    /**上一个Intent**/
    private Intent lastIntent;

    /**是否是头像**/
    private boolean isHeadIcon;

    /**默认压缩质量**/
    private int quity = 40;

    /**照片存放路径目录**/
    private String cameraTempPathDir = null;

    /**拍照文件绝对路径**/
    private File cameraTempFile = null;

    /**拍照截取文件绝对路径**/
    private File cutTempFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_pic_dialog);
        dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
        dialogLayout.setOnClickListener(this);
        takePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
        takePhotoBtn.setOnClickListener(this);
        pickPhotoBtn = (Button) findViewById(R.id.btn_pick_photo);
        pickPhotoBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
        lastIntent = getIntent();
        isHeadIcon = getIntent().getBooleanExtra("key_head_icon", false);
        cameraTempPathDir = spUtil.getCameraTempPath();
        if (isHeadIcon) {
            quity = 50;
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
                    StatService.onEvent(SelectPicPopupWindow.this, "takepic", "拍照");
                    MobclickAgent.onEvent(SelectPicPopupWindow.this, "takepic", "拍照");
                    takePhoto();
                } else {
                    CustomToast.showToast(SelectPicPopupWindow.this, "请检查有无可用存储卡", Toast.LENGTH_LONG);
                }
                break;
            case R.id.btn_pick_photo:
                if (checkSDCard()) {
                    pickPhoto();
                } else {
                    CustomToast.showToast(SelectPicPopupWindow.this, "请检查有无可用存储卡", Toast.LENGTH_LONG);
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
     * @throws IOException
     */
    private void takePhoto() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
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
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        Uri outputFileUri = Uri.fromFile(cameraTempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        spUtil.setTempQuizImg(cameraTempFile.getAbsolutePath());
        startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setClass(SelectPicPopupWindow.this, SelectLocalPhotoActivity.class);
        // 跳转到相册
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
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
                    spUtil.setTempQuizImg(null);
                    finish();
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            } else if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {// 选择图库
                String selePicPath = data.getStringExtra("LocalPhoto");// 获取本地选择到的图片
                Intent intent = new Intent();
                intent.setClass(SelectPicPopupWindow.this, CropperView.class);
                intent.putExtra("PhotoType", 2);
                intent.putExtra("CropperTempPath", selePicPath);
                // 跳转到裁剪页面
                startActivityForResult(intent, CUP_PIC_CODE);
                // // 对选择好的照片进行压缩操作
                // new CompressTask(new File(selePicPath),
                // CompressTask.PHOTO_TYPE).execute();
            } else if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {// 拍照

                Intent intent = new Intent();
                intent.setClass(SelectPicPopupWindow.this, CropperView.class);
                intent.putExtra("CropperTempPath", cameraTempFile.getAbsolutePath());
                intent.putExtra("PhotoType", 1);
                // 跳转到裁剪页面
                startActivityForResult(intent, CUP_PIC_CODE);

                // // 对拍好的照片进行压缩操作
                // new CompressTask(cameraTempFile,
                // CompressTask.CAMERA_TYPE).execute();
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

    // private Button btn_take_photo, btn_pick_photo, btn_cancel;
    // private View mMenuView;
    //
    // public SelectPicPopupWindow(Activity context, OnClickListener
    // itemsOnClick) {
    // super(context);
    // LayoutInflater inflater = (LayoutInflater)
    // context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    // mMenuView = inflater.inflate(R.layout.add_question_pic_dialog, null);
    // btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);
    // btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
    // btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
    // //取消按钮
    // btn_cancel.setOnClickListener(new OnClickListener() {
    //
    // public void onClick(View v) {
    // //销毁弹出框
    // dismiss();
    // }
    // });
    // //设置按钮监听
    // btn_pick_photo.setOnClickListener(itemsOnClick);
    // btn_take_photo.setOnClickListener(itemsOnClick);
    // //设置SelectPicPopupWindow的View
    // this.setContentView(mMenuView);
    // //设置SelectPicPopupWindow弹出窗体的宽
    // this.setWidth(LayoutParams.FILL_PARENT);
    // //设置SelectPicPopupWindow弹出窗体的高
    // this.setHeight(LayoutParams.WRAP_CONTENT);
    // //设置SelectPicPopupWindow弹出窗体可点击
    // this.setFocusable(true);
    // //设置SelectPicPopupWindow弹出窗体动画效果
    // this.setAnimationStyle(R.style.AnimBottom);
    // //实例化一个ColorDrawable颜色为半透明
    // ColorDrawable dw = new ColorDrawable(0xb0000000);
    // //设置SelectPicPopupWindow弹出窗体的背景
    // this.setBackgroundDrawable(dw);
    // //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
    // mMenuView.setOnTouchListener(new OnTouchListener() {
    //
    // public boolean onTouch(View v, MotionEvent event) {
    //
    // int height = mMenuView.findViewById(R.id.dialog_layout).getTop();
    // int y = (int) event.getY();
    // if (event.getAction() == MotionEvent.ACTION_UP) {
    // if (y < height) {
    // dismiss();
    // }
    // }
    // return true;
    // }
    // });
    //
    // }

}
