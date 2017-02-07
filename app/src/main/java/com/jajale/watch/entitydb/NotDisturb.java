package com.jajale.watch.entitydb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 免打扰
 * <p/>
 * Created by lilonghui on 2015/12/4.
 * Email:lilonghui@bjjajale.com
 */

@DatabaseTable(tableName = "NotDisturb")
public class NotDisturb {
    public static String ID = "_id";
    public static String WATCHID = "watchID";

    /**
     * beginTime : 14:00
     * endTime : 15:00
     * number : 1
     * onOFF : 1
     * setState : 1
     */


    @DatabaseField(id = true, columnName = "_id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DatabaseField(columnName = "watchId")
    private String watchId;

    public String getWatchId() {
        return watchId;
    }

    public void setWatchId(String watchId) {
        this.watchId = watchId;
    }

    @DatabaseField(columnName = "beginTime")
    private String beginTime;
    @DatabaseField(columnName = "endTime")
    private String endTime;
    @DatabaseField(columnName = "number")
    private int number;
    @DatabaseField(columnName = "onOFF")
    private int onOFF;
    @DatabaseField(columnName = "setState")
    private int setState;

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setOnOFF(int onOFF) {
        this.onOFF = onOFF;
    }

    public void setSetState(int setState) {
        this.setState = setState;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getNumber() {
        return number;
    }

    public int getOnOFF() {
        return onOFF;
    }

    public int getSetState() {
        return setState;
    }
}
