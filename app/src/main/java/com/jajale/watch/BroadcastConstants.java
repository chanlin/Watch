package com.jajale.watch;

/**
 * Created by chunlongyuan on 11/16/15.
 */
public class BroadcastConstants {

    /** 发送消息 */
    public static final String ACTION_MESSAGE_SEND = "jalale.intent.action.ACTION_MESSAGE_SEND";
    /** 收到消息 */
    public static final String ACTION_MESSAGE_RECEIVE = "jalale.intent.action.ACTION_MESSAGE_RECEIVE";
    /** 更新消息数据 */
    public static final String ACTION_MESSAGE_UPDATA = "jalale.intent.action.ACTION_UPDATE_MESSAGE_DADA";
    /** 闹钟开关 */
    public static final String CLOCK_ONOFF_RECEIVE = "jalale.intent.action.CLOCK_ONOFF_RECEIVE";
    /** 免打扰开关 */
    public static final String CLOCK_NOT_DiSTUIB_RECEIVE = "jalale.intent.action.CLOCK_NOT_DiSTUIB_RECEIVE";


    public static final String ACTION_MESSAGE_LOGOUT = "jalale.intent.action.MESSAGE_LOGOUT";


    /*临时这样写 后期处理有序广播拦截问题后 删除该广播控制*/
    public static final String ACTION_HOME_MSG_NEED_RECEIVER = "action_home_message_need_receive";

//    定位广播
    public static final String POSITION_REFRESH_RECEIVER = "position_refresh_receiver";
    //    定位广播
    public static final String WATCH_LIST_REFRESH_RECEIVER = "watch_list_refresh_receiver";
    //向手表发送语音广播
    public static final String ACTION = "broadcastreceiver.SENDBROADCAST";
    //解除socket通知
    public static final String  SOCKET_TRUE_OR_FLASE_RECEIVER = "socket_true_or_flase_receiver";
    /** 收到消息 */
    public static final String SEND_VOICE_SUCCESS = "SEND_VOICE_SUCCESS";

}
