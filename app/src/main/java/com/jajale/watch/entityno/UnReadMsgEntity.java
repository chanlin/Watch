package com.jajale.watch.entityno;

import java.util.List;

/**
 * Created by athena on 2015/12/9.
 * Email: lizhiqiang@bjjajale.com
 */
public class UnReadMsgEntity {
    public String wID ;
    public String count ;
    public List<UnReadMsgListItem> msgList ;

    public String getwID() {
        return wID;
    }

    public void setwID(String wID) {
        this.wID = wID;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<UnReadMsgListItem> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<UnReadMsgListItem> msgList) {
        this.msgList = msgList;
    }
}
