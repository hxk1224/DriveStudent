package com.drive.student.util;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;

/**
 * <p>
 * Title:UUID主键生成器
 * </p>
 *
 * @author 韩新凯
 * @version 1.0
 */
public class UUIDPK implements Serializable {
    private static final long serialVersionUID = 6764469243374414791L;
    public String POID;

    /**
     * 这个无参的构造函数是必须要有的。
     */
    public UUIDPK() {
    }

    /**
     * @param id POID的值。
     * @return
     * @throws
     * @author 韩新凯
     * @function:
     */
    public UUIDPK(String id) {
        POID = id;
    }

    @Override
    public String toString() {
        return POID;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other == this) {
            return true;
        }
        if (!(other instanceof UUIDPK)) {
            return false;
        }

        result = POID == null ? ((UUIDPK) other).POID == null : POID.equals(((UUIDPK) other).POID);

        return result;
    }

    @Override
    public int hashCode() {
        return POID == null ? 0 : POID.hashCode();
    }

    // --------以下为UUID的生成函数---------

    /**
     * @param obj 生成UUID的thirdPart时所需要使用的对象的实例引用。
     * @return
     * @throws
     * @author 韩新凯
     * @function: 按UUID的产生机制产生一个32位长的Hex字符串。
     */

    public static String getUUID(Object obj) {
        String uuid = "";
        uuid = firstPart() + secondPart() + thirdPart(obj) + fourthPart();
        return uuid;
    }

    /**
     * @param
     * @return 返回当前时间（毫秒）表示的long型数字（Hex格式）的低8位
     * @throws
     * @author 韩新凯
     * @function: 返回当前时间（毫秒）表示的long型数字（Hex格式）的低8位。
     */
    private static String firstPart() {
        long tmp = 0L;
        String tmpHex = null;
        tmp = System.currentTimeMillis();
        tmpHex = Long.toHexString(tmp);
        return tmpHex.substring(tmpHex.length() - 8);
    }

    /**
     * @param
     * @return 底层IP地址表示的32位整数的Hex字符串。
     * @throws
     * @author 韩新凯
     * @function: 底层IP地址表示的32位整数的Hex字符串。
     */
    private static String secondPart() {
        String sndPart = "";
        int tmpIp = 0;
        int tmp = 0;
        InetAddress localIPAddr = null;
        try {
            localIPAddr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            // 基本不可能发生。
        }
        byte[] ipParts = localIPAddr.getAddress();
        // 0
        tmp = ipParts[0];
        /** IP地址的第0段，tmp左移24位 */
        tmpIp = tmp << 24;
        // 1
        tmp = ipParts[1];
        tmp = tmp << 16;
        tmpIp = tmpIp ^ tmp;
        // 2
        tmp = ipParts[2];
        tmp = tmp << 8;
        tmpIp = tmpIp ^ tmp;
        // 3
        tmp = ipParts[3];
        tmpIp = tmpIp ^ tmp;
        sndPart = Integer.toHexString(tmpIp);
        return getEightHex(sndPart);
    }

    /**
     * @param obj 使用该类生成hashCode。
     * @return 调用对象的HashCode码的Hex形式的字符串。
     * @throws
     * @author 韩新凯
     * @function: 调用对象的HashCode码的Hex形式的字符串。
     */
    private static String thirdPart(Object obj) {
        String tmpHex = "";
        tmpHex = Integer.toHexString(obj.hashCode());
        return getEightHex(tmpHex);
    }

    /**
     * @param
     * @return 在一毫秒内对同一个方法的多个调用的唯一值。
     * @throws
     * @author 韩新凯
     * @function: SecureRandom类所产生的在一毫秒内对同一个方法的多个调用的唯一值。
     */
    private static String fourthPart() {
        String tmpHex = "";
        SecureRandom sr = new SecureRandom();
        tmpHex = Integer.toHexString(sr.nextInt());
        return getEightHex(tmpHex);
    }

    /**
     * @param part 要处理的字符串。
     * @return 如果字符串不足8位，返回8位长的字符串（前几位补0）；否则返回原字符串。
     * @throws
     * @author 韩新凯
     * @function: 把传递过来的字符串加工成8位长。
     */
    private static String getEightHex(String part) {
        if (part.length() >= 8) {
            return part;
        }
        switch (part.length()) {
            case (0): {
                part = "00000000";
                break;
            }
            case (1): {
                part = "0000000" + part;
                break;
            }
            case (2): {
                part = "000000" + part;
                break;
            }
            case (3): {
                part = "00000" + part;
                break;
            }
            case (4): {
                part = "0000" + part;
                break;
            }
            case (5): {
                part = "000" + part;
                break;
            }
            case (6): {
                part = "00" + part;
                break;
            }
            case (7): {
                part = "0" + part;
                break;
            }
        }
        return part;
    }
}
