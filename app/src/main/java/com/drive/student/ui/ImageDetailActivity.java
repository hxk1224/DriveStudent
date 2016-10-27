package com.drive.student.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.config.UrlConfig;
import com.drive.student.util.BitmapHelp;
import com.drive.student.util.BitmapIncise;
import com.drive.student.util.CustomToast;
import com.drive.student.util.FileUtil;
import com.drive.student.util.StringUtil;
import com.drive.student.view.LoadingDialog;
import com.drive.student.xutils.BitmapUtils;
import com.drive.student.xutils.ViewUtils;
import com.drive.student.xutils.bitmap.BitmapDisplayConfig;
import com.drive.student.xutils.bitmap.callback.BitmapLoadFrom;
import com.drive.student.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.drive.student.xutils.cache.FileNameGenerator;
import com.drive.student.xutils.cache.MD5FileNameGenerator;
import com.drive.student.xutils.view.annotation.ViewInject;

import org.json.JSONException;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

/***
 * 展现图片详情的界面
 */
public class ImageDetailActivity extends ActivitySupport implements OnViewTapListener {
    /** 加载的progressBar */
    @ViewInject(R.id.detail_img_progressBar)
    private ProgressBar detail_img_progressBar;
    private ImageView detail_img_srcImage;
    /** 向左的按钮 */
    @ViewInject(R.id.btn_turn_left)
    private ImageButton btn_turn_left;
    /** 向右的按钮 */
    @ViewInject(R.id.btn_turn_right)
    private ImageButton btn_turn_right;
    private PhotoViewAttacher mAttacher;
    /** 保存 */
    @ViewInject(R.id.save_gallery_bt)
    private Button save_gallery_bt;

    private String mImageUrl;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail);
        ViewUtils.inject(this);
        loadingDialog = getProgressDialog();
        BitmapUtils bitmapUtils = BitmapHelp.getPhotoBitmap(getApplicationContext());
        Intent intent = getIntent();
        mImageUrl = intent.getStringExtra(Constant.IMAGE_DETAIL_PATH);
        initViews();
        if (mImageUrl != null) {
            detail_img_progressBar.setVisibility(View.VISIBLE);
            bitmapUtils.display(detail_img_srcImage, mImageUrl, new DefaultBitmapLoadCallBack<ImageView>() {

                @Override
                public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
                    super.onLoadCompleted(container, uri, bitmap, config, from);
                    save_gallery_bt.setVisibility(View.VISIBLE);
                    detail_img_progressBar.setVisibility(View.GONE);
                    btn_turn_left.setEnabled(true);
                    btn_turn_right.setEnabled(true);
                    mAttacher.update();
                }

                @Override
                public void onLoadFailed(ImageView container, String uri, Drawable drawable) {
                    super.onLoadFailed(container, uri, drawable);
                    save_gallery_bt.setVisibility(View.GONE);
                }

            });
        } else {
            finish();
        }
    }

    private void initViews() {
        View header = findViewById(R.id.header);
        TextView header_tv_back = (TextView) header.findViewById(R.id.header_tv_back);
        header_tv_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageDetailActivity.this.finish();
            }
        });
        TextView header_tv_title = (TextView) header.findViewById(R.id.header_tv_title);
        header_tv_title.setText("查看图片");

        detail_img_srcImage = (ImageView) findViewById(R.id.detail_img_srcImage);
        btn_turn_left.setOnClickListener(listener);
        btn_turn_right.setOnClickListener(listener);
        save_gallery_bt.setOnClickListener(listener);
        /** 加载中默认不可点击 */
        btn_turn_left.setEnabled(false);
        btn_turn_right.setEnabled(false);
        mAttacher = new PhotoViewAttacher(detail_img_srcImage);
        mAttacher.setScaleType(ScaleType.FIT_CENTER);
        mAttacher.setOnViewTapListener(this);
    }

    /** 向左、向右的单击事件 */
    public OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            BitmapDrawable bd = (BitmapDrawable) detail_img_srcImage.getDrawable();
            int degree;
            switch (v.getId()) {
                case R.id.btn_turn_left:// 向左的按钮
                    degree = -90;
                    BitmapIncise.rotaingImageView(detail_img_srcImage, degree, bd.getBitmap());
                    mAttacher.update();
                    break;
                case R.id.btn_turn_right:// 向右的按钮
                    degree = 90;
                    BitmapIncise.rotaingImageView(detail_img_srcImage, degree, bd.getBitmap());
                    mAttacher.update();
                    break;
                case R.id.save_gallery_bt:// 保存
                    savaPictureToGallery();
                    break;
            }
        }
    };

    private void savaPictureToGallery() {
        if (!StringUtil.isBlank(mImageUrl)) {
            FileNameGenerator generator = new MD5FileNameGenerator();
            String fileName = generator.generate(mImageUrl);
            String filePath = spUtil.getBitmapCachePath() + fileName + ".0";
            if (new File(filePath).exists()) {
                new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected void onPreExecute() {
                        loadingDialog.show();
                        save_gallery_bt.setEnabled(false);
                    }

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        FileUtil.savePictureToGallery(ImageDetailActivity.this, filePath);
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean res) {
                        loadingDialog.dismiss();
                        showToastInThread("图片保存成功!");
                        save_gallery_bt.setEnabled(true);
                    }
                }.execute();
            } else {
                showToastInThread("图片正在下载!");
            }
        } else {
            showToastInThread("图片保存失败!");
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * 单击屏幕，activity消失
     */
    @Override
    public void onViewTap(View arg0, float arg1, float arg2) {
        ImageDetailActivity.this.finish();
    }
}
