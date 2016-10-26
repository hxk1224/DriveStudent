package com.drive.student.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.drive.student.R;
import com.drive.student.xutils.BitmapUtils;

/***
 * 用于管理缓存对象 为了是整个应用的缓存都指向同一个位置
 * <p/>
 * 基本用法： bitmapUtils =
 * BitmapHelp.getBitmapUtils(this.getActivity().getApplicationContext());
 * bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
 * bitmapUtils.configDefaultLoadFailedImage(R.drawable.bitmap);
 * bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
 */
public class BitmapHelp {

    private BitmapHelp() {
    }

    private static BitmapUtils photoBitmap, headPortrait;

    /**
     * 功能:初始化图片缓存，请使用getApplicationContext()
     *
     * @param context 请使用getApplicationContext()
     */
    public static BitmapUtils getPhotoBitmap(Context context) {
        if (photoBitmap == null) {
            photoBitmap = new BitmapUtils(context, getCacheRoot(context));
            photoBitmap.configDefaultLoadingImage(R.drawable.imagedetail_defaullt);
            photoBitmap.configDefaultLoadFailedImage(R.drawable.imagedetail_failed);
            photoBitmap.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
            // FIX:中兴手机出现图片翻转
            photoBitmap.configDefaultAutoRotation(true);
        }
        return photoBitmap;
    }

    /**
     * 功能:初始化头像缓存,请使用getApplicationContext()
     *
     * @param context 请使用getApplicationContext()
     */
    public static BitmapUtils getHeadPortrait(Context context) {
        if (headPortrait == null) {
            headPortrait = new BitmapUtils(context, getCacheRoot(context));
            headPortrait.configDefaultLoadingImage(R.drawable.head_default_icon);
            headPortrait.configDefaultLoadFailedImage(R.drawable.head_default_icon);// 加载失败默认图片
            headPortrait.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        }

        return headPortrait;

    }

    /***
     * 得到文件缓存的位置
     *
     * @param context
     * @return 文件缓存的位置
     */
    private static String getCacheRoot(Context context) {
        SharePreferenceUtil spUtil = new SharePreferenceUtil(context);
        String root = spUtil.getBitmapCachePath();
        return root;
    }

}
