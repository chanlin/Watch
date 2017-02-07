package com.jajale.watch;

/**
 *
 * 用于存放所有的 intent Action Key
 *
 * Created by athena on 2015/11/13.
 * Email: lizhiqiang@bjjajale.com
 */
public class IntentAction {

    public static final String KEY_MESSAGE_TYPE = "key_message_type";
    public static final String KEY_MESSAGE_WATCHID= "key_message_watchid";
    public static final String KEY_USER_NAME = "key_user_name";
    public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_WATCH_ID = "key_watch_id";
    public static final String KEY_WATCH_RELATION = "key_watch_relation";
    public static final String KEY_REPEAT_DATE_BOOLEAN_ARRAY = "key_repeat_date_boolean_array";
    public static final String KEY_IS_MANAGE = "key_is_manage";

    public static final String KEY_CLOCK_POSITION = "key_colck_position";//闹钟position
    public static final String KEY_CLOCK_ONOFF = "key_colck_onoff";//闹钟开关
    public static final String KEY_CLOCK_CODE = "key_colck_code";//闹钟指令

    public static final String KEY_WATCH_POSITION = "key_watch_position";//手表position

    public static final String KEY_NOT_DISTURB_POSITION = "key_not_disturb_position";//免打扰position
    public static final String KEY_NOT_DISTURB_ONOFF  = "key_not_disturb_onoff";//免打扰开关

    public static final String KEY_IMEI_CODE  = "key_imei_code";//IMEI码
    public static final String KEY_CLASS_NAME  = "key_class_name";//类名

    public static final String KEY_SOCKET_ENTITY = "key_socket_entity";


    public static final String HOME_MSG_RECEIVER_ORDER = "home_msg_receiver_order";
    public static final String KEY_SAFE_FRENCE = "key_safe_frence";
    public static final String KEY_WEATHER_DATA = "key_weather_data";


    /*临时这样写 后期处理有序广播拦截问题后 删除该广播控制*/
    public static final String ACTION_OPEN_HOME_MSG_ABLE = "action_home_message_receive";
    public static final String ACTION_OPEN_HOME_MSG_UNABLE = "action_home_message_unable";

    //    安全区域传递数据key

    public static final String SAFE_FENCE_LAT = "safe_fence_lat";
    public static final String SAFE_FENCE_LON = "safe_fence_lon";
    public static final String SAFE_FENCE_SEX = "safe_fence_sex";
    public static final String SAFE_FENCE_ADDRESS = "safe_fence_address";
    public static final String SAFE_FENCE_FENCENAME = "safe_fence_fenceName";
    public static final String SAFE_FENCE_WATCHID = "safe_fence_watchId";
    public static final String SAFE_FENCE_SAFEID = "safe_fence_safeId";
    public static final String SAFE_FENCE_RADIU = "safe_fence_radiu";
    public static final String SAFE_FENCE_LIST = "safe_fence_list";

    //打开HomeActivity
    public static final String OPEN_HONE_FROM = "open_home_from";
    public static final String OPEN_HONE_ID = "open_home_id";
    public static final String OPEN_HONE_PW = "open_home_pw";

    public static final String GET_SYS_MSG_OK = "get_sys_msg_ok";
    public static final String OPEN_SPLASH_TAB = "open_splash_tab";






}
