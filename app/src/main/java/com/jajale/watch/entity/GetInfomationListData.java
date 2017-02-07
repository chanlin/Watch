package com.jajale.watch.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2016/2/18.
 * Email: lizhiqiang@bjjajale.com
 */
public class GetInfomationListData {
//<<<<<<< HEAD
    private String type;
    private String count;
    private List<InfomationItemEntity> informationList = new ArrayList<InfomationItemEntity>();
//=======
//    private String msgType;
//    private String msgCount;
//    private String contentType ;
//
//
//    private List<InfomationItemEntity> msgList = new ArrayList<InfomationItemEntity>();
//>>>>>>> dev


    public String getMsgType() {
        return type;
    }

    public void setMsgType(String msgType) {
        this.type = msgType;
    }

    public String getMsgCount() {
        return count;
    }

    public void setMsgCount(String msgCount) {
        this.count = msgCount;
    }

    public List<InfomationItemEntity> getMsgList() {
        return informationList;
    }

    public void setMsgList(List<InfomationItemEntity> msgList) {
        this.informationList = msgList;
    }

    public String getContentType() {
        return type;
    }

    public void setContentType(String contentType) {
        this.type = contentType;
    }
}
