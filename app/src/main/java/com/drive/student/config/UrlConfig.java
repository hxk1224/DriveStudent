package com.drive.student.config;

public interface UrlConfig {
    /******************** 是否开启加密 *******************/
    boolean ENCRYPT_ENABLE = true;
    String KEY = "chqp_CarSupplier";
    String ANDROID = "Android";
    String CLIENT = "CHQP_QP";

    /** 正式地址 */
    String DOMAIN_OFFICIAL = "http://www.chehe180.com/";
    /** 外网测试地址-开发用 */
    String DOMAIN_CESHI = "http://58.135.81.28/";

//    String DOMAIN = DOMAIN_CESHI;
    /** 内网地址 */
    String DOMAIN = "http://192.168.1.152:8080/";

    String ZASION_HOST = DOMAIN + "master_if/qp_srv.do";
    /*** 发送聊天消息-发给修理厂 */
    String ZASION_SEND_MESSAGE_TO_REPAIR = DOMAIN + "chat/comet_sendChat2Device.do";
    /*** 发送聊天消息-发给汽配商 */
    String ZASION_SEND_MESSAGE_TO_SUPPLIER = DOMAIN + "chat/sendMsg.do";
    /** 汽车品牌图片 */
    String CAR_BRAND_HOST = DOMAIN + "chqp/brand/";
    /**
     * 上传照片
     * 1204上传物流单证 orderId
     * 1406头像 userAccountId
     * 1407营业执照、法人身份证照片 supplierId
     * 1408询价单上传照片 inquiryId
     * 1409售后上传照片 returnId
     * 1410店铺照片上传 supplierId
     * 1421商圈图片上传 tradingCircleId
     */
    String ZASION_UPLOAD = DOMAIN + "master_if/qp_upload.do";

    /** 询价单列表接口 **/
    int INQUIRY_LIST_CODE = 1101;
    /** 询价单汽配商报价明细接口 **/
    int SUPLIER_PRICE_DETAIL_CODE = 1102;
    /** 询价单报价接口 **/
    int SUBMIT_INQUIRY_PRICE = 1103;
    /** 搜索询价单接口 **/
    int SEARCH_INQUIRY_CODE = 1104;
    /** 询价单查询聊天记录接口 **/
    int INQUIRY_QUERY_MESSAGE_CODE = 1105;
    /** 查询历史报价接口 */
    int GET_HISTORY_PRICE_CODE = 1106;
    /** 获取询价单新消息数量接口 **/
    int GET_HOME_NEW_MSG_NUM_CODE = 1107;

    /** 查询订单接口 **/
    int QUERY_ORDER_CODE = 1201;
    /** 查询订单明细接口 **/
    int QUERY_ORDER_DETAIL_CODE = 1202;
    /** 修改订单报价和备注接口 **/
    int CHANGE_ORDER_PRICE_PS_CODE = 1203;
    /** 上传订单物流单证图片接口 **/
    int UPLOAD_ORDER_WULIU_CODE = 1204;
    /** 搜索订单接口 **/
    int SEARCH_ORDER_CODE = 1205;
    /** 查询订单物流接口 **/
    int QUERY_ORDER_WULIU_CODE = 1206;
    /** 查询订单泡泡接口 **/
    int QUERY_ORDER_PAOPAO = 1207;
    /** 查询订单评论接口 */
    int GET_ORDER_COMMENT_DETAIL_CODE = 1208;

