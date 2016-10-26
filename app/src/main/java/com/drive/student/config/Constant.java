package com.drive.student.config;

/** 系统全局配置 */
public interface Constant {
    /******************** 商圈全车件/专项件选择汽配商 *******************/
    /** 全车件选择汽配商 */
    String BUSINESS_QUANCHE_TYPE = "0";
    /** 专项件选择汽配商 */
    String BUSINESS_ZHUANXIANG_TYPE = "1";
    /******************** VIN车架号详情 *******************/
    /** VIN 的长度 */
    int VIN_LENGTH = 17;
    /** VIN键盘 */
    String KEYBOARD_KEYS[] = new String[]{
            "0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E",
            "F", "G", "H", "J", "K",
            "L", "M", "N", "P", "R",
            "S", "T", "U", "V", "W",
            "X", "Y", "Z", "清空", "删除"
    };
    /******************** 微信支付 *******************/
    /** 微信支付APP_ID */
    String WX_APP_ID = "wx4175596d6df0fc73";
    /** 支付成功-正确返回 */
    int WX_PAY_ERR_OK = 0;
    /** 支付失败-一般错误 */
    int WX_PAY_ERR_COMM = -1;
    /** 支付失败-支付取消 */
    int WX_PAY_ERR_USER_CANCEL = -2;
    /** 支付失败-发送失败 */
    int WX_PAY_ERR_SENT_FAILED = -3;
    /** 支付失败-认证被否决 */
    int WX_PAY_ERR_AUTH_DENIED = -4;
    /** 支付失败-不支持错误 */
    int WX_PAY_ERR_UNSUPPORT = -5;
    /** 是否支持微信支付 */
    boolean WX_PAY_ENABLE = true;
    /** 微信支付回调 */
    String WX_PAY_RESULT_ACTION = "supplier.wxpayback.action";
    /******************** 是否支持支付宝支付 *******************/
    boolean ALI_PAY_ENABLE = true;
    /****************** 登陆成功后多长时间内不显示推送提示Notification ************************/
    int NOTIFICATION_NOT_SHOW_TIME_AFTER_LOGIN = 60 * 1000;
    /*********** 登陆用户信息：主账户和子账户 ******/
    String USER_MAIN_ACCOUNT = "1";
    String USER_CHILD_ACCOUNT = "0";
    /*********** 询价单隐藏键盘 ******/
    String INQUIRY_DETAIL_HIDE_SOFT = "supplier.hidesoft.action";
    /******************* 刷新首页数量 ************************/
    String HOME_REFRESH_NUM_ACTION = "supplier.home.refreshnum.action";
    /*******************支付************************/
    /** “00”–银联正式环境，“01”–银联测试环境，该环境中不发生真实交易 */
    String UPPAY_SERVER_MODE = "00";
    /******************* 控制个人中心是否显示售后有新的消息 ************************/
    String POST_SALE_SHOW_MSG_NOTICE_ACTION = "supplier.show_postsale.notice.action";
    String POST_SALE_HIDE_MSG_NOTICE_ACTION = "supplier.hide_postsale.notice.action";
    /******************* 售后模块 ************************/
    /* 1、等待卖家同意
     * 2、卖家同意、等待买家回寄
	 * 3、有异议,平台介入 
	 * 4、买家已发货 
	 * 5、卖家收到货、同意退款 
	 * 6、卖家收货,并发货（换货）
	 * 7、买家签收（换货）
	 * 8,已完成
	 */
    String PS_WAIT_SALER_AGREE = "1";
    String PS_SALER_AGREE_WAIT_SEND = "2";
    String PS_NEED_KEFU = "3";
    String PS_BUYER_SEND_GOODS = "4";
    String PS_SALER_RECEIVE_GOODS = "5";
    String PS_SALER_SEND_NEW_GOODS = "6";
    String PS_BUYER_RECEIVE_NEW_GOODS = "7";
    String PS_COMPLETE = "8";
    /** 刷新售后单列表 */
    String REFREH_POSTSALE_ACTION = "supplier.refresh_ps.action";
    /*******************商圈************************/
    /** 刷新商圈列表 */
    String BUSINESS_REFRESH_AREA_ACTION = "supplier.refresh_business_area.action";
    /** 刷新商圈聊天列表 */
    String BUSINESS_REFRESH_CHAT_LIST_ACTION = "supplier.refresh_business_chat.action";
    /** 刷新商圈交易列表 */
    String BUSINESS_REFRESH_TRADE_LIST_ACTION = "supplier.refresh_business_trade.action";
    /** 查询所有商圈消息 */
    String BUSINESS_ALL_MSG = "0";
    /** 商圈我发布的消息 */
    String BUSINESS_MY_MSG = "1";
    /** 商圈系统发布的消息 */
    String BUSINESS_SYSTEM_MSG = "2";
    /*******************询价单图片类型-************************/
    /** 类型	picType	String		1、修理厂，2配件商 */
    String INQUIRY_PHOTO_BUYER = "1";
    /** 类型	picType	String		1、修理厂，2配件商 */
    String INQUIRY_PHOTO_SALER = "2";
    /******************* 在线聊天 ************************/
    /** 买家消息 */
    String CHAT_MESSAGE_TYPE_BUYER = "0";
    /** 卖家消息 */
    String CHAT_MESSAGE_TYPE_SALER = "1";
    /** 系统消息 */
    String CHAT_MESSAGE_TYPE_SYSTEM = "2";
    /** 发送方消息 */
    String CHAT_MESSAGE_TYPE_SENDER = "3";
    /** 接收方消息 */
    String CHAT_MESSAGE_TYPE_RECEIVER = "4";
    /** 普通消息 */
    String CHAT_NORMAL_MESSAGE = "1";
    /** 语音消息 */
    String CHAT_VOICE_MESSAGE = "2";
    /** 单发照片 */
    String CHAT_IMAGE_MESSAGE = "3";
    /** 群发照片 */
    String CHAT_IMAGE_GROUP_MESSAGE = "4";
    /** 商圈交易信息 */
    String CHAT_BUSINESS_TRADE = "5";
    /** 文件信息 */
    String CHAT_FILE_MESSAGE = "6";
    /*******************获取验证码类型************************/
    /** 注册的时候获取短信验证码 */
    int AUTH_CODE_REGIST = 1;
    /** 非注册的时候获取短信验证码 */
    int AUTH_CODE_OTHER = 2;
    /** 找回登陆密码获取短信验证码 */
    int AUTH_CODE_FIND_LOGIN_PWD = 3;
    /*******************推送************************/
    /** 商圈收到新的聊天消息 */
    String RECEIVE_BUSINESS_MSG_ACTION = "supplier.business.msg.action";
    /** 询价单收到新的留言 */
    String RECEIVE_INQUIRY_MSG_ACTION = "supplier.inquiry.receive.msg.action";
    /** 订单收到新的聊天消息 */
    String RECEIVE_ORDER_MSG_ACTION = "supplier.order.msg.action";
    /** 收到订单支付 */
    String RECEIVE_ORDER_PAY_ACTION = "supplier.receive.orderpay.action";
    /** 售后单收到新的留言 */
    String RECEIVE_MESSAGE_POST_ACTION = "supplier.receive.postmsg.action";
    /** 刷新物流 */
    String RECEIVE_REFRESH_WULIU_ACTION = "supplier.receive.refreshwuliu.action";
    String RECEIVE_UPLOAD_WULIU_ACTION = "supplier.receive.uploadwuliu.action";
    /** 售后状态更新通知 */
    String RECEIVE_POST_SALE_ACTION = "supplier.receive.postsale.action";
    int NOTIFY_ORDER_PAY = 0x1;
    int NOTIFY_NEW_INQUIRY = 0x3;
    int NOTIFY_NEW_ORDER = 0x4;
    int NOTIFY_POST_SALE_NEW_STATUS = 0x5;
    int NOTIFY_POST_SALE_NEW_MESSAGE = 0x6;
    int NOTIFY_ORDER_CHANGE_PRICE = 0x7;
    int NOTIFY_BUSINESS_NEW_MESSAGE = 0x8;
    int NOTIFY_ORDER_NEW_MESSAGE = 0x9;
    int NOTIFY_INQUIRY_NEW_MESSAGE = 0x10;

