package com.drive.student.config;

/** 系统全局配置 */
public interface Constant {
    /******************** 数据库名称 *******************/
    String DB_NAME = "drive_student.db";
    /******************** 科目一/四练习题文件名称 *******************/
    String SUBJECT_ONE_TXT = "subject_one.txt";
    String SUBJECT_FOUR_TXT = "subject_four.txt";
    /******************** 科目一/三练习题分类 *******************/
    /** 科目一练习题 */
    String SUBJECT_ONE_TRAIN = "subject_one_train";
    /** 科目四练习题 */
    String SUBJECT_FOUR_TRAIN = "subject_four_train";
    /** "0"-判断题 判断题只有answer_a和answei_b */
    String SUBJECT_JUDGE_TYPE = "0";
    /** "1"-选择题 */
    String SUBJECT_CHOICE_TYPE = "1";
    /** "2"-科目四多选题 */
    String SUBJECT_MULTIPLE_CHOICE_TYPE = "2";

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
    String WX_PAY_RESULT_ACTION = "drive.student.wxpayback.action";
    /******************** 是否支持支付宝支付 *******************/
    boolean ALI_PAY_ENABLE = true;
    /*******************支付************************/
    /** “00”–银联正式环境，“01”–银联测试环境，该环境中不发生真实交易 */
    String UPPAY_SERVER_MODE = "00";

    /** 推送升级消息 */
    String PUSH_UPDATE = "001";
    /*********************** 版本升级 ********************/
    int UPDATE_NEEDLESS = 1;
    int UPDATE_RECOMMEND = 2;
    int UPDATE_FORCE = 3;
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

    /***********************网络异常*******************************/
    /***** 返回码 SUCCESS ****/
    int RETURN_CODE_OK = 1;
    /** Token失效 **/
    int TOKEN_INVALIDATE = 804;
    /** 强制升级返回码 */
    int UPDATE_FORCE_RETURN_CODE = 807;

    /**** 服务异常 ****/
    int SER_ERR_CODE = 0;
    /***** 网络异常 ****/
    int NET_ERR_CODE = 1;
    /***** 空数据,或者自定义错误 ****/
    int EMP_ERR_CODE = 2;
    /************************ 系统设置 *******************/
    String IS_FIRST = "is_first";// 是否第一次运行
    String STORAGE_PATH = "storagepath";// 存储路径
    /** 录音-语音后缀名 */
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
    /** 最大上传照片张数 */
    int MAX_UPLOAD_PIC_SIZE = 20;
    /************ 拍照截图后压缩的需要上传的图 ****************/
    /** 单张图 */
    String KEY_UPLOAD_PIC_PATH = "key_upload_pic_path";
    /** 多张图 */
    String KEY_UPLOAD_PIC_PATH_LIST = "key_upload_pic_path_list";
    String IMAGE_DETAIL_PATH = "IMAGE_DETAIL_PATH";
    /** 传递的图片list地址 */
    String IMAGE_LIST_DETAIL_PATH = "IMAGE_LIST_DETAIL_PATH";
    /** 传递的图片list，点击的图片位置 */
    String IMAGE_LIST_DETAIL_CUR_POS = "IMAGE_LIST_DETAIL_CUR_POS";

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
}