    /** 登陆接口 **/
    int USER_LOGIN_CODE = 1301;
    /** 获取店铺介绍接口 **/
    int GET_SHOP_INFO_CODE = 1303;
    /** 修改店铺信息接口 **/
    int CHANGE_SHOP_INFO_CODE = 1305;
    /** 查询我的结算银行卡接口 **/
    int QUERY_SETTLEMENT_BANK_CARD_CODE = 1306;
    /** 修改登录密码接口 **/
    int RESET_LOGIN_PWD_CODE = 1307;
    /** 找回登录密码接口 **/
    int FIND_LOGIN_PWD_CODE = 1308;
    /** 获取账户信息接口 **/
    int GET_USER_INFO_CODE = 1309;
    /** 查询交易记录接口-废弃 */
    @Deprecated
    int GET_TRADE_RECORD_CODE = 1310;
    /** 查询交易记录订单列表明细接口 */
    int GET_TRADE_RECORD_LIST_CODE = 1311;
    /** 提交商圈订单接口 */
    int SUBMIT_BUSINESS_TRADE_CODE = 1312;
    /** 商圈交易列表接口 **/
    int BUSIMESS_TRADE_LIST_CODE = 1313;
    /** 商圈交易明细接口 **/
    int BUSINESS_DETAIL_CODE = 1314;
    /** 商圈交易确认收货接口 */
    int BUSINESS_CONFIRM_RECEIVE_GOODS_CODE = 1315;
    /** 商圈取消交易接口 **/
    int BUSINESS_CANCEL_TRADE_CODE = 1316;
    /** 我的账户提现查询手续费等数据接口 **/
    int CHECK_WITH_DRAW_CODE = 1317;
    /** 我的账户提现接口 **/
    int SUBMIT_WITH_DRAW_CODE = 1318;
    /** 我的账户提现记录接口 **/
    int SUBMIT_WITH_DRAW_RECORD_CODE = 1319;
    /** 添加收货地址接口 **/
    int ADD_ADDRESS_CODE = 1320;
    /** 查询收货地址接口 **/
    int QUERY_ADDRESS_CODE = 1321;
    /** 删除收货地址接口 **/
    int DELETE_ADDRESS_CODE = 1322;
    /** 编辑收货地址接口 **/
    int EDIT_ADDRESS_CODE = 1323;
    /** 设置支付密码接口 **/
    int SET_PAY_PWD_CODE = 1324;
    /** 修改支付密码接口 **/
    int RESET_PAY_PWD_CODE = 1325;
    /** 找回支付密码接口 **/
    int FIND_PAY_PWD_CODE = 1326;
    /** 查询交易记录-新接口 */
    int GET_TRADE_RECORD_CODE_NEW = 1327;
    /** 账户明细接口 **/
    int GET_ACCOUNT_DETAIL_CODE = 1328;

    /** 注册接口 **/
    int REGIST_CODE = 1401;
    /** 注册经营品牌查询接口 **/
    int GET_CAR_BRAND_CODE = 1402;
    /** 获取短信验证码接口 **/
    int GET_AUTH_CODE = 1403;
    /** 检验新版本接口 **/
    int CHECK_UPDATE_CODE = 1404;
    /** 上传用户头像接口-userAccountId **/
    int UPLOAD_HEADER_CODE = 1406;
    /** 上传营业执照-法人身份证照片接口 **/
    int UPLOAD_LICENSE_CODE = 1407;
    /** 上传询价单图片接口 **/
    int UPLOAD_INQUIRY_PHOTO_CODE = 1408;
    /** 上传售后图片接口 **/
    int UPLOAD_POST_SALE_PHOTO_CODE = 1409;
    /** 上传店铺照片接口 **/
    int UPLOAD_SHOP_CODE = 1410;
    /** 获取营业执照接口 **/
    int GET_LICENSE_PIC_CODE = 1413;
    /** 获取询价单图片接口 **/
    int GET_INQUIRY_PIC_CODE = 1414;
    /** 查看上传的售后图片接口 **/
    int GET_POST_SALE_PHOTO_CODE = 1415;
    /** 获取店铺照片接口 **/
    int GET_SHOP_INTRO_PIC_CODE = 1416;
    /** 提交用户反馈接口 **/
    int SUBMIT_FEED_BACK_CODE = 1417;
    /** 联系我们和操作指南WebView接口 **/
    int GET_CONTACT_HELP_CODE = 1418;
    /** 获取首页图片接口 **/
    int GET_HOME_PICTURES_CODE = 1419;
    /** 删除店铺介绍照片接口 **/
    int DELETE_SHOP_IMAGE_CODE = 1420;
    /** 上传商圈照片接口 **/
    int UPLOAD_BUSIMESS_IMAGE_CODE = 1421;
    /** 聊天常用语接口 **/
    int CHAT_COMMEN_TEXT_CODE = 1422;
    /** VIN查询车型明细接口 **/
    int QUERY_CAR_BY_VIN_CODE = 1423;
    /** 根据品牌查询车系接口 **/
    int GET_CAR_SERIES_CODE = 1424;
    /** 全部品牌请求码 */
    int CAR_ALL_BRAND_CODE = 1425;
    /** 车系请求码 */
    int CAR_STYLE_CODE = 1426;
    /** 查询汽配商接口 **/
    int QUERY_SUPLIER_CODE = 1427;
    /** 热门城市接口 **/
    int HOT_CITIES = 1428;
    /** 注册专项件经营品牌查询接口 **/
    int GET_ZX_CAR_BRAND_CODE = 1429;
    /** 注册专项件根据经营品牌查询车系接口 **/
    int GET_ZX_CAR_SERIES_CODE = 1430;

