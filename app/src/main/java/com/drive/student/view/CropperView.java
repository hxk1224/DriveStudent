package com.drive.student.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.umeng.analytics.MobclickAgent;
import com.drive.student.R;
import com.drive.student.ui.ActivitySupport;
import com.drive.student.util.BitmapCompressUtil;
import com.drive.student.util.SharePreferenceUtil;
import com.drive.student.util.cropper.CropImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 图片剪裁.
 *
 * @author 韩新凯
 */
public class CropperView extends ActivitySupport {

    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final int ROTATE_NINETY_DEGREES_RIGHT = 90;
    private static final int ROTATE_NINETY_DEGREES_LEFT = -90;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private Intent lastIntent;

    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
    /** 剪裁后的图片 **/
    private Bitmap croppedImage;
    /** 需要裁剪的文件路径 **/
    private String cropperTempPath;
    /** 压缩后的新路径 **/
    private String compressPath;
    /** 待剪切图片方位 **/
    public int cropperTempDigree = 0;
    /** 向右/向左旋转按钮 **/
    private ImageView rotateButtonLeft, rotateButtonRight;
    /** 待剪裁图片 **/
    private CropImageView cropImageView;
    /** 旋转按钮 **/
    private ImageView cropButton;
    /** 图片来源类型 **/
    private int photoType;
    /** 拍照得到的图 **/
    public static final int CAMERA_TYPE = 1;
    /** 图库选择的图 **/
    public static final int PHOTO_TYPE = 2;

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cropimage_view);
        lastIntent = getIntent();
        cropperTempPath = lastIntent.getStringExtra("CropperTempPath");
        photoType = lastIntent.getIntExtra("PhotoType", 2);

        // Initialize components of the app
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        new CompressTask(new File(cropperTempPath), photoType).execute();
//		cropImageView.setImageBitmap(loadBitmap(cropperTempPath, true));
        // Sets initial aspect ratio to 10/10, for demonstration purposes
