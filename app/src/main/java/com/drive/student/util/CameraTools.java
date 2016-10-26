package com.drive.student.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * 相机工具类.
 *
 * @author 韩新凯
 */
public class CameraTools {
    private Context mContext;
    private String FILE_SAVEPATH = null;

    /** 剪裁后等待上传的临时路径
     public static String cutTempPath; **/
    /**
     * 剪裁后等待上传的临时文件
     * public static File cutTempFile;
     **/
    public static String picPath;
    private static CameraTools cameraTools;

    private CameraTools(Context context) {
        this.mContext = context;
        SharePreferenceUtil su = new SharePreferenceUtil(context);
        FILE_SAVEPATH = su.getCameraTempPath();
    }

    public static CameraTools getInstance(Context context) {
        if (cameraTools == null) {
            cameraTools = new CameraTools(context);
        }
        return cameraTools;
    }

    /**
     * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     *
     * @param uri
     * @return
     */
    public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file:///sdcard" + File.separator;
        String pre2 = "file:///sdcard" + File.separator;
        String pre3 = "file:///storage/sdcard0" + File.separator;

        if (mUriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre2.length());
        } else if (mUriString.startsWith(pre3)) {
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre3.length());
        }
        return filePath;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 通过uri获取文件的绝对路径
     *
     * @param uri
     * @return
     */
    public static String getAbsoluteImagePath(Context context, Uri uri) {
        String imagepath = "";
        Cursor c = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                imagepath = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }
        return imagepath;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (isEmpty(fileName))
            return "";
        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    /**
     *
     * 得到剪裁后等待上传的临时文件.
     *
     * @param uri
     * @return
     * @author 韩新凯
     * @update 2014年6月23日 下午5:46:00

    public Uri getCutTempFile(Uri uri) {
    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    // 判断uri的格式
    String thePath = getAbsolutePathFromNoStandardUri(uri);
    if (isEmpty(thePath)) {
    thePath = getAbsoluteImagePath(mContext, uri);
    }
    String ext = getFileFormat(thePath);
    ext = isEmpty(ext) ? "jpg" : ext;

    String cropFileName = timeStamp + "." + ext;
    cutTempPath = FILE_SAVEPATH + File.separatorChar + cropFileName;
    cutTempFile = new File(cutTempPath);
    Uri testUri = Uri.fromFile(cutTempFile);
    return testUri;
    }*/

    /**
     * 获取分享图片.
     *
     * @return
     * @author 韩新凯
     * @update 2014年11月4日 上午11:24:39
     */
    public String getSharePic() {
        String cropFileName = "sharePic";
        picPath = FILE_SAVEPATH + File.separatorChar + cropFileName;
        return picPath;
    }

    /**
     * 功能:获取学霸分享图片
     *
     * @return XBsharePic
     * @author 韩新凯
     */
    public String getXBSharePic() {
        String cropFileName = "XBsharePic";
        picPath = FILE_SAVEPATH + File.separatorChar + cropFileName;
        return picPath;

    }

    /**
     * 功能:获取评论后的分享图片
     *
     * @return
     * @author ting
     */
    public String getCommentsSharePic() {
        String cropFileName = "ShareIcon";
        picPath = FILE_SAVEPATH + File.separatorChar + cropFileName;
        return picPath;

    }
}