    /** 售后进度查询-申请售后列表接口 **/
    int POST_SALE_PROGRESS_LIST_CODE = 1501;
    /** 售后进度查询-零件售后进度明细接口 **/
    int POST_SALE_PROGRESS_DETAIL_CODE = 1502;
    /** 售后-同意售后接口 **/
    int POST_SALE_AGREE_CODE = 1503;
    /** 售后-确认收货（如果是换货的话还包括发货）接口 **/
    int POST_SALE_RECEIVE_CODE = 1504;
    /** 售后纠纷申请客服介入接口 **/
    int SUBMIT_CALL_KEFU = 1505;
    /** 查询售后单聊天记录接口 */
    int GET_POST_SALE_MESSAGE_CODE = 1506;
    /** 售后-同意退款接口 **/
    int POST_SALE_AGREE_REFUND_CODE = 1507;

    /** 商圈发布新的消息接口 */
    int BUSINESSS_NEW_CODE = 1701;
    /** 商圈消息列表接口 **/
    int BUSINESS_LIST_CODE = 1702;
    /** 系统消息接口-和商圈消息使用同一个接口 **/
    int SYS_NOTICE_CODE = 1702;
    /** 获取商户介绍明细接口 **/
    int GET_SUPLIER_DETAIL_CODE = 1703;
    /** 删除商圈消息接口 **/
    int BUSINESS_MSG_DELETE_CODE = 1704;
    /** 商圈聊天列表接口 **/
    int BUSIMESS_CHAT_LIST_CODE = 1705;
    /** 商圈聊天记录接口 **/
    int BUSIMESS_CHAT_HISTORY_CODE = 1706;
    /** 商圈删除聊天记录接口 **/
    int BUSIMESS_CHAT_DELETE_CODE = 1707;

    /** 未绑定银行卡快捷支付，获取tn号接口 **/
    int GET_ORDER_TN_CODE = 1803;
    /** 获取银行卡预留手机号短信验证码接口 **/
    int GET_BANK_AUTH_CODE = 1805;
    /** 获取用户账户信息接口 **/
    int GET_USER_ACCOUNT_INFO_CODE = 1806;
    /** 订单快捷支付接口 **/
    int ORDER_PAY_CODE = 1808;
    /** 获取微信支付需要的参数接口 **/
    int GET_WX_PAY_PARAMS_CODE = 1809;
    /** 获取支付宝支付需要的参数接口 **/
    int GET_ALI_PAY_PARAMS_CODE = 1810;
    /** 绑定银行卡接口 **/
    int BIND_CARD_CODE = 1811;
    /** 查询银行卡接口 **/
    int QUERY_BANK_CARD_CODE = 1812;
    /** 删除银行卡接口 **/
    int DELETE_BANK_CARD_CODE = 1813;
    /** 余额支付接口 **/
    int BALANCE_PAY_CODE = 1814;
}
