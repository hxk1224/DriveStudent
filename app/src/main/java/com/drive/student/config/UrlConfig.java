package com.drive.student.config;

public interface UrlConfig {
    /******************** 是否开启加密 *******************/
    String KEY = "drive_student";
    String CLIENT = "Android";

    /** 正式地址 */
    String DOMAIN_OFFICIAL = "http://www.chehe180.com/";
    /** 外网测试地址-开发用 */
    String DOMAIN_CESHI = "http://58.135.81.28/";

    String DOMAIN = DOMAIN_CESHI;

    String ZASION_HOST = DOMAIN + "master_if/qp_srv.do";

    /** 询价单列表接口 **/
    // int INQUIRY_LIST_CODE = 1101;
}
