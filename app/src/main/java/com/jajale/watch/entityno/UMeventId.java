package com.jajale.watch.entityno;

/**
 * Created by athena on 2015/11/12.
 * Email: lizhiqiang@bjjajale.com
 * <p/>
 * 友盟eventID
 * event id不能使用特殊字符，不建议使用中文，且长度不能超过128个字节；map中的key和value 都不能使用特殊字符，key 不能超过128个字节，value 不能超过256个字节。
 * id， ts， du是保留字段，不能作为event id及key的名称。
 * 每个应用至多添加500个自定义事件，每个event 的 key不能超过10个，每个key的取值不能超过1000个。如需要统计支付金额、内容浏览数量等数值型的连续变量，
 * 请使用计算事件（不允许通过key-value结构来统计类似搜索关键词，网页链接等随机生成的字符串信息）。
 */
public class UMeventId {

    public static final String UMENG_ACTIVATION_NUMBER = "Activation_number";//激活数,启动APP未注册用户
    public static final String UMENG_BINDING_WATCH = "Binding_Watch";//绑定手表数
    public static final String UMENG_NUMBER_OF_SOLUTIONS = "Number_of_Solutions";//解绑数
    public static final String UMENG_NUMBER_OF_POSITIONING_MODULE = "Number_of_positioning_module";//定位模块使用人数
//    public static final String UMENG_POSITIONING_MODULE_USE_NUMBER_OF_TIMES = "Positioning_module_use_number_of_times";//模块使用次数
    public static final String UMENG_PER_CAPITA_NUMBER_OF_TEXT_MESSAGES = "Per_capita_number_of_text_messages";//均发文字消息数,发消息数/注册登录人数
    public static final String UMENG_VOICE_MESSAGES_PER_CAPITA = "Voice_messages_per_capita";//人均发语音消息数,发消息数/注册登录人数
    public static final String UMENG_SOS_MESSAGE_NUMBER = "SOS_message_number";//SOS消息数
    public static final String UMENG_GROWTH_USING_NUMBER = "Growth_using_number";//成长使用人数,添加成长记录的人数
    public static final String UMENG_VACCINE_USE = "Vaccine_use";//疫苗使用人数,设定疫苗的人数
    public static final String UMENG_PER_CAPITA_BOUND_WATCH = "Per_capita_bound_Watch";//人均绑定手表数,绑定手表数/注册用户
    public static final String UMENG_ALARM_CLOCK_USE = "Alarm_clock_use";//闹钟使用人数,设定闹钟的人数
//    public static final String UMENG_TOLL_FREE_NUMBER = "Toll_free_number";//免打扰使用人数,设定免打扰的人数
    public static final String UMENG_FREE_USE_OF_FREQUENCY = "Free_use_of_frequency";//免打扰使用次数,设定免打扰的次数
    public static final String UMENG_NUMBER_OF_STEPS = "Number_of_steps";//计步设定人数,按钮开启关闭人数
    public static final String UMENG_ENTER_THE_NUMBER_OF_INSURANCE_PAGES = "Enter_the_number_of_insurance_pages";//进入保险页人数
    public static final String UMENG_INSURANCE_NUMBER = "Insurance_number";//领取保险人数,点击领取按钮的人数
    public static final String UMENG_SUCCESSFUL_SUBMISSION_OF_POLICY = "Successful_submission_of_policy";//成功提交保单人数
    public static final String UMENG_NUMBER_OF_COMMON_PROBLEMS = "Number_of_common_problems";//常见问题进入人数
    public static final String UMENG_LOOKING_FOR_THE_NUMBER_OF_WATCHES = "Looking_for_the_number_of_watches";//找手表使用人数
    public static final String UMENG_POSITIONING_MODE_SETTING = "Positioning_mode_setting";//定位模式设定人数
    public static final String UMENG_NORMAL_MODE_NUMBER = "Normal_mode_number";//正常模式人数
    public static final String UMENG_HIGH_FREQUENCY_MODE_NUMBER = "High_frequency_mode_number";//高频模式人数
    public static final String UMENG_LOW_FREQUENCY_MODE_NUMBER = "Low_frequency_mode_number";//低频模式人数
    public static final String UMENG_NUMBER_OF_REMOTE_SHUTDOWN = "Number_of_remote_shutdown";//远程关机使用人数
    public static final String UMENG_HISTORY_TRACK_INTO = "History_track_into";//进入历史轨迹人数
    public static final String UMENG_SAFE_FENCE_INTO = "Safe_fence_into";//进入安全围栏人数
    public static final String UMENG_SAFE_FENCE_SETTING = "Safe_fence_setting";//设置安全围栏人数

    public static final String UMENG_SOS_INTO_NUMBER = "Sos_into_number";//SOS页面进入人数
    public static final String UMENG_SOS_SETTING_NUMBER = "Sos_setting_number";//SOS设置人数

    public static final String ERROR_SEND_MSG = "error_send_msg";
    public static final String ERROR_SEND_UPLOAD_QINIU = "error_send_upload_qiniu";
    public static final String ERROR_INTERFACE_PARSE = "error_interface_parse";
    public static final String ERROR_INTERFACE = "error_interface";

    public static final String UMENG_TELEPHONE_SETTINGS_NUMBER = "telephone_settings_number";

    public static final String UMENG_HEALTHY_CLICK_TIMES = "healthy_click_times";
    public static final String UMENG_EDUCATION_CLICK_TIMES = "education_click_times";
    public static final String UMENG_TOURISM_CLICK_TIMES = "tourism_click_times";
    public static final String UMENG_SECURITY_CLICK_TIMES = "security_click_times";
    public static final String UMENG_INSURANCE_CLICK_TIMES = "insurance_click_times";
    public static final String UMENG_COMMUNICATION_CLICK_TIMES = "communication_click_times";
    public static final String UMENG_WELFARE_CLICK_TIMES = "welfare_click_times";
    public static final String UMENG_VIDEO_CLICK_TIMES = "video_click_times";
    public static final String UMENG_NEWSPAPER_CLICK_TIMES = "newspaper_click_times";
    public static final String UMENG_CEC_TV_CLICK_TIMES = "cec_tv_click_times";
}
