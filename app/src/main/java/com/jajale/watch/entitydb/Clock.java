package com.jajale.watch.entitydb;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 家庭成员
 *
 * Created by lilonghui on 2015/12/2.
 * Email:lilonghui@bjjajale.com
 */

@DatabaseTable(tableName = "Clock")
public class Clock implements Parcelable {


    public static String ID = "_id";
    public static String WATCHID = "watchID";

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

    @DatabaseField(columnName = "time")
    private String time;
    @DatabaseField(columnName = "number")
    private int number;
    @DatabaseField(columnName = "onOFF")
    private int onOFF;
    @DatabaseField(columnName = "type")
    private int type;
    @DatabaseField(columnName = "setValues")
    private String setValues;
    @DatabaseField(columnName = "setState")
    private int setState;

    public void setTime(String time) {
        this.time = time;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setOnOFF(int onOFF) {
        this.onOFF = onOFF;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSetValues(String setValues) {
        this.setValues = setValues;
    }

    public void setSetState(int setState) {
        this.setState = setState;
    }

    public String getTime() {
        return time;
    }

    public int getNumber() {
        return number;
    }

    public int getOnOFF() {
        return onOFF;
    }

    public int getType() {
        return type;
    }

    public String getSetValues() {
        return setValues;
    }

    public int getSetState() {
        return setState;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time);
        dest.writeInt(this.number);
        dest.writeInt(this.onOFF);
        dest.writeInt(this.type);
        dest.writeString(this.setValues);
        dest.writeInt(this.setState);
    }

    public Clock() {
    }

    protected Clock(Parcel in) {
        this.time = in.readString();
        this.number = in.readInt();
        this.onOFF = in.readInt();
        this.type = in.readInt();
        this.setValues = in.readString();
        this.setState = in.readInt();
    }

    public static final Parcelable.Creator<Clock> CREATOR = new Parcelable.Creator<Clock>() {
        public Clock createFromParcel(Parcel source) {
            return new Clock(source);
        }

        public Clock[] newArray(int size) {
            return new Clock[size];
        }
    };
}
