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
import com.drive.student.bean.Head;
import com.drive.student.config.Constant;
import com.drive.student.config.UrlConfig;
import com.drive.student.dto.CommonDTO;
import com.drive.student.http.HttpTransferCallBack;
import com.drive.student.http.HttpTransferUtil;
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
    private int fromType;
    private String mPicId;
    private int mDeleteRequestCode;
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
        mPicId = intent.getStringExtra(Constant.IMAGE_DETAIL_ID);
        fromType = intent.getIntExtra(Constant.IMAGE_DETAIL_FROM_TYPE, 0);
        mDeleteRequestCode = intent.getIntExtra(Constant.IMAGE_DETAIL_DELETE_REQUEST_CODE, 0);
        boolean showDelete = intent.getBooleanExtra(Constant.IMAGE_DETAIL_SHOW_DELETE, false);
        initViews(showDelete);
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

    private void initViews(boolean showDelete) {
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
        TextView header_tv_right = (TextView) header.findViewById(R.id.header_tv_right);
        header_tv_right.setText("删除");
        if (showDelete) {
            header_tv_right.setVisibility(View.VISIBLE);
        } else {
            header_tv_right.setVisibility(View.GONE);
        }
        header_tv_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

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

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定删除图片?").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Constant.IMAGE_DETAIL_FROM_TAKE_PHOTO == fromType) {
                    if (!StringUtil.isBlank(mPicId) && mDeleteRequestCode > 0) {
                        deleteImage();
                    } else {
                        Intent data = new Intent();
                        data.putExtra(Constant.DELETE_PIC_URL, mImageUrl);
                        setResult(Activity.RESULT_OK, data);
                        ImageDetailActivity.this.finish();
                    }
                } else if (Constant.IMAGE_DETAIL_FROM_IMAGE_WATCHER == fromType) {
                    deleteImage();
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
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

    private void deleteImage() {
        if (!hasInternetConnected()) {
            return;
        }
        CommonDTO dto = new CommonDTO(mDeleteRequestCode);
        dto.addParam("picId", mPicId);
        String content = JSON.toJSONString(dto);
        new HttpTransferUtil().sendHttpPost(UrlConfig.ZASION_HOST, content, new HttpTransferCallBack() {

            @Override
            public void onStart() {
                loadingDialog.show();
            }

            @Override
            public void onSuccess(String json) {
                loadingDialog.dismiss();
                if (!validateToken(json)) {
                    return;
                }
                try {
                    Head head = checkHead(json);
                    if (head.returnCode == Constant.RETURN_CODE_OK) {
                        CustomToast.showToast(ImageDetailActivity.this, "删除成功", 0);
                        Intent data = new Intent();
                        data.putExtra(Constant.DELETE_PIC_URL, mImageUrl);
                        setResult(Activity.RESULT_OK, data);
                        ImageDetailActivity.this.finish();
                    } else {
                        CustomToast.showToast(ImageDetailActivity.this, head.message, 0);
                    }
                } catch (JSONException e) {
                    CustomToast.showToast(ImageDetailActivity.this, "删除失败", 0);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure() {
                loadingDialog.dismiss();
                CustomToast.showToast(ImageDetailActivity.this, getString(R.string.server_net_error), 0);
            }

        });

    }
}
