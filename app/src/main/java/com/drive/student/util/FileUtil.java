package com.drive.student.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.ta.utdid2.android.utils.StringUtils;

import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Calendar;

public class FileUtil {

    private static final String TAG = "FileUtil";

    private static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    private static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    private static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    private static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    private static final String[][] MIME_MAPTABLE = {
            //{后缀名，	MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    public static void savePictureToGallery(Context context, String picPath) {
        if (StringUtil.isBlank(picPath)) {
            return;
        }
        File file = new File(picPath);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(picPath);
            if (bitmap != null) {
                saveBitmapToGallery(context, bitmap);
            }
        }
    }

    public static void saveBitmapToGallery(Context context, Bitmap bm) {
        // 获取系统图片存储路径
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // Make sure the Pictures directory exists.
        if (!path.exists()) {
            path.mkdirs();
        }
        // 根据当前时间生成图片名称
        Calendar c = Calendar.getInstance();
        String name = "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH) + c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND) + ".png";
        String fileName = path.getPath() + "/" + name;
        File file = new File(fileName);
        if (!file.exists()) {
            // 未保存
            try {
                FileOutputStream fos = new FileOutputStream(new File(fileName));
                // 将 bitmap 压缩成其他格式的图片数据
                bm.compress(Bitmap.CompressFormat.PNG, 50, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 发送广播提醒相册更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(fileName));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /** 打开文件 */
    public static void openFile(Context context, String fileUrl) {
        File file = new File(fileUrl);
        if (file.exists()) {
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //设置intent的Action属性
                intent.setAction(Intent.ACTION_VIEW);
                //获取文件file的MIME类型
                String type = getMIMEType(file);
                //设置intent的data和Type属性。
                intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
                context.startActivity(intent);
            } catch (Throwable t) {
                t.printStackTrace();
                CustomToast.showToast(context, "文件无法打开!", Toast.LENGTH_SHORT);
            }
        } else {
            CustomToast.showToast(context, "文件没有找到!", Toast.LENGTH_SHORT);
        }
    }

    /** 打开文件 */
    public static void openFile(Context context, File file) {
        if (file != null && file.exists()) {
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //设置intent的Action属性
                intent.setAction(Intent.ACTION_VIEW);
                //获取文件file的MIME类型
                String type = getMIMEType(file);
                //设置intent的data和Type属性。
                intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
                context.startActivity(intent);
            } catch (Throwable t) {
                t.printStackTrace();
                CustomToast.showToast(context, "文件无法打开!", Toast.LENGTH_SHORT);
            }
        } else {
            CustomToast.showToast(context, "文件没有找到!", Toast.LENGTH_SHORT);
        }
    }

    /** 根据文件后缀名获得对应的MIME类型 */
    public static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (StringUtils.isEmpty(end)) {
            return type;
        }
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (String[] aMIME_MAPTABLE : MIME_MAPTABLE) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(aMIME_MAPTABLE[0]))
                type = aMIME_MAPTABLE[1];
        }
        return type;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /** 压缩Bitmap */
    public static byte[] compressBitmapFile(String srcPath, /*String dstPath,*/int maxWidth, int maxHeight, long maxSize) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= maxWidth && h <= maxHeight) {
            size = 1;
        } else {
            // The decoder uses a final value based on powers of 2,
            // any other value will be rounded down to the nearest power of 2.
            // So we use a ceil log value to keep both of them under limits.
            // See doc:
            // http://developer.android.com/reference/android/graphics/BitmapFactory.Options.html#inSampleSize
            double scale = w >= h ? w / maxWidth : h / maxHeight;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = size;
        ByteArrayOutputStream baos = null;
        try {
            bitmap = BitmapFactory.decodeFile(srcPath, opts);
            float scale = getScaling(bitmap, maxWidth, maxHeight);
            Bitmap bmp2 = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale), (int) (bitmap.getHeight() * scale), true);
            baos = new ByteArrayOutputStream();
            int quality = 100;
            bmp2.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            while (baos.toByteArray().length > maxSize && quality > 10) {
                baos.reset();
                bmp2.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                quality -= 10;
            }
            bitmap.recycle();
            bmp2.recycle();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*try {
            baos.writeTo(new FileOutputStream(dstPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				baos.flush();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
        return null;
    }

    /** 根据需要旋转图片 */
    public static Bitmap rotateBitmapIfNeeded(File bitmapFile, Bitmap bitmap) {
        Bitmap result = bitmap;
        if (bitmapFile != null && bitmapFile.exists()) {
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(bitmapFile.getPath());
            } catch (Throwable e) {
                return result;
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            int angle = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
                default:
                    angle = 0;
                    break;
            }
            if (angle != 0) {
                Matrix m = new Matrix();
                m.postRotate(angle);
                result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                bitmap.recycle();
                bitmap = null;
            }
        }
        return result;
    }

    /** @param quality 0-100. 0 meaning compress for small size, 100 meaning compress for max quality. */
    public static byte[] decodeBitmap(String path, int quality, int destWidth, int destHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = computeSampleSize(opts.outWidth, opts.outHeight, destWidth, destHeight);
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        opts.inTempStorage = new byte[16 * 1024];
        FileInputStream is = null;
        Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        try {
            is = new FileInputStream(path);
            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
            float scale = getScaling(bmp, destWidth, destHeight);
            baos = new ByteArrayOutputStream();
            if (scale >= 1.2 || scale < 1) {
                Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, (int) (opts.outWidth * scale), (int) (opts.outHeight * scale), true);
                bmp2.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                bmp.recycle();
                bmp2.recycle();
                return baos.toByteArray();
            }
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            bmp.recycle();
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
        return null;
    }

    private static float getScaling(Bitmap bitmap, int destWidth, int destHeight) {
        float hR = (float) destHeight / bitmap.getHeight();
        hR = (float) (((int) (hR * 10)) / 10.0);
        float wR = (float) destWidth / bitmap.getWidth();
        wR = (float) (((int) (wR * 10)) / 10.0);
        float radio = 1;// 图片缩放比例
        if (hR > wR) {
            radio = wR;
        } else {
            radio = hR;
        }
        return radio;
    }

    public static int computeSampleSize(int optionsWidth, int optionsHeight, int destWidth, int destHeight) {
        int roundedSize = 1;
        int wRatio = (int) Math.ceil(optionsWidth / (float) destWidth);
        int hRatio = (int) Math.ceil(optionsHeight / (float) destHeight);
        // 判断是按高比率缩放还是宽比例缩放
        if (hRatio > 1 || wRatio > 1) {
            if (hRatio > wRatio) {
                roundedSize = hRatio;
            } else {
                roundedSize = wRatio;
            }
        }
        return roundedSize;
    }

    /**
     * 删除保存的临时语音文件-在子线程执行
     *
     * @param tmpAudioPath
     * @return 删除成功返回true 删除失败或者文件不存在或者传入的是网络地址返回false
     */
    public static void deleteTmpRadioFile(final String tmpAudioPath) {
        if (TextUtils.isEmpty(tmpAudioPath)) {
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                File file = new File(tmpAudioPath);
                deleteTmpRadioFile(file);
            }
        }).start();
    }

    /** 删除保存的临时语音文件，tmp_xxxxx.amr */
    public static void deleteTmpRadioFile(File file) {
        if (file != null && file.exists()) {
            if (file.isFile() && file.getName().startsWith("tmp_")) {
                // NOTE:to fix bug : open failed: EBUSY (Device or resource
                // busy)
                final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
                file.renameTo(to);
                to.delete();
//				file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        deleteTmpRadioFile(files[i]);
                    }
                }
            }
            // NOTE:不要删除文件夹
        } else {
            // file not exist
        }
    }

    /** 删除文件如果是文件夹的话删除文件夹里面的所有文件 */
    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        deleteFile(files[i]);
                    }
                }
            }
            file.delete();
        } else {
            // file not exist
        }
    }

    /**
     * 删除保存的临时语音文件-在子线程执行
     *
     * @param tmpPath 文件路径
     */
    public static void deleteTmpCameraFileInThread(final String tmpPath) {
        if (TextUtils.isEmpty(tmpPath)) {
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                File file = new File(tmpPath);
                deleteTmpCameraFile(file);
            }
        }).start();
    }

    /** 删除保存的临时图片压缩和截图文件 */
    public static void deleteTmpCameraFile(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                // NOTE:to fix bug : open failed: EBUSY (Device or resource
                // busy)
                final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
                file.renameTo(to);
                to.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File file1 : files) {
                        deleteTmpCameraFile(file1);
                    }
                }
                // NOTE:不要删除文件夹
            }
        } else {
            // file not exist
        }
    }

    /** 删除文件如果是文件夹的话删除文件夹里面的所有文件 */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        deleteFile(files[i]);
                    }
                }
            }
            file.delete();
        } else {
            // file not exist
        }
    }

    /**
     * 删除dir目录下的文件，保留文件名是retainName的文件，不删除文件夹
     *
     * @param dir        目录
     * @param remainFile 要保留的文件
     */
    public static void deleteOtherFile(String dir, String remainFile) {
        File file = new File(dir);
        if (file.exists()) {
            if (file.isFile()) {
                if (!file.getName().equalsIgnoreCase(remainFile)) {
                    file.delete();
                }
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        deleteFile(files[i]);
                    }
                }
            }
        } else {
            // file not exist
        }
    }

    public static String getFileMD5(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String md5 = new String(Hex.encodeHex(digest.digest()));
        LogUtil.e(TAG, "getFileMD5 md5 = " + md5 + ", file=" + file.getAbsolutePath());
        return md5;
    }

    /**
     * 压缩图片质量并保存图片
     *
     * @param bitmap      图片
     * @param filePath    文件保存地址
     * @param quality     0-100. 0 meaning compress for small size, 100 meaning compress for max quality.
     * @param needRecycle 是否需要回收Bitmap资源
     * @return 保存的图片地址
     */
    public static String saveCompressBitmap(Bitmap bitmap, String filePath, int quality, boolean needRecycle) {
        File reviewImgFile = new File(filePath);
        if (reviewImgFile.exists()) {
            reviewImgFile.delete();
        }
        try {
            reviewImgFile.createNewFile();
            FileOutputStream out = new FileOutputStream(reviewImgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
            if (needRecycle && bitmap != null && !bitmap.isRecycled()) {
                // 回收Bitmap资源
                bitmap.recycle();
                bitmap = null;
            }
            return reviewImgFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 两张图片合成一张图片，图片重叠合并 */
    public static Bitmap mixtureBitmap(Bitmap first, Bitmap second) throws NullPointerException {
        if (first == null || second == null) {
            throw new NullPointerException("first is null or second is null");
        }
        Canvas cv = new Canvas(first);
        cv.drawBitmap(second, 0, 0, null);
        cv.save(Canvas.CLIP_SAVE_FLAG);
        cv.restore();
        return first;
    }

    /** 获得获取文件的扩展名 */
    public static String getExtension(final File file) {
        String suffix = "";
        String name = file.getName();
        final int idx = name.lastIndexOf(".");
        if (idx > 0) {
            suffix = name.substring(idx + 1);
        }
        return suffix;
    }

    /** 通过扩展名获取MIME类型 */
    public static String getMimeType(final File file) {
        String extension = getExtension(file);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public static boolean isPicture(String path) {
        boolean res = false;
        if (StringUtil.isBlank(path)) {
            return res;
        }
        File file = new File(path);
        if (file.isDirectory()) {
            return res;
        }
        try {
            @SuppressWarnings("resource")
            FileInputStream inputStream = new FileInputStream(path);
            byte[] buffer = new byte[2];
            String filecode = "";
            if (inputStream.read(buffer) != -1) {
                for (int i = 0; i < buffer.length; i++) {
                    filecode += Integer.toString((buffer[i] & 0xFF));
                }
                switch (Integer.parseInt(filecode)) {
                    case 255216:// "jpg"
                        res = true;
                        break;
                    case 6677:// "bmp"
                        res = true;
                        break;
                    case 13780:// "png"
                        res = true;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /** 是否是图片 */
    public static boolean isPicture(File file) {
        boolean res = false;
        if (null == file || file.isDirectory()) {
            return res;
        }
        try {
            @SuppressWarnings("resource")
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[2];
            String filecode = "";
            if (inputStream.read(buffer) != -1) {
                for (int i = 0; i < buffer.length; i++) {
                    filecode += Integer.toString((buffer[i] & 0xFF));
                }
                switch (Integer.parseInt(filecode)) {
                    case 255216:// "jpg"
                        res = true;
                        break;
                    case 6677:// "bmp"
                        res = true;
                        break;
                    case 13780:// "png"
                        res = true;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFolderSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFolderSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param file 文件
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileSize(File file) {
        if (file != null) {
            long blockSize = 0;
            try {
                if (file.isDirectory()) {
                    blockSize = getFolderSizes(file);
                } else {
                    blockSize = getFileSize(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return FormetFileSize(blockSize);
        }
        return null;
    }

    /** 获取指定文件大小 */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
        }
        return size;
    }

    /** 获取指定文件大小 */
    public static long getFileSize(String filePath) throws Exception {
        File file = new File(filePath);
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
        }
        return size;
    }

    /** 获取指定文件夹 */
    public static long getFolderSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFolderSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /** 转换文件大小 */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /** 转换文件大小,指定转换的类型 */
    public static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static void readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            System.out.println("以字节为单位读取文件内容，一次读一个字节：");
            // 一次读一个字节
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(fileName);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /** 以字符为单位读取文件，常用于读文本，数字等类型的文件 */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            System.out.println("以字符为单位读取文件内容，一次读一个字节：");
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("以字符为单位读取文件内容，一次读多个字节：");
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示
                if ((charread == tempchars.length) && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /** 以行为单位读取文件，常用于读面向行的格式化文件 */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /** 随机读取文件内容 */
    public static void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            System.out.println("随机读取一段文件内容：");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(fileName, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 读文件的起始位置
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
            // 将一次读取的字节数赋给byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public static void writeFile(String content, String dirPath, String fileName) {
        FileOutputStream fop = null;
        File file;
        try {
            File directory = new File(dirPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            file = new File(dirPath, fileName);
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            fop = new FileOutputStream(file);
            // get the content in bytes
            byte[] contentInBytes = content.getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeFile(String content, String dirPath, String fileName, WriteFileCallBack callback) {
        FileOutputStream fop = null;
        File file;
        try {
            callback.onStart();
            File directory = new File(dirPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            file = new File(dirPath, fileName);
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            fop = new FileOutputStream(file);
            // get the content in bytes
            byte[] contentInBytes = content.getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
            callback.onFinish();
        } catch (Throwable t) {
            callback.onError(t);
            t.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                callback.onError(e);
                e.printStackTrace();
            }
        }
    }

    public interface WriteFileCallBack {
        void onFinish();

        void onError(Throwable t);

        void onStart();
    }

    public static void writeFile(InputStream in, String dirPath, String fileName) {
        FileOutputStream fop = null;
        File file;
        try {
            file = new File(dirPath, fileName);
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            fop = new FileOutputStream(file);
            // get the content in bytes
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
                fop.write(buffer);
            }
            fop.flush();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
