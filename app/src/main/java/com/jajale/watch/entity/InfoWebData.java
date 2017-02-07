package com.jajale.watch.entity;

/**
 * Created by lizhiqiang on 19/2/16.
 */
public class InfoWebData {



    private String id;

    private String title;

    private String detailed;

    private String publish_time;


    public String getMsgID() {
        return id;
    }

    public void setMsgID(String msgID) {
        this.id = msgID;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailed() {
        return detailed;
    }

    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }

    public String getReleaseTime() {
        return publish_time;
    }

    public void setReleaseTime(String releaseTime) {
        this.publish_time = releaseTime;
    }
}
