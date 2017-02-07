package com.jajale.watch;

/**
 * 请求用 字符常量 统一放在这里
 * <p/>
 * <p/>
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class AppConstants {

    public static final int MaxIndex = 5000;
    public static final int GetNewOffset = 1 * 60 * 1000;


    public static final String API_VERSION = 1 + "";

    public static final int MAX_IMAGE_SIZE = 100;//限制最大上传图片100K

    public static final String S_KEY = "pneumonoultramicroscopicsilicovolcanoconiosis";
    public static final String SECRET_KEY = "yhgt!d%sd*aw%dsa#mng~dsq";

    public static final String CHANNEL_KEY = "";

    //数据库名称
    public static final String ACCOUNT_HELPER_NAME = "JJLAccount";

    public static final String SYSTEM_WATCH_ID = "000";

    //===================================================================================//

//    public static final String FORMAL_DOMAIN = "http://www.jajale.com/";   //正式线上环境


    public static final String TAG_CRASH_HANDLER = "tag_crash_handler";

    public static String QINIU_MEDIA_DOMAIN = "http://7xosdf.com2.z0.glb.qiniucdn.com/";
    public static String QINIU_IMG_DOMAIN = "http://7xosde.com2.z0.glb.qiniucdn.com/";

    public static String DADI_PHONE = "95590";
    public static String DADI_NET = "http://www.95590.cn/ebiz/view/insuranceClaim/claimMain.jsp?showflag=1#";

    //===========================================================================================/
    //                    socket  端口 及 ip(新)

    public static final String TEST_SOCKET_IP = "120.76.47.120";
    public static final int TEST_SOCKET_PORT = 9292;
//    public static final String TEST_SOCKET_IP = "192.168.1.125";
//    public static final int TEST_SOCKET_PORT = 9999;

    public static final String EXTRA_SOCKET_IP = "120.76.47.120";
    public static final String EXTRA_SOCKET_HOST = "s.bjjajale.com";
    public static final int EXTRA_SOCKET_PORT = 9292;

    public static final String SOCKET_IP = PublicSwitch.isFormal ? EXTRA_SOCKET_IP : TEST_SOCKET_IP;
    public static final int SOCKET_PORT = PublicSwitch.isFormal ? EXTRA_SOCKET_PORT : TEST_SOCKET_PORT;


    //===================================================================================//
    //                     JAVA     服务器请求

    public static final String EXTRA_JAVA_URL_DOMAIN_API = "http://ap.bjjajale.com:8801/";//线上接口API
    public static final String EXTRA_JAVA_URL_DOMAIN_CENTER = "http://ap.bjjajale.com:8802/";//线上接口CENTER
    public static final String EXTRA_JAVA_URL_DOMAIN_CONTROLLER = "http://shequ.bjjajale.com:8821/";//线上接口CONTROLLER

    //    public static final String TEST_JAVA_URL_API = "http://192.168.1.125:8082/";
//    public static final String TEST_JAVA_URL_CENTER = "http://192.168.1.125:8083/";
//    public static final String TEST_JAVA_URL_API = "http://192.168.1.110:8080/api/";泽辉接口
    public static final String TEST_JAVA_URL_API = "http://61.50.105.128:20004/api/";
    public static final String TEST_JAVA_URL_CENTER = "http://61.50.105.128:20004/";
    public static final String TEST_JAVA_URL_CONTROLLER = "http://shequ.bjjajale.com:8821/";
//    public static final String TEST_JAVA_URL_CONTROLLER = "http://61.50.105.128:8821/";
//    public static final String TEST_JAVA_URL_CONTROLLER = "http://192.168.1.110:8083/";


    //以下为各个java后台程序员本机测试接口
    public static final String TEST_JAVA_URL_YAJUN = "http://192.168.1.113:8080/";
    public static final String TEST_JAVA_URL_ZUODONG = "http://192.168.1.111:8080/";
    public static final String TEST_JAVA_URL_XIANGYANG = "http://192.168.1.113:8080/";
    public static final String TEST_JAVA_URL_DAHUI = "http://192.168.1.113:8080/";


    public static final String MODULE_INSURANCE_CONTROL = "ucInsuranceControl/";//保险
    public static final String MODULE_INSURANCE = "insurance/";//保险
    public static final String INSURACNE_IS_EXIST_DOMAIN = MODULE_INSURANCE_CONTROL + "isExist.do";//保险开关
    public static final String INSURANCE_CHECK_STATUS = MODULE_INSURANCE + "get.do";//保险获取
    public static final String INSURANCE_INSERT = MODULE_INSURANCE + "insert.do";//保险
    public static final String UNICOM_AGREEMENT = "http://cms.bjjajale.com/cms/insurance/proto.do?i=6";//联通协议web接口


    //用户相关
    public static final String MODULE_API_USER_CONTROL = "apiUserControl/";//用户相关
    public static final String JAVA_LOGIN_URL = MODULE_API_USER_CONTROL + "login.do";//用户登录
    public static final String JAVA_IS_REGISTER_URL = MODULE_API_USER_CONTROL + "isRegister.do";//检测是否注册
    public static final String JAVA_REGISTER_URL = MODULE_API_USER_CONTROL + "register.do";//用户注册
    public static final String JAVA_BIND_WATCH_URL = MODULE_API_USER_CONTROL + "bindWatch.do";//绑定手表
    public static final String JAVA_GET_WATCH_LIST_URL = MODULE_API_USER_CONTROL + "getWatchList.do";//获取手表信息
    public static final String JAVA_GET_FAMILY_LIST_URL = MODULE_API_USER_CONTROL + "getFamilyList.do";//获取家庭成员列表
    public static final String JAVA_RESET_PASSWORD_URL = MODULE_API_USER_CONTROL + "resetPassword.do";//重置密码
    public static final String JAVA_GET_SOS_MSG_URL = MODULE_API_USER_CONTROL + "getSosMsg.do";//获取报警信息
    public static final String JAVA_EDIT_USER_URL = MODULE_API_USER_CONTROL + "update.do";//编辑用户信息
    public static final String JAVA_EDIT_BABY_URL = MODULE_API_USER_CONTROL + "update.do";// 编辑宝贝信息

    //权限管理
    public static final String MODULE_API_USER_RELATION_CONTROL = "apiUserRelationControl/";//成员关系管理
    public static final String JAVA_MOVE_MANAGE_URL = MODULE_API_USER_RELATION_CONTROL + "moveManage.do";//移交管理员权限
    public static final String JAVA_DELETE_RELATION_URL = MODULE_API_USER_RELATION_CONTROL + "deleteRelation.do";//解除绑定
    public static final String JAVA_INSERT_RELATION_URL = MODULE_API_USER_RELATION_CONTROL + "insert.do";//设置家庭成员关系


    //成长
    public static final String MODULE_BABY_GROWTH_CONTROL = "apiBabyGrowthControl/";//成长相关
    public static final String JAVA_BABY_GROWTH_LIST_URL = MODULE_BABY_GROWTH_CONTROL + "getApiBabyGrowthListByParams.do";//成长记录列表
    public static final String JAVA_DEL_BABY_GROWTH_URL = MODULE_BABY_GROWTH_CONTROL + "delApiBabyGrowthInfo.do";//删除成长记录
    public static final String JAVA_ADD_BABY_GROWTH_URL = MODULE_BABY_GROWTH_CONTROL + "addApiBabyGrowthInfo.do";//添加宝贝成长记录
    public static final String JAVA_EDIT_BABY_GROWTH_URL = MODULE_BABY_GROWTH_CONTROL + "editApiBabyGrowthInfo.do";//编辑宝贝成长记录


    //消息
    public static final String MODULE_MSG_CONTROLLER = "msgController/";//消息相关
    public static final String JAVA_MSG_CONTROLLER_SEND_MESSAGE = MODULE_MSG_CONTROLLER + "sendMsg.do";//发送消息
    public static final String JAVA_MSG_CONTROLLER_GET_MESSAGE = MODULE_MSG_CONTROLLER + "getMsgList.do";//拉取消息(列表)


    //手表配置
    public static final String MODULE_WATCH_CONFIG = "watchConfig/";//手表配置
    public static final String JAVA_WATCH_SET_WATCH_CLOCK_URL = MODULE_WATCH_CONFIG + "setApiWatchClock.do";//闹钟
    public static final String JAVA_WATCH_FIND_WATCH_URL = MODULE_WATCH_CONFIG + "findWatch.do";//找手表
    public static final String JAVA_WATCH_DISTURB_URL = MODULE_WATCH_CONFIG + "disturb.do";//免打扰
    public static final String JAVA_WATCH_SOS_PHONE_URL = MODULE_WATCH_CONFIG + "sos_phone_set.do";//SOS设置
    public static final String JAVA_WATCH_PHONE_BOOK_URL = MODULE_WATCH_CONFIG + "phone_book.do";//电话簿
    public static final String JAVA_WATCH_SWITCH_STEP_URL = MODULE_WATCH_CONFIG + "switch_step.do";//计步器
    public static final String JAVA_WATCH_GPS_MODE_URL = MODULE_WATCH_CONFIG + "gpsMode.do";//定位模式
    public static final String JAVA_WATCH_POWER_OFF_URL = MODULE_WATCH_CONFIG + "power_off.do";//关机指令
    public static final String JAVA_WATCH_MONITOR_URL = MODULE_WATCH_CONFIG + "monitor.do";//聆听指令
    public static final String JAVA_WATCH_POSITION_URL = MODULE_WATCH_CONFIG + "cr.do";//聆听指令


    //手表信息获取
    public static final String MODULE_WATCH_CONFIG_CONTROL = "apiWatchConfigControl/";//手表信息获取
    public static final String JAVA_WATCH_CLOCK_LIST_URL = MODULE_WATCH_CONFIG_CONTROL + "getWatchClockList.do";//获取闹钟列表
    public static final String JAVA_WATCH_DISTURB_LIST_URL = MODULE_WATCH_CONFIG_CONTROL + "getWatchDisturbList.do";//获取免打扰列表
    public static final String JAVA_WATCH_SOS_LIST_URL = MODULE_WATCH_CONFIG_CONTROL + "getWatchSOSList.do";//获取SOS列表
    public static final String JAVA_WATCH_PHONE_BOOK_LIST_URL = MODULE_WATCH_CONFIG_CONTROL + "getWatchPhoneBookList.do";//获取电话簿列表

    public static final String JAVA_WATCH_ADD_SAFE_AREA_URL = MODULE_WATCH_CONFIG_CONTROL + "addWatchSafeArea.do";//添加安全围栏
    public static final String JAVA_WATCH_EDIT_SAFE_AREA_URL = MODULE_WATCH_CONFIG_CONTROL + "upWatchSafeArea.do";//编辑安全围栏
    public static final String JAVA_WATCH_DELETE_SAFE_AREA_URL = MODULE_WATCH_CONFIG_CONTROL + "delWatchSafeArea.do";//删除安全围栏
    public static final String JAVA_WATCH_GET_WATCH_SAFE_AREA_LIST_URL = MODULE_WATCH_CONFIG_CONTROL + "getWatchSafeAreaList.do";//安全围栏列表


    //疫苗
    public static final String MODULE_VACCINE_CONTROL = "apiVaccineControl/";//疫苗相关
    public static final String MODULE_VACCINE_BABY_CONTROL = "apiVaccineBabyControl/";//疫苗用户相关
    public static final String JAVA_VACCINE_GET_AGES_LIST_URL = MODULE_VACCINE_CONTROL + "getAgesList.do";//获取疫苗月/年龄列表
    public static final String JAVA_VACCINE_DATA_LIST_URL = MODULE_VACCINE_BABY_CONTROL + "getApiVaccinesByUserIdAndVaccineId.do";//获取宝贝月/年龄下疫苗数据
    public static final String JAVA_VACCINE_INSERT_UPDATE_URL = MODULE_VACCINE_BABY_CONTROL + "insertOrUpdate.do";//]编辑宝贝疫苗接种情况


    //资讯
    public static final String MODULE_API_INFO_RMATION_CONTROL = "mgInformationControl/";//资讯相关
    public static final String JAVA_INFOR_MATION_LIST_URL = MODULE_API_INFO_RMATION_CONTROL + "getInformationList.do";//资讯列表
    public static final String JAVA_INFOR_MATION_DETAIL_URL = MODULE_API_INFO_RMATION_CONTROL + "getInformationdetail.do";//资讯内容页


    //实名认证
    public static final String MODULE_WATCH_CERTIFICATION_CONTROL = "apiWatchCertificationControl/";//实名认证
    public static final String JAVA_UPLOAD_CERTIFICATION_DATA_URL = MODULE_WATCH_CERTIFICATION_CONTROL + "uploadCertificationData.do";//上传实名认证数据
    public static final String JAVA_IS_CERTIFICATION_URL = MODULE_WATCH_CERTIFICATION_CONTROL + "isCertification.do";//是否需要实名认证


    //其他
    public static final String JAVA_VALIDATE_KEY_URL = "apiCmsValidateControl/validateKey.do";//发送短信验证码
    public static final String JAVA_GET_API_FEED_BACK_BY_PARAMS_URL = "apiFeedBackControl/addUserApiFeedBack.do";//用户反馈
    public static final String JAVA_GET_ALL_API_QA_URL = "apiQaControl/getAllApiQa.do";//常见问题
    public static final String JAVA_GET_ENTRANCE_URL = "/apiController/entrance.do";//使用说明
    public static final String JAVA_APP_UPDATE_URL = "apiAppVersionControl/appUpdate.do";//版本更新
    public static final String JAVA_EDUCATION_BOOK_URL = "http://cms.bjjajale.com/cms/books/book_list.do";//教育--书籍
    public static final String JAVA_TRAJECTORY_LIST_URL = "apiWatchReportDataControl/trajectoryList.do";//历史轨迹
    public static final String JAVA_INFOR_MATION_IMG_URL = "apiAppAdControl/informationImg.do";//资讯广告轮播图
    public static final String JAVA_SPLASH_IMG_URL = "apiAppAdControl/show.do";//资讯广告轮播图

    //含有Code
    public static final String JAVA_HAS_CODE_URL = "apiController/entrance.do";


    //===================================================================================//
    //        分享

    public static final String WX_APP_ID = "wx0e35eaa7a9ec83e2";

    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static final String SINA_APP_KEY = "2941940417";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p/>
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    public static final String SINA_SCOPE = "";

//    =======================================================================================

    public static final String PLATFORM = "0";
    //========================================================================================//
//    数据迁移接口

    public static final String MOVE_DATA_CHECK_USER = "http://move.bjjajale.com/index/checkuser";//    是否数据迁移
    public static final String MOVE_DATA_MV = "http://move.bjjajale.com/index/mv";//    数据迁移

    //ximalaya的AppSecret
    public static final String mAppSecret="1dd13280298914de5af0408261b74b31";

}
