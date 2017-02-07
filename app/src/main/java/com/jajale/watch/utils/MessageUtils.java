package com.jajale.watch.utils;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Response;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.dao.SqliteHelper;
import com.jajale.watch.entity.ReturnMessageData;
import com.jajale.watch.entity.ReturnMessageListData;
import com.jajale.watch.entitydb.DbHelper;
import com.jajale.watch.entitydb.Message;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.entityno.GetMsgResult;
import com.jajale.watch.entityno.MessageContentStatus;
import com.jajale.watch.entityno.MessageContentType;
import com.jajale.watch.factory.MessageFactory;
import com.jajale.watch.factory.MsgMemberFactory;
import com.jajale.watch.listener.SimpleResponseListener;
import com.jajale.watch.listener.VoiceDownloadListener;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by athena on 2015/11/24.
 * Email: lizhiqiang@bjjajale.com
 */
public class MessageUtils {

    public static void sendVoiceLetter(String fromU, String toU, String content, String autioTime, String msg_id, SimpleResponseListener rListener, Response.ErrorListener eListener, Activity activity) {
//        sendMsg(fromU, toU, MessageType.USER_MSG + "", MessageContentType.VOICE + "", content, autioTime, msg_id, rListener, eListener, activity);
    }

    public static void sendTextLetter(Message msg, SimpleResponseListener rListener, Response.ErrorListener eListener, Activity activity) {
//        sendMsg(msg.from_user, msg.to_user, msg.getMessage_type() + "", MessageContentType.TEXT + "", msg.content, msg.msg_id, rListener, eListener, activity);
    }