    /** 推送升级消息 */
    String PUSH_UPDATE = "001";
    /** 推送新的询价单 */
    String PUSH_NEW_INQUIQY = "101";
    /** 推送买家新的回复 */
    String PUSH_INQUIRY_CHAT = "102";
    /** 推送新的订单 */
    String PUSH_NEW_ORDER = "201";
    /** 推送订单付款 */
    String PUSH_ORDER_PAY = "202";
    /** 推送订单聊天消息 */
    String PUSH_ORDER_CHAT = "206";
    /** 推送退换货申请 */
    String PUSH_NEW_POST_SALE = "301";
    /** 推送退换货受理有新的动态 */
    String PUSH_POST_SALE_NEW_STATUS = "302";
    /** 推送退换货受理有新的消息 */
    String PUSH_POST_SALE_NEW_MESSGE = "303";
    /** 推送-给后台发消息重置系统消息 */
    String PUSH_SYS_MSG_BACK = "901";
    /** 推送-给后台发消息重置聊天 */
    String PUSH_MSG_BACK = "902";
    /** 推送商圈聊天消息 */
    String PUSH_BUSINESS_CHAT = "501";//TODO
    /*********************** 版本升级 ********************/
    int UPDATE_NEEDLESS = 1;
    int UPDATE_RECOMMEND = 2;
    int UPDATE_FORCE = 3;
    /***********************订单状态********************/
//	1，买家提交订单，2，买家已经付款，3，买家确认收货 4，售后中，5、卖家上传物流单证 
    /** 买家提交订单 */
    String SUBMIT_ORDER = "1";
    /** 买家已经付款 */
    String PAY_ORDER = "2";
    /** 买家确认收货 */
    String RECEIVED_GOODS = "3";
    /** 售后中 */
    String POST_SALE_IN = "4";
    /** 卖家上传物流单证 */
    String SEND_GOODS = "5";
    /*********************** 汽配商零件报价明细 ********************/
    /*1为原厂价格 2为品牌价格 3为副厂价格 4为翻新价格*/
    String YUAN_CHANG = "1";
    String PIN_PAI = "2";
    String FU_CHANG = "3";
    String CHAI_CHE = "4";
    /*********************** SelectAreaActivity 选择城市 ********************/
    int SELECT_AREA_DISTANCE_TYPE = 0;
    int SELECT_AREA_CITY_TYPE = 1;
    int SELECT_AREA_PROVINCE_TYPE = 2;
    String SELECT_AREA_TYPE = "SELECT_AREA_TYPE";
    String SELECT_AREA_PROVINCE = "SELECT_AREA_PROVINCE";
    String SELECT_AREA_CITY = "SELECT_AREA_CITY";
    String SELECT_AREA_DISTANCE = "SELECT_AREA_DISTANCE";
    String SELECT_AREA_PROVINCE_CODE = "SELECT_AREA_PROVINCE_CODE";
    String SELECT_AREA_CITY_CODE = "SELECT_AREA_CITY_CODE";
    String SELECT_AREA_DISTANCE_CODE = "SELECT_AREA_DISTANCE_CODE";
    /*********************** Image ViewPager ********************/
    String VPAGER_PIC_LIST = "PIC_LIST";// 图片列表
    String VPAGER_PIC_TITLE = "VPAGER_PIC_TITLE";// 图片列表
    /***** 输入框和备注跳转到InputActivity的标志 ***********/
    String JUMP_INPUT_TYPE = "JUMP_INPUT_TYPE";
    String JUMP_TITLE = "JUMP_TITLE";
    String JUMP_HINT = "JUMP_HINT";
    /** 传回去的数据 */
    String INPUT_RETURN_TEXT = "INPUT_RETURN_TEXT";
    /** 传递的数据 */
    String JUMP_TRANSVER_TEXT = "JUMP_TRANSVER_TEXT";
    /** 询价单明细-单个零件报价的备注 */
    int SHOW = 3;

