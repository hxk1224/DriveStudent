package com.drive.student.util;

import org.apache.commons.codec.binary.Base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileBase64Util {

    public static void main(String[] args) {
        String imgFile = "f:\\test\\1234.mp3";// 待处理的图片
        String imgbese = getFileStr(imgFile);
        System.out.println(imgbese.length());
        System.out.println(imgbese);
        String imgFilePath = "f:\\test\\12345.mp3";// 新生成的图片
        generateFile(imgbese, imgFilePath);
    }

    /**
     * 将文件转换成Base64编码
     *
     * @param filePath 待处理文件地址
     */
    public static String getFileStr(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return null;
        }
        // 将文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in;
        byte[] data = null;
        // 读取文件字节数组
        try {
            in = new FileInputStream(filePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (data == null) {
            return null;
        }
        return new String(Base64.encodeBase64(data));
    }

    /**
     * 对字节数组字符串进行Base64解码并生成文件
     *
     * @param fileStr  文件数据
     * @param filePath 保存文件全路径地址
     * @return
     */
    public static boolean generateFile(String fileStr, String filePath) {
        //
        if (fileStr == null) {
            return false;
        }

        try {
            // Base64解码
            byte[] b = Base64.decodeBase64(fileStr.getBytes());
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片

            OutputStream out = new FileOutputStream(filePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
