package com.jajale.watch.entity;

/**
 * Created by athena on 2016/2/18.
 * Email: lizhiqiang@bjjajale.com
 */
public class InfomationItemEntity {
    private String id;
    private String title;
    private String imgUrl;

    private String abstracts;
    private String publish_time ;


    //为视频类信息列表增加
    private String url ;


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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDetailed() {
        return abstracts;
    }

    public void setDetailed(String detailed) {
        this.abstracts = detailed;
    }

    public String getReleaseTime() {
        return publish_time;
    }

    public void setReleaseTime(String releaseTime) {
        this.publish_time = releaseTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
}
