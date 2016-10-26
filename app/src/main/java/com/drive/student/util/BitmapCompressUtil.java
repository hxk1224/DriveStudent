package com.drive.student.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片压缩器.
 *
 * @author 韩新凯
 *         <p/>
 *         压缩原理： 1.获取原始图片的长和宽 2.计算压缩比例并缩放 3.对图片质量进行压缩
 */
public class BitmapCompressUtil {
    /**
     * 计算图片的缩放值.
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;// 1表示不缩放

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     *
     * 图片按比例大小压缩.
     *
     * @param filePath
     *            图片路径
     */
    // public static Bitmap compImageFromFilePath(String filePath) {
    // final BitmapFactory.Options options = new BitmapFactory.Options();
    // // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
    // options.inJustDecodeBounds = true;
    // Bitmap bitmap=BitmapFactory.decodeFile(filePath, options);
    //
    // // 换算合适的图片缩放值
    // options.inSampleSize = calculateInSampleSize(options, 640, 960);
    //
    // // Decode bitmap with inSampleSize set
    // options.inJustDecodeBounds = false;
    // // inPurgeable 设定为 true，可以让java系统, 在内存不足时先行回收部分的内存
    // options.inPurgeable = true;
    // // 减少对Aphla 通道
    // options.inPreferredConfig = Bitmap.Config.RGB_565;
    // options.inTempStorage = new byte[1000 * 1024];
    // bitmap = BitmapFactory.decodeFile(filePath, options);
    //
    // /**循环进行质量压缩**/
    // return compressImage(bitmap,options);
    // }
    /**
     *
     * 图片按比例大小压缩.
     *
     * @param inputStream
     *            图片流
     */
    // public static Bitmap compImageFromInputStream(InputStream inputStream) {
    // BitmapFactory.Options options = new BitmapFactory.Options();
    // // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
    // options.inJustDecodeBounds = true;
    // Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
    //
    // /**换算合适的图片缩放值**/
    // options.inSampleSize = calculateInSampleSize(options, 640, 960);
    //
    // // Decode bitmap with inSampleSize set
    // options.inJustDecodeBounds = false;
    // // inPurgeable 设定为 true，可以让java系统, 在内存不足时先行回收部分的内存
    // options.inPurgeable = true;
    // // 减少对Aphla 通道
    // options.inPreferredConfig = Bitmap.Config.RGB_565;
    // options.inTempStorage = new byte[1000 * 1024];
    // bitmap = BitmapFactory.decodeStream(inputStream, null, options);
    //
    // /**循环进行质量压缩**/
    // return compressImage(bitmap,options);
    // }

    /**
     * 图片按比例大小压缩.
     */
    public static Bitmap compImageFromBitmapdd(Bitmap image) {
        int x = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出

        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 10, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();

        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, options);

        // 换算合适的图片缩放值
        // options.inSampleSize = calculateInSampleSize(options, 360, 600);
        options.inSampleSize = 4;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        // inPurgeable 设定为 true，可以让java系统, 在内存不足时先行回收部分的内存
        options.inPurgeable = true;
        // 减少对Aphla 通道
        // options.inPreferredConfig = Bitmap.Config.RGB_565;
        // options.inTempStorage = new byte[1000 * 1024];

        isBm = new ByteArrayInputStream(baos.toByteArray());
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap.recycle();
        }
        Bitmap tempbitmap = BitmapFactory.decodeStream(isBm, null, options);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        baos = null;
        // bitmap = Bitmap.createScaledBitmap(tempbitmap, options.outWidth,
        // options.outHeight, false);
        return tempbitmap;
    }

    /**
     * 图片质量循环压缩.
     *
     * @param bitmap
     * @param options
     */
    private static Bitmap compressImage(Bitmap bitmap, BitmapFactory.Options options) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 90;

        while (baos.toByteArray().length / 1024 > 100 && quality > 0) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            quality -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        bitmap = BitmapFactory.decodeStream(isBm, null, options);// 把ByteArrayInputStream数据生成图片
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        baos = null;
        return bitmap;
    }

    /**
     * 把Bitmap转换成Base64.
     *
     * @param bitmap
     */
    public static String getBitmapStrBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, 0);
    }

    /**
     * 把Base64转换成Bitmap.
     *
     * @param iconBase64
     */
    public static Bitmap getBitmapFromBase64(String iconBase64) {
        byte[] bitmapArray = Base64.decode(iconBase64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    /**
     * 这里用一句话描述这个方法的作用.
     *
     * @param bmp
     * @param name  路径
     * @param quity 压缩比例
     */

    public static String SaveBitmap(Bitmap bmp, String name, int quity) {

        return SaveBitmap(bmp, null, name, quity);
    }

    /**
     *
     * 这里用一句话描述这个方法的作用.
     *
     * @param bmp
     * @param name
     *            路径
     * @param quity
     *            压缩比例
     */

    /***
     * 这个方法作用：根据(oldName)的文件生成的bmp，转换生成新的文件（newName），(oldName)生成原始文件会删除，生成新文件（
     * newName）
     *
     * @param bmp     bitmap 图像
     * @param oldName 文件原始名字 原始文件会删除
     * @param newName 文件压缩后需要保存的新路径
     * @param quity   压缩质量
     */
    public static String SaveBitmap(Bitmap bmp, String oldName, String newName, int quity) {
        if (StringUtil.empty(newName)) {
            return null;
        }
        File pic = new File(newName);
        if (pic.exists()) {
            pic.delete();
        }
        try {
            pic.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(pic);
            bmp.compress(Bitmap.CompressFormat.JPEG, quity, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Log.e("BitmapCompressUtil", "BitmapCompressUtil saveBmp is here:" + newName);
        } catch (Exception e) {

        }
        return pic.getPath();
    }

    public static String Big2Small(String imgPath) {
        if (StringUtil.isEmpty(imgPath)) {
            return "";
        }
        String imgBodyP = "";
        if (imgPath.lastIndexOf(".") != -1) {
            String imgBodyS = imgPath.substring(0, imgPath.lastIndexOf("."));
            String imgBodyS2 = imgPath.substring(imgPath.lastIndexOf("."), imgPath.length());
            imgBodyP = imgBodyS + "_s" + System.currentTimeMillis() + imgBodyS2;
        }
        return imgBodyP;

    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 720);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /****
     * 这个方法作用：压缩剪裁后的图片吗，会删除传进来的大图
     *
     * @param filePath 原始文件路径
     * @param maxSize  保存文件最大大小
     * @param quity    默认质量 40
     * @return 返回压缩后新文件路径
     */
    public static String getSmallBitmapAndSave(String filePath, String newPath, int maxSize, int quity) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 540, 960);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        String last = SaveBitmap(bitmap, filePath, newPath, quity);
        quity -= 10;
        while (last != null && new File(last).exists() && new File(last).length() / 1024 > maxSize && quity >= 10) {
            last = SaveBitmap(bitmap, last, newPath, quity);
            quity -= 10;
        }
        if (last == null || !new File(last).exists() || new File(last).length() == 0) {
            last = filePath;
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return last;
    }
}
