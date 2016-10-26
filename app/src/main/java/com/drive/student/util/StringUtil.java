package com.drive.student.util;

import android.app.Activity;
import android.graphics.Rect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 字符串工具类.
 *
 * @author 韩新凯
 */
public class StringUtil {

    /**
     * @description 验证字符串是否为空
     * @version 1.0
     * @author 韩新凯
     * @update May 31, 2010 2:16:46 PM
     */
    public static boolean isEmpty(String str) {
        return str == null || str.equals("null") || str.trim().equals("");

    }

    public static boolean equalsNull(String str) {

        return isBlank(str) || str.equalsIgnoreCase("null");

    }

    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     * <pre>
     * StringUtils.isBlank(null) = true
     * StringUtils.isBlank("") = true
     * StringUtils.isBlank(" ") = true
     * StringUtils.isBlank("bob") = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)))) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 处理空字符串
     *
     * @param str
     * @return String
     */
    public static String doEmpty(String str) {
        return doEmpty(str, "");
    }

    /**
     * 处理空字符串
     *
     * @param str
     * @param defaultValue
     * @return String
     */
    public static String doEmpty(String str, String defaultValue) {
        if (str == null || str.equalsIgnoreCase("null") || str.trim().equals("") || str.trim().equals("－请选择－")) {
            str = defaultValue;
        } else if (str.startsWith("null")) {
            str = str.substring(4, str.length());
        }
        return str.trim();
    }

    /**
     * 请选择
     */
    final static String PLEASE_SELECT = "请选择...";

    public static boolean notEmpty(Object o) {
        return o != null && !"".equals(o.toString().trim()) && !"null".equalsIgnoreCase(o.toString().trim()) && !"undefined".equalsIgnoreCase(o.toString().trim()) && !PLEASE_SELECT.equals(o.toString().trim());
    }

    public static boolean empty(Object o) {
        return o == null || "".equals(o.toString().trim()) || "null".equalsIgnoreCase(o.toString().trim()) || "undefined".equalsIgnoreCase(o.toString().trim()) || PLEASE_SELECT.equals(o.toString().trim());
    }

    public static boolean num(Object o) {
        int n = 0;
        try {
            n = Integer.parseInt(o.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public static boolean decimal(Object o) {
        double n = 0;
        try {
            n = Double.parseDouble(o.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return n > 0.0;
    }

    public static String getSmalPath(String bigPath) {
        if (empty(bigPath)) {
            return null;
        }
        if (bigPath.length() <= 4) {
            return null;
        }
        return bigPath.substring(0, bigPath.length() - 4) + "_s" + bigPath.substring(bigPath.length() - 4);
    }

    public static String readStream(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;

        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = is.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    /***
     * 将字符串转换成浏览器utf-8的形式
     *
     * @param s
     * @return
     * @author 韩新凯
     * @update 2014-5-22 上午9:26:16
     */
    public static String getUtf8Url(String s) {
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /***
     * 将图片地址中的空格替换
     *
     * @param url
     * @return
     * @author 韩新凯
     * @update 2014-5-23 上午9:19:37
     */
    public static String replaceSpace(String url) {
        if (url == null || url == "") {
            return "";
        }
        return url.replace(" ", "%20");
    }

    /**
     * 将图片url转换成小图片的url
     *
     * @param bigurl
     * @return
     * @author 韩新凯
     * @update 2014-6-6 上午11:27:38
     */
    public static String convertBigImgToSmall(String bigurl) {
        if ("".equals(bigurl) || null == bigurl) {
            throw new IllegalArgumentException("url 不能为空!");
        }
        return bigurl + "-s";
    }

    /**
     * 将小图片url转换成大图片的url
     *
     * @param bigurl
     * @return
     * @author 韩新凯
     * @update 2014-6-6 上午11:27:38
     */
    public static String convertSmallImgToBig(String bigurl) {
        if ("".equals(bigurl) || null == bigurl) {
            throw new IllegalArgumentException("url 不能为空!");
        }
        if (!bigurl.endsWith("-s")) {
            throw new IllegalArgumentException("格式不正确 不是小图片的url!");
        }
        return bigurl.split("-s")[0];
    }

    /**
     * 按字节截取字符串
     *
     * @param orignal 原始字符串
     * @param count   截取位数
     * @return 截取后的字符串
     * @throws UnsupportedEncodingException 使用了JAVA不支持的编码格式
     */
    public static String substring(String orignal, int count) throws UnsupportedEncodingException {
        try {
            // 原始字符不为null，也不是空字符串
            if (orignal != null && !"".equals(orignal)) {
                // 将原始字符串转换为GBK编码格式
                orignal = new String(orignal.getBytes(), "UTF-8");//
                // System.out.println(orignal);
                // System.out.println(orignal.getBytes().length);
                // 要截取的字节数大于0，且小于原始字符串的字节数
                if (count > 0 && count < orignal.getBytes("UTF-8").length) {
                    StringBuffer buff = new StringBuffer();
                    char c;
                    for (int i = 0; i < count; i++) {
                        c = orignal.charAt(i);
                        buff.append(c);
                        if (isChineseChar(c)) {
                            // 遇到中文汉字，截取字节总数减1
                            --count;
                        }
                    }
                    return new String(buff.toString().getBytes(), "UTF-8");
                }
            }

        } catch (Exception e) {

        }
        return orignal;
    }

    /**
     * 判断是否是一个中文汉字
     *
     * @param c 字符
     * @return true表示是中文汉字，false表示是英文字母
     * @throws UnsupportedEncodingException 使用了JAVA不支持的编码格式
     */
    public static boolean isChineseChar(char c) throws UnsupportedEncodingException {
        // 如果字节数大于1，是汉字
        // 以这种方式区别英文字母和中文汉字并不是十分严谨，但在这个题目中，这样判断已经足够了
        return String.valueOf(c).getBytes("UTF-8").length > 1;
    }

    /**
     * 图片路径处理 大图转小图
     */

    public static String BigConvertSmall(String url) {
        if (StringUtil.isEmpty(url)) {
            return "";
        }
        String imgBodyP = "";
        if (!url.contains("mofangge.com")) {
            return url;
        }
        if (url.lastIndexOf(".") != -1) {
            String imgBodyS = url.substring(0, url.lastIndexOf("."));
            String imgBodyS2 = url.substring(url.lastIndexOf("."), url.length());
            imgBodyP = imgBodyS + "_s" + imgBodyS2;
        }
        return imgBodyP;

    }

    /***
     * 这个方法作用：大图转小图 ，拍照专用
     *
     * @param imgPath
     * @return
     * @author 韩新凯
     * @update 2014-7-8 下午12:15:13
     */
    public static String Big2Small(String imgPath) {
        if (StringUtil.isEmpty(imgPath)) {
            return "";
        }
        String imgBodyP = "";
        if (imgPath.lastIndexOf(".") != -1) {
            String imgBodyS = imgPath.substring(0, imgPath.lastIndexOf("."));
            String imgBodyS2 = imgPath.substring(imgPath.lastIndexOf("."), imgPath.length());
            imgBodyP = imgBodyS + "_s" + imgBodyS2;
        }
        return imgBodyP;

    }

    /**
     * 得到英文汉字混排的文字的长度
     *
     * @param value
     * @return
     * @author 韩新凯
     * @update 2014-7-10 下午5:59:58
     */
    public static int String_length(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 获得通知栏的高度
     *
     * @param activity
     * @return
     * @author 韩新凯
     * @update 2014-7-13 上午11:50:03
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 检测是否有表情等非法字符串
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                // do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤表情等非文字字符
     */
    public static String filterEmoji(String source) {

        if (!containsEmoji(source)) {
            return source;// 如果不包含，直接返回
        }
        // 到这里铁定包含
        StringBuilder buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            } else {
            }
        }

        if (buf == null) {
            return source;// 返回原字符串
        } else {
            if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }

    }

    public static String inputStream2String(InputStream in) throws IOException {
        if (in == null) {
            return null;
        }
        byte[] buf = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i; (i = in.read(buf)) != -1; ) {
            baos.write(buf, 0, i);
        }
        return baos.toString("UTF-8");
    }
}
