package com.jajale.watch.factory;

import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.entity.ReturnMessageData;
import com.jajale.watch.entitydb.Message;
import com.jajale.watch.entityno.MessageContentStatus;
import com.jajale.watch.entityno.MessageContentType;
import com.jajale.watch.entityno.MessageType;
import com.jajale.watch.entityno.MsgResult;
import com.jajale.watch.entityno.UnReadMsgListItem;
import com.jajale.watch.entityno.WarningResponseEntity;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.CacheUtils;

import java.util.UUID;

/**
 * Created by athena on 2015/11/26.
 * Email: lizhiqiang@bjjajale.com
 */
public class MessageFactory {
    private static MessageFactory instance = null;

    //    private static BaseArea baseArea ;
    private MessageFactory() {
    }

    public static MessageFactory getInstance() {
        if (instance == null) {
            instance = new MessageFactory();
        }
        return instance;
    }

    /**
     * 生产一条发送中状态的消息
     * @param from_u
     * @param to_u
     * @param context
     * @param contentType
     * @return
     */
    public Message createInprogressMessage(String from_u, String to_u, String context, int contentType) {
        Message msg = new Message();
        msg.content = context;
        msg.from_user = from_u;
        msg.to_user = to_u;
        msg.create_time = System.currentTimeMillis() + "";
        msg.message_status = MessageContentStatus.SEND_INPROFRESS+"";//INPROFRESS
        msg.content_type = contentType + "";
        msg.message_type = MessageType.USER_MSG + "";
        msg.msg_id = getUUID();
        return msg;
    }

    public Message createSysWelcomeMsg(){

        Message msg = new Message();
        msg.content = "欢迎使用华英智联儿童星，您在使用中遇到的任何问题都可在<使用说明>中进行查询" ;
        msg.from_user = "000";
        msg.to_user = BaseApplication.getUserInfo().userID;

        msg.create_time = System.currentTimeMillis() + "";
        msg.message_status = MessageContentStatus.RECEIVE_UNREAD+"";
        msg.content_type = MessageContentType.TEXT+"";
        msg.message_type ="0";
        msg.msg_id = getUUID();
        return msg;

    }


    public Message createSystemMessage(String context , int contentType){
        Message msg = new Message();
        msg.content = context ;
        msg.from_user = "000";
        msg.to_user = BaseApplication.getUserInfo().userID;

        msg.create_time = System.currentTimeMillis() + "";
        msg.message_status = MessageContentStatus.RECEIVE_UNREAD+"";
        msg.content_type = contentType+"";
        msg.message_type = MessageType.SYSTEM_MSG+"";
        msg.msg_id = getUUID();
        return msg;
    }

    public Message createMsgByHistoryResult(MsgResult result){
        Message msg = new Message();
        msg.msg_id = result.getMsgID();
        msg.server_id = result.getMsgID();
        msg.from_user = result.getSendID();
        msg.to_user = result.getReceiveID();
        msg.create_time = result.msTime+"";
        msg.message_status = msg.from_user.equals(BaseApplication.getUserInfo().userID)?MessageContentStatus.SEND_SUCCESS+"" : MessageContentStatus.RECEIVE_READ+"";
        msg.content_type = result.getType();
        msg.message_type = MessageType.USER_MSG+"";
        msg.content = result.getContent();
        if (msg.content_type.equals(MessageContentType.VOICE+"")){
            String fileName = System.currentTimeMillis() + "";
            msg.url = CacheUtils.getExternalCacheDir(BaseApplication.getContext()) + fileName + ".amr";
        }
        return msg;
    }

    public Message creatMsgByReturnMessage(ReturnMessageData message){
        Message msg = new Message();
        msg.msg_id =message.getMsg_uuid();
        msg.from_user = message.getMsg_type()==1? message.getSend_user():"000";
        msg.to_user = message.getReceive_user();
        msg.create_time = CMethod.getTimestamp(message.getCreate_time())+"";
        msg.message_status = MessageContentStatus.RECEIVE_UNREAD+"";
        msg.content_type = message.getContent_type()==1?MessageContentType.TEXT+"":MessageContentType.VOICE+"";
        msg.message_type =message.getMsg_type()+"";
        msg.content = message.getContent();
        if (msg.content_type.equals(MessageContentType.VOICE+"")){
            String fileName = System.currentTimeMillis() + "";
            msg.url = CacheUtils.getExternalCacheDir(BaseApplication.getContext()) + fileName + ".amr";
        }
        return msg;
    }

