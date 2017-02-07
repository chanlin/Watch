package com.jajale.watch.entitydb;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by chunlongyuan on 12/10/15.
 */
public class SocketEntity implements Serializable {

    @DatabaseField(id = true, columnName = "ID")
    private String ID;//ip+:+port
    @DatabaseField(columnName = "watchID")
    private String watchID;
    @DatabaseField(columnName = "ip")
    private String ip;
    @DatabaseField(columnName = "port")
    private int port;



    public SocketEntity() {

    }

//    public static SocketEntity getSocketEntityByWatch(SmartWatch watch) {
//        SocketEntity socketEntity = new SocketEntity();
//        socketEntity.setWatchID(watch.getUser_id());
//        socketEntity.setIp(AppConstants.SOCKET_IP);
//        socketEntity.setPort(AppConstants.SOCKET_PORT);
//        return socketEntity;
//    }

    public String getID() {
        return ID;
    }

    public String getWatchID() {
        return watchID;
    }

    public void setWatchID(String watchID) {
        this.watchID = watchID;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
        this.ID = this.ip + ":" + this.port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        this.ID = this.ip + ":" + this.port;
    }
}
