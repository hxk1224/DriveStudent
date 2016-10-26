package com.drive.student.util;

import android.text.TextUtils;

/**
 * 检查输入的手机号及密码,邮箱的合法性.
 *
 * @author 韩新凯
 */
public class CheckLegalUtil {
    /**
     * 检查手机号的合法性.
     *
     * @param telephone
     * @return true 合法
     * @author 韩新凯
     * @update 2015年1月13日 下午5:13:14
     */
    public static boolean checkTelephone(String telephone) {
        if (TextUtils.isEmpty(telephone)) {
            return false;
        } else if ((telephone.startsWith("86") && telephone.length() == 13) || (!telephone.startsWith("86") && telephone.length() == 11)) {
            return true;
        }
        return false;
    }

    /**
     * 检查验证码的合法性
     *
     * @param verifyCode 验证码
     * @return true 合法
     * @author 韩新凯
     * @update 2015年1月14日 下午3:17:34
     */
    public static boolean checkVerifyCode(String verifyCode) {
        if (TextUtils.isEmpty(verifyCode)) {
            return false;
        } else {
            String verRegex = "[0-9]{6}";
            return verifyCode.matches(verRegex);

        }
    }

    /**
     * 检查邮箱的合法性.
     *
     * @param email
     * @return true 合法
     * @author 韩新凯
     * @update 2015年1月13日 下午9:48:57
     */
    public static boolean checkEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            String emailRegex = "^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            return email.matches(emailRegex);
        }
    }
}
