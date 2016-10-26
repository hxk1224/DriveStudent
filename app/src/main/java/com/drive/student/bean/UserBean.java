package com.drive.student.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class UserBean implements Serializable {

    private static final long serialVersionUID = -77015097219571933L;

    /** 用户ID **/
    public String userAccountId;
    /** 机构账号ID--对应supplierId */
    public String userMbrOrgId;
    /** 是否主账户标示 "1"-主账号  "0"-子账户 */
    public String mainFlag;
    /** 是否设置了支付密码 */
    public boolean addPayPwd;
    /** 聊天是否允许跟买家联系,查看买家电话 */
    public boolean vipSee;
    /** 手机号 **/
    public String phone = "";
    /** 用户编码=phone(登录名) **/
    public String userCode = "";
    /** 用户名 **/
    public String userName = "";
    /** 头像路径 **/
    public String facePicUrl;
    /** 角色类型 "0"-汽配 "1"-车和客服 */
    public String userType;
    /** 客服电话 */
    public ArrayList<String> servicePhone;
    /** 省份 **/
    public String province;
    /** 城市 **/
    public String city;
    /** 县区 **/
    public String district;
    /** 省份 **/
    public String provinceCode;
    /** 城市 **/
    public String cityCode;
    /** 县区 **/
    public String districtCode;
    /** 街道 **/
    public String address;
}