//		cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

        // 向右旋转
        rotateButtonRight = (ImageView) findViewById(R.id.button_rotate_right);
        rotateButtonRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rotateButtonRight.setEnabled(false);
                rotateButtonLeft.setEnabled(false);
                cropButton.setEnabled(false);
                cropImageView.rotateImage(ROTATE_NINETY_DEGREES_RIGHT);
                rotateButtonRight.setEnabled(true);
                cropButton.setEnabled(true);
                rotateButtonLeft.setEnabled(true);
            }
        });

        // 向左旋转
        rotateButtonLeft = (ImageView) findViewById(R.id.button_rotate_left);
        rotateButtonLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rotateButtonRight.setEnabled(false);
                rotateButtonLeft.setEnabled(false);
                cropButton.setEnabled(false);
                cropImageView.rotateImage(ROTATE_NINETY_DEGREES_LEFT);
                rotateButtonRight.setEnabled(true);
                cropButton.setEnabled(true);
                rotateButtonLeft.setEnabled(true);
            }
        });

        // 裁剪
        cropButton = (ImageView) findViewById(R.id.Button_crop);
        cropButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cropButton.setEnabled(false);
                rotateButtonLeft.setEnabled(false);
                rotateButtonRight.setEnabled(false);
                if (photoType == CAMERA_TYPE) {
                    StatService.onEvent(CropperView.this, "takepic_ok", "拍照-确认");
                    MobclickAgent.onEvent(CropperView.this, "takepic_ok", "拍照-确认");
                } else {
                    StatService.onEvent(CropperView.this, "selectpic_ok", "选择图库-确认");
                    MobclickAgent.onEvent(CropperView.this, "selectpic_ok", "选择图库-确认");
                }
                new AsyncTask<String, Integer, File>() {
                    com.drive.student.view.LoadingDialog myDilaDialog = null;

                    @Override
                    protected File doInBackground(String... arg0) {
                        croppedImage = cropImageView.getCroppedImage();
                        File cutTempFile = saveBitmapFile(croppedImage);
                        return cutTempFile;
                    }

                    @Override
                    protected void onPostExecute(File cutTempFile) {
                        try {
                            myDilaDialog.dismiss();
                            // 拍摄的原图要删除
                            if (photoType == CAMERA_TYPE) {
                                File cropperTempFile = new File(cropperTempPath);
                                if (cropperTempFile.exists()) {
                                    cropperTempFile.delete();
                                }
                            }
                            // 压缩后的原图要删除
                            File compressFile = new File(compressPath);
                            if (compressFile.exists()) {
                                compressFile.delete();
                            }
                            lastIntent.putExtra("CutTempPath", cutTempFile.getAbsolutePath());
                            setResult(Activity.RESULT_OK, lastIntent);
                            finish();
                        } catch (Exception e) {
                            return;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        myDilaDialog = com.drive.student.view.LoadingDialog.createDialog(CropperView.this);
                        myDilaDialog.setCancelable(false);
                        myDilaDialog.setMessage("正在保存中....");
                        myDilaDialog.show();
                    }
                }.execute();
            }
        });
    }

    /**
     * 对拍好的照片进行压缩.
     *
     * @author 韩新凯
     */
    class CompressTask extends AsyncTask<Void, Integer, Void> {
        private com.drive.student.view.LoadingDialog myDilaDialog = null;

        /** 待压缩图片 **/
        private File tempFile;

        /** 图片来源类型 **/
        private int photoType;

        public CompressTask(File tempFile, int photoType) {
            this.tempFile = tempFile;
            this.photoType = photoType;
        }

        @Override
        protected void onPreExecute() {
            myDilaDialog = com.drive.student.view.LoadingDialog.createDialog(CropperView.this);
            myDilaDialog.setCancelable(false);
            myDilaDialog.setMessage("正在载入中....");
            myDilaDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // 执行压缩操作
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
            SharePreferenceUtil spUtil = new SharePreferenceUtil(CropperView.this);
            String newTempPath = spUtil.getCameraTempPath() + timeStamp + "_s.jpg";
            cropperTempDigree = getBitmapDigree(tempFile.getAbsolutePath());
            compressPath = BitmapCompressUtil.getSmallBitmapAndSave(tempFile.getAbsolutePath(), newTempPath, 100, 60);
            if (null == compressPath) {
                compressPath = tempFile.getAbsolutePath();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                myDilaDialog.dismiss();
            } catch (Exception e) {
                return;
            }

            if (compressPath != null) {
                cropImageView.setImageBitmap(loadMatrixBitmap(compressPath, true));
                cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

                // 压缩完成后将拍摄的原图删除掉
                if (photoType == CAMERA_TYPE) {
                    try {
                        if (tempFile != null && tempFile.exists()) {
                            tempFile.delete();
                        }
                    } catch (Exception e) {
                    }
                }
            } else {// 如果没有压缩成功，就原图返回
                cropImageView.setImageBitmap(loadMatrixBitmap(tempFile.getAbsolutePath(), true));
                cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
//				setResult(Activity.RESULT_CANCELED);
//				finish();
            }
        }
    }

    /** 从给定的路径加载图片，并指定是否自动旋转方向 */
    public int getBitmapDigree(String imgpath) {
        int digree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imgpath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        if (exif != null) {
            // 读取图片中相机方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }
        return digree;
    }

    /** 从给定路径加载图片 */
    public static Bitmap loadBitmap(String imgpath) {
        return BitmapFactory.decodeFile(imgpath);
    }

    /** 从给定的路径加载图片，并指定是否自动旋转方向 */
    public Bitmap loadMatrixBitmap(String imgpath, boolean adjustOritation) {
        if (!adjustOritation) {
            return loadBitmap(imgpath);
        } else {
            Bitmap reBm = null;
            Bitmap bm = loadBitmap(imgpath);
            if (cropperTempDigree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(cropperTempDigree);
                reBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            }
            if (null == reBm) {
                return bm;
            }
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
            return reBm;
        }
    }

    /**
     * 这里关闭.
     *
     * @param view
     * @author 韩新凯
     * @update 2014年11月5日 下午3:19:10
     */
    public void closeButton(View view) {
        if (photoType == CAMERA_TYPE) {
            StatService.onEvent(CropperView.this, "takepic_cancel", "拍照-取消");
            MobclickAgent.onEvent(CropperView.this, "takepic_cancel", "拍照-取消");
        } else {
            StatService.onEvent(CropperView.this, "selectpic_cancel", "选择图库-取消");
            MobclickAgent.onEvent(CropperView.this, "selectpic_cancel", "选择图库-取消");
        }
        setResult(Activity.RESULT_CANCELED, lastIntent);
        finish();
    }

    /**
     * 将bitmap保存至本地.
     *
     * @param bitmap
     * @return
     * @author 韩新凯
     * @update 2014年11月5日 下午3:03:54
     */
    public File saveBitmapFile(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        SharePreferenceUtil spUtil = new SharePreferenceUtil(this);
        File cutTempFile = new File(spUtil.getCameraTempPath() + timeStamp + "_s.jpg");
        if (cutTempFile.exists()) {
            cutTempFile.delete();
        }
        try {
            cutTempFile.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cutTempFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {

        }
        String newTempPath2 = spUtil.getCameraTempPath() + System.currentTimeMillis() + "_s.jpg";
        String cutTempFile2 = BitmapCompressUtil.getSmallBitmapAndSave(cutTempFile.getAbsolutePath(), newTempPath2, 100, 100);
        if (cutTempFile2 != null && new File(cutTempFile2).exists() && new File(cutTempFile2).length() > 0) {
            cutTempFile.delete();
            return new File(cutTempFile2);
        }
        return cutTempFile;
    }
}
