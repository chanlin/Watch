package com.jajale.watch.entityno;

/**
 * Created by athena on 2015/12/9.
 * Email: lizhiqiang@bjjajale.com
 */
public class UnReadMsgListItem {
    public String msgID;
    public String type;
    public String content;
    public String msTime;

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
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

    public String getMsTime() {
        return msTime;
    }

    public void setMsTime(String msTime) {
        this.msTime = msTime;
    }
}
