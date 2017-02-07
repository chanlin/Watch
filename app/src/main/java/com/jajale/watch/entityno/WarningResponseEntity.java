package com.jajale.watch.entityno;

/**
 * Created by athena on 2015/12/4.
 * Email: lizhiqiang@bjjajale.com
 */
public class WarningResponseEntity {
    public String lat ;
    public String lon ;
    public String type;  //报警消息类型
    public String wID ;  //手表ID
    public String time ; //时间戳
    public String nickName ; //昵称
    public String electricity ; //电量
    public String msTime ;
    public String  safeTitle;


    public String getSafeTitle() {
        return safeTitle;
    }

    public void setSafeTitle(String safeTitle) {
        this.safeTitle = safeTitle;
    }

    public String getMsTime() {
        return msTime;
    }

    public void setMsTime(String msTime) {
        this.msTime = msTime;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getwID() {
        return wID;
    }

    public void setwID(String wID) {
        this.wID = wID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }
}
