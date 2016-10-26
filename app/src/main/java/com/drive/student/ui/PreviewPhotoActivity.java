/**
 * @author 韩新凯
 * @version 创建时间：2014-7-8 下午1:35:57
 */

package com.drive.student.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.drive.student.R;
import com.drive.student.config.Constant;
import com.drive.student.util.BitmapHelp;
import com.drive.student.util.BitmapIncise;
import com.drive.student.xutils.BitmapUtils;
import com.drive.student.xutils.ViewUtils;
import com.drive.student.xutils.bitmap.BitmapDisplayConfig;
import com.drive.student.xutils.bitmap.callback.BitmapLoadFrom;
import com.drive.student.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.drive.student.xutils.view.annotation.ViewInject;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

/**
 * 图片预览.
 */
public class PreviewPhotoActivity extends ActivitySupport implements OnViewTapListener {
    /** 加载的progressBar */
    @ViewInject(R.id.detail_img_progressBar)
    private ProgressBar detail_img_progressBar;
    /** 显示的大图片 */
    @ViewInject(R.id.detail_img_srcImage)
    private ImageView detail_img_srcImage;
    /** 向左的按钮 */
    @ViewInject(R.id.btn_turn_left)
    private ImageButton btn_turn_left;
    /** 向右的按钮 */
    @ViewInject(R.id.btn_turn_right)
    private ImageButton btn_turn_right;
    /** 删除按钮 **/
    @ViewInject(R.id.btn_delete)
    private ImageButton deleteButton;
    private PhotoViewAttacher mAttacher;
    private Intent intentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail);
        ViewUtils.inject(this);
        intentType = getIntent();
        deleteButton.setVisibility(View.VISIBLE);
        btn_turn_left.setOnClickListener(listener);
        btn_turn_right.setOnClickListener(listener);
        deleteButton.setOnClickListener(listener);
        /** 加载中默认不可点击 */
        btn_turn_left.setEnabled(false);
        btn_turn_right.setEnabled(false);
        deleteButton.setEnabled(false);
        BitmapUtils wentiBitmapUtils = BitmapHelp.getPhotoBitmap(getApplicationContext());
        mAttacher = new PhotoViewAttacher(detail_img_srcImage);
        mAttacher.setScaleType(ScaleType.FIT_CENTER);
        mAttacher.setOnViewTapListener(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra(Constant.IMAGE_DETAIL_PATH);

        if (url != null) {
            detail_img_progressBar.setVisibility(View.VISIBLE);
            wentiBitmapUtils.display(detail_img_srcImage, url, new DefaultBitmapLoadCallBack<ImageView>() {
                @Override
                public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
                    super.onLoadCompleted(container, uri, bitmap, config, from);
                    detail_img_progressBar.setVisibility(View.GONE);
                    btn_turn_left.setEnabled(true);
                    btn_turn_right.setEnabled(true);
                    deleteButton.setEnabled(true);
                    mAttacher.update();
                }

                @Override
                public void onLoadFailed(ImageView container, String uri, Drawable drawable) {
                    super.onLoadFailed(container, uri, drawable);
                    btn_turn_left.setEnabled(false);
                    btn_turn_right.setEnabled(false);
                    deleteButton.setEnabled(false);
                }

            });
        }

    }

    /** 向左、向右的单击事件 */
    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int degree;
            BitmapDrawable bd = (BitmapDrawable) detail_img_srcImage.getDrawable();
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
                case R.id.btn_delete:// 删除按钮
                    AlertDialog.Builder builder = new AlertDialog.Builder(PreviewPhotoActivity.this);
                    builder.setMessage("是否删除该图片").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            setResult(Activity.RESULT_OK, intentType);
                            finish();
                        }
                    }).setNegativeButton("取消", null).create().show();
                    break;
            }
        }
    };

    /**
     * 单击屏幕，activity消失
     */
    @Override
    public void onViewTap(View arg0, float arg1, float arg2) {
        PreviewPhotoActivity.this.finish();
    }
}