    public static List<MsgMember> getMsgMemberList() {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<MsgMember> dbHelper = new DbHelper<MsgMember>(sqliteHelper, MsgMember.class);
            List<MsgMember> lists = dbHelper.queryForAll();
            return lists;
        }
        return null;
    }

    public static void addMsgMemberIfNotExit(MsgMember msgMember) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper dbHelper = new DbHelper<MsgMember>(BaseApplication.getBaseHelper(), MsgMember.class);
            dbHelper.createIfNotExists(msgMember);
        }
    }

    public static void clearMsgMemberDb() {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        DbHelper dbHelper = new DbHelper<MsgMember>(sqliteHelper, MsgMember.class);
        dbHelper.clear();
    }

    public static void refreshMsgMemberDb(final List<MsgMember> watchList) {
        clearMsgMemberDb();
        for (MsgMember msgMember : watchList) {
            addMsgMemberIfNotExit(msgMember);
        }
//        ThreadUtils.getPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                clearMsgMemberDb();
//                for (MsgMember msgMember : watchList) {
//                    addMsgMemberIfNotExit(msgMember);
//                }
//            }
//        });
    }

    public static MsgMember getMsgMemberByUserId(String user_id) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper dbHelper = new DbHelper<MsgMember>(BaseApplication.getBaseHelper(), MsgMember.class);
            List<MsgMember> list = dbHelper.queryForEq("user_id", user_id);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    public static int updateMsgMember(MsgMember msgMember) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        int result = -1;
        if (sqliteHelper != null) {
            DbHelper dbHelper = new DbHelper<MsgMember>(BaseApplication.getBaseHelper(), MsgMember.class);
            result = dbHelper.update(msgMember);
        }

        return result;

    }

    public static int getAllUnreadCount(){
        try {
            SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
            if (sqliteHelper != null) {
                DbHelper dbHelper = new DbHelper<MsgMember>(BaseApplication.getBaseHelper(), MsgMember.class);
                Dao dao = dbHelper.getDao();
                GenericRawResults<String[]> countResults = null;
                countResults = dao.queryRaw("select sum(count_unread) from MsgMember");
                String[] strings = countResults.getResults().get(0);
                return (strings.length > 0 && !CMethod.isEmptyOrZero(strings[0])) ? Integer.parseInt(strings[0]) : 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;

    }



//    public static void sendMsg(String fromU, String toU, String messageType, String contentType, String content, String audioTime, String msg_id, final SimpleResponseListener rListener, Response.ErrorListener eListener, Activity activity) {
//        if (CMethod.isNet(activity)) {
//            try {
//                String url = AppConstants.POST_URL;
//
//                JSONObject paramJO = new JSONObject();
//                paramJO.put("code", CodeConstants.SEND_MSG_CODE);
//
//                JSONObject dataObj = new JSONObject();
//                dataObj.put("userID", fromU);
//                dataObj.put("msgID", msg_id);
//                dataObj.put("wID", toU);
//                dataObj.put("messagetype", messageType);
//                dataObj.put("type", contentType);
//                dataObj.put("content", content);
//                dataObj.put("audio_time", audioTime);
//                paramJO.put("data", dataObj);
//
//                RequestManager.getInstance().addRequest(new SimpleStringRequest(activity.getApplicationContext(), url, paramJO, rListener, eListener), null);
//                MobclickAgent.onEvent(activity, UMeventId.UMENG_PER_CAPITA_NUMBER_OF_TEXT_MESSAGES);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    T.s("网络不给力");
//                    rListener.onError(AppConstants.POST_URL, new SimpleResult());
//                }
//            });
//        }
//
//
//    }

//    /**
//     * 发送信息
//     *
//     * @param fromU
//     * @param toU
//     * @param messageType
//     * @param contentType
//     * @param content
//     * @param rListener
//     * @param eListener
//     * @param activity
//     */
//    public static void sendMsg(String fromU, String toU, String messageType, String contentType, String content, String msg_id, final SimpleResponseListener rListener, Response.ErrorListener eListener, Activity activity) {
//        if (CMethod.isNet(activity)) {
//            try {
//                String url = AppConstants.POST_URL;
//
//                JSONObject paramJO = new JSONObject();
//                paramJO.put("code", CodeConstants.SEND_MSG_CODE);
//
//                JSONObject dataObj = new JSONObject();
//                dataObj.put("msgID", msg_id);
//                dataObj.put("userID", fromU);
//                dataObj.put("wID", toU);
//                dataObj.put("messagetype", messageType);
//                dataObj.put("type", contentType);
//                dataObj.put("content", content);
//                paramJO.put("data", dataObj);
//                RequestManager.getInstance().addRequest(new SimpleStringRequest(activity.getApplicationContext(), url, paramJO, rListener, eListener), null);
//                MobclickAgent.onEvent(activity, UMeventId.UMENG_VOICE_MESSAGES_PER_CAPITA);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    T.s("网络不给力");
//                    rListener.onError(AppConstants.POST_URL, new SimpleResult());
//                }
//            });
//        }
//
//
//    }

//    public static void getHistoryMsg(String userID, String wID, String pagesize, String last_msg_id, final SimpleResponseListener rListener, Response.ErrorListener eListener, Activity activity) {
//        if (CMethod.isNet(activity)) {
//            try {
//                String url = AppConstants.POST_URL;
//                JSONObject paramJO = new JSONObject();
//                paramJO.put("code", CodeConstants.GET_HISTORY_MSG_CODE);
//
//                JSONObject dataObj = new JSONObject();
//                dataObj.put("userID", userID);
//                dataObj.put("wID", wID);
//                dataObj.put("msgID", last_msg_id);
//                dataObj.put("pageIndex", "1");
//                dataObj.put("pageSize", pagesize);
//                paramJO.put("data", dataObj);
//                RequestManager.getInstance().addRequest(new SimpleStringRequest(activity.getApplicationContext(), url, paramJO, rListener, eListener), null);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    T.s("网络不给力");
//                    rListener.onError(AppConstants.POST_URL, new SimpleResult());
//                }
//            });
//
//        }
//
//
//    }

//    public static void getUnReadMsg(String userID, final SimpleResponseListener rListener, Response.ErrorListener eListener, Activity activity) {
//        if (CMethod.isNet(activity)) {
//            try {
//                String url = AppConstants.POST_URL;
//                JSONObject paramJO = new JSONObject();
//                paramJO.put("code", CodeConstants.GET_UNREAD_MSG_CODE);
//                JSONObject dataObj = new JSONObject();
//                dataObj.put("userID", userID);
//                paramJO.put("data", dataObj);
//
//
//
//                SimpleStringRequest request = new SimpleStringRequest(activity.getApplicationContext(), url, paramJO, new SimpleResponseListener() {
//                    @Override
//                    public void onSuccess(String url, String result) {
//
//                        getUnReadMsgResult response = new Gson().fromJson(result, getUnReadMsgResult.class);
//                        if (response == null)
//                            return;
//                        if (response.getAmlList().size() > 0) {
//                            WarningResponseEntity entity=response.getAmlList().get(0);
//                            String nickName=entity.getNickName();
//                            String type=entity.getType();
//                            CMethod.showNotification(BaseApplication.getContext(),"系統消息",getMessageType(nickName,type));
//                        } else if (response.getWatchList().size() > 0) {
//                            CMethod.showNotification(BaseApplication.getContext(), "宝贝","发来语音消息");
//                        }
//                        rListener.onSuccess(url, result);
//                    }
//
//                    @Override
//                    public void onFailure(String url, SimpleResult response) {
//                        rListener.onError(url, response);
//                    }
//
//                    @Override
//                    public void onError(String url, SimpleResult result) {
//                        rListener.onError(url, result);
//                    }
//
//                }, eListener);
//
//                RequestManager.getInstance().addRequest(request, null);
////                RequestManager.getInstance().addRequest(new SimpleStringRequest(activity.getApplicationContext(), url, paramJO, rListener, eListener), null);
//            } catch (Exception e) {
//                rListener.onError(AppConstants.POST_URL, new SimpleResult());
//            }
//        } else {
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    T.s("网络不给力");
//                    rListener.onError(AppConstants.POST_URL, new SimpleResult());
//                }
//            });
//        }
//
//    }


    private static String getMessageType(String nickName, String type) {
        String messageType = nickName;
        if (type.equals("11")) {
            messageType = messageType + "离开了安全区域";
        } else if (type.equals("12")) {
            messageType = messageType + "手表电量过低";
        } else if (type.equals("13")) {
            messageType = messageType + "发来SOS报警";
        } else if (type.equals("14")) {
            messageType = messageType + "的手表被解除";
        } else if (type.equals("15")){
            messageType = messageType + "进入了安全区域";
        }
        return messageType;

    }








    /**
     * 将返回信息添加到Message数据库
     *
     * @param listData
     */
    public static void addReturnMessageToDB(Context context, ReturnMessageListData listData, final VoiceDownloadListener listener) {

        if (listData.getMsgList().size()>0){
            List<MsgMember> msgMemberListBySmartWatchList = MsgMemberFactory.getInstance().createMsgMemberListByReturnMsgList(listData.getMsgList());
            MessageUtils.refreshMsgMemberDb(msgMemberListBySmartWatchList);
        }

        for (int i = 0; i < listData.getMsgList().size(); i++) {
            ReturnMessageData returnMessageData = listData.getMsgList().get(i);
            final Message message = MessageFactory.getInstance().creatMsgByReturnMessage(returnMessageData);
            saveMsg2DB(message);
//            msg_type;//消息类型：1聊天，2：系统消息 3,手表指令 4,内部命令 5,报警消息
            if (message.getMessage_type() == 1) {
                L.e("message_return_msg_type==聊天");
                if (listener != null)
//                    content_type;//内容类型 1：文本，2：语音
                    if (message.getContent_type() == MessageContentType.CONTENT_TYPE_VOICE) {//如果是语音文件
                        updateMsgByUUID(message.getMsg_id(), MessageContentStatus.RECEIVE_UNREAD + "", message.getContent());
                    }
            } else{
                L.e("message_return_msg_type==系统消息");
                if (message.getContent_type() == MessageContentType.CONTENT_TYPE_TEXT) {//
                    updateMsgByUUID(message.getMsg_id(), MessageContentStatus.RECEIVE_UNREAD + "", message.getContent());
                }else if(message.getContent_type() == MessageContentType.CONTENT_TYPE_VOICE){//如果是语言文件

                }
            }


        }

        listener.onSuccess("");
    }


    /**
     * 增加一条消息记录
     *
     * @param msg
     * @return
     */
    public static int saveMsg2DB(Message msg) {

        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper dbHelper = new DbHelper<Message>(BaseApplication.getBaseHelper(), Message.class);
            return dbHelper.createIfNotExists(msg);
        }
        return -1;
    }


    public static int deleteAllMsgByUserName(String wID) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<Message> dbHelper = new DbHelper<Message>(BaseApplication.getBaseHelper(), Message.class);
            try {
                Dao dao = dbHelper.getDao();
                String sql = "delete from Message where from_user = \'" + wID + "\' or to_user = \'" + wID + "\'";
                L.e("sql is ：" + sql);
                return dao.executeRawNoArgs(sql);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;

    }


    public static int deleteMsgByUUID(String uuid) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<Message> dbHelper = new DbHelper<Message>(BaseApplication.getBaseHelper(), Message.class);
            List<Message> list = dbHelper.queryForEq("msg_id", uuid);
            if (list.size()==0)
                return -1;

            Message result = list.get(0);
            return dbHelper.delete(result);
        }
        return -1;
    }

    public static List<Message> loadMoreMsgWithLastMsgTime(String userId, String last_msg_create_time, String pageSize) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper dbHelper = new DbHelper<Message>(BaseApplication.getBaseHelper(), Message.class);
            try {
                Dao dao = dbHelper.getDao();
//                String where = "select * from Message where from_user = \'" + userId + "\' or to_user = \'" + userId +"\' and create"++" limit 0 , "+pageSize;
                String where = "select *from Message where (from_user = \'" + userId + "\' or to_user = \'" + userId + "\' )and create_time < " + last_msg_create_time + " order by create_time desc limit 0 ," + pageSize;

                L.e("sql is ：" + where);

                GenericRawResults<Message> grr = dao.queryRaw(where, new RawRowMapper<Message>() {
                    @Override
                    public Message mapRow(String[] strings, String[] strings2) throws SQLException {
                        Message message = new Message();
                        message = analysisSQLResult(message, strings, strings2);
                        return message;
                    }
                });


                ArrayList<Message> messageList = new ArrayList<Message>();
                Iterator<Message> iterator = grr.iterator();
                while (iterator.hasNext()) {
                    Message message = iterator.next();
//                    messageList.add(message);
                    messageList.add(message);
                }
                L.e("----->", "" + messageList.size());

                return messageList;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static List<Message> getLastestMessageWithUserId(String userId, String pageSize) {

        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper dbHelper = new DbHelper<Message>(BaseApplication.getBaseHelper(), Message.class);
            try {
                Dao dao = dbHelper.getDao();
                String where = "select * from Message where from_user = \'" + userId + "\' or to_user = \'" + userId + "\' order by create_time desc limit 0 , " + pageSize;

                L.e("sql is ：" + where);

                GenericRawResults<Message> grr = dao.queryRaw(where, new RawRowMapper<Message>() {
                    @Override
                    public Message mapRow(String[] strings, String[] strings2) throws SQLException {
                        Message message = new Message();
                        message = analysisSQLResult(message, strings, strings2);
                        return message;
                    }
                });


                ArrayList<Message> messageList = new ArrayList<Message>();
                Iterator<Message> iterator = grr.iterator();
                while (iterator.hasNext()) {
                    Message message = iterator.next();
//                    messageList.add(message,0);
                    messageList.add(0, message);
                }
                L.e("----->", "" + messageList.size());

                return messageList;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static List<Message> getAllMessageWithUserId(String userId) {

        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper dbHelper = new DbHelper<Message>(BaseApplication.getBaseHelper(), Message.class);
            try {
                Dao dao = dbHelper.getDao();
                String where = "select * from Message where from_user = \'" + userId + "\' or to_user = \'" + userId + "\' order by" + " create_time" ;

                L.e("sql is ：" + where);

                GenericRawResults<Message> grr = dao.queryRaw(where, new RawRowMapper<Message>() {
                    @Override
                    public Message mapRow(String[] strings, String[] strings2) throws SQLException {
                        Message message = new Message();
                        message = analysisSQLResult(message, strings, strings2);
                        return message;
                    }
                });


                ArrayList<Message> messageList = new ArrayList<Message>();
                Iterator<Message> iterator = grr.iterator();
                while (iterator.hasNext()) {
                    Message message = iterator.next();
                    messageList.add(message);
                }
                L.e("----->", "" + messageList.size());

                return messageList;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 整理成一个方法,因为都是全查  字段一致
     *
     * @param strings2
     * @return
     */
    private static Message analysisSQLResult(Message msg, String[] strings, String[] strings2) {

        for (int i = 0; i < strings.length; i++) {

//            L.e("gerRelationFromArray === > " + strings[i] + "<``==``>"+strings2[i]);

            try {
                Field content = Message.class.getDeclaredField(strings[i]);

                if (content.getType().getCanonicalName().equals(Integer.class.getCanonicalName())) {
                    content.set(msg, Integer.parseInt(strings2[i]));
                } else if (content.getType().getCanonicalName().equals(Long.class.getCanonicalName())) {
                    content.set(msg, Long.parseLong(strings2[i]));
                } else {
                    content.set(msg, strings2[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    /**
     * 将对应的user_name 的全部文本信息
     *
     * @param user_name
     * @return
     */
    public static int updateUnReadStatusByUserName(String user_name) {

//        String sql = "update Message set message_type = 1 where from";
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<Message> dbHelper = new DbHelper<Message>(BaseApplication.getBaseHelper(), Message.class);
            try {
                Dao dao = dbHelper.getDao();
                UpdateBuilder<T, Long> updateBuilder = dao.updateBuilder();
                updateBuilder.where().eq("from_user", user_name).and().eq("content_type", "" + MessageContentType.TEXT).and().eq("message_status", "" + MessageContentStatus.RECEIVE_UNREAD);
                updateBuilder.updateColumnValue("message_status", "" + MessageContentStatus.RECEIVE_READ);
                return updateBuilder.update();
            } catch (SQLException e) {
                L.e(e);
            }
        }
        return -1;
    }


    public static int updateMsgByUUID(String uuid, String message_status, String content) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<Message> dbHelper = new DbHelper<Message>(BaseApplication.getBaseHelper(), Message.class);
            List<Message> list = dbHelper.queryForEq("msg_id", uuid);
            if (list.size()>0){
                Message result = list.get(0);
                result.message_status = message_status;
                if (content != null) {
                    result.content = content;
                }
                return dbHelper.update(result);
            }

        }
        return -1;
    }

    public static int updateMsgByUUID(String uuid, String message_status, String content, Long time) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<Message> dbHelper = new DbHelper<Message>(BaseApplication.getBaseHelper(), Message.class);
            List<Message> list = dbHelper.queryForEq("msg_id", uuid);
            Message result = list.get(0);
            result.message_status = message_status;
            result.create_time = time + "";
            if (content != null) {
                result.content = content;
            }
            return dbHelper.update(result);
        }
        return -1;
    }
    public static int updateMsgByUUID(String uuid, String message_status, String content, Long time,String filePath) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<Message> dbHelper = new DbHelper<Message>(BaseApplication.getBaseHelper(), Message.class);
            List<Message> list = dbHelper.queryForEq("msg_id", uuid);
            Message result = list.get(0);
            result.message_status = message_status;
            if (time!=null)
            result.create_time = time + "";
            result.url=filePath;
            if (content != null) {
                result.content = content;
            }
            return dbHelper.update(result);
        }
        return -1;
    }

    public static List<Message> getLastMessageWithUserId(String userId) {

        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper dbHelper = new DbHelper<Message>(BaseApplication.getBaseHelper(), Message.class);
            try {
                Dao dao = dbHelper.getDao();

                String where = "select * from Message where from_user = \'" + userId + "\' or to_user = \'" + userId + "\' order by" + " create_time" + " desc limit 0 ,1";

//              select * from Message where from_user = 185605720 or to_user = 185605720 order by desc count 1
//              java.sql.SQLException: Could not perform raw query for select * from Message where from_user = 185605720 or to_user = 185605720 order bycreate_time desc count 1
                GenericRawResults<Message> grr = dao.queryRaw(where, new RawRowMapper<Message>() {
                    @Override
                    public Message mapRow(String[] strings, String[] strings2) throws SQLException {
                        Message message = new Message();
                        message = analysisSQLResult(message, strings, strings2);
                        return message;
                    }
                });

                ArrayList<Message> messageList = new ArrayList<Message>();
                Iterator<Message> iterator = grr.iterator();
                while (iterator.hasNext()) {
                    Message message = iterator.next();
                    messageList.add(message);
                    return messageList;
                }
            } catch (SQLException e) {
                L.e(e.toString());
            }
        }

        return null;
    }

    public static void insertMsgByHistoryResult(GetMsgResult result) {

        if (result.getMsgList() != null && result.getMsgList().size() > 0) {
            for (int i = 0; i < result.getMsgList().size(); i++) {
                Message history_msg = MessageFactory.getInstance().createMsgByHistoryResult(result.getMsgList().get(i));
                saveMsg2DB(history_msg);
            }
        }
    }

    public static void insertMsgByRecentResult(String fromID, String toID, GetMsgResult result) {
        if (result.getMsgList() != null && result.getMsgList().size() > 0) {
            for (int i = 0; i < result.getMsgList().size(); i++) {
                Message msg = MessageFactory.getInstance().createMsgByRecentResult(fromID, toID, result.getMsgList().get(0));
                saveMsg2DB(msg);
            }
        }
    }


}
