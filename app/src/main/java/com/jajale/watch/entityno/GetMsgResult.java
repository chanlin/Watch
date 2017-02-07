package com.jajale.watch.entityno;

import java.util.List;

/**
 * Created by athena on 2015/12/2.
 * Email: lizhiqiang@bjjajale.com
 */
public class GetMsgResult {
    public String pageIndex  ;
    public List<MsgResult> msgList ;

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<MsgResult> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<MsgResult> msgList) {
        this.msgList = msgList;
    }
}
