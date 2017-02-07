package com.jajale.watch.entityno;

/**
 * Created by athena on 2015/12/2.
 * Email: lizhiqiang@bjjajale.com
 */
public class MsgResult {
    public String msgID ; //服务器上的消息唯一标识
    public String sendID ;
    public String receiveID;
    public String type ;
    public String content ;
    public String msTime ;

    public String contentType ;

    public String getSendID() {
        return sendID;
    }

    public void setSendID(String sendID) {
        this.sendID = sendID;
    }

    public String getReceiveID() {
        return receiveID;
    }

    public void setReceiveID(String receiveID) {
        this.receiveID = receiveID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getcTtime() {
        return msTime;
    }

    public void setcTtime(String cTtime) {
        this.msTime = cTtime;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
