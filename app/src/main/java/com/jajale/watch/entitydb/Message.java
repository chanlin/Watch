package com.jajale.watch.entitydb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.utils.L;

/**
 * Created by athena on 2015/11/24.
 * Email: lizhiqiang@bjjajale.com
 */

@DatabaseTable(tableName = "Message")
public class Message {
    @DatabaseField(columnName = "msg_id" , id=true)
    public String msg_id;
    @DatabaseField(columnName = "server_id")
    public String server_id;//服务器上存储消息的唯一标识

    @Override
    public String toString() {
        return "Message{" +
                "msg_id='" + msg_id + '\'' +
                ", server_id='" + server_id + '\'' +
                ", from_user='" + from_user + '\'' +
                ", to_user='" + to_user + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", content_type='" + content_type + '\'' +
                ", message_type='" + message_type + '\'' +
                ", audio_time='" + audio_time + '\'' +
                ", message_status='" + message_status + '\'' +
                ", create_time='" + create_time + '\'' +
                ", extra_type='" + extra_type + '\'' +
                ", action='" + action + '\'' +
                ", electricity='" + electricity + '\'' +
                ", warning_child='" + warning_child + '\'' +
                ", exten_1='" + exten_1 + '\'' +
                ", exten_2='" + exten_2 + '\'' +
                ", exten_3='" + exten_3 + '\'' +
                '}';
    }

    @DatabaseField(columnName = "from_user")
    public String from_user;//发送方userid
    @DatabaseField(columnName = "to_user")
    public String to_user;//接收方userid
    @DatabaseField(columnName = "content")
    public String content; // 消息内容
    @DatabaseField(columnName = "url")
    public String url ;
    @DatabaseField(columnName = "content_type")
    public String content_type; //消息内容的类型 （IMAGE/VOICE/TEXT）
    @DatabaseField(columnName = "message_type")
    public String message_type; //消息的种类
    @DatabaseField(columnName = "audio_time")
    public String audio_time; //音频时间
    //    @DatabaseField(columnName = "is_read_audio") //(1是已读,2是未读); // 并归到message_status中
//    public String is_read_audio; //是否已读该条音频
    @DatabaseField(columnName = "message_status")
    public String message_status; //消息的状态 (1是发送失败 2是发送成功 3是发送中 4是接收的已读 2是接收的消息未读)
    @DatabaseField(columnName = "create_time")
    public String create_time ;//每条消息的发送时间戳
    @DatabaseField(columnName = "extra_type")
    public String extra_type;
    @DatabaseField(columnName = "action")
    public String action ;
    @DatabaseField(columnName = "electricity")
    public String electricity ;
    @DatabaseField(columnName = "warning_child")
    public String warning_child ;

    //扩展业务
    @DatabaseField(columnName = "exten_1")//手表用户保险结束时间
    public String exten_1;
    @DatabaseField(columnName = "exten_2")//手表用户保险结束时间
    public String exten_2;
    @DatabaseField(columnName = "exten_3")//手表用户保险结束时间
    public String exten_3;



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSender_id() {
        return server_id;
    }

    public void setSender_id(String sender_id) {
        this.server_id = sender_id;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public String getTo_user() {
        return to_user;
    }

    public void setTo_user(String to_user) {
        this.to_user = to_user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContent_type() {
        try {
            return Integer.parseInt(content_type);
        }catch (Exception e){
            return -1;
        }
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public int getMessage_type() {
        try {
            return Integer.parseInt(message_type);
        }catch (Exception e){
            return -1;
        }

    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getAudio_time() {
        return audio_time;
    }

    public void setAudio_time(String audio_time) {
        this.audio_time = audio_time;
    }

    public int getMessage_status() {
        try{
            return Integer.parseInt(message_status);
        }catch (Exception e){
            return -1;
        }

    }

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getExtra_type() {
        return extra_type;
    }

    public void setExtra_type(String extra_type) {
        this.extra_type = extra_type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getExten_1() {
        return exten_1;
    }

    public void setExten_1(String exten_1) {
        this.exten_1 = exten_1;
    }

    public String getExten_2() {
        return exten_2;
    }

    public void setExten_2(String exten_2) {
        this.exten_2 = exten_2;
    }

    public String getExten_3() {
        return exten_3;
    }

    public void setExten_3(String exten_3) {
        this.exten_3 = exten_3;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getWarning_child() {
        return warning_child;
    }

    public void setWarning_child(String warning_child) {
        this.warning_child = warning_child;
    }

    /**
     * true是send
     * false是receive
     * @return
     */
    public boolean getDrict(){//

//        if (from_user.equals("watch")){
//            return false;
//        }
        L.e("content"+from_user.toString());
        return from_user.equals("1");
//        return from_user.equals(BaseApplication.getUserInfo().userID);
    }
}