    /***********************网络异常*******************************/
    /***** 返回码 SUCCESS ****/
    int RETURN_CODE_OK = 1;
    /***** 返回码 用户名或密码错误 ****/
    int RETURN_USER_PWD_ERR = 803;
    /** Token失效 **/
    int TOKEN_INVALIDATE = 804;
    /** 该手机号码已注册，请更换其它手机号码 */
    Integer USER_ERR_REG = 805;
    /** 身份证号码验证失败！ */
    Integer USER_ERR_PSN = 806;
    /** 强制升级返回码 */
    int UPDATE_FORCE_RETURN_CODE = 807;
    /***** 返回码 验证码错误 ****/
    int RETURN_ERR_AUTH_CODE = 994;

    /**** 服务异常 ****/
    int SER_ERR_CODE = 0;
    /***** 网络异常 ****/
    int NET_ERR_CODE = 1;
    /***** 空数据,或者自定义错误 ****/
    int EMP_ERR_CODE = 2;
    /*********************** 定位 *******************************/
    String SHOW_MAP_NOTICE = "show_map_notice";
    /** 纬度 */
    String LATITUDE = "latitude";
    /** 经度 */
    String LONGITUDE = "longitude";
    String ADDRESS = "address";
    String PROVINCE_S = "province";// 省
    String CITY_S = "city";// 市
    String CITY_S_CODE = "city_code";// 市
    String SUPLIER_PROVINCE = "SUPLIER_PROVINCE";// 汽配商所在省
    String SUPLIER_PROVINCE_CODE = "SUPLIER_PROVINCE_CODE";// 汽配商所在省
    String SUPLIER_CITY = "SUPLIER_CITY";// 汽配商所在市
    String SUPLIER_CITY_CODE = "SUPLIER_CITY_CODE";// 汽配商所在市
    String SUPLIER_DISTRICT = "SUPLIER_DISTRICT";// 汽配商所在区县
    String SUPLIER_DISTRICT_CODE = "SUPLIER_DISTRICT_CODE";// 汽配商所在区县
    String DISTRICT = "district";// 区县
    String DISTRICT_CODE = "district_code";// 区县
    String STREET = "street";// 街道
    String STREET_NUMBER = "streetNumber";// 街道号
    String DETAIL_LOCATION = "detail_location";// 具体地址
    /** 定位经度坐标 */
    String LONGITUDE_AUTO = "longitude";
    /** 定位纬度坐标 */
    String LATITUDE_AUTO = "latitude";
    /**************** 我的店铺 ***************************/
    String CHANGE_SHOP_INFO_RETURN_PROVUNCE = "CHANGE_SHOP_INFO_RETURN_PROVUNCE";
    String CHANGE_SHOP_INFO_RETURN_CITY = "CHANGE_SHOP_INFO_RETURN_CITY";
    String CHANGE_SHOP_INFO_RETURN_AREA = "CHANGE_SHOP_INFO_RETURN_AREA";
    String CHANGE_SHOP_INFO_RETURN_ADDRESS = "CHANGE_SHOP_INFO_RETURN_ADDRESS";
    /**************** 修改用户信息页面 ***************************/
    String CHANGE_SHOP_INFO_RETURN = "CHANGE_SHOP_INFO_RETURN";
    String CHANGE_SHOP_INFO_TITLE = "CHANGE_SHOP_INFO_TITLE";
    String CHANGE_SHOP_INFO_TYPE = "CHANGE_SHOP_INFO_TYPE";
    String CHANGE_SHOP_NAME = "CHANGE_SHOP_NAME";
    String CHANGE_SHOP_MOBILE = "CHANGE_SHOP_MOBILE";
    String CHANGE_SHOP_TEL = "CHANGE_SHOP_TEL";
    String CHANGE_SHOP_MAIL = "CHANGE_SHOP_MAIL";
    String CHANGE_SHOP_FAX = "CHANGE_SHOP_FAX";
    String CHANGE_SHOP_INTRO = "CHANGE_SHOP_INTRO";
    String CHANGE_SHOP_INFO_TRANSFER = "CHANGE_SHOP_INFO_TRANSFER";
    /*******************收货地址***************************/
    /** 编辑地址 */
    String FROM_EDIT_ADDRESS = "FROM_EDIT_ADDRESS";
    /** 添加地址 */
    String FROM_ADD_ADDRESS = "FROM_ADD_ADDRESS";
    /** 管理地址类型 */
    String ADDRESS_FROM_TYPE = "ADDRESS_FROM_TYPE";
    /** 选择收货地址 */
    String CHOSE_ADDRESS = "CHOSE_ADDRESS";
    /** 来自询价单选择收货地址 */
    String ADDRESS_FROM_INQUIRY_DETAIL = "ADDRESS_FROM_INQUIRY_DETAIL";
    String ADDRESS_RETURN_DATA = "ADDRESS_RETURN_DATA";
    /************************ 系统设置 *******************/
    String LOGIN_SET = "login_set";// 登录设置
    String IS_FIRST = "1.0.0_is_first";// 是否第一次运行
    String ENCODING = "UTF-8";// 编码格式
    String STORAGE_PATH = "storagepath";// 存储路径
    /************ 清除密码 ****************/
    String CLEAR_PSW = "clear_psw";
    /** 录音-语音后缀名 */
//	public static final String VOICE_SUFFIX = ".amr";// 存储路径
    String VOICE_SUFFIX = ".mp3";// 存储路径
    /**
     * sampleRateInHz the sample rate expressed in Hertz. 44100Hz is currently the only
     * rate that is guaranteed to work on all devices, but other rates such as 22050,
     * 16000, and 11025 may work on some devices.
     */
    int DEFAULT_SAMPLING_RATE = 22050;
    /** brate compression ratio in KHz */
    int BIT_RATE = 16;
    /************ 最大上传照片或文件数量大小 ****************/
    /** 最大上传文件大小 */
    long MAX_UPLOAD_FILE_SIZE = 20 * 1024 * 1024;
    /** 最大上传文件大小 */
    String MAX_UPLOAD_FILE_SIZE_STR = "20M";
    /** 最大上传照片张数 */
    int MAX_UPLOAD_PIC_SIZE = 20;
    /** 单次最多选择9张照片 */
    int MAX_SELECT_PIC_SIZE = 9;
    /** 商圈最多上传9张照片 */
    int MAX_BUSINESS_UPLOAD_PIC_SIZE = 9;
    /************ 拍照截图后压缩的需要上传的图 ****************/
    /** 单张图 */
    String KEY_UPLOAD_PIC_PATH = "key_upload_pic_path";
    /** 多张图 */
    String KEY_UPLOAD_PIC_PATH_LIST = "key_upload_pic_path_list";
    /******************** 拍照 裁剪 选择图片 *******************/
    int CHOSE_IMAGE = 100;
    int TAKE_PHOTO = 101;
    int CUP_PIC_CODE = 102;
    int SELECT_PICTURE = 103;
    int UPLOAD_FILE = 104;
    /************ ImageDetail查看大图 ****************/
    int IMAGE_DETAIL_FROM_IMAGE_WATCHER = 1;
    int IMAGE_DETAIL_FROM_TAKE_PHOTO = 2;
    int IMAGE_DETAIL_WATCHER = 10;
    int IMAGE_DETAIL_TAKE_PHOTO = 11;
    String IMAGE_DETAIL_SHOW_DELETE = "IMAGE_DETAIL_SHOW_DELETE";
    String IMAGE_DETAIL_DELETE_REQUEST_CODE = "IMAGE_DETAIL_DELETE_REQUEST_CODE";
    String IMAGE_DETAIL_FROM_TYPE = "IMAGE_DETAIL_FROM_TYPE";
    String IMAGE_DETAIL_PATH = "IMAGE_DETAIL_PATH";
    String DELETE_PIC_URL = "DELETE_PIC_URL";
    String IMAGE_DETAIL_ID = "IMAGE_DETAIL_ID";
    /** 删除图片list地址 */
    String DELETE_PIC_LIST_URL = "DELETE_PIC_LIST_URL";
    /** 传递的图片list地址 */
    String IMAGE_LIST_DETAIL_PATH = "IMAGE_LIST_DETAIL_PATH";
    /** 传递的图片list，点击的图片位置 */
    String IMAGE_LIST_DETAIL_CUR_POS = "IMAGE_LIST_DETAIL_CUR_POS";
    /** 是否显示删除图片 */
    String IMAGE_LIST_DETAIL_DELETABLE = "IMAGE_LIST_DETAIL_DELETABLE";
    /************ 省份 *****************/
    int PROVINCE = 1;
    /************ 城市 *****************/
    int CITY = 2;
    /************ 县区 *****************/
    int AREA = 3;

    /************************** HTTP请求配置 ***************************/
    String CONTENT_TYPE_XML = "text/xml;charset=GBK";
    String CONTENT_TYPE_HTML = "text/html;charset=GBK";
    String CONTENT_TYPE_PLAIN = "text/plain;charset=GBK";
    String CONTENT_TYPE_JSON = "text/json;charset=UTF-8";
    String ACCEPT_ENCODING_GZIP = "gzip, deflate";
    String ENCODING_UTF = "UTF-8";
    String REQUEST_POST = "POST";
    String REQUEST_GET = "GET";
    int BUFFER_SIZE = 1024 * 8;
    int TIMEOUT = 40 * 1000;

    /******************** 发票 *********************************/
    String INVOICE_ZENGZHI_TYPE = "0";
    String INVOICE_COMMON_TYPE = "1";
}