    public Message createMsgByUnReadItem(String fromID,String toID,UnReadMsgListItem result){
        Message msg = new Message();
        msg.msg_id = result.getMsgID();
        msg.from_user = fromID;
        msg.to_user = toID;
        msg.create_time = result.msTime+"";
        msg.message_status = MessageContentStatus.RECEIVE_UNREAD+"";
        msg.content_type = result.type.equals("1")?MessageContentType.TEXT+"":MessageContentType.VOICE+"";
        msg.message_type = MessageType.USER_MSG+"";
        msg.content = result.getContent();
//        if (msg.content_type.equals(MessageContentType.VOICE+"")){
//            msg.url = result.getContent();
//        }
        return msg;
    }


    public Message createMsgByRecentResult(String fromID,String toID,MsgResult result){
        Message msg = new Message();
        msg.msg_id = result.getMsgID();
//        msg.server_id = ;
        msg.from_user = fromID;
        msg.to_user = toID;
        msg.create_time = result.msTime+"";
        msg.message_status = MessageContentStatus.RECEIVE_UNREAD+"";
        msg.content_type = MessageContentType.VOICE+"";
        msg.message_type = MessageType.USER_MSG+"";
        msg.content = result.getContent();
        return msg;
    }

    public Message createMsgByWarningResp(WarningResponseEntity response){
        Message msg = new Message();

        msg.msg_id = getUUID();
        msg.from_user = AppConstants.SYSTEM_WATCH_ID;
        msg.to_user = BaseApplication.getUserInfo().userID;
        msg.create_time = response.getMsTime()+"";
        msg.message_status = MessageContentStatus.RECEIVE_UNREAD+"";
        msg.content_type = response.getType();
        msg.message_type = MessageType.SYSTEM_MSG+"";
        msg.electricity = response.electricity;
        msg.warning_child = response.getNickName();
        msg.exten_1 = response.getSafeTitle();

        if(msg.getContent_type() == MessageContentType.SYS_WELCOME){
            msg.content = "欢迎使用华英智联儿童星";
            msg.url = "欢迎使用华英智联儿童星，\n" +
                    "陪伴宝贝健康成长";
        }else if(msg.getContent_type() == MessageContentType.SYS_BETTRATY){
            msg.content = "宝贝手表电量告急";
            msg.url = response.getNickName()+"的手表电量低于"+response.electricity+"\n,请尽快充电";
        }else if(msg.getContent_type() == MessageContentType.SYS_LOCATION){
            msg.content = "NICK已离开TITLE";

            String result = "NICK离开了TITLE，请尽快与宝贝取得联系";
            String title = "安全区";
            String nickName = "宝贝";
            if (!CMethod.isEmpty(msg.exten_1)){
                String [] arr = msg.exten_1.split("_");
                title =arr[0];
            }
            if (!CMethod.isEmpty(response.getNickName())){
                nickName = response.getNickName();
            }
            result = result.replace("NICK",nickName).replace("TITLE",title);
            msg.content = msg.content.replace("NICK",nickName).replace("TITLE",title);
            msg.url = result;
//                        msg.url = "#NIC#的位置电量低于#PEC#,请尽快充电";
        }else if(msg.getContent_type() == MessageContentType.SYS_SOS){
            msg.content = "宝贝发来求救警报";
            msg.url = response.getNickName()+"发出SOS警报，请尽快与宝贝取得联系";

        }else if (msg.getContent_type() == MessageContentType.SYS_RELEASE){
            msg.content = response.getNickName()+"的手表被解除";
        }else if (msg.getContent_type() == MessageContentType.SYS_LOCATION_ENTER){
            String reuslt = "NICK已到达了TITLE";
            String title = "安全区";
//            msg.content =
            String nickName = "宝贝";
            if (!CMethod.isEmpty(response.getNickName())){
                nickName = response.getNickName();
            }
            if (!CMethod.isEmpty(msg.exten_1)){
                String [] arr = msg.exten_1.split("_");
                title =arr[0];
            }
            msg.content = reuslt.replace("NICK",nickName).replace("TITLE",title);
            msg.url = response.getNickName()+"到达了安全区";
        }
//        showNotification(BaseApplication.getContext(),"");
        return msg;
    }


    public String getUUID() {
//        return UUID.randomUUID().toString().replace("-", "");
        return UUID.randomUUID().toString() ;
    }

}
